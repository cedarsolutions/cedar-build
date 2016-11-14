// vim: set ft=groovy ts=4 sw=4:
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// *              C E D A R
// *          S O L U T I O N S       "Software done right."
// *           S O F T W A R E
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Copyright (c) 2013-2015 Kenneth J. Pronovici.
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
// * Project  : Secret Santa Exchange
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.cedarsolutions.gradle

import org.gradle.api.Project
import org.gradle.api.InvalidUserDataException
import org.apache.tools.ant.taskdefs.condition.Os
import java.nio.file.WatchService;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.FileSystems;
import java.util.concurrent.TimeUnit;

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
        return project.file(project.appengineDownloadSdk.explodedSdkDirectory.getPath() + 
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

        // See: http://stackoverflow.com/questions/14564858/groovy-get-java-home-from-program
        def javaHome = project.file(System.env.'JAVA_HOME').canonicalPath 
        def java = project.file(javaHome + "/bin/java").canonicalPath

        def xvfb = project.cedarGwtOnGae.getXvfbRunPath()
        def warDir = project.appengineExplodeApp.explodedAppDirectory.getPath()
        def workingDir = warDir
        def cacheDir = project.file(warDir + "/WEB-INF/appengine-generated").canonicalPath
        def classesDir = project.file(warDir + "/WEB-INF/classes").canonicalPath
        def libDir = project.file(warDir + "/WEB-INF/lib")

        def libJars = libDir.listFiles().findAll { it.name.endsWith(".jar") }.collect().sort()
        def devmodeJars = project.configurations.devmodeRuntime.each { jar -> jar.canonicalPath }.collect()
        def runtimeJars = project.configurations.providedRuntime.each { jar -> jar.canonicalPath }.collect()
        def sourceDirs = project.sourceSets.main.java.srcDirs.each { dir -> dir }.collect()
        def classpath = (libJars + [ classesDir, ] + devmodeJars + runtimeJars + sourceDirs).join(isWindows() ? ";" : ":")

        def noSuperDevMode = project.cedarGwtOnGae.isPostGwt27() ? "-nosuperDevMode" : "";

        project.file(cacheDir).deleteDir()  // clean up the database every time the server is rebooted

        if (project.cedarGwtOnGae.getIsHeadlessModeAvailable()) {
            project.ant.exec(executable: xvfb, dir: workingDir, spawn: project.cedarGwtOnGae.getSpawnProcesses()) {
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
                arg(line: noSuperDevMode)
                arg(line: "-codeServerPort")
                arg(value: project.cedarGwtOnGae.getDevmodeCodeserverPort())
                arg(line: "-port")
                arg(value: project.cedarGwtOnGae.getDevmodeServerPort())
                arg(line: "-server")
                arg(line: launcher)
                arg(value: project.cedarGwtOnGae.getAppEntryPoint())
            }
        } else {
            project.ant.exec(executable: java, dir: workingDir, spawn: project.cedarGwtOnGae.getSpawnProcesses()) {
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
                arg(line: noSuperDevMode)
                arg(line: "-codeServerPort")
                arg(value: project.cedarGwtOnGae.getDevmodeCodeserverPort())
                arg(line: "-port")
                arg(value: project.cedarGwtOnGae.getDevmodeServerPort())
                arg(line: "-server")
                arg(line: launcher)
                arg(value: project.cedarGwtOnGae.getAppEntryPoint())
            }
        }

        restoreApplicationJavascript()  // this also waits for the server to finish booting
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
        sleep project.cedarGwtOnGae.getStopWait()
        project.convention.plugins.cedarGwtOnGae.bootDevmode()
    }

    /** Archive off application Javascript files for safe-keeping. */
    private void archiveApplicationJavascript() {
        // See discussion below in restoreApplicationJavascript() regarding why this is necessary

        def warDir = project.appengineExplodeApp.explodedAppDirectory.getPath()
        def sourceDir = project.file(warDir + "/" + project.cedarGwtOnGae.getAppModuleName()).canonicalPath
        def archiveDir = project.file(project.projectDir.canonicalPath + "/build/tmp/javascript-archive").canonicalPath
        def nocacheJs = project.cedarGwtOnGae.getAppModuleName() + ".nocache.js"
        def devmodeJs = project.cedarGwtOnGae.getAppModuleName() + ".devmode.js"

        project.file(archiveDir).deleteDir()
        project.file(archiveDir).mkdirs()
        project.ant.copy(todir: archiveDir, overwrite: true, force: true) {
            fileset(dir: sourceDir) {
                include(name: nocacheJs)
                include(name: devmodeJs)
            }
        }
    }

   /** Restore archived application Javascript files. */
    private void restoreApplicationJavascript() {

        // GWT 2.7.0 changed the boot behavior for dev mode, relative to all previous versions
        // of GWT.  Any existing <app>.nocache.js file gets replaced when the server boots.
        // This breaks the application, and every URL gives the error "GWT module '<app>' may
        // need to be recompiled." The <app>.devmode.js file is also replaced at boot, but
        // since it is unchanged this doesn't make any difference.
        //
        // To work around this, we save off the original nocahe and devmode javascript files
        // when the application is compiled, and then  put them back after the server rewrites
        // them, and this usually solves the problem.  Originally, we saved off the files
        // within bootDevmode(), but it seems to work better to archive them once and always
        // work off a canonical source.
        //
        // Because we need to wait for the server to come up before copying in the original
        // files, this function also eliminates the need for a separate waitForDevmode()
        // function that sleeps waiting for the server to be ready.  Instead, we can watch for
        // the files to change and we know that the server is up once that happens.  This
        // appears to work in both GWT 2.7.0-RC1 and GWT 2.7.0, even though RC1 doesn't exhibit
        // the bug that overwrites the compiled code.  Worst-case, we fall back on the old
        // behavior: if no modified files are discovered, the wait loop still times out after
        // the configured server wait period.
        //
        // Note: you WILL have problems if you set server wait timeouts that are too small.
        // If you start seeing the error message shown above, the first thing to try is to
        // increase the timeouts.  Otherwise, it's possible that the loop below will give up
        // too early and restore the correct Javascript before the server boot process
        // has completed replacing it. There's a warning below that tries to give you a hint
        // about this situation.
        //
        // See the bug I filed: https://code.google.com/p/google-web-toolkit/issues/detail?id=9021

        def warDir = project.appengineExplodeApp.explodedAppDirectory.getPath()
        def sourceDir = project.file(warDir + "/" + project.cedarGwtOnGae.getAppModuleName()).canonicalPath
        def archiveDir = project.file(project.projectDir.canonicalPath + "/build/tmp/javascript-archive").canonicalPath
        def nocacheJs = project.cedarGwtOnGae.getAppModuleName() + ".nocache.js"
        def devmodeJs = project.cedarGwtOnGae.getAppModuleName() + ".devmode.js"

        WatchService watchService = FileSystems.getDefault().newWatchService()
        Path watchPath = Paths.get(sourceDir)
        watchPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY)

        def start = new Date()
        def elapsedSec = 0
        def foundNocacheJs = false
        def foundDevmodeJs = false

        while (elapsedSec < project.cedarGwtOnGae.getServerWait()) {
            WatchKey key = watchService.poll(1000, TimeUnit.MILLISECONDS);
            if (key != null) {
                for (WatchEvent<?> event: key.pollEvents()) {
                    if (event.kind.name() == "ENTRY_MODIFY") {
                        if (event.context.toFile().getName() == nocacheJs) {
                            foundNocacheJs = true;
                        } else if (event.context.toFile().getName() == devmodeJs) {
                            foundDevmodeJs = true;
                        }
                    }
                }

                if (foundNocacheJs && foundDevmodeJs) {
                    break;
                }

                key.reset()
            }

            elapsedSec = (new Date().time - start.time) / 1000.0
        }

        project.ant.copy(todir: sourceDir, overwrite: true, force: true) {
            fileset(dir: archiveDir) {
                include(name: nocacheJs)
                include(name: devmodeJs)
            }
        }

        if (!(foundNocacheJs && foundDevmodeJs)) {
            project.logger.warn("Warning: timed out waiting for server boot and restored application Javascript anyway.")
            project.logger.warn("If you see error \"GWT module '<app>' may need to be recompiled\", try increasing serverWait or stopWait.")
        }
    }

    private boolean isWindows() {
        return Os.isFamily(Os.FAMILY_WINDOWS);
    } 

}
