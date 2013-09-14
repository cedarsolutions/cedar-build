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
import org.gradle.api.InvalidUserDataException
import java.io.File
import java.util.concurrent.Callable

/** 
 * Plugin extension for cedarSigning. 
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarSigningPluginExtension {

   /** Project tied to this extension. */
   private Project project;

   /** Create an extension for a project. */
   public CedarSigningPluginExtension(Project project) {
      this.project = project;
   }

   /** The id of the GPG key that will be used to sign code. */
   def gpgKeyId

   /** Path to the GPG secret key that will be used to sign code. */
   def gpgSecretKey
   
   /** Projects that require configuration for digital signatures. */
   def projects

   /** Get the GPG id, allowing for closure assignment. */
   String getGpgKeyId() {
      return gpgKeyId != null && gpgKeyId instanceof Callable ? gpgKeyId.call() : gpgKeyId
   }  

   /** Get the GPG secret key, allowing for closure assignment. */
   String getGpgSecretKey() {
      return gpgSecretKey != null && gpgSecretKey instanceof Callable ? gpgSecretKey.call() : gpgSecretKey
   }  

   /** Get the list of projects, allowing for closure assignment. */
   def getProjects() {
      return projects != null && projects instanceof Callable ? projects.call() : projects
   }  

}
