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
package org.gradle.tooling.internal.provider;

import org.gradle.api.Project;
import org.gradle.api.internal.GradleInternal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractModelBuilder implements ModelBuildingAdapter.Builder {
    private DefaultEclipseProject currentProject;
    private final Map<String, DefaultEclipseProject> projectMapping = new HashMap<String, DefaultEclipseProject>();
    private GradleInternal gradle;

    public void buildAll(GradleInternal gradle) {
        this.gradle = gradle;
        build(gradle.getRootProject());
    }

    public DefaultEclipseProject getProject() {
        return currentProject;
    }

    protected Map<String, DefaultEclipseProject> getProjectMapping() {
        return projectMapping;
    }

    protected abstract DefaultEclipseProject build(Project project);

    protected List<DefaultEclipseProject> buildChildren(Project project) {
        List<DefaultEclipseProject> children = new ArrayList<DefaultEclipseProject>();
        for (Project child : project.getChildProjects().values()) {
            children.add(build(child));
        }
        return children;
    }

    protected void addProject(Project project, DefaultEclipseProject eclipseProject) {
        if (project == gradle.getDefaultProject()) {
            currentProject = eclipseProject;
        }
        projectMapping.put(project.getPath(), eclipseProject);
    }

    protected DefaultEclipseProject get(Project project) {
        return projectMapping.get(project.getPath());
    }
}
