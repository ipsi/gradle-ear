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
package org.gradle.integtests.tooling

import org.gradle.integtests.fixtures.GradleDistribution
import org.gradle.integtests.fixtures.internal.IntegrationTestHint
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.gradle.tooling.UnsupportedVersionException
import org.gradle.tooling.internal.consumer.DistributionFactory
import org.gradle.util.SetSystemProperties
import org.junit.Rule
import spock.lang.Specification

class ToolingApiSpecification extends Specification {
    @Rule public final SetSystemProperties sysProperties = new SetSystemProperties()
    @Rule public final GradleDistribution dist = new GradleDistribution()

    def setup() {
        System.properties[DistributionFactory.USE_CLASSPATH_AS_DISTRIBUTION] = 'true'
    }

    def withConnection(Closure cl) {
        GradleConnector connector = GradleConnector.newConnector()
        withConnection(connector, cl)
    }

    def withConnection(GradleConnector connector, Closure cl) {
        configureConnector(connector)
        try {
            withConnectionRaw(connector, cl)
        } catch (UnsupportedVersionException e) {
            throw new IntegrationTestHint(e);
        }
    }

    private def configureConnector(GradleConnector connector) {
        connector.useGradleUserHomeDir(dist.userHomeDir)
        connector.forProjectDirectory(dist.testDir)
        connector.searchUpwards(false)
    }

    def maybeFailWithConnection(GradleConnector connector, Closure cl) {
        configureConnector(connector)
        try {
            withConnectionRaw(connector, cl)
        } catch (Throwable e) {
            return e
        }
        return null
    }

    def withConnectionRaw(GradleConnector connector, Closure cl) {
        ProjectConnection connection = connector.connect()
        try {
            return cl.call(connection)
        } finally {
            connection.close()
        }
    }
}
