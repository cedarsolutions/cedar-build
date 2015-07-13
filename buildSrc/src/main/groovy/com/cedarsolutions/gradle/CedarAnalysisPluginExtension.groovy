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
import java.util.concurrent.Callable

/**
 * Plugin extension for cedarAnalysis.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarAnalysisPluginExtension {

    /** Project tied to this extension. */
    private Project project;

    /** List of the source folders. */
    def sourceFolders;

    /** List of the test folders. */
    def testFolders;

    /** Create an extension for a project. */
    public CedarAnalysisPluginExtension(Project project) {
        this.project = project;
    }

    /** Get sourceFolders, accounting for closures. */
    def getSourceFolders() {
        return sourceFolders != null && sourceFolders instanceof Callable ? sourceFolders.call() : sourceFolders
    }

    /** Get testFolders, accounting for closures. */
    def getTestFolders() {
        return testFolders != null && testFolders instanceof Callable ? testFolders.call() : testFolders
    }

}
