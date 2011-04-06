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
package org.gradle.plugins.ide.api;

import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.internal.ConventionTask;
import org.gradle.api.specs.Specs;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.listener.ActionBroadcast;
import org.gradle.plugins.ide.internal.generator.generator.Generator;

import java.io.File;

/**
 * <p>A {@code GeneratorTask} generates a configuration file based on a domain object of type T.
 * When executed the task:
 * <ul>
 *
 * <li>loads the object from the input file, if it exists.</li>
 *
 * <li>Calls the beforeConfigured actions, passing the object to each action.</li>
 *
 * <li>Configures the object in some task-specific way.</li>
 *
 * <li>Calls the afterConfigured actions, passing the object to each action.</li>
 *
 * <li>writes the object to the output file.</li>
 *
 * </ul>
 *
 * @param <T> The domain object for the configuration file.
 */
public class GeneratorTask<T> extends ConventionTask {
    private File inputFile;
    private File outputFile;
    protected final ActionBroadcast<T> beforeConfigured = new ActionBroadcast<T>();
    protected final ActionBroadcast<T> afterConfigured = new ActionBroadcast<T>();
    protected Generator<T> generator;

    protected T domainObject;

    public GeneratorTask() {
        getOutputs().upToDateWhen(Specs.satisfyNone());
    }

    @TaskAction
    void generate() {
        configureDomainObject();
        generator.write(getDomainObject(), getOutputFile());
    }

    public void configureDomainObject() {
        if (domainObject != null) {
            return;
        }
        T object;
        if (getInputFile().exists()) {
            object = generator.read(getInputFile());
        } else {
            object = generator.defaultInstance();
        }
        beforeConfigured.execute(object);
        generator.configure(object);
        afterConfigured.execute(object);
        domainObject = object;
    }

    /**
     * The input file to load the initial configuration from. Defaults to the output file. If the specified input file
     * does not exist, this task uses some default initial configuration.
     *
     * @return The input file.
     */
    public File getInputFile() {
        return inputFile != null ? inputFile : getOutputFile();
    }

    /**
     * Sets the input file to load the initial configuration from.
     *
     * @param inputFile The input file. Use null to use the output file.
     */
    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * The output file to write the final configuration to.
     *
     * @return The output file.
     */
    @OutputFile
    public File getOutputFile() {
        return outputFile;
    }

    /**
     * Sets the output file to write the final configuration to.
     *
     * @param outputFile The output file.
     */
    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    /**
     * <p>Adds a closure to be called before the domain object is configured by this task. The domain object is passed
     * as a parameter to the closure.</p>
     *
     * <p>The closure is executed after the domain object has been loaded from the input file. Using this method allows
     * you to change the domain object in some way before the task configures it.</p>
     *
     * @param closure The closure to execute.
     */
    public void beforeConfigured(Closure closure) {
        beforeConfigured.add(closure);
    }

    /**
     * <p>Adds an action to be called before the domain object is configured by this task. The domain object is passed
     * as a parameter to the action.</p>
     *
     * <p>The action is executed after the domain object has been loaded from the input file. Using this method allows
     * you to change the domain object in some way before the task configures it.</p>
     *
     * @param action The action to execute.
     */
    public void beforeConfigured(Action<? super T> action) {
        beforeConfigured.add(action);
    }

    /**
     * <p>Adds a closure to be called after the domain object has been configured by this task. The domain object is
     * passed as a parameter to the closure.</p>
     *
     * <p>The closure is executed just before the domain object is written to the output file. Using this method allows
     * you to override the configuration applied by this task.</p>
     *
     * @param closure The closure to execute.
     */
    public void whenConfigured(Closure closure) {
        afterConfigured.add(closure);
    }

    /**
     * <p>Adds an action to be called after the domain object has been configured by this task. The domain object is
     * passed as a parameter to the action.</p>
     *
     * <p>The action is executed just before the domain object is written to the output file. Using this method allows
     * you to override the configuration applied by this task.</p>
     *
     * @param action The action to execute.
     */
    public void whenConfigured(Action<? super T> action) {
        afterConfigured.add(action);
    }

    protected T getDomainObject() {
        if (this.domainObject == null) {
            throw new IllegalStateException("Domain object was not configured for this task. See configureDomainObject() method.");
        }
        return this.domainObject;
    }

    protected void setDomainObject(T domainObject) {
        this.domainObject = domainObject;
    }

}
