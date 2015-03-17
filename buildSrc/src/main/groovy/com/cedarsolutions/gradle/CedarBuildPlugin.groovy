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
import org.gradle.api.Plugin

/** 
  * The cedarBuild plugin. 
  * @author Kenneth J. Pronovici <pronovic@ieee.org>
  */
class CedarBuildPlugin implements Plugin<Project> {

    /** Apply the plugin. */
    void apply(Project project) {
        project.extensions.cedarProperties = new CedarProperties(project)
        project.extensions.create("cedarSigning", CedarSigningPluginExtension, project)
        project.extensions.create("cedarLabel", CedarLabelPluginExtension, project)

        project.convention.plugins.cedarBuild = new CedarBuildPluginConvention(project)
        project.convention.plugins.cedarKeyValueStore = new CedarKeyValueStorePluginConvention(project)
        project.convention.plugins.cedarSigning = new CedarSigningPluginConvention(project)
        project.convention.plugins.cedarLabel = new CedarLabelPluginConvention(project)

        String added = project.convention.plugins.cedarKeyValueStore.getCacheValue("testSummaryAdded")
        if (added == null) {
            project.gradle.addListener(new TestSummary())
            project.convention.plugins.cedarKeyValueStore.setCacheValue("testSummaryAdded", "yes")
        }

        project.gradle.taskGraph.whenReady { 
            taskGraph -> project.convention.plugins.cedarSigning.applySignatureConfiguration(taskGraph) 
        }

        project.task("validateLabelSetup") << {
            project.cedarLabel.validateLabelConfig()
        }

        project.task("label", dependsOn: [ project.tasks.validateLabelSetup, ]) << {
            project.convention.plugins.cedarLabel.labelMercurialRepositories()
        }
    }

}
