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

import java.util.ArrayList;
import java.util.List;

/**
 * Command line to be executed by <code>CommandLineUtils.execute()</code>.
 * This class is duplicated from CedarCommon for build purposes.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class CommandLine {

    /** List of elements in the command line. */
    private List<String> list = new ArrayList<String>();

    /**
     * Create a command line, initializing it with a string command.
     * @param command  String command, the program to be executed
     */
    public CommandLine(String command) {
        list.add(command);
    }

    /**
     * Create a command line, initializing it with a command and arguments.
     * @param command  String command, the program to be executed
     * @param args     String arguments to the command
     */
    public CommandLine(String command, String ... args) {
        list.add(command);
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                list.add(args[i]);
            }
        }
    }

    /**
     * Create a command line, initializing it with a command and arguments.
     * @param command  String command, the program to be executed
     * @param args     String arguments to the command
     */
    public CommandLine(String command, List<String> args) {
        list.add(command);
        list.addAll(args);
    }

    /** Get the command, the first element in the command line. */
    public String getCommand() {
        return this.list.size() >= 1 ? list.get(0) : null;
    }

    /** Get the arguments to the command. */
    public List<String> getArgs() {
        List<String> arguments = new ArrayList<String>();

        if (this.list.size() > 1) {
            for (int i = 1; i < list.size(); i++) {
                arguments.add(list.get(i));
            }
        }

        return arguments;
    }

    /** Add an argument to the list of arguments. */
    public void addArg(String arg) {
        this.list.add(arg);
    }

    /** Get the entire command, suitable for passing to the constructor for <code>ProcessBuilder</code>. */
    public List<String> getEntireCommand() {
        return new ArrayList<String>(this.list);
    }

    /** Return the command-line as a string. */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        for (String element : this.list) {
            buffer.append(element);
            buffer.append(" ");
        }

        return StringUtils.trim(buffer.toString());
    }

}
