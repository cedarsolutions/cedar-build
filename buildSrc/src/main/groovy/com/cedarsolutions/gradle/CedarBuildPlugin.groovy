// vim: set ft=groovy ts=3:
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

class CedarBuildPlugin implements Plugin<Project> {

   @Override
   void apply(Project project) {
      project.extensions.create("cedarSigning", CedarSigningPluginExtension, project)
      project.extensions.cedarProperties = new CedarProperties(project)

      project.convention.plugins.cedarBuild = new CedarBuildPluginConvention(project)
      project.convention.plugins.cedarSigning = new CedarSigningPluginConvention(project)

      project.gradle.addListener(new TestSummary())
      project.gradle.taskGraph.whenReady { 
         taskGraph -> project.convention.plugins.cedarSigning.applySignatureConfiguration(taskGraph) 
      }
   }

}
