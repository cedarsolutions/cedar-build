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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

/**
 * Unit tests for CommandLineArguments.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class CommandLineArgumentsTest {

    /** Test the constructor. */
    @Test public void testConstructor() {
        String[] args = new String[] { "one", "two", };
        Args impl = new Args(args);
        assertSame(args, impl.getArgs());
        assertSame(args, impl.executed);  // proves that parseArguments() was invoked
    }

    /** Test parseFlag(). */
    @Test public void testParseFlag() {
        String[] args;
        boolean result;

        args = null;
        result = Args.parseFlag(args, "--whatever");
        assertFalse(result);

        args = new String[] { };
        result = Args.parseFlag(args, "--whatever");
        assertFalse(result);

        args = new String[] { "--whatever", };
        result = Args.parseFlag(args, "--whatever");
        assertTrue(result);
    }

    /** Test parseRequiredParameter(). */
    @Test public void testParseRequiredParameter() {
        String[] args;
        String result;

        try {
            args = null;
            Args.parseRequiredParameter(args, "--whatever");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        try {
            args = new String[] { };
            Args.parseRequiredParameter(args, "--whatever");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        try {
            args = new String[] { "--whatever", };
            Args.parseRequiredParameter(args, "--whatever");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        try {
            args = new String[] { "--whatever", "one", "--whatever", "two", };
            Args.parseRequiredParameter(args, "--whatever");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        args = new String[] { "--whatever", "one", };
        result = Args.parseRequiredParameter(args, "--whatever");
        assertEquals("one", result);

        args = new String[] { "--whatever", "one", "--another", "two", };
        result = Args.parseRequiredParameter(args, "--whatever");
        assertEquals("one", result);
    }

    /** Test parseOptionalParameter(). */
    @Test public void testParseOptionalParameter() {
        String[] args;
        String result;

        args = null;
        result = Args.parseOptionalParameter(args, "--whatever");
        assertNull(result);

        args = new String[] { };
        result = Args.parseOptionalParameter(args, "--whatever");
        assertNull(result);

        try {
            args = new String[] { "--whatever", };
            Args.parseOptionalParameter(args, "--whatever");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        try {
            args = new String[] { "--whatever", "one", "--whatever", "two", };
            Args.parseOptionalParameter(args, "--whatever");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        args = new String[] { "--whatever", "one", };
        result = Args.parseOptionalParameter(args, "--whatever");
        assertEquals("one", result);

        args = new String[] { "--whatever", "one", "--another", "two", };
        result = Args.parseOptionalParameter(args, "--whatever");
        assertEquals("one", result);
    }

    /** Test parseRequiredParameterList(). */
    @Test public void testParseRequiredParameterList() {
        String[] args;
        List<String> result;

        try {
            args = null;
            Args.parseRequiredParameterList(args, "--whatever");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        try {
            args = new String[] { };
            Args.parseRequiredParameterList(args, "--whatever");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        try {
            args = new String[] { "--whatever", };
            Args.parseRequiredParameterList(args, "--whatever");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        args = new String[] { "--whatever", "one", };
        result = Args.parseRequiredParameterList(args, "--whatever");
        assertEquals(1, result.size());
        assertEquals("one", result.get(0));

        args = new String[] { "--whatever", "one", "--whatever", "two", };
        result = Args.parseRequiredParameterList(args, "--whatever");
        assertEquals(2, result.size());
        assertEquals("one", result.get(0));
        assertEquals("two", result.get(1));

        args = new String[] { "--whatever", "one", "--another", "two", };
        result = Args.parseRequiredParameterList(args, "--whatever");
        assertEquals(1, result.size());
        assertEquals("one", result.get(0));

        args = new String[] { "--whatever", "one", "--another", "two", "--whatever", "three", };
        result = Args.parseRequiredParameterList(args, "--whatever");
        assertEquals(2, result.size());
        assertEquals("one", result.get(0));
        assertEquals("three", result.get(1));
    }

    /** Test parseOptionalParameterList(). */
    @Test public void testParseOptionalParameterList() {
        String[] args;
        List<String> result;

        args = null;
        result = Args.parseOptionalParameterList(args, "--whatever");
        assertTrue(result.isEmpty());

        args = new String[] { };
        result = Args.parseOptionalParameterList(args, "--whatever");
        assertTrue(result.isEmpty());

        try {
            args = new String[] { "--whatever", };
            Args.parseOptionalParameterList(args, "--whatever");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) { }

        args = new String[] { "--whatever", "one", };
        result = Args.parseOptionalParameterList(args, "--whatever");
        assertEquals(1, result.size());
        assertEquals("one", result.get(0));

        args = new String[] { "--whatever", "one", "--whatever", "two", };
        result = Args.parseOptionalParameterList(args, "--whatever");
        assertEquals(2, result.size());
        assertEquals("one", result.get(0));
        assertEquals("two", result.get(1));

        args = new String[] { "--whatever", "one", "--another", "two", };
        result = Args.parseOptionalParameterList(args, "--whatever");
        assertEquals(1, result.size());
        assertEquals("one", result.get(0));

        args = new String[] { "--whatever", "one", "--another", "two", "--whatever", "three", };
        result = Args.parseOptionalParameterList(args, "--whatever");
        assertEquals(2, result.size());
        assertEquals("one", result.get(0));
        assertEquals("three", result.get(1));
    }

    /** Concrete class that we can use for testing. */
    private static class Args extends CommandLineArguments {
        protected String[] executed;

        public Args(String[] args) {
            super(args);
        }

        @Override
        protected void parseArguments(String[] args) throws RuntimeException {
            this.executed = args;
        }
    }
}
