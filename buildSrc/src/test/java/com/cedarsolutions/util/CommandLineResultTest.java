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
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Unit tests for CommandLineResult.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class CommandLineResultTest {

    /** Test the constructors and getters. */
    @Test public void testConstructorAndGetters() {
        CommandLineResult result;

        result = new CommandLineResult(10);
        assertEquals(10, result.getExitCode());
        assertNull(result.getOutput());

        result = new CommandLineResult(3, "one");
        assertEquals(3, result.getExitCode());
        assertEquals("one", result.getOutput());
    }
}
