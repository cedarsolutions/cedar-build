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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.cedarsolutions.util.StringUtils;
import com.cedarsolutions.util.FilesystemUtils;
import com.cedarsolutions.util.CommandLine;
import com.cedarsolutions.util.CommandLineArguments;
import com.cedarsolutions.util.CommandLineUtils;
import com.cedarsolutions.util.CommandLineResult;
import com.cedarsolutions.util.JaxbUtils;
import com.cedarsolutions.xml.bindings.hg.log.Log;
import com.cedarsolutions.xml.bindings.hg.log.Logentry;

/**
 * Cedar Solutions copyright tool.
 *
 * <p>
 * This tool updates copyright statements in Cedar Solutions source files that
 * follow the file header standard.  The update is based on information in the
 * Mercurial revision control repository.  Every source file with a standard
 * header will be updated to include every year that exists in revision
 * control.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class CopyrightTool {

    /** Command-line arguments. */
    private CopyrightToolArguments arguments;

    /**
     * Constructor in terms of command-line arguments.
     *
     * <p>
     * Note: This constructor calls system.exit(2) if command-line arguments
     * are invalid.  It does this to strictly control the output from the
     * program (we want to make sure that the usage is displayed simply and
     * prominently).
     * </p>
     *
     * @param args     Array of command-line arguments, as from main()
     */
    public CopyrightTool(String[] args) {
        try {
            this.arguments = new CopyrightToolArguments(args);
        } catch (RuntimeException e) {
            System.err.println(generateHelp());
            System.exit(2);
        }
    }

    /** Java entry point. */
    public static void main(String[] args) throws Exception {
        new CopyrightTool(args).internalMain();
    }

    /** Command-line arguments. */
    public CopyrightToolArguments getArguments() {
        return this.arguments;
    }

    /** Internal implementation of the main routine. */
    public void internalMain() {
        String mercurial = this.arguments.getMercurial();
        String repository = this.arguments.getRepository();
        Pattern licensePattern = this.arguments.getLicensePattern();
        List<Pattern> patterns = this.arguments.getPatterns();

        System.out.println("");
        System.out.println("Copyright Tool");

        System.out.println("");
        System.out.println("Mercurial......: " + mercurial);
        System.out.println("Repository.....: " + repository);
        System.out.println("License Pattern: " + licensePattern);
        System.out.println("Patterns.......: ");
        for (Pattern pattern : patterns) {
            System.out.println("   " + pattern);
        }

        System.out.println("");
        System.out.println("Analyzing Mercurial repository...");
        Map<String, List<Integer>> map = generateFileYearsMap(mercurial, repository, patterns);

        int source = updateSourceFiles(repository, map);
        int license = updateLicenseFiles(mercurial, repository, licensePattern, map);

        if (source + license > 0) {
            System.out.println("Note: you must review and commit these changes.");
        }

        System.out.println("");
    }

    /**
     * Update copyright statements in source files.
     * @param repository   Mercurial repository to be modified
     * @param fileYearsMap Map as from generateFileYearsMap()
     * @return Number of files that were updated.
     */
    private static int updateSourceFiles(String repository, Map<String, List<Integer>> fileYearsMap) {
        int updated = 0;

        System.out.println("Updating copyright statements in source files...");
        for (String trackedFile : fileYearsMap.keySet()) {
            String path = FilesystemUtils.join(repository, trackedFile);
            updated += updateCopyrightStatement(path, fileYearsMap.get(trackedFile));
        }

        System.out.println("Updated " + updated + " out of " + fileYearsMap.size() + " source files.");
        return updated;
    }

    /**
     * Update copyright statements in license files.
     * @param mercurial      Path to the Mercurial (hg) executable.
     * @param repository     Mercurial repository to be modified
     * @param licensePattern Regular expression pattern for license files
     * @param fileYearsMap   Map as from generateFileYearsMap()
     * @return Number of files that were updated.
     */
    private static int updateLicenseFiles(String mercurial, String repository, Pattern licensePattern, Map<String, List<Integer>> fileYearsMap) {
        int updated = 0;
        List<Integer> overallYears = generateOverallYears(fileYearsMap);
        List<String> trackedFiles = getTrackedFiles(mercurial, repository, licensePattern);

        System.out.println("Updating copyright statements in license files...");
        for (String trackedFile : trackedFiles) {
            String path = FilesystemUtils.join(repository, trackedFile);
            updated += updateCopyrightStatement(path, overallYears);
        }

        System.out.println("Updated " + updated + " out of " + trackedFiles.size() + " license files.");
        return updated;
    }

    /**
     * Generate a map from file name to years that file was modified in.
     * @param mercurial  Path to the Mercurial (hg) executable.
     * @param repository Mercurial repository to be modified
     * @param patterns   Regular expression patterns which specify the files to update.
     * @return Map from file name to years.
     */
    protected static Map<String, List<Integer>> generateFileYearsMap(String mercurial, String repository, List<Pattern> patterns) {
        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();

        List<String> trackedFiles = getTrackedFiles(mercurial, repository, patterns);
        for (String trackedFile : trackedFiles) {
            List<Integer> years = getYearsForTrackedFile(mercurial, repository, trackedFile);
            map.put(trackedFile, years);
        }

        return map;
    }

    /**
     * Generate an overall range of years among all files in the repository.
     * @param fileYearsMap  Map as from generateFileYearsMap()
     * @return Range of years that files were modified in, sorted.
     */
    protected static List<Integer> generateOverallYears(Map<String, List<Integer>> fileYearsMap) {
        List<Integer> overallYears = new ArrayList<Integer>();

        for (String file : fileYearsMap.keySet()) {
            for (Integer year : fileYearsMap.get(file)) {
                if (!overallYears.contains(year)) {
                    overallYears.add(year);
                }
            }
        }

        Collections.sort(overallYears);
        return overallYears;
    }

    /**
     * Get a list of the files tracked in the repository, filtered down to the ones which match a pattern.
     * @param mercurial  Path to the Mercurial (hg) executable.
     * @param repository Mercurial repository to be modified
     * @param patterns   Regular expression pattern which specifies the files to update.
     * @return List of tracked files.
     */
    protected static List<String> getTrackedFiles(String mercurial, String repository, Pattern pattern) {
        List<Pattern> patterns = new ArrayList<Pattern>();
        patterns.add(pattern);
        return getTrackedFiles(mercurial, repository, patterns);
    }

    /**
     * Get a list of the files tracked in the repository, filtered down to the ones which match a set of patterns.
     * @param mercurial  Path to the Mercurial (hg) executable.
     * @param repository Mercurial repository to be modified
     * @param patterns   Regular expression patterns which specify the files to update.
     * @return List of tracked files.
     */
    protected static List<String> getTrackedFiles(String mercurial, String repository, List<Pattern> patterns) {
        List<String> trackedFiles = new ArrayList<String>();

        CommandLine command = new CommandLine(mercurial);
        command.addArg("locate");

        // It works best to execute the command from within the repository location.
        // Mercurial is not always consistent about behavior if you specify the path to the repo.
        CommandLineResult result = CommandLineUtils.executeCommand(command, repository, true);
        if (result.getExitCode() != 0) {
            throw new RuntimeException("Command failed: " + command.toString());
        }

        for (String file : StringUtils.splitLines(result.getOutput())) {
            String normalized = FilesystemUtils.normalize(file);
            for (Pattern pattern : patterns) {
                if (StringUtils.matches(normalized, pattern)) {
                    trackedFiles.add(normalized);
                    break;
                }
            }
        }

        return trackedFiles;
    }

    /**
     * Get the years in which a tracked file was modified in Mercurial.
     * @param mercurial   Path to the Mercurial (hg) executable.
     * @param repository  Mercurial repository to be modified
     * @param trackedFile Tracked file to analyze
     * @return List of years in which the tracked file was modified.
     */
    protected static List<Integer> getYearsForTrackedFile(String mercurial, String repository, String trackedFile) {
        List<Integer> years = new ArrayList<Integer>();

        CommandLine command = new CommandLine(mercurial);
        command.addArg("log");
        command.addArg("--follow");
        command.addArg("--style");
        command.addArg("xml");
        command.addArg(trackedFile);

        // It works best to execute the command from within the repository location.
        // Mercurial is not always consistent about behavior if you specify the path to the repo.
        CommandLineResult result = CommandLineUtils.executeCommand(command, repository, true);
        if (result.getExitCode() != 0) {
            throw new RuntimeException("Command failed: " + command.toString());
        }

        String xml = StringUtils.trimToNull(result.getOutput());
        if (xml != null) {
            Log log = JaxbUtils.getInstance().unmarshalDocument(Log.class, xml, false);
            for (Logentry logentry : log.getLogentry()) {
                try {
                    Integer year = Integer.valueOf(StringUtils.substring(logentry.getDate(), 0, 4));
                    if (!years.contains(year)) {
                        years.add(year);
                    }
                } catch (Exception e) { }
            }
        }

        return years;
    }

    /**
     * Update the copyright statement in a file.
     * @param file   File to modify
     * @param years  List of years that the file was edited in
     * @return Number of files updated (either 0 or 1).
     */
    protected static int updateCopyrightStatement(String file, List<Integer> years) {
        String contents = FilesystemUtils.getFileContentsAsString(file);
        String range = generateCopyrightRange(years);
        String regex = "(Copyright \\(c\\) )(.*)( Kenneth J. Pronovici)";
        String replacement = "$1" + range + "$3";

        String result = contents.replaceAll(regex, replacement);
        if (!result.equals(contents)) {
            // No point in writing the file unless something has changed
            FilesystemUtils.writeFileContents(file, result);
            return 1;
        }

        return 0;
    }

    /**
     * Generate a copyright year range from a list of years.
     *
     * <p>
     * A range is something like "2011,2013-2015,2017".  Consecutive
     * years are bundled together with a "-" and other years are just
     * added with a comma.
     * </p>
     *
     * @param years   List of years
     * @return Copyright year range for the list of years, or null if no range can be derived.
     */
    protected static String generateCopyrightRange(List<Integer> years) {
        if (years == null || years.size() == 0) {
            return null;
        } else {
            Integer lastYear = null;
            int rangeStart = 0;
            int last = 0;
            int current = 0;
            boolean firstTime = true;
            StringBuffer buffer = new StringBuffer();

            Collections.sort(years);
            for (Integer currentYear : years) {
                if (firstTime) {
                    current = currentYear.intValue();
                    buffer.append(currentYear.toString());
                    rangeStart = current;
                    lastYear = currentYear;
                    firstTime = false;
                } else {
                    last = lastYear.intValue();
                    current = currentYear.intValue();

                    if (current - last > 1) {
                        if (last > rangeStart) {
                            buffer.append("-");
                            buffer.append(lastYear.toString());
                        }

                        buffer.append(",");
                        buffer.append(currentYear.toString());
                        rangeStart = current;
                    }

                    lastYear = currentYear;
                }
            }

            if (lastYear != null) {
                last = lastYear.intValue();
                if (last > rangeStart) {
                    buffer.append("-");
                    buffer.append(lastYear.toString());
                }
            }

            return buffer.toString();
        }
    }

    /** Generate a command-line help statement. */
    protected static String generateHelp() {
        return "Usage: CopyrightTool hgpath repository pattern [pattern, ...]\n" +
               "\n" +
               "   hgpath      Path to the Mercurial executable, including .exe on Windows\n" +
               "   repository  Path to the Mercurial repository to be updated\n" +
               "   pattern     Regular expression pattern matching files to be updated" +
               "\n" +
               "Update copyright statements in the indicated Mercurial repositories.\n" +
               "The code is assumed to follow Cedar Solutions standards.  After running\n" +
               "the tool, you'll need to review changes and commit them.\n" +
               "\n" +
               "The regular expression patterns must be designed to match paths that are\n" +
               "relative to the repository root.  These patterns can assume that the path " +
               "separator has been normalized to / on both Windows and UNIX-like systems.";
    }

}
