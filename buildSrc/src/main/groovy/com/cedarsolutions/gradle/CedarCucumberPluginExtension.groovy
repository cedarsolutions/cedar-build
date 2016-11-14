// vim: set ft=groovy ts=4 sw=4:
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// *              C E D A R
// *          S O L U T I O N S       "Software done right."
// *           S O F T W A R E
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Copyright (c) 2013-2016 Kenneth J. Pronovici.
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
import org.gradle.api.InvalidUserDataException
import java.io.File
import java.util.concurrent.Callable
import org.apache.tools.ant.taskdefs.condition.Os

/**
 * Plugin extension for cedarCucumber.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarCucumberPluginExtension {

    /** Project tied to this extension. */
    private Project project;

    /** Create an extension for a project. */
    public CedarCucumberPluginExtension(Project project) {
        this.project = project;
    }

    /** Path to the Ruby install directory. */
    def rubyInstallDir

    /** Directory that the Cucumber tests live in. */
    def cucumberDir

    /** Subdirectory (within cucumberDir) where features live. */
    def featuresSubdir

    /** Subdirectory (within cucumberDir) where Ruby code lives. */
    def rubySubdir

    /** Amount of memory to give JRuby when running cucumber. */
    def jrubyCucumberMemory;

    /** URL for JRuby. */
    def jrubyDownloadUrl = "http://jruby.org.s3.amazonaws.com/downloads/1.7.6/jruby-bin-1.7.6.tar.gz"

    /** Required version of Selenium, possibly null to get latest version. */
    def seleniumVersion = null

    /** Required version of Rspec gem, possibly null to get latest version. */
    def rspecVersion = "2.14.1"

    /** Required version of mime-types gem, possibly null to get latest version. */
    def mimeTypesVersion = "2.6.2"

    /** Required version of rack gem, possibly null to get latest version. */
    def rackVersion = "1.6.4";

    /** Required version of Capybara gem, possibly null to get latest version. */
    def capybaraVersion = "2.1.0"

    /** Required version of Cucumber gem, possibly null to get latest version. */
    def cucumberVersion = "1.3.8"

    /** Required version of Headless gem, possibly null to get latest version. */
    def headlessVersion = "1.0.1"

    /** Get the Cucumber formatter to use, or null to use the default. */
    def cucumberFormatter

    /** Get rubyInstallDir, accounting for closures. */
    String getRubyInstallDir() {
        return rubyInstallDir != null && rubyInstallDir instanceof Callable ? rubyInstallDir.call() : rubyInstallDir
    }

    /** Get cucumberDir, accounting for closures. */
    String getCucumberDir() {
        return cucumberDir != null && cucumberDir instanceof Callable ? cucumberDir.call() : cucumberDir
    }

    /** Get featuresSubdir, accounting for closures. */
    String getFeaturesSubdir() {
        return featuresSubdir != null && featuresSubdir instanceof Callable ? featuresSubdir.call() : featuresSubdir
    }

    /** Get rubySubdir, accounting for closures. */
    String getRubySubdir() {
        return rubySubdir != null && rubySubdir instanceof Callable ? rubySubdir.call() : rubySubdir
    }

    /** Get jrubyCucumberMemory, accounting for closures. */
    String getJrubyCucumberMemory() {
        return jrubyCucumberMemory != null && jrubyCucumberMemory instanceof Callable ? jrubyCucumberMemory.call() : jrubyCucumberMemory
    }

    /** Get jrubyDownloadUrl, accounting for closures. */
    String getJRubyDownloadUrl() {
        return jrubyDownloadUrl != null && jrubyDownloadUrl instanceof Callable ? jrubyDownloadUrl.call() : jrubyDownloadUrl
    }

    /** Get seleniumVersion, accounting for closures. */
    String getSeleniumVersion() {
        return seleniumVersion != null && seleniumVersion instanceof Callable ? seleniumVersion.call() : seleniumVersion
    }

    /** Get rspecVersion, accounting for closures. */
    String getRspecVersion() {
        return rspecVersion != null && rspecVersion instanceof Callable ? rspecVersion.call() : rspecVersion
    }

    /** Get mimeTypesVersion, accounting for closures. */
    String getMimeTypesVersion() {
        return mimeTypesVersion != null && mimeTypesVersion instanceof Callable ? mimeTypesVersion.call() : mimeTypesVersion
    }

    /** Get rackVersion, accounting for closures. */
    String getRackVersion() {
        return rackVersion != null && rackVersion instanceof Callable ? rackVersion.call() : rackVersion
    }

    /** Get capybaraVersion, accounting for closures. */
    String getCapybaraVersion() {
        return capybaraVersion != null && capybaraVersion instanceof Callable ? capybaraVersion.call() : capybaraVersion
    }

    /** Get cucumberVersion, accounting for closures. */
    String getCucumberVersion() {
        return cucumberVersion != null && cucumberVersion instanceof Callable ? cucumberVersion.call() : cucumberVersion
    }

    /** Get headlessVersion, accounting for closures. */
    String getHeadlessVersion() {
        return headlessVersion != null && headlessVersion instanceof Callable ? headlessVersion.call() : headlessVersion
    }

    /** Get cucumberFormatter, accounting for closures. */
    String getCucumberFormatter() {
        return cucumberFormatter != null && cucumberFormatter instanceof Callable ? cucumberFormatter.call() : cucumberFormatter
    }

    /** Indicates whether the Ruby interpreter is JRuby. */
    boolean isJRuby() {
        return getRubyPath().contains("jruby")
    }

    /** Get the path to the Ruby interpreter. */
    String getRubyPath() {
        if (isWindows()) {
            if (project.file(getRubyInstallDir() + "/bin/jruby.exe").exists()) {
                return project.file(getRubyInstallDir() + "/bin/jruby.exe").canonicalPath
            } else {
                return project.file(getRubyInstallDir() + "/bin/ruby.exe").canonicalPath
            }
        } else {
            if (project.file(getRubyInstallDir() + "/bin/jruby").exists()) {
                return project.file(getRubyInstallDir() + "/bin/jruby").canonicalPath
            } else {
                return project.file(getRubyInstallDir() + "/bin/ruby").canonicalPath
            }
        }
    }

    /** Get the path to the Ruby gem executable. */
    String getGemPath() {
        if (project.file(getRubyInstallDir() + "/bin/jgem").exists()) {
            return project.file(getRubyInstallDir() + "/bin/jgem").canonicalPath
        } else {
            return project.file(getRubyInstallDir() + "/bin/gem").canonicalPath
        }
    }

    /** Get the path to the Ruby cucumber executable. */
    String getCucumberPath() {
        return project.file(getRubyInstallDir() + "/bin/cucumber").canonicalPath
    }

    private boolean isWindows() {
        return Os.isFamily(Os.FAMILY_WINDOWS);
    }

}
