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
// * Language : Gradle (>= 1.7)
// * Project  : Common Gradle Build Functionality
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package com.cedarsolutions.gradle

import org.gradle.api.Project 
import org.gradle.api.InvalidUserDataException
import java.io.File
import java.util.concurrent.Callable

/** 
 * Plugin extension for cedarPublish. 
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarPublishPluginExtension {

    /** Project tied to this extension. */
    private Project project;

    /** Create an extension for a project. */
    public CedarPublishPluginExtension(Project project) {
        this.project = project;
    }

    /** Path to the Mercurial-based Maven project that code will be published into. */
    def mercurialMavenProject

    /** URL of the real Maven repository that code will be published into. */
    def mavenRepositoryUrl

    /** Username to use when publishing to Maven URL. */
    def mavenRepositoryUser

    /** Get the project name, allowing for closure assignment. */
    String getMercurialMavenProject() {
        return mercurialMavenProject != null && mercurialMavenProject instanceof Callable ? mercurialMavenProject.call() : mercurialMavenProject
    }

    /** Get the Maven repository URL, allowing for closure assignment. */
    String getMavenRepositoryUrl() {
        return mavenRepositoryUrl != null && mavenRepositoryUrl instanceof Callable ? mavenRepositoryUrl.call() : mavenRepositoryUrl
    }

    /** Get the Maven repository username, allowing for closure assignment. */
    String getMavenRepositoryUser() {
        return mavenRepositoryUser != null && mavenRepositoryUser instanceof Callable ? mavenRepositoryUser.call() : mavenRepositoryUser
    }

    /** Whether digital signatures are required for the current publish actions. */
    def isSignatureRequired() {
        // Gradle's behavior varies depending on whether there are subprojects.
        return (isMavenRepositoryUrlConfigured() || isMercurialRepositoryConfigured()) && 
               (project.gradle.taskGraph.hasTask(":uploadArchives") || 
               project.gradle.taskGraph.hasTask(":${project.name}:uploadArchives"));
    }

    /** Get the proper Maven repository URL to use for publishing. */
    def getPublishRepositoryUrl() {
        if (isMavenRepositoryUrlConfigured()) {
            return getMavenRepositoryUrl()
        } else {
            if (!isMercurialRepositoryConfigured()) {
                 return null;
            } else {
                 return "file://" + new File(getMercurialMavenProject()).canonicalPath.replace("\\", "/") + "/maven"
            }
        }
    }

    /** Whether a valid Mercurial-based Maven repository is configured. */
    def isMercurialRepositoryConfigured() {
        if (getMercurialMavenProject() == null || getMercurialMavenProject() == "unset") {
            return false
        } else {
            if (!(new File(getMercurialMavenProject()).isDirectory()
               && new File(getMercurialMavenProject() + "/.hg").isDirectory()
               && new File(getMercurialMavenProject() + "/maven").isDirectory())) {
                return false
            } else {
                return true
            }
        }
    }

    /** Whether a specific Maven repository URL is configured. */
    def isMavenRepositoryUrlConfigured() {
        return !(getMavenRepositoryUrl() == null || getMavenRepositoryUrl() == "unset");
    }

    /** Whether a specific Maven repository username is configured. */
    def isMavenRepositoryUserConfigured() {
        return !(getMavenRepositoryUser() == null || getMavenRepositoryUser() == "unset");
    }

    /** Validate the Mercurial-based Maven repository URL. */
    def validateMavenRepositoryConfig() {
        if (!isMavenRepositoryUrlConfigured() && !isMercurialRepositoryConfigured()) {
            throw new InvalidUserDataException("Must configure either Maven repository URL or valid Mercurial repository.")
        }
    }

}
