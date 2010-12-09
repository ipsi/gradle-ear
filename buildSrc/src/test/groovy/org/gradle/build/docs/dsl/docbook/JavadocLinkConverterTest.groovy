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
package org.gradle.build.docs.dsl.docbook

import org.gradle.build.docs.dsl.TypeNameResolver
import org.gradle.build.docs.dsl.XmlSpecification
import org.gradle.build.docs.dsl.model.ClassMetaData

class JavadocLinkConverterTest extends XmlSpecification {
    final ClassLinkRenderer linkRenderer = Mock()
    final TypeNameResolver nameResolver = Mock()
    final ClassMetaData classMetaData = Mock()
    final JavadocLinkConverter converter = new JavadocLinkConverter(document, nameResolver, linkRenderer)

    def convertsClassNameToLink() {
        when:
        def link = converter.resolve('someName', classMetaData)

        then:
        format(link) == '<someLinkElement/>'
        _ * nameResolver.resolve('someName', classMetaData) >> 'org.gradle.SomeClass'
        _ * linkRenderer.link({it.name == 'org.gradle.SomeClass'}) >> parse('<someLinkElement/>')
    }

    def resolvesUnknownFullyQualifiedClassName() {
        when:
        def link = converter.resolve('org.gradle.SomeClass', classMetaData)

        then:
        format(link) == '''<UNHANDLED-LINK>org.gradle.SomeClass</UNHANDLED-LINK>'''
    }
}