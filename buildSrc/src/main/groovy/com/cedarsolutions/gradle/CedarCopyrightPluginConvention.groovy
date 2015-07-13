// vim: set ft=groovy ts=4 sw=4:
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// *              C E D A R
// *          S O L U T I O N S       "Software done right."
// *           S O F T W A R E
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Copyright (c) 2013,2015 Kenneth J. Pronovici.
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
import org.gradle.api.InvalidUserDataException

/** 
 * Plugin convention for cedarCopyright. 
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarCopyrightPluginConvention {

    /** The project tied to this convention. */
    private Project project;

    /** Create a convention tied to a project. */
    public CedarCopyrightPluginConvention(Project project) {
        this.project = project
    }

    /** Update copyright statements, using the copyright tool. */
    def updateCopyrightStatements() {
        def repositories = project.cedarCopyright.getRepositories()
        def mercurialPath = project.cedarCopyright.getMercurialPath()

        def licensePattern = '"' + project.cedarCopyright.getLicensePattern() + '"'

        def sourcePatterns = ""
        project.cedarCopyright.getSourcePatterns().each { pattern ->
            sourcePatterns += ' "' + pattern + '"'
        }

        if (repositories == null || repositories.isEmpty()) {
            project.logger.lifecycle("CedarBuild copyright tool: no Mercurial repositories to update")
        } else {
            project.logger.lifecycle("CedarBuild copyright tool: updating copyright statements")

            // This is a hack, but there's apparently no other way to get at our buildSrc jar
            def cp = project.buildscript.configurations.classpath.asPath
            if (cp.length() == 0 && project.name == "CedarBuild") {
                cp = "buildSrc/build/libs/buildSrc.jar"
            }

            repositories.each { repository ->
                project.logger.lifecycle(" --> ${repository}")
                project.ant.java(classname : "com.cedarsolutions.tools.copyright.CopyrightTool", fork: "true", failonerror: "true") {
                    arg(value : project.cedarCopyright.getMercurialPath())
                    arg(value : repository)
                    arg(value : licensePattern)
                    arg(line  : sourcePatterns)
                    classpath() {
                        pathelement(path: cp)
                    }
                }
            }
        } 
    }

}
