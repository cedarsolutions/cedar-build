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
// * Project  : Secret Santa Exchange
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.cedarsolutions.gradle

import org.gradle.api.Project
import org.gradle.api.InvalidUserDataException
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.util.Jvm

/**
 * Plugin convention for cedarGwtOnGae.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarGwtOnGaePluginConvention {

    /** The project tied to this convention. */
    private Project project;

    /** Create a convention tied to a project. */
    public CedarGwtOnGaePluginConvention(Project project) {
        this.project = project
    }

    /** Clean up the test directories that are created in the workspace. */
    public void cleanupCacheDirs() {
        project.file("gwt-unitCache").deleteDir()
        project.file("www-test").deleteDir()
        project.file("build/gwtUnitCache").deleteDir()
    }

    /** Get the location of the exploded App Engine SDK directory on disk. */
    public String getAppEngineSdkDir() {
        return project.file(project.gaeDownloadSdk.explodedSdkDirectory.getPath() + 
                            "/appengine-java-sdk-" + 
                            project.cedarGwtOnGae.getAppEngineVersion()).canonicalPath
    }

    /** Get the location of the appengine agent jar. */
    public String getAppEngineAgentJar() {
        return project.file(getAppEngineSdkDir() + "/lib/agent/appengine-agent.jar").canonicalPath
    } 

    /** Boot the development mode server. */
    public void bootDevmode() {
        def serverClass = "com.google.gwt.dev.DevMode"
        def agentJar = getAppEngineAgentJar()
        def launcher = "com.google.appengine.tools.development.gwt.AppEngineLauncher"

        def xvfb = project.cedarGwtOnGae.getXvfbRunPath()
        def java = Jvm.current().getJavaExecutable().canonicalPath
        def warDir = project.gaeExplodeWar.explodedWarDirectory.getPath()
        def workingDir = warDir
        def cacheDir = project.file(warDir + "/WEB-INF/appengine-generated").canonicalPath
        def classesDir = project.file(warDir + "/WEB-INF/classes").canonicalPath
        def libDir = project.file(warDir + "/WEB-INF/lib")

        def libJars = libDir.listFiles().findAll { it.name.endsWith(".jar") }.collect().sort()
        def devmodeJars = project.configurations.devmodeRuntime.each { jar -> jar.canonicalPath }.collect()
        def runtimeJars = project.configurations.providedRuntime.each { jar -> jar.canonicalPath }.collect()
        def sourceDirs = project.sourceSets.main.java.srcDirs.each { dir -> dir }.collect()
        def classpath = (libJars + [ classesDir, ] + devmodeJars + runtimeJars + sourceDirs).join(isWindows() ? ";" : ":")

        project.file(cacheDir).deleteDir()  // clean up the database every time the server is rebooted

        if (project.cedarGwtOnGae.getIsHeadlessModeAvailable()) {
            project.ant.exec(executable: xvfb, dir: workingDir, spawn: true) {
                arg(value: "--auto-servernum")
                arg(value: "--server-num=300")
                arg(value: java)
                arg(value: "-javaagent:" + agentJar)
                arg(value: "-Xmx" + project.cedarGwtOnGae.getDevmodeServerMemory())
                arg(value: "-XX:MaxPermSize=" + project.cedarGwtOnGae.getDevmodeServerPermgen())
                arg(line: "-classpath")
                arg(value: classpath)
                arg(value: serverClass)
                arg(line: "-startupUrl")
                arg(value: project.cedarGwtOnGae.getAppStartupUrl())
                arg(line: "-war")
                arg(value: warDir)
                arg(line: "-logLevel")
                arg(value: "INFO")
                arg(line: "-codeServerPort")
                arg(value: project.cedarGwtOnGae.getDevmodeCodeserverPort())
                arg(line: "-port")
                arg(value: project.cedarGwtOnGae.getDevmodeServerPort())
                arg(line: "-server")
                arg(line: launcher)
                arg(value: project.cedarGwtOnGae.getAppEntryPoint())
            }
        } else {
            project.ant.exec(executable: java, dir: workingDir, spawn: true) {
                arg(value: "-javaagent:" + agentJar)
                arg(value: "-Xmx" + project.cedarGwtOnGae.getDevmodeServerMemory())
                arg(value: "-XX:MaxPermSize=" + project.cedarGwtOnGae.getDevmodeServerPermgen())
                arg(line: "-classpath")
                arg(value: classpath)
                arg(value: serverClass)
                arg(line: "-startupUrl")
                arg(value: project.cedarGwtOnGae.getAppStartupUrl())
                arg(line: "-war")
                arg(value: warDir)
                arg(line: "-logLevel")
                arg(value: "INFO")
                arg(line: "-codeServerPort")
                arg(value: project.cedarGwtOnGae.getDevmodeCodeserverPort())
                arg(line: "-port")
                arg(value: project.cedarGwtOnGae.getDevmodeServerPort())
                arg(line: "-server")
                arg(line: launcher)
                arg(value: project.cedarGwtOnGae.getAppEntryPoint())
            }
        }
    }

    /** Kill the development mode server. */
    public void killDevmode() {
        if (isWindows()) {
            // AFAIK, there's no better way to do this than to kill the window with the known title
            project.ant.exec(executable: "taskkill") {
                arg(value: "/fi")
                arg(value: '"Windowtitle eq GWT Development Mode"')
            }
        } else {
            // This is the equivalent of: kill $(ps -fww -C java | grep '-javaagent:.*appengine-agent\.jar' | awk '{print $2}')

            def stdout = new ByteArrayOutputStream()
            def stderr = new ByteArrayOutputStream()

            def result = project.exec {
                standardOutput = stdout
                errorOutput = stderr
                executable = "ps"
                args = [ "-fww", "-C", "java", ]
            }

            def contents = stdout.toString()
            for (String line : contents.split("\n")) {
                def regex = ~/(^.*)(-javaagent:.*appengine-agent\.jar)(.*$)/
                def matcher = regex.matcher(line)
                if (matcher.matches()) {
                    project.exec {
                        standardOutput = stdout
                        errorOutput = stderr
                        executable = "kill"
                        args = [ line.split(/\s+/)[1], ]
                    }
                } 
            }
        }
    }

    /** Reboot devmode, stopping and then starting it. */
    public void rebootDevmode() {
        project.convention.plugins.cedarGwtOnGae.killDevmode()
        project.convention.plugins.cedarGwtOnGae.bootDevmode()
    }

    /** Wait for devmode to start, based on configuration. */
    def waitForDevmode() {
        sleep project.cedarGwtOnGae.getServerWait() * 1000;  // wait for dev mode to finish booting
    }

    private boolean isWindows() {
        return Os.isFamily(Os.FAMILY_WINDOWS);
    } 

}
