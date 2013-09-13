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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for FilesystemUtils.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class FilesystemUtilsTest {

    /** Setup before all tests. */
    @BeforeClass public static void prepare() {
        if (FilesystemUtils.dirExists("target/working")) {
            FilesystemUtils.removeDir("target/working", true);
        }
        FilesystemUtils.createDir("target/working");
    }

    /** Cleanup after all tests. */
    @AfterClass public static void cleanup() {
        if (FilesystemUtils.dirExists("target")) {
            FilesystemUtils.removeDir("target", true);
        }
    }

    /** Test the normalize() method. */
    @Test public void testNormalize() {
        assertEquals(null, FilesystemUtils.normalize(null));
        assertEquals("", FilesystemUtils.normalize(""));
        assertEquals("path", FilesystemUtils.normalize("path"));
        assertEquals("one/two", FilesystemUtils.normalize("one/two"));
        assertEquals("/one/two", FilesystemUtils.normalize("/one/two"));
        assertEquals("/one/two", FilesystemUtils.normalize("\\one\\two"));
        assertEquals("/one/two", FilesystemUtils.normalize("\\one/two"));
    }

    /** Test the join() method. */
    @Test public void testJoin() {
        assertEquals("", FilesystemUtils.join());
        assertEquals("element", FilesystemUtils.join("element"));
        assertEquals("element1/element2", FilesystemUtils.join("element1", "element2"));
        assertEquals("1/2/3/4/5/6/7", FilesystemUtils.join("1", "2", "3", "4", "5", "6", "7"));
        assertEquals("element1/element2", FilesystemUtils.join(null, "element1", "element2"));
    }

    /** Test the getBasename() method. */
    @Test public void testGetBasename() {
        assertEquals(null, FilesystemUtils.getBasename(null));
        assertEquals(null, FilesystemUtils.getBasename(""));
        assertEquals("a.txt", FilesystemUtils.getBasename("a.txt"));
        assertEquals("a.txt", FilesystemUtils.getBasename("dir/a.txt"));
        assertEquals("a.txt", FilesystemUtils.getBasename("c:/dir/a.txt"));
        assertEquals("a.txt", FilesystemUtils.getBasename("c:/path/to/dir/a.txt"));
        assertEquals("a.txt", FilesystemUtils.getBasename("/path/to/dir/a.txt"));
    }

    /** Test the getDirname() method. */
    @Test public void testGetDirname() {
        assertEquals(null, FilesystemUtils.getDirname(null));
        assertEquals(null, FilesystemUtils.getDirname(""));
        assertEquals(null, FilesystemUtils.getDirname("a.txt"));
        assertEquals("dir", FilesystemUtils.getDirname("dir/a.txt"));
        assertEquals("c:/dir", FilesystemUtils.getDirname("c:/dir/a.txt"));
        assertEquals("c:/path/to/dir", FilesystemUtils.getDirname("c:/path/to/dir/a.txt"));
        assertEquals("/path/to/dir", FilesystemUtils.getDirname("/path/to/dir/a.txt"));
    }

    /** Test the directory methods. */
    @Test public void testCreateRemoveDir() throws Exception {
        File targetDir = new File("target");
        File firstSubdir = new File("target/working/subdir");
        File secondSubdir = new File("target/working/subdir/subdir2");
        File thirdSubdir = new File("target/working/subdir/subdir3");

        assertTrue(targetDir.exists());
        assertFalse(firstSubdir.exists());
        assertFalse(secondSubdir.exists());
        assertFalse(thirdSubdir.exists());

        assertTrue(FilesystemUtils.dirExists("target"));
        assertFalse(FilesystemUtils.dirExists("target/working/subdir"));
        assertFalse(FilesystemUtils.dirExists("target/working/subdir/subdir2"));

        FilesystemUtils.createDir("target/working/subdir/subdir2");

        assertTrue(targetDir.exists());
        assertTrue(firstSubdir.exists());
        assertTrue(secondSubdir.exists());

        assertTrue(FilesystemUtils.dirExists("target"));
        assertTrue(FilesystemUtils.dirExists("target/working/subdir"));
        assertTrue(FilesystemUtils.dirExists("target/working/subdir/subdir2"));

        FilesystemUtils.createDir("target/working/subdir/subdir2");

        assertTrue(targetDir.exists());
        assertTrue(firstSubdir.exists());
        assertTrue(secondSubdir.exists());

        assertTrue(FilesystemUtils.dirExists("target"));
        assertTrue(FilesystemUtils.dirExists("target/working/subdir"));
        assertTrue(FilesystemUtils.dirExists("target/working/subdir/subdir2"));

        FilesystemUtils.removeEmptyDir("target/working/subdir/subdir2");

        assertTrue(targetDir.exists());
        assertTrue(firstSubdir.exists());
        assertFalse(secondSubdir.exists());

        assertTrue(FilesystemUtils.dirExists("target"));
        assertTrue(FilesystemUtils.dirExists("target/working/subdir"));
        assertFalse(FilesystemUtils.dirExists("target/working/subdir/subdir2"));

        FilesystemUtils.removeEmptyDir("target/working/subdir/subdir2");  // not there, but it shouldn't matter

        assertTrue(targetDir.exists());
        assertTrue(firstSubdir.exists());
        assertFalse(secondSubdir.exists());

        assertTrue(FilesystemUtils.dirExists("target"));
        assertTrue(FilesystemUtils.dirExists("target/working/subdir"));
        assertFalse(FilesystemUtils.dirExists("target/working/subdir/subdir2"));

        FilesystemUtils.removeEmptyDir("target/working/subdir");

        assertTrue(targetDir.exists());
        assertFalse(firstSubdir.exists());
        assertFalse(secondSubdir.exists());

        assertTrue(FilesystemUtils.dirExists("target"));
        assertFalse(FilesystemUtils.dirExists("target/working/subdir"));
        assertFalse(FilesystemUtils.dirExists("target/working/subdir/subdir2"));

        FilesystemUtils.createDir("target/working/subdir/subdir2");
        FilesystemUtils.createDir("target/working/subdir/subdir3");

        assertTrue(targetDir.exists());
        assertTrue(firstSubdir.exists());
        assertTrue(secondSubdir.exists());
        assertTrue(thirdSubdir.exists());

        assertTrue(FilesystemUtils.dirExists("target"));
        assertTrue(FilesystemUtils.dirExists("target/working/subdir"));
        assertTrue(FilesystemUtils.dirExists("target/working/subdir/subdir2"));
        assertTrue(FilesystemUtils.dirExists("target/working/subdir/subdir3"));

        FilesystemUtils.removeDir("target/working/subdir", true);

        assertTrue(targetDir.exists());
        assertFalse(firstSubdir.exists());
        assertFalse(secondSubdir.exists());
        assertFalse(thirdSubdir.exists());

        assertTrue(FilesystemUtils.dirExists("target"));
        assertFalse(FilesystemUtils.dirExists("target/working/subdir"));
        assertFalse(FilesystemUtils.dirExists("target/working/subdir/subdir2"));
        assertFalse(FilesystemUtils.dirExists("target/working/subdir/subdir3"));
    }

    /** Test the createFile() and removeFile() methods. */
    @Test public void testCreateRemoveFile() throws Exception {
        File targetDir = new File("target");
        File targetFile = new File("target/working/file.txt");

        assertTrue(targetDir.exists());
        assertFalse(targetFile.exists());
        assertFalse(FilesystemUtils.fileExists("target/working/file.txt"));

        FilesystemUtils.removeFile("target/working/file.txt");

        assertTrue(targetDir.exists());
        assertFalse(targetFile.exists());
        assertFalse(FilesystemUtils.fileExists("target/working/file.txt"));

        FilesystemUtils.createFile("target/working/file.txt");

        assertTrue(targetDir.exists());
        assertTrue(targetFile.exists());
        assertTrue(FilesystemUtils.fileExists("target/working/file.txt"));

        FilesystemUtils.removeFile("target/working/file.txt");

        assertTrue(targetDir.exists());
        assertFalse(targetFile.exists());
        assertFalse(FilesystemUtils.fileExists("target/working/file.txt"));
    }

    /** Test the getFileContents() method. */
    @Test public void testGetFileContents() throws Exception {
        try {
            File targetDir = new File("target");
            File targetFile = new File("target/working/file.txt");

            assertTrue(targetDir.exists());

            FileOutputStream stream = new FileOutputStream("target/working/file.txt");
            stream.write(StringUtils.getBytes("LINE1\n"));
            stream.write(StringUtils.getBytes("LINE2\n"));
            stream.write(StringUtils.getBytes("LINE3\n"));
            stream.write(StringUtils.getBytes("LINE4\n"));
            stream.close();

            assertTrue(targetDir.exists());
            assertTrue(targetFile.exists());

            List<String> lines = FilesystemUtils.getFileContents("target/working/file.txt");
            assertNotNull(lines);
            assertEquals(4, lines.size());
            assertEquals("LINE1", lines.get(0));
            assertEquals("LINE2", lines.get(1));
            assertEquals("LINE3", lines.get(2));
            assertEquals("LINE4", lines.get(3));
        } finally {
            FilesystemUtils.removeFile("target/working/file.txt");
        }
    }

    /** Test the getFileContentsAsString() method, with a trailing newline. */
    @Test public void testGetFileContentsAsString1() throws Exception {
        try {
            File targetDir = new File("target");
            File targetFile = new File("target/working/file.txt");

            assertTrue(targetDir.exists());

            FileOutputStream stream = new FileOutputStream("target/working/file.txt");
            stream.write(StringUtils.getBytes("LINE1\n"));
            stream.write(StringUtils.getBytes("LINE2\n"));
            stream.write(StringUtils.getBytes("LINE3\n"));
            stream.write(StringUtils.getBytes("LINE4\n"));
            stream.close();

            assertTrue(targetDir.exists());
            assertTrue(targetFile.exists());

            String result = FilesystemUtils.getFileContentsAsString("target/working/file.txt");
            assertNotNull(result);
            assertEquals("LINE1\nLINE2\nLINE3\nLINE4\n", result);
        } finally {
            FilesystemUtils.removeFile("target/working/file.txt");
        }
    }

    /** Test the getFileContentsAsString() method, without a trailing newline. */
    @Test public void testGetFileContentsAsString2() throws Exception {
        try {
            File targetDir = new File("target");
            File targetFile = new File("target/working/file.txt");

            assertTrue(targetDir.exists());

            FileOutputStream stream = new FileOutputStream("target/working/file.txt");
            stream.write(StringUtils.getBytes("LINE1\n"));
            stream.write(StringUtils.getBytes("LINE2\n"));
            stream.write(StringUtils.getBytes("LINE3\n"));
            stream.write(StringUtils.getBytes("LINE4"));
            stream.close();

            assertTrue(targetDir.exists());
            assertTrue(targetFile.exists());

            String result = FilesystemUtils.getFileContentsAsString("target/working/file.txt");
            assertNotNull(result);
            assertEquals("LINE1\nLINE2\nLINE3\nLINE4", result);
        } finally {
            FilesystemUtils.removeFile("target/working/file.txt");
        }
    }

    /** Test writeFileContents(). */
    @Test public void testWriteFileContentsString() throws Exception {
        assertFalse(FilesystemUtils.fileExists("target/working/file.txt"));

        String source = "LINE1\nLINE2\nLINE3\nLINE4\n";

        FilesystemUtils.writeFileContents("target/working/file.txt", source);

        List<String> resultLines = FilesystemUtils.getFileContents("target/working/file.txt");
        assertNotNull(resultLines);
        assertEquals(4, resultLines.size());
        assertEquals("LINE1", resultLines.get(0));
        assertEquals("LINE2", resultLines.get(1));
        assertEquals("LINE3", resultLines.get(2));
        assertEquals("LINE4", resultLines.get(3));

        FilesystemUtils.removeFile("target/working/file.txt");
    }

    /** Test writeFileContents(). */
    @Test public void testWriteFileContentsList() throws Exception {
        assertFalse(FilesystemUtils.fileExists("target/working/file.txt"));

        List<String> sourceLines = new ArrayList<String>();
        sourceLines.add("LINE1");
        sourceLines.add("LINE2");
        sourceLines.add("LINE3");
        sourceLines.add("LINE4");

        FilesystemUtils.writeFileContents("target/working/file.txt", sourceLines);

        List<String> resultLines = FilesystemUtils.getFileContents("target/working/file.txt");
        assertNotNull(resultLines);
        assertEquals(4, resultLines.size());
        assertEquals("LINE1", resultLines.get(0));
        assertEquals("LINE2", resultLines.get(1));
        assertEquals("LINE3", resultLines.get(2));
        assertEquals("LINE4", resultLines.get(3));

        FilesystemUtils.removeFile("target/working/file.txt");
    }

    /** Test the copyFile() method. */
    @Test public void testCopyFile() throws Exception {
        assertFalse(FilesystemUtils.fileExists("target/working/file.txt"));
        assertFalse(FilesystemUtils.fileExists("target/working/copy.txt"));

        List<String> sourceLines = new ArrayList<String>();
        sourceLines.add("LINE1");
        sourceLines.add("LINE2");
        sourceLines.add("LINE3");
        sourceLines.add("LINE4");

        FilesystemUtils.writeFileContents("target/working/file.txt", sourceLines);
        FilesystemUtils.copyFile("target/working/file.txt", "target/working/copy.txt");

        assertTrue(FilesystemUtils.fileExists("target/working/copy.txt"));

        List<String> resultLines = FilesystemUtils.getFileContents("target/working/copy.txt");
        assertNotNull(resultLines);
        assertEquals(4, resultLines.size());
        assertEquals("LINE1", resultLines.get(0));
        assertEquals("LINE2", resultLines.get(1));
        assertEquals("LINE3", resultLines.get(2));
        assertEquals("LINE4", resultLines.get(3));

        FilesystemUtils.removeFile("target/working/file.txt");
        FilesystemUtils.removeFile("target/working/copy.txt");
    }

    /** Test the copyFileToDir() method. */
    @Test public void testCopyFileToDir() throws Exception {
        assertFalse(FilesystemUtils.fileExists("target/working/file.txt"));
        assertFalse(FilesystemUtils.dirExists("target/working/newdir"));
        assertFalse(FilesystemUtils.fileExists("target/working/newdir/file.txt"));

        FilesystemUtils.createDir("target/working/newdir");

        List<String> sourceLines = new ArrayList<String>();
        sourceLines.add("LINE1");
        sourceLines.add("LINE2");
        sourceLines.add("LINE3");
        sourceLines.add("LINE4");

        FilesystemUtils.writeFileContents("target/working/file.txt", sourceLines);
        FilesystemUtils.copyFileToDir("target/working/file.txt", "target/working/newdir");

        assertTrue(FilesystemUtils.fileExists("target/working/newdir/file.txt"));

        List<String> resultLines = FilesystemUtils.getFileContents("target/working/newdir/file.txt");
        assertNotNull(resultLines);
        assertEquals(4, resultLines.size());
        assertEquals("LINE1", resultLines.get(0));
        assertEquals("LINE2", resultLines.get(1));
        assertEquals("LINE3", resultLines.get(2));
        assertEquals("LINE4", resultLines.get(3));

        FilesystemUtils.removeFile("target/working/newdir/file.txt");
        FilesystemUtils.removeFile("target/working/file.txt");
        FilesystemUtils.removeEmptyDir("target/working/newdir");
    }

    /** Test getDirContents(). */
    @Test public void testGetDirContents() throws Exception {
        try {
            assertFalse(FilesystemUtils.dirExists("target/working/contents"));

            try {
                FilesystemUtils.getDirContents("target/working/contents");
                fail("Expected RuntimeException");
            } catch (RuntimeException e) { }

            FilesystemUtils.createDir("target/working/contents");
            FilesystemUtils.createDir("target/working/contents/subdir1");
            FilesystemUtils.createDir("target/working/contents/subdir2");
            FilesystemUtils.createDir("target/working/contents/subdir3");
            FilesystemUtils.createFile("target/working/contents/file1");
            FilesystemUtils.createFile("target/working/contents/file2");
            FilesystemUtils.createFile("target/working/contents/subdir1/file3");

            List<String> contents = FilesystemUtils.getDirContents("target/working/contents");
            assertEquals(5, contents.size());
            assertTrue(contents.contains("subdir1"));
            assertTrue(contents.contains("subdir2"));
            assertTrue(contents.contains("subdir3"));
            assertTrue(contents.contains("file1"));
            assertTrue(contents.contains("file2"));

            contents = FilesystemUtils.getDirContents("target/working/contents/subdir1");
            assertEquals(1, contents.size());
            assertTrue(contents.contains("file3"));

            contents = FilesystemUtils.getDirContents("target/working/contents/subdir2");
            assertTrue(contents.isEmpty());

            contents = FilesystemUtils.getDirContents("target/working/contents/subdir3");
            assertTrue(contents.isEmpty());

            try {
                FilesystemUtils.getDirContents("target/working/contents/file1");
                fail("Expected RuntimeException");
            } catch (RuntimeException e) { }
        } finally {
            FilesystemUtils.removeDir("target/working/contents", true);
        }
    }

    /** Test unzip(). */
    @Test public void testUnzip() {
        FilesystemUtils.copyFile("src/test/java/com/cedarsolutions/util/test.zip", "target/working/test.zip");
        FilesystemUtils.createDir("target/working/unzip");
        FilesystemUtils.unzip("target/working/test.zip", "target/working/unzip");
        List<String> contents = FilesystemUtils.getDirContents("target/working/unzip", true);
        assertEquals(6, contents.size());
        assertTrue(contents.contains("test"));
        assertTrue(contents.contains("test/file1.txt"));
        assertTrue(contents.contains("test/folder1"));
        assertTrue(contents.contains("test/folder1/file2.txt"));
        assertTrue(contents.contains("test/folder2"));
        assertTrue(contents.contains("test/folder2/file3.txt"));
    }
}
