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
import groovy.swing.SwingBuilder
import javax.swing.JFrame 
import java.util.Properties

class CedarBuildPluginConvention {

   /** Project tied to this convention. */
   private Project project;

   /** Create a convention for a project. */
   public CedarBuildPluginConvention(Project project) {
      this.project = project;
   }

   /** 
    * Configure Eclipse to ignore resources in a set of directories.
    * This adds a new stanza at the bottom of the Eclipse .project file.
    * @see: http://forums.gradle.org/gradle/topics/eclipse_generated_files_should_be_put_in_the_same_place_as_the_gradle_generated_files
    */
   def ignoreResourcesFromDirectories(provider, directories) {
      def filter = provider.asNode().appendNode("filteredResources").appendNode("filter")
      filter.appendNode("id", String.valueOf(System.currentTimeMillis()))  // this id must be unique
      filter.appendNode("name")
      filter.appendNode("type", "26")
      def matcher = filter.appendNode("matcher")
      matcher.appendNode("id", "org.eclipse.ui.ide.orFilterMatcher")
      def arguments = matcher.appendNode("arguments")
      directories.each {
         def dirMatcher = arguments.appendNode("matcher")
         dirMatcher.appendNode("id", "org.eclipse.ui.ide.multiFilter")
         dirMatcher.appendNode("arguments", "1.0-projectRelativePath-matches-true-false-${it}")
      }
   } 

   /**
    * Get input from the user, executing a closure with the result.
    * If the console is available, we'll use it. Otherwise, we'll fall back on a GUI pop-up.
    * @param title   Title for the input we're requesting
    * @param label   Label for the input field
    * @param secure  Whether the input is secure and should be masked
    * @param action  The action to execute when the input has been retrieved
    */
   def getInput(String title, String label, boolean secure, Closure action) {
      Console console = System.console()
      if (console != null) {
         getInputViaConsole(title, label, secure, action)
      } else {
         getInputViaPopup(title, label, secure, action)
      }
   }

   /**
    * Get input from a user via the system console, executing a closure with the result
    * @param title   Title for the input we're requesting
    * @param label   Label for the input field
    * @param secure  Whether the input is secure and should be masked
    * @param action  The action to execute when the input has been retrieved
    */
   def getInputViaConsole(String title, String label, boolean secure, Closure action) {
      Console console = System.console()
      if (secure) {
         def value = console.readPassword("\n\n[" + title + "] " + label + ": ")
         action(value)
      } else {
         def value = console.readLine("\n\n[" + title + "]" + label + ": ")
         action(value)
      }
   }

   /**
    * Get input from a user via the a GUI pop-up, executing a closure with the result
    * @param title   Title for the input we're requesting
    * @param label   Label for the input field
    * @param secure  Whether the input is secure and should be masked
    * @param action  The action to execute when the input has been retrieved
    */
   def getInputViaPopup(String title, String label, boolean secure, Closure action) {
      boolean alive = true

      def swing = new SwingBuilder()
      def button = swing.button("Ok")
      def prefix = swing.label(label)
      def value = null

      def frame = swing.frame(title: title, defaultCloseOperation: JFrame.EXIT_ON_CLOSE) {
         panel {
            widget(prefix)
            if (secure) {
               value = passwordField(columns:18)
            } else {
               value = textField(columns:18)
            }
            widget(button)
         }
      }

      frame.setLocationRelativeTo(null)

      button.actionPerformed = {
         action(value.text)
         alive = false
      }

      value.actionPerformed = {
         action(value.text)
         alive = false
      }

      frame.pack()
      frame.show()

      while (alive) {
         sleep(1000)
      }

      frame.hide()
   }

}
