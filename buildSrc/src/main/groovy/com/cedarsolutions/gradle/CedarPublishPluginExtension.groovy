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

class CedarPublishPluginExtension {

   /** Project tied to this extension. */
   private Project project;

   /** Create an extension for a project. */
   public CedarPublishPluginExtension(Project project) {
      this.project = project;
   }

   /** Path to the Mercurial-based Maven project that code will be published into. */
   String mercurialMavenProject

   /** Whether digital signatures are required for the current publish actions. */
   def isSignatureRequired() {
      // Gradle's behavior varies depending on whether there are subprojects.
      return project.cedarPublish.isMercurialRepositoryConfigured() &&
             (project.gradle.taskGraph.hasTask(":uploadArchives") || 
              project.gradle.taskGraph.hasTask(":${project.name}:uploadArchives"));
   }

   /** Get the proper Mercurial-based Maven repository URL. */
   def getPublishRepositoryUrl() {
      if (!isMercurialRepositoryConfigured()) {
         return null;
      } else {
         return "file://" + new File(mercurialMavenProject).canonicalPath.replace("\\", "/") + "/maven"
      }
   }

   /** Whether a valid Mercurial-based Maven repository is configured. */
   def isMercurialRepositoryConfigured() {
      if (mercurialMavenProject == null || mercurialMavenProject == "unset") {
         return false
      } else {
         if (!(new File(mercurialMavenProject).isDirectory()
               && new File(mercurialMavenProject + "/.hg").isDirectory()
               && new File(mercurialMavenProject + "/maven").isDirectory())) {
            return false
         } else {
            return true
         }
      }
   }

   /** Validate the Mercurial-based Maven repository URL. */
   def validateMavenRepositoryConfig() {
      if (mercurialMavenProject == null || mercurialMavenProject == "unset") {
         throw new InvalidUserDataException("Publish error: mercurialMavenProject is unset")
      } 

      if (!(new File(mercurialMavenProject).isDirectory()
               && new File(mercurialMavenProject + "/.hg").isDirectory()
               && new File(mercurialMavenProject + "/maven").isDirectory())) {
         throw new InvalidUserDataException("Publish error: not a Mercurial-based Maven repository: " + mercurialMavenProject)
      }
   }

}
