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
import java.io.File
import java.util.concurrent.Callable
import org.apache.tools.ant.taskdefs.condition.Os

/**
 * Plugin extension for cedarGwtOnGae.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarGwtOnGaePluginExtension {

    /** Project tied to this extension. */
    private Project project;

    /** Create an extension for a project. */
    public CedarGwtOnGaePluginExtension(Project project) {
        this.project = project;
    }

    /** Module name for the application, taken from the main .gwt.xml file. */
    def appModuleName

    /** Fully-qualified name of the GWT application to be executed. */
    def appEntryPoint

    /** Startup URL for the application, like "MySite.html". */
    def appStartupUrl

    /** Amount of memory to give the GWT compiler, like "256M". */
    def gwtCompilerMemory

    /** Port to be used for HTTP traffic */
    def devmodeServerPort

    /** Port to be used for the GWT codeserver. */
    def devmodeCodeserverPort

    /** Amount of memory to give the devmode server, like "512M". */
    def devmodeServerMemory

    /** Expected boot time for the server, in seconds. */
    def serverWait
   
    /** The version of Google App Engine. */
    def appEngineVersion

    /** Get appModuleName, accounting for closures. */
    String getAppModuleName() {
        return appModuleName != null && appModuleName instanceof Callable ? appModuleName.call() : appModuleName
    }

    /** Get appEntryPoint, accounting for closures. */
    String getAppEntryPoint() {
        return appEntryPoint != null && appEntryPoint instanceof Callable ? appEntryPoint.call() : appEntryPoint
    }

    /** Get appStartupUrl, accounting for closures. */
    String getAppStartupUrl() {
        return appStartupUrl != null && appStartupUrl instanceof Callable ? appStartupUrl.call() : appStartupUrl
    }

    /** Get gwtCompilerMemory, accounting for closures. */
    String getGwtCompilerMemory() {
        return gwtCompilerMemory != null && gwtCompilerMemory instanceof Callable ? gwtCompilerMemory.call() : gwtCompilerMemory
    }

    /** Get devmodeServerPort, accounting for closures. */
    String getDevmodeServerPort() {
        return devmodeServerPort != null && devmodeServerPort instanceof Callable ? devmodeServerPort.call() : devmodeServerPort
    }

    /** Get devmodeCodeserverPort, accounting for closures. */
    String getDevmodeCodeserverPort() {
        return devmodeCodeserverPort != null && devmodeCodeserverPort instanceof Callable ? devmodeCodeserverPort.call() : devmodeCodeserverPort
    }

    /** Get devmodeServerMemory, accounting for closures. */
    String getDevmodeServerMemory() {
        return devmodeServerMemory != null && devmodeServerMemory instanceof Callable ? devmodeServerMemory.call() : devmodeServerMemory
    }

    /** Get serverWait, accounting for closures. */
    Integer getServerWait() {
        String result = serverWait != null && serverWait instanceof Callable ? serverWait.call() : serverWait
        return result == null ? null : Integer.parseInt(result)
    }

    /** Get appEngineVersion, accounting for closures. */
    String getAppEngineVersion() {
        return appEngineVersion != null && appEngineVersion instanceof Callable ? appEngineVersion.call() : appEngineVersion
    }

    /** Validate the GWT configuration. */
    def validateGwtConfig() {
        if (getAppModuleName() == null || getAppModuleName() == "unset") {
            throw new InvalidUserDataException("GWT error: appModuleName is unset")
        }

        if (getAppEntryPoint() == null || getAppEntryPoint() == "unset") {
            throw new InvalidUserDataException("GWT error: appEntryPoint is unset")
        }

        if (getAppStartupUrl() == null || getAppStartupUrl() == "unset") {
            throw new InvalidUserDataException("GWT error: appStartupUrl is unset")
        }

        if (getGwtCompilerMemory() == null || getGwtCompilerMemory() == "unset") {
            throw new InvalidUserDataException("GWT error: gwtCompilerMemory is unset")
        }

        if (getDevmodeServerPort() == null || getDevmodeServerPort() == "unset") {
            throw new InvalidUserDataException("GWT error: devmodeServerPort is unset")
        }

        if (getDevmodeCodeserverPort() == null || getDevmodeCodeserverPort() == "unset") {
            throw new InvalidUserDataException("GWT error: devmodeCodeserverPort is unset")
        }

        if (getDevmodeServerMemory() == null || getDevmodeServerMemory() == "unset") {
            throw new InvalidUserDataException("GWT error: devmodeServerMemory is unset")
        }

        if (getDevmodeServerMemory() == null || getDevmodeServerMemory() == "unset") {
            throw new InvalidUserDataException("GWT error: devmodeServerMemory is unset")
        }
    }

}
