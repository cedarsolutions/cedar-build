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
import org.gradle.api.InvalidUserDataException
import java.util.concurrent.Callable

/** 
 * Plugin extension for cedarLabel. 
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarLabelPluginExtension {

    /** Project tied to this extension. */
    private Project project;

    /** Create an extension for a project. */
    public CedarLabelPluginExtension(Project project) {
        this.project = project;
    }

    /** Whether label generation is enabled. */
    def enabled

    /** The name of the project that the label is tied to. */
    def projectName

    /** The version that should be used for the label. */
    def projectVersion

    /** List of Mercurial directories that should be labeled. */
    def repositories

    /** Path to the Mercurial exectuable. */
    def mercurialPath

    /** Get the enabled flag, allowing for a default (null=true) and closure assignment. */
    boolean isEnabled() {
        if (enabled != null) {
            if (enabled instanceof Callable) {
                return enabled.call() == "true" ? true : false
            } else {
                return enabled == "true" ? true : false
            }
        } else {
            return true
        }
    }

    /** Get the project name, allowing for closure assignment. */
    String getProjectName() {
        return projectName != null && projectName instanceof Callable ? projectName.call() : projectName
    }  

    /** Get the project version, allowing for closure assignment. */
    String getProjectVersion() {
        return projectVersion != null && projectVersion instanceof Callable ? projectVersion.call() : projectVersion
    }  

    /** Get the repositories list, allowing for closure assignment. */
    def getRepositories() {
        return repositories != null && repositories instanceof Callable ? repositories.call() : repositories
    }  

    /** Get the Mercurial path, allowing for closure assignment. */
    String getMercurialPath() {
        return mercurialPath != null && mercurialPath instanceof Callable ? mercurialPath.call() : mercurialPath
    }  

    /** Validate the label configuration. */
    def validateLabelConfig() {
        if (getEnabled()) {
            if (getRepositories() != null && !getRepositories().isEmpty()) {
                if (getProjectName() == null || getProjectName() == "unset") {
                    throw new InvalidUserDataException("Label error: projectName is unset")
                }

                if (getProjectVersion() == null || getProjectVersion() == "unset") {
                    throw new InvalidUserDataException("Label error: projectVersion is unset")
                }

                if (getMercurialPath() == null || getMercurialPath() == "unset") {
                    throw new InvalidUserDataException("Label error: mercurialPath is unset")
                }
            }
        }
    }

}

