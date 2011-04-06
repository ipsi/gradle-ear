/*
 * Copyright 2010 the original author or authors.
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
package org.gradle.plugins.ide.eclipse

import org.gradle.api.JavaVersion
import org.gradle.plugins.ide.api.GeneratorTask
import org.gradle.plugins.ide.eclipse.model.Jdt
import org.gradle.plugins.ide.internal.generator.generator.PersistableConfigurationObjectGenerator
import org.gradle.plugins.ide.internal.generator.generator.ConfigurationTarget

/**
 * Generates the Eclipse JDT configuration file.
 */
class EclipseJdt extends GeneratorTask<Jdt> implements ConfigurationTarget {
    /**
     * The source Java language level.
     */
    JavaVersion sourceCompatibility

    /**
     * The target JVM to generate {@code .class} files for.
     */
    JavaVersion targetCompatibility

    EclipseJdt() {
        generator = new PersistableConfigurationObjectGenerator<Jdt>() {
            Jdt create() {
                return new Jdt()
            }

            void configure(Jdt jdt) {
                jdt.sourceCompatibility = getSourceCompatibility()
                jdt.targetCompatibility = getTargetCompatibility()
            }
        }
    }
}
