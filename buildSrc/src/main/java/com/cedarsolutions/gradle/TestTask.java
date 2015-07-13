/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013 Kenneth J. Pronovici.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Apache License, Version 2.0.
 * See LICENSE for more information about the licensing terms.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Author   : Kenneth J. Pronovici <pronovic@ieee.org>
 * Language : Java 6
 * Project  : Common Java Functionality
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.cedarsolutions.gradle;

import java.util.concurrent.Callable;
import org.gradle.api.tasks.testing.Test;
import org.gradle.logging.StyledTextOutputFactory;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.internal.Factory;
import org.gradle.messaging.actor.ActorFactory;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.logging.ProgressLoggerFactory;
import org.gradle.process.internal.WorkerProcessBuilder;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import java.util.concurrent.Callable;

/**
 * Customized Gradle test task.
 *
 * <p>
 * The standard Gradle test task has some flaws: most configuration parameters
 * don't accept closures and can only be set at task creation.  That makes the
 * test task difficult to use with generalized plugins that want to create the
 * task first and configure it later.
 * </p>
 *
 * <p>
 * This is a customized version of the standard test task that's a little more
 * flexible.  It supports closures for most configuration attributes, and also
 * adds a new <code>deferredConfig</code> closure that's executed immediately
 * before the tests.  This allows late binding, so you can set configuration
 * options with values that are only available after task creation.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@SuppressWarnings("unchecked")
public class TestTask extends Test {

    private Callable deferredConfig;

    public TestTask() {
        super();
    }

    @TaskAction
    public void executeTests() {
        if (this.deferredConfig != null) {
            try {
                this.deferredConfig.call();
            } catch (Exception e) { }
        }

        super.executeTests();
    }

    public void deferredConfig(Callable closure) {
        this.deferredConfig = closure;
    }

    public void setWorkingDir(Callable closure) {
        if (closure != null) {
            try {
                super.setWorkingDir((Object) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setExecutable(Callable closure) {
        if (closure != null) {
            try {
                super.setExecutable((Object) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setSystemProperties(Callable closure) {
        if (closure != null) {
            try {
                super.setSystemProperties((Map<String, ?>) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setBootstrapClasspath(Callable closure) {
        if (closure != null) {
            try {
                super.setBootstrapClasspath((FileCollection) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setDefaultCharacterEncoding(Callable closure) {
        if (closure != null) {
            try {
                super.setDefaultCharacterEncoding((String) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setMinHeapSize(Callable closure) {
        if (closure != null) {
            try {
                super.setMinHeapSize((String) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setMaxHeapSize(Callable closure) {
        if (closure != null) {
            try {
                super.setMaxHeapSize((String) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setJvmArgs(Callable closure) {
        if (closure != null) {
            try {
                super.setJvmArgs((Iterable<?>) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setEnableAssertions(Callable closure) {
        if (closure != null) {
            try {
                super.setEnableAssertions((Boolean) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setDebug(Callable closure) {
        if (closure != null) {
            try {
                super.setDebug((Boolean) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setAllJvmArgs(Callable closure) {
        if (closure != null) {
            try {
                super.setAllJvmArgs((Iterable<?>) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setEnvironment(Callable closure) {
        if (closure != null) {
            try {
                super.setEnvironment((Map<String, ?>) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setTestClassesDir(Callable closure) {
        if (closure != null) {
            try {
                super.setTestClassesDir((File) closure.call());
            } catch (Exception e) { }
        }
    }

    public Test setIncludes(Callable closure) {
        if (closure == null) {
            return null;
        } else {
            try {
                return super.setIncludes((Iterable<String>) closure.call());
            } catch (Exception e) {
                return null;
            }
        }
    }

    public Test setExcludes(Callable closure) {
        if (closure == null) {
            return null;
        } else {
            try {
                return super.setExcludes((Iterable<String>) closure.call());
            } catch (Exception e) {
                return null;
            }
        }
    }

    public void setIgnoreFailures(Callable closure) {
        if (closure != null) {
            try {
                super.setIgnoreFailures((Boolean) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setClasspath(Callable closure) {
        if (closure != null) {
            try {
                super.setClasspath((FileCollection) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setTestSrcDirs(Callable closure) {
        if (closure != null) {
            try {
                super.setTestSrcDirs((List<File>) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setScanForTestClasses(Callable closure) {
        if (closure != null) {
            try {
                super.setScanForTestClasses((Boolean) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setForkEvery(Callable closure) {
        if (closure != null) {
            try {
                super.setForkEvery((Long) closure.call());
            } catch (Exception e) { }
        }
    }

    public void setMaxParallelForks(Callable closure) {
        if (closure != null) {
            try {
                super.setMaxParallelForks((Integer) closure.call());
            } catch (Exception e) { }
        }
    }

}

