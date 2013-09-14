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

/** 
 * The cedarTestSuite plugin. 
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarTestSuitePlugin implements Plugin<Project> {

   /** Apply the plugin. */
   void apply(Project project) {

      // Add the extension
      project.extensions.create("cedarTestSuite", CedarTestSuitePluginExtension, project)

      // Run unit tests, assumed to be found in a class suites/UnitTestSuite. 
      // The caller that applies this plugin still has responsibility for making sure the suite gets compiled.
      project.task("unittest", type: org.gradle.api.tasks.testing.Test) {
         workingDir = { project.cedarTestSuite.getWorkingDir() == null ? project.projectDir : project.cedarTestSuite.getWorkingDir() }
         scanForTestClasses = false
         enableAssertions = false
         outputs.upToDateWhen { false }
         include "suites/UnitTestSuite.class"
      }

      // Run GWT client tests, assumed to be found in a class suites/ClientTestSuite. 
      // The caller that applies this plugin still has responsibility for making sure the suite gets compiled.
      project.task("clienttest", type: org.gradle.api.tasks.testing.Test) {
         workingDir = { project.cedarTestSuite.getWorkingDir() == null ? project.projectDir : project.cedarTestSuite.getWorkingDir() }
         scanForTestClasses = false
         scanForTestClasses = false
         enableAssertions = false
         outputs.upToDateWhen { false }

         systemProperty "gwt.args", "-out www-test -logLevel ERROR"
         systemProperty "java.awt.headless", "true"   // required on Linux to avoid deferred binding errors

         include "suites/ClientTestSuite.class"

         beforeSuite { descriptor ->
            def wwwTest = project.file(workingDir.canonicalPath + "/www-test")
            def gwtCache = project.file(workingDir.canonicalPath + "/gwt-unitCache")
            wwwTest.deleteDir()
            gwtCache.deleteDir()
         }

         afterSuite { descriptor ->
            def wwwTest = project.file(workingDir.canonicalPath + "/www-test")
            def gwtCache = project.file(workingDir.canonicalPath + "/gwt-unitCache")
            wwwTest.deleteDir()
            gwtCache.deleteDir()
         }
      }

      // Effectively disable the standard test runner by making it look for a bogus class. 
      project.tasks.test.include("**/bogus.class") 

      // Redefine the test runner in terms of the unit and client test suites.
      project.tasks.test.dependsOn(project.tasks.clienttest, project.tasks.unittest)
      project.tasks.clienttest.mustRunAfter project.tasks.unittest

   }


}
