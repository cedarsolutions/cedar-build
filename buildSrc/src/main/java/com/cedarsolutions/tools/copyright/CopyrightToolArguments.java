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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.cedarsolutions.util.FilesystemUtils;
import com.cedarsolutions.util.CommandLineArguments;

/**
 * Command-line arguments for CopyrightTool.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class CopyrightToolArguments extends CommandLineArguments {

    /** Path to the Mercurial (hg) executable. */
    private String mercurial;

    /** Mercurial repository to be modified. */
    private String repository;

    /** Regular expression pattern matching the license file, with the project-wide copyright in it. */
    private Pattern licensePattern;

    /** Regular expression patterns which specify the files to update. */
    private List<Pattern> patterns;

    /**
     * Constructor in terms of command-line arguments.
     * @param args Array of command-line arguments, as from main()
     */
    public CopyrightToolArguments(String[] args) {
        super(args);
    }

    /**
     * Parse command-line arguments.
     * @param args Array of command-line arguments, as from main()
     * @throws RuntimeException If the command-line arguments are invalid.
     */
    @Override
    protected void parseArguments(String[] args) throws RuntimeException {
        if (args == null || args.length < 4) {
            throw new RuntimeException("Arguments are invalid.");
        }

        this.mercurial = FilesystemUtils.normalize(args[0]);
        this.repository = FilesystemUtils.normalize(args[1]);
        this.licensePattern = Pattern.compile(args[2]);

        this.patterns = new ArrayList<Pattern>();
        for (int i = 3; i < args.length; i++) {
            Pattern pattern = Pattern.compile(args[i]);
            this.patterns.add(pattern);
        }
    }

    /** Path to the Mercurial (hg) executable. */
    public String getMercurial() {
        return this.mercurial;
    }

    /** Mercurial repository to be modified. */
    public String getRepository() {
        return this.repository;
    }

    /** Regular expression pattern matching the license file, with the project-wide copyright in it. */
    public Pattern getLicensePattern() {
        return this.licensePattern;
    }

    /** Regular expression patterns which specify the files to update. */
    public List<Pattern> getPatterns() {
        return this.patterns;
    }

}
