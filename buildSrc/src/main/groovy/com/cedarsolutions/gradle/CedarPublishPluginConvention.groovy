// vim: set ft=groovy ts=4 sw=4:
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// *              C E D A R
// *          S O L U T I O N S       "Software done right."
// *           S O F T W A R E
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Copyright (c) 2015 Kenneth J. Pronovici.
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

/** 
 * Plugin convention for cedarPublish. 
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarPublishPluginConvention {

    /** The project tied to this convention. */
    private Project project;

    /** Create a convention tied to a project. */
    public CedarPublishPluginConvention(Project project) {
        this.project = project
    }

    /** Get a Maven repository password from the user, calling a closure with the result. */
    def getMavenRepositoryPassword() {
        def password = null  // def NOT String, otherwise closure assignment won't work

        if (project.extensions.cedarPublish.isMavenRepositoryUserConfigured()) {
            if (project.gradle.startParameter.taskNames.contains("publish")) {
                String title = "Maven user " + project.extensions.cedarPublish.getMavenRepositoryUser()
                String label = "Enter password"
                def action = { value -> password = value }
                project.convention.plugins.cedarBuild.getInput(title, label, true, action)
            } 
        }

        return password.toString()
    }

}
