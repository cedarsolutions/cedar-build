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

/** 
 * Plugin action for cedarProperties. 
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
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

   /**
    * Load properties from a GWT Constants file.
    * This supports fields annotated with \@DefaultStringValue, \@DefaultIntValue, and \@DefaultBooleanValue.
    * @param file    Path to the GWT constants class on disk
    * @param names   List of property names to be pulled into the project namespace
    */
   def loadGwtProperties(file, names) {
      project.logger.info("Cedar Build GWT properties loader: loading GWT properties")

      def added = 0
      names.each { name ->
         def regex
         def matcher
         def contents = new File(file).getText()

         regex = ~/(?s)(@DefaultStringValue[(]["])([^"]*)(["])([)])(\s*)(String\s+)(${name})([(][)];)/
         matcher = regex.matcher(contents)
         while (matcher.find()) {
            project.ext[matcher.group(7)] = matcher.group(2)
            project.logger.info("Set project.ext[" + matcher.group(7) + "] to [" + matcher.group(2) + "]")
            added += 1
         }

         regex = ~/(?s)(@DefaultIntValue[(])([.0-9]*)([)])(\s*)((int|Integer)\s+)(${name})([(][)];)/
         matcher = regex.matcher(contents)
         while (matcher.find()) {
            project.ext[matcher.group(7)] = Integer.parseInt(matcher.group(2))
            project.logger.info("Set project.ext[" + matcher.group(7) + "] to [" + matcher.group(2) + "]")
            added += 1
         }

         regex = ~/(?s)(@DefaultBooleanValue[(])(true|false)([)])(\s*)((boolean|Boolean)\s+)(${name})([(][)];)/
         matcher = regex.matcher(contents)
         while (matcher.find()) {
            project.ext[matcher.group(7)] = matcher.group(2) == "true" ? true : false
            project.logger.info("Set project.ext[" + matcher.group(7) + "] to [" + matcher.group(2) + "]")
            added += 1
         }
      }

      project.logger.lifecycle("CedarBuild GWT properties loader: added ${added} project.ext properties from: " + project.file(file).name)
   }

}
