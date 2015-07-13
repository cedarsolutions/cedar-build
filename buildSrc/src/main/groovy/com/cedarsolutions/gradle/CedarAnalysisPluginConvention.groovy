// vim: set ft=groovy ts=4 sw=4:
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// *              C E D A R
// *          S O L U T I O N S       "Software done right."
// *           S O F T W A R E
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Copyright (c) 2014 Kenneth J. Pronovici.
// * All rights reserved.
// *
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the Apache License, Version 2.0.
// * See LICENSE for more information about the licensing terms.
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Author   : Kenneth J. Pronovici <pronovic@ieee.org>
// * Language : Gradle (>= 2.5)
// * Project  : Common Gradle Build Functionality
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package com.cedarsolutions.gradle

import org.gradle.api.Project
import groovy.swing.SwingBuilder
import javax.swing.JFrame
import java.util.Properties

/**
 * Plugin convention for cedarAnalysis.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarAnalysisPluginConvention {

   /** Project tied to this convention. */
   private Project project;

   /** Create a convention for a project. */
   public CedarAnalysisPluginConvention(Project project) {
        this.project = project;
   }

   /**
    * Run JavaNCSS for a set of source folders on a single command-line.
    * This generates output onto the console as well as a complete XML report.
    * @param sourceFolders  List of source folders
    * See: http://stackoverflow.com/questions/7111362
    * See: http://stackoverflow.com/questions/13474842
    * See: http://stackoverflow.com/questions/19687050
    */
   def runJavancss(name, folders) {

        def reportDir = project.file(project.projectDir.canonicalPath + "/build/reports/ncss").canonicalPath
        def reportFile = project.file(reportDir + "/report.xml").canonicalPath

        project.file(reportDir).mkdirs()
        project.javaexec {
            classpath project.configurations.analysis
            main = "javancss.Main"
            args = [ "-recursive", "-all", "-xml", "-out", reportFile ] + folders
        }

        println ""
        println "================================================================="
        println "JavaNCSS Analysis for " + name
        println "================================================================="
        println "See: http://www.kclee.de/clemens/java/javancss/"
        println ""

        project.javaexec {
            classpath project.configurations.analysis
            main = "javancss.Main"
            args = [ "-recursive", "-ncss", "-package", ] + folders
        }

        println ""


   }

}
