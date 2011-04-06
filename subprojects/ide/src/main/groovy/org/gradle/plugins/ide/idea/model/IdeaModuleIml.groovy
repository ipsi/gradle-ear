/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.plugins.ide.idea.model

import org.gradle.api.internal.XmlTransformer
import org.gradle.listener.ActionBroadcast

/**
 * Models the generation/parsing/merging capabilities of idea module
 * <p>
 * For example see docs for {@link IdeaModule}
 * <p>
 * Author: Szczepan Faber, created at: 4/5/11
 */
class IdeaModuleIml {

    ActionBroadcast whenMerged = new ActionBroadcast()
    ActionBroadcast beforeMerged = new ActionBroadcast()
    XmlTransformer xmlTransformer

    /**
     * Adds a closure to be called after *.iml content is loaded from existing file
     * but before gradle build information is merged
     * <p>
     * This is advanced api that gives access to internal implementation of idea plugin.
     * It might be useful if you want to alter the way gradle build information is merged into existing *.iml content
     * <p>
     * The {@link Module} object is passed as a parameter to the closure
     * <p>
     * For example see docs for {@link IdeaModule}
     *
     * @param closure The closure to execute.
     */
    public void beforeMerged(Closure closure) {
        beforeMerged.add(closure)
    }

    /**
     * Adds a closure to be called after *.iml content is loaded from existing file
     * and after gradle build information is merged
     * <p>
     * This is advanced api that gives access to internal implementation of idea plugin.
     * Use it only to tackle some tricky edge cases.
     * <p>
     * The {@link Module} object is passed as a parameter to the closure
     * <p>
     * For example see docs for {@link IdeaModule}
     *
     * @param closure The closure to execute.
     */
    public void whenMerged(Closure closure) {
        whenMerged.add(closure)
    }

    /**
     * Adds a closure to be called when the *.iml file has been created. The XML is passed to the closure as a
     * parameter in form of a {@link org.gradle.api.artifacts.maven.XmlProvider}. The closure can modify the XML before
     * it is written to the output file.
     * <p>
     * For example see docs for {@link IdeaModule}
     *
     * @param closure The closure to execute when the XML has been created.
     */
    public void withXml(Closure closure) {
        xmlTransformer.addAction(closure)
    }

    /**
     * Folder where the *.iml file will be generated to
     * <p>
     * For example see docs for {@link IdeaModule}
     */
    File generateTo
}
