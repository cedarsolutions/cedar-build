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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Command-line utilities.
 * This class is duplicated from CedarCommon for build purposes.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class CommandLineUtils {

    /**
     * Synchronously execute a command line, without returning output.
     * @param command  Command to execute
     * @return Result of the command.
     */
    public static CommandLineResult executeCommand(CommandLine command) throws RuntimeException {
        return executeCommand(command, false);
    }

    /**
     * Synchronously execute a command line, optionally returning output.
     * Output will always include both stderr and stdout, intermingled.
     * @param command       Command to execute
     * @param returnOutput  Whether to return output
     * @return Result of the command.
     */
    public static CommandLineResult executeCommand(CommandLine command, boolean returnOutput) throws RuntimeException {
        return executeCommand(command, null, returnOutput);
    }

    /**
     * Synchronously execute a command line, optionally returning output.
     * Output will always include both stderr and stdout, intermingled.
     * @param command       Command to execute
     * @param workingDir    Working directory to operate in, or null for current directory
     * @param returnOutput  Whether to return output
     * @return Result of the command.
     */
    public static CommandLineResult executeCommand(CommandLine command, String workingDir, boolean returnOutput) throws RuntimeException {
        try {
            ProcessBuilder pb = new ProcessBuilder(command.getEntireCommand());
            pb.redirectErrorStream(true);

            if (workingDir != null) {
                pb.directory(new File(workingDir));
            }

            Process process = pb.start();

            String line;
            StringBuffer output = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = reader.readLine()) != null) {
                if (returnOutput) {
                    // We have to read it so that the process doesn't block.
                    // However, there's no need to track it unless we'll actually return it.
                    output.append(line);
                    output.append("\n");
                }
            }

            process.waitFor();
            if (returnOutput) {
                return new CommandLineResult(process.exitValue(), output.toString());
            } else {
                return new CommandLineResult(process.exitValue());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute command: " + e.getMessage(), e);
        }
    }

}
