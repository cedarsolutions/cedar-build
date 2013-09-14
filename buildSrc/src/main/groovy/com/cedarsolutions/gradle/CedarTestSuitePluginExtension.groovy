// vim: set ft=groovy ts=3:
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// *              C E D A R
// *          S O L U T I O N S       "Software done right."
// *           S O F T W A R E
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * TestSuite (c) 2013 Kenneth J. Pronovici.
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
import java.util.concurrent.Callable

/** 
 * Plugin extension for cedarTestSuite. 
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarTestSuitePluginExtension {

   /** Project tied to this extension. */
   private Project project;

   /** Create an extension for a project. */
   public CedarTestSuitePluginExtension(Project project) {
      this.project = project;
   }

   /** The working directory to use for tests. */
   def workingDir;

   /** Get the working directory, allowing for closure assignment. */
   String getWorkingDir() {
      return workingDir != null && workingDir instanceof Callable ? workingDir.call() : workingDir
   }  

}

