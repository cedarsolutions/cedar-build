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
package com.cedarsolutions.tools.copyright;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for CopyrightToolArguments.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class CopyrightToolArgumentsTest {

    /** Test the constructor. */
    @Test public void testConstructor() {
        CopyrightToolArguments args;

        try {
            args = new CopyrightToolArguments(null);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        try {
            args = new CopyrightToolArguments(new String[] {});
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        try {
            args = new CopyrightToolArguments(new String[] { "one",  });
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        try {
            args = new CopyrightToolArguments(new String[] { "one", "two", });
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        try {
            args = new CopyrightToolArguments(new String[] { "one", "two", "three", });
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        args = new CopyrightToolArguments(new String[] { "one", "two", "three", "four" });
        assertEquals("one", args.getMercurial());
        assertEquals("two", args.getRepository());
        assertEquals("three", args.getLicensePattern().pattern());
        assertEquals(1, args.getPatterns().size());
        assertEquals("four", args.getPatterns().get(0).pattern());

        args = new CopyrightToolArguments(new String[] { "one", "two", "three", "four", "five", });
        assertEquals("one", args.getMercurial());
        assertEquals("two", args.getRepository());
        assertEquals("three", args.getLicensePattern().pattern());
        assertEquals(2, args.getPatterns().size());
        assertEquals("four", args.getPatterns().get(0).pattern());
        assertEquals("five", args.getPatterns().get(1).pattern());
    }

}
