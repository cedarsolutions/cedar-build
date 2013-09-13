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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cedarsolutions.util.FilesystemUtils;

/**
 * Unit tests for CopyrightTool.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class CopyrightToolTest {

    /** Working directory for tests. */
    private static final String WORKING_DIR = "target/working";

    /** Directory that this test lives in. */
    private static final String PACKAGE_DIR = "src/test/java/com/cedarsolutions/tools/copyright";

    /** Mercurial repository for testing. */
    private static final String REPOSITORY = "target/working/repo";

    /** Mercurial command for testing. */
    private static final String MERCURIAL = "C:/Program Files/TortoiseHg/hg.exe";

    /** List of patterns. */
    private static List<Pattern> PATTERNS;

    /** Setup before all tests. */
    @BeforeClass public static void prepare() {
        if (FilesystemUtils.dirExists(WORKING_DIR)) {
            FilesystemUtils.removeDir(WORKING_DIR, true);
        }

        FilesystemUtils.createDir(WORKING_DIR);

        String source = FilesystemUtils.join(PACKAGE_DIR, "repo.zip");
        FilesystemUtils.unzip(source, WORKING_DIR);
        assertTrue(FilesystemUtils.dirExists(REPOSITORY));

        PATTERNS = new ArrayList<Pattern>();
        PATTERNS.add(Pattern.compile("^.*\\.java$"));
        PATTERNS.add(Pattern.compile("^.*\\.rb$"));
        PATTERNS.add(Pattern.compile("^.*\\.feature$"));
        PATTERNS.add(Pattern.compile("^.*LICENSE$"));
    }

    /** Cleanup after all tests. */
    @AfterClass public static void cleanup() {
        if (FilesystemUtils.dirExists(WORKING_DIR)) {
            FilesystemUtils.removeDir(WORKING_DIR, true);
        }
    }

    /** Test generateFileYearsMap(). */
    @Test public void testGenerateFileYearsMap() {
        // We'll only run this test if the Mercurial command defined above exists
        // That way, I don't have to come up with some sort of config mechanism for unit tests.
        org.junit.Assume.assumeTrue(FilesystemUtils.fileExists(MERCURIAL));

        List<Integer> years = new ArrayList<Integer>();
        years.add(new Integer(2012));

        Map<String, List<Integer>> map = CopyrightTool.generateFileYearsMap(MERCURIAL, REPOSITORY, PATTERNS);
        assertEquals(5, map.size());
        assertEquals(years, map.get("LICENSE"));
        assertEquals(years, map.get("file.feature"));
        assertEquals(years, map.get("file.java"));
        assertEquals(years, map.get("file.rb"));
        assertEquals(years, map.get("file2-renamed.java"));
    }

    /** Test updateCopyrightStatement(). */
    @Test public void testUpdateCopyrightStatement() {
        String source = FilesystemUtils.join(PACKAGE_DIR, "sample.txt");
        String expected = FilesystemUtils.join(PACKAGE_DIR, "expected.txt");
        String target = FilesystemUtils.join(WORKING_DIR, "sample.txt");
        FilesystemUtils.copyFile(source, target);
        CopyrightTool.updateCopyrightStatement(target, createList(2011, 2013, 2014, 2015, 2017));
        assertEquals(FilesystemUtils.getFileContentsAsString(expected), FilesystemUtils.getFileContentsAsString(target));
    }

    /** Test generateCopyrightRange(). */
    @Test public void testGenerateCopyrightRange() {
        assertEquals(null, CopyrightTool.generateCopyrightRange(null));
        assertEquals(null, CopyrightTool.generateCopyrightRange(new ArrayList<Integer>()));
        assertEquals("2011", CopyrightTool.generateCopyrightRange(createList(2011)));
        assertEquals("2011-2012", CopyrightTool.generateCopyrightRange(createList(2011, 2012)));
        assertEquals("2011-2012,2014", CopyrightTool.generateCopyrightRange(createList(2011, 2012, 2014)));
        assertEquals("2011-2012,2014,2016", CopyrightTool.generateCopyrightRange(createList(2011, 2012, 2014, 2016)));
        assertEquals("2011-2012,2014,2016-2017", CopyrightTool.generateCopyrightRange(createList(2011, 2012, 2014, 2016, 2017)));
        assertEquals("2011-2012,2014,2016-2018", CopyrightTool.generateCopyrightRange(createList(2011, 2012, 2014, 2016, 2017, 2018)));
        assertEquals("2011-2012,2014,2016-2018", CopyrightTool.generateCopyrightRange(createList(2018, 2012, 2014, 2011, 2017, 2016)));
    }

    /** Test generateOverallYears(). */
    @Test public void testGenerateOverallYears() {
        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(2013);
        list1.add(2017);

        List<Integer> list2 = new ArrayList<Integer>();
        list1.add(2011);
        list1.add(2012);

        List<Integer> list3 = new ArrayList<Integer>();
        list1.add(2012);
        list1.add(2013);

        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        map.put("file1", list1);
        map.put("file2", list2);
        map.put("file3", list3);

        List<Integer> expected = new ArrayList<Integer>();
        expected.add(2011);
        expected.add(2012);
        expected.add(2013);
        expected.add(2017);

        assertEquals(expected, CopyrightTool.generateOverallYears(map));
    }

    /** Test generateHelp(). */
    @Test public void testGenerateHelp() {
        assertNotNull(CopyrightTool.generateHelp()); // just make sure it runs
    }

    /** Create a list based on an array of items. */
    private List<Integer> createList(Integer ... items) {
        List<Integer> list = new ArrayList<Integer>();

        for (Integer item : items) {
            list.add(item);
        }

        return list;
    }

}
