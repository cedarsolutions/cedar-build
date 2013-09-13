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

/**
 * Result returned by by <code>CommandLineUtils.execute()</code>.
 * This class is duplicated from CedarCommon for build purposes.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class CommandLineResult {

    /** Exit code. */
    private int exitCode;

    /** Output. */
    private String output;

    /** Create a result with no output. */
    public CommandLineResult(int exitCode) {
        this(exitCode, null);
    }

    /** Create a result. */
    public CommandLineResult(int exitCode, String output) {
        this.exitCode = exitCode;
        this.output = output;
    }

    /** Exit code. */
    public int getExitCode() {
        return this.exitCode;
    }

    /** Output from the command. */
    public String getOutput() {
        return this.output;
    }

}
