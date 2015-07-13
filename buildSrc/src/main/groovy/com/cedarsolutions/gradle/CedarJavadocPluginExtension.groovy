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
// * Language : Gradle (>= 2.5)
// * Project  : Common Gradle Build Functionality
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package com.cedarsolutions.gradle

import org.gradle.api.Project 
import org.gradle.api.InvalidUserDataException
import java.io.File
import java.util.concurrent.Callable

/** 
 * Plugin extension for cedarJavadoc. 
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarJavadocPluginExtension {

    /** Project tied to this extension. */
    private Project project;

    /** Create an extension for a project. */
    public CedarJavadocPluginExtension(Project project) {
        this.project = project;
    }

    /** Classpath to be used with the Javadoc command. */
    def classpath

    /** Title to be used for both windowtitle and doctitle. */
    def title

    /** Source directories to scan for classes. */
    def srcDirs

    /** List of subpackages for which to generate Javadoc, often just [ "com", ]. */
    def subpackages

    /** Directory that output should be written to. */
    def output

    /** Get the classpath, accounting for closures. */
    def getClasspath() {
        return classpath != null && classpath instanceof Callable ? classpath.call() : classpath
    }  

    /** Get the title, accounting for closures. */
    String getTitle() {
        return title != null && title instanceof Callable ? title.call() : title
    }  

    /** Get the subpackages, accounting for closures. */
    def getSubpackages() {
        return subpackages != null && subpackages instanceof Callable ? subpackages.call() : subpackages
    }  

    /** Get the source directories, accounting for closures. */
    def getSrcDirs() {
        return srcDirs != null && srcDirs instanceof Callable ? srcDirs.call() : srcDirs
    }  

    /** Get the output directory, accounting for closures. */
    def getOutput() {
        return output != null && output instanceof Callable ? output.call() : output
    }  

    /** Validate the Javadoc configuration. */
    def validateJavadocConfig() {
        if (getSrcDirs() != null && !getSrcDirs().isEmpty()) {
            if (getSubpackages() != null && !getSubpackages().isEmpty()) {
                if (getClasspath() == null || getClasspath() == "unset") {
                    throw new InvalidUserDataException("Javadoc error: classpath is unset")
                }

                if (getTitle() == null || getTitle() == "unset") {
                    throw new InvalidUserDataException("Javadoc error: title is unset")
                }

                if (getOutput() == null || getOutput() == "unset") {
                    throw new InvalidUserDataException("Javadoc error: output is unset")
                }
            }
        }
    }

}
