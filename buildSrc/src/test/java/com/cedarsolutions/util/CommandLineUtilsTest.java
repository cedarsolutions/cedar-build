/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013 Kenneth J. Pronovici.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Apache License, Version 2.0.
 * See LICENSE for more information about the licensing terms.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Author   : Kenneth J. Pronovici <pronovic@ieee.org>
 * Language : Java 6
 * Project  : Common Java Functionality
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.cedarsolutions.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cedarsolutions.util.FilesystemUtils;

/**
 * Unit tests for CommandLineUtils.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class CommandLineUtilsTest {

    /** Expected Java version. */
    private static String JAVA_VERSION;

    /** Path to the current Java runtime on disk. */
    private static CommandLine JAVA_COMMAND;

    /** Figure out the Java runtime. */
    @BeforeClass public static void determineRuntime() {
        // There's no particular significance to using Java for these tests.
        // It's just an example of an executable that must exist on any machine
        // running these tests, and we can count on the output.
        String javaVersion = System.getProperty("java.version");
        String javaHome = System.getProperty("java.home");
        String exePath = FilesystemUtils.join(javaHome, "bin", "java");
        JAVA_COMMAND = new CommandLine(exePath, "-version");
        JAVA_VERSION = "java version \"" + javaVersion + "\"";
    }

    /** Test executeCommand(), default value for returnOutput. */
    @Test public void testExecuteCommandDefault() {
        CommandLineResult result = CommandLineUtils.executeCommand(JAVA_COMMAND);
        assertNotNull(result);
        assertEquals(0, result.getExitCode());
        assertNull(result.getOutput());
    }

    /** Test executeCommand(), returnOutput=false. */
    @Test public void testExecuteCommandFalse() {
        CommandLineResult result = CommandLineUtils.executeCommand(JAVA_COMMAND, false);
        assertNotNull(result);
        assertEquals(0, result.getExitCode());
        assertNull(result.getOutput());
    }

    /** Test executeCommand(), returnOutput=true. */
    @Test public void testExecuteCommandTrue() {
        CommandLineResult result = CommandLineUtils.executeCommand(JAVA_COMMAND, true);
        assertNotNull(result);
        assertEquals(0, result.getExitCode());
        assertNotNull(result.getOutput());
        assertTrue(result.getOutput().startsWith(JAVA_VERSION));
    }

}
