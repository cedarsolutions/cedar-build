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
import org.gradle.api.Action

class CedarProperties implements Action<Plugin> {

   /** Project tied to this extension. */
   private Project project;

   /** Create an extension for a project. */
   public CedarProperties(Project project) {
      this.project = project;
   }

   /** Implementation of Action interface */
   void execute(Plugin plugin) {
      executeDeferrals()
   }

   /** Load standard properties files from disk, setting project.ext. */
   def loadStandardProperties() {
      loadProperties([ "build.properties", "local.properties", ])
   }

   /**
    * Load properties from disk in a standard way, setting project.ext.
    * @param files  List of properties files to load, in order
    */
   def loadProperties(files) {
      Properties properties = new Properties()
      project.logger.info("Cedar Build properties loader: loading project properties")

      files.each { file ->
         def fp = project.file(file)
         if (fp.isFile()) {
            fp.withInputStream {
               properties.load(it)
            }
         }
      }

      def added = 0
      properties.propertyNames().each { property ->
         project.logger.info("Set project.ext[" + property + "] to [" + properties.getProperty(property) + "]")
         project.ext[property] = properties.getProperty(property)
         added += 1
      }

      project.logger.lifecycle("CedarBuild properties loader: added ${added} project.ext properties from: " + files)
   }

}
