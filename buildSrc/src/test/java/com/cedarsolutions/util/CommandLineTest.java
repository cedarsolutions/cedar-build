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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Unit tests for CommandLine.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class CommandLineTest {

    /** Test the constructors. */
    @Test public void testConstructors() {
        CommandLine command;

        command = new CommandLine("command");
        assertEquals("command", command.getCommand());
        assertTrue(command.getArgs().isEmpty());
        assertEquals(1, command.getEntireCommand().size());
        assertEquals("command", command.getEntireCommand().get(0));
        assertEquals("command", command.toString());

        command = new CommandLine("command", new ArrayList<String>());
        assertEquals("command", command.getCommand());
        assertTrue(command.getArgs().isEmpty());
        assertEquals(1, command.getEntireCommand().size());
        assertEquals("command", command.getEntireCommand().get(0));
        assertEquals("command", command.toString());

        command = new CommandLine("command", "arg1");
        assertEquals("command", command.getCommand());
        assertEquals(1, command.getArgs().size());
        assertEquals("arg1", command.getArgs().get(0));
        assertEquals(2, command.getEntireCommand().size());
        assertEquals("command", command.getEntireCommand().get(0));
        assertEquals("arg1", command.getEntireCommand().get(1));
        assertEquals("command arg1", command.toString());

        command = new CommandLine("command", "arg1", "arg2");
        assertEquals("command", command.getCommand());
        assertEquals(2, command.getArgs().size());
        assertEquals("arg1", command.getArgs().get(0));
        assertEquals("arg2", command.getArgs().get(1));
        assertEquals(3, command.getEntireCommand().size());
        assertEquals("command", command.getEntireCommand().get(0));
        assertEquals("arg1", command.getEntireCommand().get(1));
        assertEquals("arg2", command.getEntireCommand().get(2));
        assertEquals("command arg1 arg2", command.toString());
    }

    /** Test addArgs(). */
    @Test public void testAddArgs() {
        CommandLine command = new CommandLine("command");

        command.addArg("arg1");
        assertEquals(1, command.getArgs().size());
        assertEquals("arg1", command.getArgs().get(0));

        command.addArg("arg2");
        assertEquals(2, command.getArgs().size());
        assertEquals("arg1", command.getArgs().get(0));
        assertEquals("arg2", command.getArgs().get(1));
    }
}
