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
package org.gradle.tooling;

import org.gradle.tooling.internal.DefaultGradleConnection;

import java.io.File;

/**
 * A {@code GradleConnector} is the main entry point to the Gradle tooling API.
 */
public class GradleConnector {
    public static GradleConnector newConnector() {
        return new GradleConnector();
    }

    public GradleConnector useInstallation(File gradleHome) {
        return this;
    }

    public GradleConnector useGradleVersion(String gradleVersion) {
        return this;
    }

    public GradleConnection forProjectDirectory(File projectDir) throws UnsupportedVersionException {
        return new DefaultGradleConnection(projectDir);
    }

    public void close() {
    }
}