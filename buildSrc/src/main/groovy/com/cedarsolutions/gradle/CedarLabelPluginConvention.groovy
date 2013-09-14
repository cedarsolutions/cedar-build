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

/** 
 * Plugin convention for cedarLabel. 
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarLabelPluginConvention {

   /** The project tied to this convention. */
   private Project project;

   /** Create a convention tied to a project. */
   public CedarLabelPluginConvention(Project project) {
      this.project = project
   }

   /** Label Mercurial repositories per configuration. */
   def labelMercurialRepositories() {
      def projectName = project.cedarLabel.getProjectName()
      def projectVersion = project.cedarLabel.getProjectVersion()
      def repositories = project.cedarLabel.getRepositories()
      def mercurialPath = project.cedarLabel.getMercurialPath()
      if (repositories == null || repositories.isEmpty()) {
         project.logger.lifecycle("CedarBuild label tool: no Mercurial repositories to label")
      } else {
         def label = generateLabel(projectName, projectVersion)
         project.logger.lifecycle("CedarBuild label tool: applying ${label}")
         repositories.each { repository ->
            project.logger.lifecycle(" --> ${repository}")
            project.ant.exec(executable: mercurialPath, dir: repository, failonerror: "true") {
               arg(value: "tag")
               arg(value: "-f")
               arg(value: label)
            }
         }
      }
   }

   /** Generate a standard label based on name and version. */
   def generateLabel(String projectName, String projectVersion) {
      def timestamp = new Date().format("yyyyMMddHHmmssSSS", TimeZone.getTimeZone("UTC"))
      return "${projectName}__v${projectVersion}__${timestamp}"
   }

}
