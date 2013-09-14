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
import java.util.concurrent.Callable

/** 
 * Plugin extension for cedarCopyright. 
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarCopyrightPluginExtension {

   /** Project tied to this extension. */
   private Project project;

   /** Create an extension for a project. */
   public CedarCopyrightPluginExtension(Project project) {
      this.project = project;
   }

   /** Regex pattern used to identify license files. */
   def licensePattern;

   /** List of regex patterns used to identify source files. */
   def sourcePatterns;

   /** List of Mercurial directories that should be labeled. */
   def repositories 

   /** Path to the Mercurial exectuable. */
   def mercurialPath
   
   /** Get the license patterns, allowing for closure assignment. */
   String getLicensePattern() {
      return licensePattern != null && licensePattern instanceof Callable ? licensePattern.call() : licensePattern
   }  

   /** Get the source patterns, allowing for closure assignment. */
   def getSourcePatterns() {
      return sourcePatterns != null && sourcePatterns instanceof Callable ? sourcePatterns.call() : sourcePatterns
   }  

   /** Get the repositories list, allowing for closure assignment. */
   def getRepositories() {
      return repositories != null && repositories instanceof Callable ? repositories.call() : repositories
   }

   /** Get the Mercurial path, allowing for closure assignment. */
   String getMercurialPath() {
      return mercurialPath != null && mercurialPath instanceof Callable ? mercurialPath.call() : mercurialPath
   } 

   /** Validate the copyright configuration. */
   def validateCopyrightConfig() {
      if (getRepositories() != null && !getRepositories().isEmpty()) { 
         if (getLicensePattern() == null || getLicensePattern() == "unset") {
            throw new InvalidUserDataException("Copyright error: licensePattern is unset")
         }

         if (getSourcePatterns() == null || getSourcePatterns().isEmpty()) {
            throw new InvalidUserDataException("Copyright error: sourcePatterns is unset")
         }

         if (getMercurialPath() == null || getMercurialPath() == "unset") {
            throw new InvalidUserDataException("Copyright error: mercurialPath is unset")
         } 
      }
   }

}

