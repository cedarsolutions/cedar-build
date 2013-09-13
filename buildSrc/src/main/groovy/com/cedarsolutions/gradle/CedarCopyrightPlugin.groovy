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
import org.gradle.api.plugins.MavenPlugin
import org.gradle.plugins.signing.SigningPlugin

/** 
 * The cedarCopyright plugin. 
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarCopyrightPlugin implements Plugin<Project> {

   /** Apply the plugin. */
   void apply(Project project) {
      project.extensions.create("cedarCopyright", CedarCopyrightPluginExtension, project)
      project.convention.plugins.cedarCopyright = new CedarCopyrightPluginConvention(project)

      project.task("validateCopyrightSetup") << {
         project.cedarCopyright.validateCopyrightConfig()
      }

      project.task("copyright", dependsOn: [ project.tasks.validateCopyrightSetup, ]) << {
         project.convention.plugins.cedarCopyright.updateCopyrightStatements()
      }
   }

}
