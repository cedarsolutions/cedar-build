// vim: set ft=groovy ts=4 sw=4:
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// *              C E D A R
// *          S O L U T I O N S       "Software done right."
// *           S O F T W A R E
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Copyright (c) 2014-2015 Kenneth J. Pronovici.
// * All rights reserved.
// *
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the Apache License, Version 2.0.
// * See LICENSE for more information about the licensing terms.
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Author   : Kenneth J. Pronovici <pronovic@ieee.org>
// * Language : Gradle (>= 2.5)
// * Project  : Common Gradle Build Functionality
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package com.cedarsolutions.gradle

import org.gradle.api.Project
import org.gradle.api.Plugin

import org.gradle.api.plugins.JavaPlugin
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.gradle.api.plugins.quality.CheckstylePlugin;

import org.gradle.util.GUtil;
import org.gradle.api.internal.plugins.StartScriptGenerator;

/**
 * The cedarAnalysis plugin.
 * @author Kenneth J. Pronovici <pronovici@ieee.org>
 */
class CedarAnalysisPlugin implements Plugin<Project> {

    /** Apply the plugin. */
    void apply(Project project) {
        project.plugins.apply(JavaPlugin)
        project.plugins.apply(JacocoPlugin)
        project.plugins.apply(CheckstylePlugin)

        project.extensions.create("cedarAnalysis", CedarAnalysisPluginExtension, project)
        project.convention.plugins.cedarAnalysis = new CedarAnalysisPluginConvention(project)

        applyCedarAnalysis(project)
    }

    /** Apply cedarAnalysis. */
    void applyCedarAnalysis(Project project) {

        // Add required dependencies to the project
        project.configurations {
            analysis
        }
        project.dependencies {
            analysis "org.codehaus.javancss:javancss:33.54"
        } 

        // Configure the standard Checkstyle plugin
        project.checkstyle {
            configFile = project.file("doc/standards/CheckstyleRules.xml")
            reportsDir = project.file("build/reports/checkstyle")
            ignoreFailures = true
            showViolations = false
            toolVersion = 5.5
            configProperties = [ config_loc : project.file(project.projectDir.canonicalPath + "/doc/standards").canonicalPath, ]
        }

        // Configure the standard Jacoco coverage report
        project.jacocoTestReport {
            reports {
                xml.enabled false
                csv.enabled false
                html.enabled false
            }
        }

        // Generate JavaNCSS metrics for the configured source directories
        project.task("javancss") << {
            project.convention.plugins.cedarAnalysis.runJavancss("Source Code", project.cedarAnalysis.getSourceFolders())
            project.convention.plugins.cedarAnalysis.runJavancss("Test Code", project.cedarAnalysis.getTestFolders())
        }

        // Run all of the configured metrics.
        project.task("metrics", dependsOn: [ project.tasks.javancss, ]) << {
        }

        // Create a single checkstyle task
        project.task("checkstyle", dependsOn: [ project.tasks.checkstyleMain, project.tasks.checkstyleTest, ]) << {
        }

        // Create a single coverage task
        project.task("coverage", dependsOn: [ project.tasks.jacocoTestReport, ]) << {
        }

    }

}
