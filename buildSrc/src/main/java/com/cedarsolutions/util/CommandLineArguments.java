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
 * Common functionality for representing command-line arguments.
 * This class is duplicated from CedarCommon for build purposes.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public abstract class CommandLineArguments {

    /** Array of command-line arguments, as from main(). */
    private String[] args;

    /**
     * Constructor in terms of command-line arguments.
     * @param args Array of command-line arguments, as from main()
     * @throws RuntimeException If the command-line arguments are invalid.
     */
    protected CommandLineArguments(String[] args) {
        this.args = args;
        this.parseArguments(args);
    }

    /** Array of command-line arguments, as from main(). */
    public String[] getArgs() {
        return this.args;
    }

    /**
     * Parse command-line arguments.
     * @param args Array of command-line arguments, as from main()
     * @throws RuntimeException If the command-line arguments are invalid.
     */
    protected abstract void parseArguments(String[] args) throws RuntimeException;

    /**
     * Parse a flag from the command-line.
     *
     * @param args    Array of command-line arguments, as from main()
     * @param option  Command-line option, like --flag
     *
     * @return True if the flag exists (even if it exists more than once), false otherwise.
     */
    protected static boolean parseFlag(String[] args, String option) {
        int i = 0;
        if (args != null) {
            while (i < args.length) {
                String item = args[i];

                if (option.equals(item)) {
                    return true;
                }

                i += 1;
            }
        }

        return false;
    }

    /**
     * Parse a required string parameter from the command-line.
     *
     * <p>
     * The parameter is expected to exist exactly once in the argument
     * list.  If it is missing, or if it exists more than once, an exception
     * will be thrown.
     * </p>
     *
     * @param args    Array of command-line arguments, as from main()
     * @param option  Command-line option, like --param
     *
     * @return String parameter value parsed from command line, always non-null.
     *
     * @throws RuntimeException  If the parameter was not found
     * @throws RuntimeException  If the parameter was found more than once
     */
    protected static String parseRequiredParameter(String[] args, String option) throws RuntimeException {
        String result = null;

        int i = 0;
        if (args != null) {
            while (i < args.length) {
                String item = args[i];

                if (option.equals(item)) {
                    if ((i + 1) >= args.length) {
                        throw new RuntimeException("Parameter " + option + " was malformed.");
                    } else {
                        if (result != null) {
                            throw new RuntimeException("Parameter " + option + " was found more than once.");
                        } else {
                            result = args[++i];
                        }
                    }
                }

                i += 1;
            }
        }

        if (result == null) {
            throw new RuntimeException("Parameter " + option + " was not found");
        }

        return result;
    }

    /**
     * Parse an optional string parameter from the command-line.
     *
     * <p>
     * The parameter is expected to exist either zero times or one time
     * in the argument list.  If it exists more than once, an exception
     * will be thrown.
     * </p>
     *
     * @param args    Array of command-line arguments, as from main()
     * @param option  Command-line option, like --param
     *
     * @return String parameter value parsed from command line, possibly null.
     *
     * @throws RuntimeException  If the parameter was found more than once
     */
    protected static String parseOptionalParameter(String[] args, String option) throws RuntimeException {
        String result = null;

        int i = 0;
        if (args != null) {
            while (i < args.length) {
                String item = args[i];

                if (option.equals(item)) {
                    if ((i + 1) >= args.length) {
                        throw new RuntimeException("Parameter " + option + " was malformed.");
                    } else {
                        if (result != null) {
                            throw new RuntimeException("Parameter " + option + " was found more than once.");
                        } else {
                            result = args[++i];
                        }
                    }
                }

                i += 1;
            }
        }

        return result;
    }

    /**
     * Parse a required string parameter list option from the command-line.
     *
     * <p>
     * The parameter is expected to exist at least once in the argument
     * list.  If it is missing, an exception will be thrown.
     * </p>
     *
     * @param args    Array of command-line arguments, as from main()
     * @param option  Command-line option, like --param
     *
     * @return List of string parameter values parsed from command line, always non-null.
     *
     * @throws RuntimeException  If the option was not found
     */
    protected static List<String> parseRequiredParameterList(String[] args, String option) throws RuntimeException {
        List<String> result = new ArrayList<String>();

        int i = 0;
        if (args != null) {
            while (i < args.length) {
                String item = args[i];

                if (option.equals(item)) {
                    if ((i + 1) >= args.length) {
                        throw new RuntimeException("Parameter " + option + " was malformed.");
                    } else {
                        result.add(args[++i]);
                    }
                }

                i += 1;
            }
        }

        if (result.isEmpty()) {
            throw new RuntimeException("Parameter " + option + " was not found");
        }

        return result;
    }

    /**
     * Parse an optional string parameter list option from the command-line.
     *
     * @param args    Array of command-line arguments, as from main()
     * @param option  Command-line option, like --param
     *
     * @return List of string parameter values parsed from command line, always non-null.
     *
     * @throws RuntimeException  If the option was not found
     */
    protected static List<String> parseOptionalParameterList(String[] args, String option) throws RuntimeException {
        List<String> result = new ArrayList<String>();

        int i = 0;
        if (args != null) {
            while (i < args.length) {
                String item = args[i];

                if (option.equals(item)) {
                    if ((i + 1) >= args.length) {
                        throw new RuntimeException("Parameter " + option + " was malformed.");
                    } else {
                        result.add(args[++i]);
                    }
                }

                i += 1;
            }
        }

        return result;
    }
}
