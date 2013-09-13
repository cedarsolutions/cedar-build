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
import org.gradle.plugins.signing.Sign
import org.gradle.api.InvalidUserDataException

class CedarSigningPluginConvention {

   /** The project tied to this convention. */
   private Project project;

   /** Create a convention tied to a project. */
   CedarSigningPluginConvention(Project project) {
      this.project = project
   }

   /** Apply signature configuration to all projects if necessary. */
   def applySignatureConfiguration(taskGraph) {
      if (taskGraphRequiresSignatures(taskGraph)) {
         getGpgPassphrase({ input -> setSignatureConfiguration(input) })
      }
   }

   /** Check whether a task graph indicates that signatures are required. */
   def taskGraphRequiresSignatures(taskGraph) {
      return taskGraph.allTasks.any { it instanceof Sign && it.required }
   }

   /** Get a GPG passphrase from the user, calling a closure with the result. */
   def getGpgPassphrase(action) {
      String title = "GPG key " + project.cedarSigning.gpgKeyId;
      String label = "Enter passphrase"
      validateGpgConfig()
      project.convention.plugins.cedarBuild.getInput(title, label, true, action)
   }

   /** Set signature configuration for all projects. */
   def setSignatureConfiguration(passphrase) {
      project.cedarSigning.projects.each { project ->
         project.ext."signing.keyId" = project.cedarSigning.gpgKeyId 
         project.ext."signing.secretKeyRingFile" = project.cedarSigning.gpgSecretKey
         project.ext."signing.password" = passphrase
      }
   }

   /** Validate the GPG configuration. */
   def validateGpgConfig() {
      if (project.cedarSigning.gpgKeyId == null || project.cedarSigning.gpgKeyId == "unset") {
         throw new InvalidUserDataException("Publish error: gpgKeyId is unset")
      }

      if (project.cedarSigning.gpgSecretKey == null || project.cedarSigning.gpgSecretKey == "unset") {
         throw new InvalidUserDataException("Publish error: gpgSecretKey is unset")
      }

      if (!(new File(project.cedarSigning.gpgSecretKey).isFile())) {
         throw new InvalidUserDataException("Publish error: GPG secret key not found: " + project.cedarSigning.gpgSecretKey)
      }
   }

}
