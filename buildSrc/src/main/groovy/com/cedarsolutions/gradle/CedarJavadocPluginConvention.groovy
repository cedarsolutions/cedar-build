// vim: set ft=groovy ts=4 sw=4:
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// *              C E D A R
// *          S O L U T I O N S       "Software done right."
// *           S O F T W A R E
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Copyright (c) 2013 Kenneth J. Pronovici.
// * All rights reserved.
// *
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the Apache License, Version 2.0.
// * See LICENSE for more information about the licensing terms.
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Author   : Kenneth J. Pronovici <pronovic@ieee.org>
// * Language : Gradle (>= 1.7)
// * Project  : Common Gradle Build Functionality
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package com.cedarsolutions.gradle

import org.gradle.api.Project
import org.gradle.plugins.signing.Sign
import org.gradle.api.InvalidUserDataException
import java.io.File;
import org.gradle.util.Jvm;  // this is deprecated, but I can't find notes on what replaces it ?!?

/** 
 * Plugin convention for cedarJavadoc. 
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarJavadocPluginConvention {

    /** The project tied to this convention. */
    private Project project;

    /** Create a convention tied to a project. */
    public CedarJavadocPluginConvention(Project project) {
        this.project = project
    }

    /** Generate Javadoc per configuration. */
    def generateJavadoc() {
        def tempDir =  project.file(project.buildDir.canonicalPath + "/tmp").canonicalPath
        def optionsFile = project.file(tempDir + "/jdoc.options").canonicalPath

        def classpath = project.cedarJavadoc.getClasspath()
        def title = project.cedarJavadoc.getTitle()
        def srcDirs = project.cedarJavadoc.getSrcDirs()
        def subpackages = project.cedarJavadoc.getSubpackages()
        def output = project.file(project.cedarJavadoc.getOutput()).canonicalPath

        if (srcDirs == null || srcDirs.isEmpty()) {
            project.logger.lifecycle("CedarBuild Javadoc tool: no source directories configured")
        } else {
            if (subpackages == null || subpackages.isEmpty()) {
                project.logger.lifecycle("CedarBuild Javadoc tool: no subpackages configured")
            } else {
                project.logger.lifecycle("CedarBuild Javadoc tool: generating Javadoc")
                project.file(output).deleteDir()
                project.file(optionsFile).delete()
                project.file(output).mkdirs()
                project.file(tempDir).mkdirs()
                generateOptionsFile(optionsFile, classpath, title, srcDirs, subpackages, output)
                project.ant.exec(executable: Jvm.current().getJavadocExecutable(), 
                                 failonerror: "true", dir: project.projectDir) {
                    arg(value: "@${optionsFile}")
                }
            }
        }
    }

    /** Generate a Javadoc options file on disk. */
    private void generateOptionsFile(optionsFile, classpath, title, srcDirs, subpackages, output) {

        // The reason this is even necessary is because GWT jars include source
        // code, and the Java 7 Javadoc compiler gets utterly confused by this.
        // The only workaround I've found is to specify a source path and
        // subpackages, because then it apparently ignores the source in the
        // jars.  However, this workaround is kind of ugly.  The command-line you
        // see below has been tested on Windows, but I'm not sure whether it will
        // work on UNIX or whether the path separator will need to change.  The
        // stupid Oracle documentation for Javadoc says that the separator is
        // always colon (":"), but that clearly doesn't work for sourcepath
        // (although it does seem to be correct for subpackages).

        new File(optionsFile).withWriter { out ->
            out.writeLine("-classpath '" + classpath.asPath.replace("\\", "\\\\") + "'")
            out.writeLine("-d '" + output.replace("\\", "\\\\") + "'")
            out.writeLine("-quiet")
            out.writeLine("-notimestamp")
            out.writeLine("-doctitle '${title}'")
            out.writeLine("-windowtitle '${title}'")
            out.writeLine("-sourcepath '" + srcDirs.join(";").replace("\\", "\\\\") + "'")
            out.writeLine("-subpackages '" + subpackages.join(":") + "'")
        }

    }

}
