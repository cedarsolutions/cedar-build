// vim: set ft=groovy ts=4 sw=4:
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
import org.apache.tools.ant.taskdefs.condition.Os

/**
 * Plugin convention for cedarCucumber.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarCucumberPluginConvention {

    /** The project tied to this convention. */
    private Project project;

    /** Create a convention tied to a project. */
    public CedarCucumberPluginConvention(Project project) {
        this.project = project
    }

    /**
     * Execute the cucumber tests, returning an ExecResult so caller can handle failures.
     * If you want the database to start over fresh, you need to reboot dev mode.
     * You can optionally provide either name or feature, but not both.
     * @param mode      The mode to run in, either "single-pass", "first-pass" or "second-pass"
     * @param name      Specific name of a test, or a substring, as for the Cucumber --name option
     * @param feature   Path to a specific feature to execute, relative to the acceptance/cucumber directory
     * See: http://jeannotsweblog.blogspot.com/2013/02/cucumber-10-command-line.html
     */
    def execCucumber(String mode, String name, String feature) {
        def command = []
        def rerunEnabled = false
        def rerunFile = project.file(project.projectDir.canonicalPath + "/build/tmp/rerun.txt").canonicalPath
        def reportDir = project.file(project.projectDir.canonicalPath + "/build/reports/cucumber").canonicalPath
        def firstPass = project.file(reportDir + "/first-pass.json").canonicalPath
        def secondPass = project.file(reportDir + "/second-pass.json").canonicalPath

        if ("single-pass".equals(mode)) {
            println("\r\nExecuting Cucumber acceptance tests, single-pass mode")
        } else if ("first-pass".equals(mode)) {
            println("\r\nExecuting Cucumber acceptance tests, pass 1 of 2")
        } else if ("second-pass".equals(mode)) {
            println("\r\nExecuting Cucumber acceptance tests, pass 2 of 2 (retry failed tests)")
        }

        if (project.cedarCucumber.isJRuby()) {
            // Work around problems with the 1.6 JVM and Selenium with JRuby.
            // Without this option set, Selenium hangs forever trying to open Firefox.
            // See: http://stackoverflow.com/questions/10983307/jruby-watir-is-hanging-when-launching-browser
            command += [ "-J-Djava.net.preferIPv4Stack=true", ]

            // Larger Cucumber test suites use more than the default 500 MB of memory in JRuby
            if (project.cedarCucumber.getJrubyCucumberMemory() != null) {
                command += [ "-J-Xmx" + project.cedarCucumber.getJrubyCucumberMemory(), ]
            }
        }

        command += [ project.cedarCucumber.getCucumberPath(), ]
        command += [ "--require", project.cedarCucumber.getRubySubdir(), ]

        if (project.cedarCucumber.getCucumberFormatter() != null) {
            command += [ "--format", project.cedarCucumber.getCucumberFormatter(), ]
        }

        project.file(reportDir).mkdirs()

        // We need to make sure that at the end of the run, only one result is left, the one
        // for the latest attempt.  Otherwise, the Jenkins Cucumber plugin might notice a
        // failure in one of the other files and fail the entire build. 
        project.file(firstPass).delete()
        project.file(secondPass).delete()

        if ("first-pass".equals(mode)) {
            rerunEnabled = true
            project.file(rerunFile).delete()
        }

        if ("second-pass".equals(mode)) {
            command += [ "@" + rerunFile, ]
            command += [ "--format", "json", "--out", secondPass, ]
        } else {
            command += [ "--format", "json", "--out", firstPass, ]
            if (name != null) {
                command += "--name"
                command += name.replaceAll('"', "")  // quotes cause problems, so just remove them and the test won't be found
                command += project.cedarCucumber.getFeaturesSubdir()
            } else if (feature != null) {
                command += project.cedarCucumber.getFeaturesSubdir() + "/" + feature
            } else {
                command += project.cedarCucumber.getFeaturesSubdir()
            }
        }

        if (rerunEnabled) {
            return project.exec {
                workingDir = project.cedarCucumber.getCucumberDir()
                ignoreExitValue = true
                environment RERUN_FILE: rerunFile
                executable = project.cedarCucumber.getRubyPath()
                args = command
            }
        } else {
            return project.exec {
                workingDir = project.cedarCucumber.getCucumberDir()
                ignoreExitValue = true
                executable = project.cedarCucumber.getRubyPath()
                args = command
            }
        }
    }

    /** Verify that all of the required components have been installed in order to run Cucumber. */
    def verifyCucumberInstall() {
        project.logger.lifecycle("Using Cucumber from: " + project.cedarCucumber.getRubyInstallDir())
        verifyRuby()
        verifyGem()
        verifyCucumber()
        verifyGemVersions()
        project.logger.lifecycle("Cucumber install is ok.")
    }

    /** Install Cucumber, including Ruby and all of the other required dependencies.  */
    def installCucumber() {
        if (project.cedarCucumber.getRubyInstallDir() != "tools/cucumber") {
            project.logger.error("Project is configured to use Ruby from: " + project.cedarCucumber.getRubyInstallDir())
            throw new InvalidUserDataException("Before running installing Cucumber, re-configure project to use tools/cucumber")
        } else {
            // Only install if it's not there.  That way, we can always use installCucumber from continuous integration.
            if (!project.file(project.cedarCucumber.getRubyInstallDir()).exists()) {
                installJRuby(project.cedarCucumber.getJRubyDownloadUrl())
                installGem("selenium-webdriver", project.cedarCucumber.getSeleniumVersion())
                installGem("rspec", project.cedarCucumber.getRspecVersion())
                installGem("capybara", project.cedarCucumber.getCapybaraVersion())
                installGem("cucumber", project.cedarCucumber.getCucumberVersion())
                installGem("headless", project.cedarCucumber.getHeadlessVersion())
                verifyCucumberInstall()
                project.logger.lifecycle("All Cucumber tooling has been installed.")
            }
        }
    }

    /** Uninstall Cucumber, removing the install directory. */
    def uninstallCucumber() {
        if (project.file("tools").exists()) {
            if (project.file("tools/cucumber").exists()) {
                project.file("tools/cucumber").deleteDir()
                project.logger.lifecycle("All Cucumber tooling has been uninstalled.")
            }

            project.ant.delete(includeemptydirs : "true", quiet : "true") {
                fileset(dir: "tools", excludes : "**/*")  // remove tools only if empty
            }
        }
    }

    /** Verify that the Ruby interpreter is available. */
    private void verifyRuby() {
        if (!isRubyAvailable()) {
            project.logger.error("Ruby interpreter not available: " + project.cedarCucumber.getRubyPath())
            project.logger.error("You must either run the installCucumber task or install Ruby and Cucumber manually.")
            throw new InvalidUserDataException("Ruby interpreter not available: " + project.cedarCucumber.getRubyPath())
        }
    }

    /** Verify that the Ruby 'gem' tool is available. */
    private void verifyGem() {
        if (!isGemAvailable()) {
            project.logger.error("Ruby 'gem' tool not available: " + project.cedarCucumber.getGemPath())
            project.logger.error("You must either run the installCucumber task or install Ruby and Cucumber manually.")
            throw new InvalidUserDataException("Ruby 'gem' tool not available: " + project.cedarCucumber.getGemPath())
        }
    }

    /** Verify that the Cucumber tool is available. */
    private void verifyCucumber() {
        if (!isCucumberAvailable()) {
            project.logger.error("Cucumber tool not available: " + project.cedarCucumber.getCucumberPath())
            project.logger.error("You must either run the installCucumber task or install Ruby and Cucumber manually.")
            throw new InvalidUserDataException("Cucumber tool not available: " + project.cedarCucumber.getCucumberPath())
        }
    }

    /** Verify the versions of some specific gems. */
    private void verifyGemVersions() {
        if (project.cedarCucumber.getSeleniumVersion() != null) {
            def seleniumVersion = getGemVersion("selenium-webdriver")
            if (!seleniumVersion != project.cedarCucumber.getSeleniumVersion()) {
                def version = project.cedarCucumber.getSeleniumVersion()
                project.logger.warn("Cucumber tests might not work due to version mismatch; expected Selenium ${version}, but got: " + seleniumVersion)
            }
        }

        if (project.cedarCucumber.getRspecVersion() != null) {
            def rspecVersion = getGemVersion("rspec")
            if (rspecVersion != project.cedarCucumber.getRspecVersion()) {
                def version = project.cedarCucumber.getRspecVersion()
                project.logger.warn("Cucumber tests might not work due to version mismatch; expected Rspec ${version}, but got: " + rspecVersion)
            }
        }

        if (project.cedarCucumber.getCapybaraVersion() != null) {
            def capybaraVersion = getGemVersion("capybara")
            if (capybaraVersion != project.cedarCucumber.getCapybaraVersion()) {
                def version = project.cedarCucumber.getCapybaraVersion()
                project.logger.warn("Cucumber tests might not work due to version mismatch; expected Capybara ${version}, but got: " + capybaraVersion)
            }
        }

        if (project.cedarCucumber.getCucumberVersion() != null) {
            def cucumberVersion = getGemVersion("cucumber")
            if (cucumberVersion != project.cedarCucumber.getCucumberVersion()) {
                def version = project.cedarCucumber.getCucumberVersion()
                project.logger.warn("Cucumber tests might not work due to version mismatch; expected Cucumber ${version}, but got: " + cucumberVersion)
            } 
        }

        if (project.cedarCucumber.getHeadlessVersion() != null) {
            def headlessVersion = getGemVersion("headless")
            if (headlessVersion != project.cedarCucumber.getHeadlessVersion()) {
                def version = project.cedarCucumber.getHeadlessVersion()
                project.logger.warn("Cucumber tests might not work due to version mismatch; expected Headless ${version}, but got: " + headlessVersion)
            }
        }
    }

    /** Check whether the Ruby interpreter is available. */
    private boolean isRubyAvailable() {
        return isCommandAvailable(project.cedarCucumber.getRubyPath(), [ "--version", ])
    }

    /** Check whether the Ruby 'gem' tool is available. */
    private boolean isGemAvailable() {
        return isCommandAvailable(project.cedarCucumber.getRubyPath(), [ project.cedarCucumber.getGemPath(), "--version", ])
    }

    /** Check whether the Cucumber tool is available. */
    private boolean isCucumberAvailable() {
        return isCommandAvailable(project.cedarCucumber.getRubyPath(), [ project.cedarCucumber.getCucumberPath(), "--version", ])
    }

    /** Get the installed version of a Ruby gem. */
    private String getGemVersion(gem) {
        try {
            def stdout = new ByteArrayOutputStream()
            def stderr = new ByteArrayOutputStream()

            def result = project.exec {
                standardOutput = stdout
                errorOutput = stderr
                executable = project.cedarCucumber.getRubyPath()
                args = [ project.cedarCucumber.getGemPath(), "list", gem, ]
            }

            def contents = stdout.toString()
            def regex = ~/(?s)(^${gem} [(])([.0-9]*)([,)].*$)/       // for a string like "selenium-webdriver (2.37.0)"
            def matcher = regex.matcher(contents)
            if (matcher.matches()) {
                return matcher.group(2)
            } 
        } catch (Exception e) { }

        project.logger.error("Error checking version for Ruby gem: " + gem)
        project.logger.error("You must either run the installCucumber task or install Ruby and Cucumber manually.")
        throw new InvalidUserDataException("Error checking version for Ruby gem: " + gem)
    }

    /** 
     * Verify whether a command is available by executing it.
     * @param command     The executable to check
     * @param arguments   Arguments to pass to the command
     * @return True if the command could be executed, false otherwise.
     */
    private boolean isCommandAvailable(command, arguments) {
        try {
            def devnull = new ByteArrayOutputStream()

            def result = project.exec {
                standardOutput = devnull
                errorOutput = devnull
                executable = command
                args = arguments
            }

            return result.getExitValue() == 0
        } catch (Exception e) {
            return false
        }
    }

    /** Install JRuby from a particular URL, expected to be a .tar.gz file. */
    private void installJRuby(url) {
        try {
            project.logger.lifecycle("Downloading JRuby from: " + url)
            project.file("build/tmp/jruby").deleteDir()
            project.file("build/tmp/jruby").mkdirs()
            project.ant.get(src: url, dest: "build/tmp/jruby/jruby.tar.gz")
            if (!project.file("build/tmp/jruby/jruby.tar.gz").exists()) {
                project.logger.error("jruby.tar.gz does not exist?")
                throw new InvalidUserDataException("Error downloading JRuby, file not retrieved.")
            }

            project.logger.lifecycle("Installing JRuby...")
            if (isWindows()) {
                project.ant.untar(src: "build/tmp/jruby/jruby.tar.gz", dest: "build/tmp/jruby", compression: "gzip")
            } else {
                // The Ant task doesn't preserve permissions, so use tar on other platforms
                def stdout = new ByteArrayOutputStream()
                def stderr = new ByteArrayOutputStream()
                def result = project.exec {
                    ignoreExitValue = true
                    workingDir = "build/tmp/jruby"
                    standardOutput = stdout
                    errorOutput = stderr
                    executable = "tar"
                    args = [ "zxvf", "jruby.tar.gz", ]
                }
            }

            project.file("tools").mkdirs()
            project.file("build/tmp/jruby").eachDir() { dir ->
                dir.renameTo("tools/cucumber") // there should be only one directory, like jruby-1.7.6
            }
        } catch (Exception e) { 
            throw new InvalidUserDataException("Error installing JRuby: " + e.getMessage(), e);
        } finally {
            project.file("build/tmp/jruby").deleteDir()
        }
    }

    /** Install a named gem, optionally specifying a version. */
    private void installGem(gem, version) {
        try {
            def arguments = [ project.cedarCucumber.getGemPath(), "install", gem, ]
            if (version != null) {
                arguments += [ "-v", version ]
            }

            if (version != null) {
                project.logger.lifecycle("Installing gem: " + gem + ", version " + version)
            } else {
                project.logger.lifecycle("Installing gem: " + gem + ", latest version")
            }
            
            def output = new ByteArrayOutputStream()
            def result = project.exec {
                ignoreExitValue = true
                standardOutput = output
                errorOutput = output
                executable = project.cedarCucumber.getRubyPath()
                args = arguments
            }

            if (result.getExitValue() != 0) {
                println(output.toString())
                project.logger.lifecycle("Note: gem install mirrors can sometimes be unavailable; you may want to try again later.")
                result.assertNormalExitValue()
            }
        } catch (Exception e) { 
            throw new InvalidUserDataException("Error installing gem: " + e.getMessage(), e);
        }
    }

    private boolean isWindows() {
        return Os.isFamily(Os.FAMILY_WINDOWS);
    }

}
