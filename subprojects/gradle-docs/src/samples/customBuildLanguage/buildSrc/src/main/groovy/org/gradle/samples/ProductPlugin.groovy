/*
 * Copyright 2009 the original author or authors.
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
package org.gradle.samples

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip

/**
 * A plugin which configures a product project. Each product is composed of several product modules.
 */
class ProductPlugin implements Plugin<Project> {
    void use(Project project) {
        project.configure(project) {
            usePlugin 'base'

            def pluginConvention = new ProductPluginConvention()
            convention.plugins.product = pluginConvention
            pluginConvention.distSrcDirs << rootProject.file('src/dist')
            pluginConvention.distSrcDirs << project.file('src/dist')

            configurations {
                runtime
            }
            tasks.add(name: 'dist', type: Zip)

            afterEvaluate {
                ProductDefinition product = pluginConvention.product
                product.modules.each {p ->
                    dependencies { runtime delegate.project(p.path) }
                }
                archivesBaseName = "some-company-${product.displayName.replaceAll('\\s+', '-').toLowerCase()}"
                dist {
                    into('lib') {
                        from configurations.runtime
                    }
                    from(pluginConvention.distSrcDirs) {
                        filter(org.apache.tools.ant.filters.ReplaceTokens, tokens: [
                                productName: product.displayName,
                                version: version
                        ])
                    }
                }
            }
        }
    }
}