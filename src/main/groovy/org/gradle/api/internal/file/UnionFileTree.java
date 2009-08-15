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
package org.gradle.api.internal.file;

import org.gradle.api.file.FileTree;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class UnionFileTree extends CompositeFileTree {
    private final Set<FileTree> sourceTrees;
    private final String displayName;

    public UnionFileTree(FileTree... sourceTrees) {
        this("file tree", Arrays.asList(sourceTrees));
    }

    public UnionFileTree(String displayName, FileTree... sourceTrees) {
        this(displayName, Arrays.asList(sourceTrees));
    }

    public UnionFileTree(String displayName, Collection<? extends FileTree> sourceTrees) {
        this.displayName = displayName;
        this.sourceTrees = new LinkedHashSet<FileTree>(sourceTrees);
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    protected Set<FileTree> getSourceCollections() {
        return sourceTrees;
    }

    public UnionFileTree add(FileTree source) {
        sourceTrees.add(source);
        return this;
    }
}