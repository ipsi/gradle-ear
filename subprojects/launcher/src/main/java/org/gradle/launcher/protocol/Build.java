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
package org.gradle.launcher.protocol;

import org.gradle.initialization.BuildClientMetaData;
import org.gradle.initialization.ParsedCommandLine;

import java.io.File;
import java.util.Map;

public class Build extends Command {
    private final ParsedCommandLine args;
    private final File currentDir;
    private final long startTime;
    private final Map<String, String> systemProperties;

    public Build(File currentDir, ParsedCommandLine args, long startTime, BuildClientMetaData clientMetaData, Map<String, String> systemProperties) {
        super(clientMetaData);
        this.currentDir = currentDir;
        this.args = args;
        this.startTime = startTime;
        this.systemProperties = systemProperties;
    }

    public Map<String, String> getSystemProperties() {
        return systemProperties;
    }

    public ParsedCommandLine getArgs() {
        return args;
    }

    public File getCurrentDir() {
        return currentDir;
    }

    public long getStartTime() {
        return startTime;
    }
}
