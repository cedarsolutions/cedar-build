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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Unit tests for StringUtils.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class StringUtilsTest {

    /** Test the toByteArray() method. */
    @Test public void testToByteArray() {
        byte[] result = null;

        try {
            StringUtils.toByteArray(null);
            fail("Expected NullPointerException.");
        } catch (NullPointerException e) {
        }

        result = StringUtils.toByteArray("");
        assertTrue(Arrays.equals(new byte[] {}, result));

        result = StringUtils.toByteArray("a");
        assertTrue(Arrays.equals(new byte[] { 'a', }, result));

        result = StringUtils.toByteArray("ab");
        assertTrue(Arrays.equals(new byte[] { 'a', 'b', }, result));
    }

    /** Test the getBytes() method. */
    @Test public void testGetBytes() {
        assertTrue(Arrays.equals(new byte[] {}, StringUtils.getBytes(null)));
        assertTrue(Arrays.equals(new byte[] {}, StringUtils.getBytes("")));
        assertTrue(Arrays.equals(new byte[] { 'A', }, StringUtils.getBytes("A")));
        assertTrue(Arrays.equals(new byte[] { 'A', 'B', }, StringUtils.getBytes("AB")));
    }

    /** Test the getString() method. */
    @Test public void testGetString() {
        assertEquals("", StringUtils.getString(null));
        assertEquals("", StringUtils.getString(new byte[] {}));
        assertEquals("A", StringUtils.getString(new byte[] { 'A', }));
        assertEquals("AB", StringUtils.getString(new byte[] { 'A', 'B', }));
    }

    /** Test the convertStreamToString() method. */
    @Test public void testConvertStreamToString() {
        InputStream stream = new ByteArrayInputStream("whatever1".getBytes());
        assertEquals("whatever1", StringUtils.convertStreamToString(stream));

        stream = new ByteArrayInputStream("whatever2".getBytes());
        assertEquals("whatever2", StringUtils.convertStreamToString(stream, "US-ASCII"));

        assertEquals("", StringUtils.convertStreamToString(null, "US-ASCII"));
    }

    /** Test the chop() method. */
    @Test public void testChop() {
        assertEquals(null, StringUtils.chop(null));
        assertEquals("", StringUtils.chop(""));
        assertEquals("", StringUtils.chop("A"));
        assertEquals("AB", StringUtils.chop("ABC"));
        assertEquals("whatever", StringUtils.chop("whatever\03"));
    }

    /** Test isEmpty() method. */
    @Test public void testIsEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertTrue(StringUtils.isEmpty(" "));
        assertTrue(StringUtils.isEmpty("\t"));
        assertTrue(StringUtils.isEmpty("  \t \t\t   "));
        assertFalse(StringUtils.isEmpty("a"));
        assertFalse(StringUtils.isEmpty(" a"));
        assertFalse(StringUtils.isEmpty(" a "));
    }

    /** Test the equals() method. */
    @Test public void testEquals() {

        assertTrue(StringUtils.equals(null, null));
        assertTrue(StringUtils.equals("", ""));
        assertTrue(StringUtils.equals(null, ""));
        assertTrue(StringUtils.equals("", null));

        assertTrue(StringUtils.equals(null, ""));
        assertFalse(StringUtils.equals(null, "a"));
        assertFalse(StringUtils.equals(null, "ab"));
        assertFalse(StringUtils.equals(null, "abc"));

        assertTrue(StringUtils.equals("", ""));
        assertFalse(StringUtils.equals("", "a"));
        assertFalse(StringUtils.equals("", "ab"));
        assertFalse(StringUtils.equals("", "abc"));

        assertFalse(StringUtils.equals("a", null));
        assertFalse(StringUtils.equals("a", ""));
        assertTrue(StringUtils.equals("a", "a"));
        assertFalse(StringUtils.equals("a", "ab"));
        assertFalse(StringUtils.equals("a", "abc"));

        assertFalse(StringUtils.equals("ab", null));
        assertFalse(StringUtils.equals("ab", ""));
        assertFalse(StringUtils.equals("ab", "a"));
        assertTrue(StringUtils.equals("ab", "ab"));
        assertFalse(StringUtils.equals("ab", "abc"));

        assertFalse(StringUtils.equals("abc", null));
        assertFalse(StringUtils.equals("abc", ""));
        assertFalse(StringUtils.equals("abc", "a"));
        assertFalse(StringUtils.equals("abc", "ab"));
        assertTrue(StringUtils.equals("abc", "abc"));
    }

    /** Test the equals() method that includes a length. */
    @Test public void testEqualsWithLength() {

        assertTrue(StringUtils.equals(null, null, 0));
        assertTrue(StringUtils.equals(null, null, 1));

        assertTrue(StringUtils.equals(null, "", 0));
        assertTrue(StringUtils.equals(null, "a", 0));
        assertTrue(StringUtils.equals(null, "abc", 0));

        assertTrue(StringUtils.equals(null, "", 1));
        assertFalse(StringUtils.equals(null, "a", 1));
        assertFalse(StringUtils.equals(null, "abc", 1));

        assertTrue(StringUtils.equals(null, "", 2));
        assertFalse(StringUtils.equals(null, "a", 2));
        assertFalse(StringUtils.equals(null, "abc", 2));

        assertTrue(StringUtils.equals(null, "", 3));
        assertFalse(StringUtils.equals(null, "a", 3));
        assertFalse(StringUtils.equals(null, "abc", 3));

        assertTrue(StringUtils.equals("", null, 0));
        assertTrue(StringUtils.equals("a", null, 0));
        assertTrue(StringUtils.equals("abc", null, 0));

        assertTrue(StringUtils.equals("", null, 1));
        assertFalse(StringUtils.equals("a", null, 1));
        assertFalse(StringUtils.equals("abc", null, 1));

        assertTrue(StringUtils.equals("", null, 2));
        assertFalse(StringUtils.equals("a", null, 2));
        assertFalse(StringUtils.equals("abc", null, 2));

        assertTrue(StringUtils.equals("", null, 3));
        assertFalse(StringUtils.equals("a", null, 3));
        assertFalse(StringUtils.equals("abc", null, 3));

        assertTrue(StringUtils.equals("", "", 0));
        assertTrue(StringUtils.equals("", "", 1));
        assertTrue(StringUtils.equals("", "", 2));
        assertTrue(StringUtils.equals("", "", 3));

        assertTrue(StringUtils.equals("A", "A", 0));
        assertTrue(StringUtils.equals("A", "A", 1));
        assertTrue(StringUtils.equals("A", "A", 2));
        assertTrue(StringUtils.equals("A", "A", 3));

        assertTrue(StringUtils.equals("A", "B", 0));
        assertFalse(StringUtils.equals("A", "B", 1));
        assertFalse(StringUtils.equals("A", "B", 2));
        assertFalse(StringUtils.equals("A", "B", 3));

        assertTrue(StringUtils.equals("AA", "AA", 0));
        assertTrue(StringUtils.equals("AA", "AA", 1));
        assertTrue(StringUtils.equals("AA", "AA", 2));
        assertTrue(StringUtils.equals("AA", "AA", 3));

        assertTrue(StringUtils.equals("AA", "AB", 0));
        assertTrue(StringUtils.equals("AA", "AB", 1));
        assertFalse(StringUtils.equals("AA", "AB", 2));
        assertFalse(StringUtils.equals("AA", "AB", 3));

        assertTrue(StringUtils.equals("AAA", "AAA", 0));
        assertTrue(StringUtils.equals("AAA", "AAA", 1));
        assertTrue(StringUtils.equals("AAA", "AAA", 2));
        assertTrue(StringUtils.equals("AAA", "AAA", 3));

        assertTrue(StringUtils.equals("AAA", "AAB", 0));
        assertTrue(StringUtils.equals("AAA", "AAB", 1));
        assertTrue(StringUtils.equals("AAA", "AAB", 2));
        assertFalse(StringUtils.equals("AAA", "AAB", 3));

        assertTrue(StringUtils.equals("A", "AAB", 0));
        assertTrue(StringUtils.equals("AA", "AAB", 0));

        assertTrue(StringUtils.equals("A", "AAB", 1));
        assertTrue(StringUtils.equals("AA", "AAB", 1));

        assertFalse(StringUtils.equals("A", "AAB", 2));
        assertTrue(StringUtils.equals("AA", "AAB", 2));

        assertFalse(StringUtils.equals("A", "AAB", 3));
        assertFalse(StringUtils.equals("AA", "AAB", 3));
    }

    /** Test the equals() method, case insensitive. */
    @Test public void testEqualsCaseInsensitive() {

        assertTrue(StringUtils.equals(null, null, false));
        assertTrue(StringUtils.equals("", "", false));
        assertTrue(StringUtils.equals(null, "", false));
        assertTrue(StringUtils.equals("", null, false));

        assertTrue(StringUtils.equals(null, "", false));
        assertFalse(StringUtils.equals(null, "a", false));
        assertFalse(StringUtils.equals(null, "ab", false));
        assertFalse(StringUtils.equals(null, "abc", false));

        assertTrue(StringUtils.equals("", "", false));
        assertFalse(StringUtils.equals("", "a", false));
        assertFalse(StringUtils.equals("", "ab", false));
        assertFalse(StringUtils.equals("", "abc", false));

        assertFalse(StringUtils.equals("a", null, false));
        assertFalse(StringUtils.equals("a", "", false));
        assertTrue(StringUtils.equals("a", "A", false));
        assertFalse(StringUtils.equals("a", "Ab", false));
        assertFalse(StringUtils.equals("a", "Abc", false));

        assertFalse(StringUtils.equals("ab", null));
        assertFalse(StringUtils.equals("ab", "", false));
        assertFalse(StringUtils.equals("ab", "A", false));
        assertTrue(StringUtils.equals("ab", "aB", false));
        assertFalse(StringUtils.equals("ab", "aBc", false));

        assertFalse(StringUtils.equals("abc", null, false));
        assertFalse(StringUtils.equals("abc", "", false));
        assertFalse(StringUtils.equals("abc", "a", false));
        assertFalse(StringUtils.equals("abc", "AB", false));
        assertTrue(StringUtils.equals("abc", "ABC", false));
    }

    /** Test the substring() method. */
    @Test public void testSubstring() {
        assertEquals(null, StringUtils.substring(null, 0));
        assertEquals("", StringUtils.substring("", 0));
        assertEquals("1", StringUtils.substring("1", 0));
        assertEquals("12", StringUtils.substring("12", 0));
        assertEquals("123", StringUtils.substring("123", 0));
        assertEquals("1234", StringUtils.substring("1234", 0));
        assertEquals("12345", StringUtils.substring("12345", 0));
        assertEquals("123456", StringUtils.substring("123456", 0));

        try {
            StringUtils.substring("", 1);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
        }

        assertEquals("", StringUtils.substring("1", 1));
        assertEquals("2", StringUtils.substring("12", 1));
        assertEquals("23", StringUtils.substring("123", 1));
        assertEquals("234", StringUtils.substring("1234", 1));
        assertEquals("2345", StringUtils.substring("12345", 1));
        assertEquals("23456", StringUtils.substring("123456", 1));

        try {
            StringUtils.substring("", 2);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
        }

        try {
            StringUtils.substring("1", 2);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
        }

        assertEquals("", StringUtils.substring("12", 2));
        assertEquals("3", StringUtils.substring("123", 2));
        assertEquals("34", StringUtils.substring("1234", 2));
        assertEquals("345", StringUtils.substring("12345", 2));
        assertEquals("3456", StringUtils.substring("123456", 2));
    }

    /** Test the substring() method with an endIndex. */
    @Test public void testSubstringWithEndIndex() {
        assertEquals(null, StringUtils.substring(null, 0, 5));
        assertEquals("", StringUtils.substring("", 0, 5));
        assertEquals("1", StringUtils.substring("1", 0, 5));
        assertEquals("12", StringUtils.substring("12", 0, 5));
        assertEquals("123", StringUtils.substring("123", 0, 5));
        assertEquals("1234", StringUtils.substring("1234", 0, 5));
        assertEquals("12345", StringUtils.substring("12345", 0, 5));
        assertEquals("12345", StringUtils.substring("123456", 0, 5));

        try {
            StringUtils.substring("", 1, 5);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
        }

        assertEquals("", StringUtils.substring("1", 1, 5));
        assertEquals("2", StringUtils.substring("12", 1, 5));
        assertEquals("23", StringUtils.substring("123", 1, 5));
        assertEquals("234", StringUtils.substring("1234", 1, 5));
        assertEquals("2345", StringUtils.substring("12345", 1, 5));
        assertEquals("2345", StringUtils.substring("123456", 1, 5));

        try {
            StringUtils.substring("", 2, 5);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
        }

        try {
            StringUtils.substring("1", 2, 5);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
        }

        assertEquals("", StringUtils.substring("12", 2, 5));
        assertEquals("3", StringUtils.substring("123", 2, 5));
        assertEquals("34", StringUtils.substring("1234", 2, 5));
        assertEquals("345", StringUtils.substring("12345", 2, 5));
        assertEquals("345", StringUtils.substring("123456", 2, 5));
    }

    /** Test the truncate() method. */
    @Test public void testTruncate() {
        assertEquals("", StringUtils.truncate(null, 0));
        assertEquals("", StringUtils.truncate("", 0));
        assertEquals("", StringUtils.truncate("1", 0));
        assertEquals("", StringUtils.truncate("12", 0));
        assertEquals("", StringUtils.truncate("123", 0));
        assertEquals("", StringUtils.truncate("1234", 0));
        assertEquals("", StringUtils.truncate("12345", 0));
        assertEquals("", StringUtils.truncate("123456", 0));
        assertEquals("", StringUtils.truncate("      ", 0));

        assertEquals("", StringUtils.truncate(null, 1));
        assertEquals("", StringUtils.truncate("", 1));
        assertEquals("1", StringUtils.truncate("1", 1));
        assertEquals("1", StringUtils.truncate("12", 1));
        assertEquals("1", StringUtils.truncate("123", 1));
        assertEquals("1", StringUtils.truncate("1234", 1));
        assertEquals("1", StringUtils.truncate("12345", 1));
        assertEquals("1", StringUtils.truncate("123456", 1));
        assertEquals("", StringUtils.truncate("      ", 1));

        assertEquals("", StringUtils.truncate(null, 2));
        assertEquals("", StringUtils.truncate("", 2));
        assertEquals("1", StringUtils.truncate("1", 2));
        assertEquals("12", StringUtils.truncate("12", 2));
        assertEquals("12", StringUtils.truncate("123", 2));
        assertEquals("12", StringUtils.truncate("1234", 2));
        assertEquals("12", StringUtils.truncate("12345", 2));
        assertEquals("12", StringUtils.truncate("123456", 2));
        assertEquals("", StringUtils.truncate("      ", 2));

        assertEquals("", StringUtils.truncate(null, 3));
        assertEquals("", StringUtils.truncate("", 3));
        assertEquals("1", StringUtils.truncate("1", 3));
        assertEquals("12", StringUtils.truncate("12", 3));
        assertEquals("123", StringUtils.truncate("123", 3));
        assertEquals("123", StringUtils.truncate("1234", 3));
        assertEquals("123", StringUtils.truncate("12345", 3));
        assertEquals("123", StringUtils.truncate("123456", 3));
        assertEquals("", StringUtils.truncate("      ", 3));

        assertEquals("", StringUtils.truncate(null, 4));
        assertEquals("", StringUtils.truncate("", 4));
        assertEquals("1", StringUtils.truncate("1", 4));
        assertEquals("12", StringUtils.truncate("12", 4));
        assertEquals("123", StringUtils.truncate("123", 4));
        assertEquals("1234", StringUtils.truncate("1234", 4));
        assertEquals("1234", StringUtils.truncate("12345", 4));
        assertEquals("1234", StringUtils.truncate("123456", 4));
        assertEquals("", StringUtils.truncate("      ", 4));

        assertEquals("", StringUtils.truncate(null, 5));
        assertEquals("", StringUtils.truncate("", 5));
        assertEquals("1", StringUtils.truncate("1", 5));
        assertEquals("12", StringUtils.truncate("12", 5));
        assertEquals("123", StringUtils.truncate("123", 5));
        assertEquals("1234", StringUtils.truncate("1234", 5));
        assertEquals("12345", StringUtils.truncate("12345", 5));
        assertEquals("12345", StringUtils.truncate("123456", 5));
        assertEquals("", StringUtils.truncate("      ", 5));

        assertEquals("", StringUtils.truncate(null, 6));
        assertEquals("", StringUtils.truncate("", 6));
        assertEquals("1", StringUtils.truncate("1", 6));
        assertEquals("12", StringUtils.truncate("12", 6));
        assertEquals("123", StringUtils.truncate("123", 6));
        assertEquals("1234", StringUtils.truncate("1234", 6));
        assertEquals("12345", StringUtils.truncate("12345", 6));
        assertEquals("123456", StringUtils.truncate("123456", 6));
        assertEquals("", StringUtils.truncate("      ", 6));

        assertEquals("", StringUtils.truncate(null, 7));
        assertEquals("", StringUtils.truncate("", 7));
        assertEquals("1", StringUtils.truncate("1", 7));
        assertEquals("12", StringUtils.truncate("12", 7));
        assertEquals("123", StringUtils.truncate("123", 7));
        assertEquals("1234", StringUtils.truncate("1234", 7));
        assertEquals("12345", StringUtils.truncate("12345", 7));
        assertEquals("123456", StringUtils.truncate("123456", 7));
        assertEquals("", StringUtils.truncate("      ", 7));
    }

    /** Test the truncateToNull() method. */
    @Test public void testTruncateToNull() {
        assertEquals(null, StringUtils.truncateToNull(null, 0));
        assertEquals(null, StringUtils.truncateToNull(null, 0));
        assertEquals(null, StringUtils.truncateToNull("1", 0));
        assertEquals(null, StringUtils.truncateToNull("12", 0));
        assertEquals(null, StringUtils.truncateToNull("123", 0));
        assertEquals(null, StringUtils.truncateToNull("1234", 0));
        assertEquals(null, StringUtils.truncateToNull("12345", 0));
        assertEquals(null, StringUtils.truncateToNull("123456", 0));
        assertEquals(null, StringUtils.truncateToNull("      ", 0));

        assertEquals(null, StringUtils.truncateToNull(null, 1));
        assertEquals(null, StringUtils.truncateToNull(null, 1));
        assertEquals("1", StringUtils.truncateToNull("1", 1));
        assertEquals("1", StringUtils.truncateToNull("12", 1));
        assertEquals("1", StringUtils.truncateToNull("123", 1));
        assertEquals("1", StringUtils.truncateToNull("1234", 1));
        assertEquals("1", StringUtils.truncateToNull("12345", 1));
        assertEquals("1", StringUtils.truncateToNull("123456", 1));
        assertEquals(null, StringUtils.truncateToNull("      ", 1));

        assertEquals(null, StringUtils.truncateToNull(null, 2));
        assertEquals(null, StringUtils.truncateToNull(null, 2));
        assertEquals("1", StringUtils.truncateToNull("1", 2));
        assertEquals("12", StringUtils.truncateToNull("12", 2));
        assertEquals("12", StringUtils.truncateToNull("123", 2));
        assertEquals("12", StringUtils.truncateToNull("1234", 2));
        assertEquals("12", StringUtils.truncateToNull("12345", 2));
        assertEquals("12", StringUtils.truncateToNull("123456", 2));
        assertEquals(null, StringUtils.truncateToNull("      ", 2));

        assertEquals(null, StringUtils.truncateToNull(null, 3));
        assertEquals(null, StringUtils.truncateToNull(null, 3));
        assertEquals("1", StringUtils.truncateToNull("1", 3));
        assertEquals("12", StringUtils.truncateToNull("12", 3));
        assertEquals("123", StringUtils.truncateToNull("123", 3));
        assertEquals("123", StringUtils.truncateToNull("1234", 3));
        assertEquals("123", StringUtils.truncateToNull("12345", 3));
        assertEquals("123", StringUtils.truncateToNull("123456", 3));
        assertEquals(null, StringUtils.truncateToNull("      ", 3));

        assertEquals(null, StringUtils.truncateToNull(null, 4));
        assertEquals(null, StringUtils.truncateToNull(null, 4));
        assertEquals("1", StringUtils.truncateToNull("1", 4));
        assertEquals("12", StringUtils.truncateToNull("12", 4));
        assertEquals("123", StringUtils.truncateToNull("123", 4));
        assertEquals("1234", StringUtils.truncateToNull("1234", 4));
        assertEquals("1234", StringUtils.truncateToNull("12345", 4));
        assertEquals("1234", StringUtils.truncateToNull("123456", 4));
        assertEquals(null, StringUtils.truncateToNull("      ", 4));

        assertEquals(null, StringUtils.truncateToNull(null, 5));
        assertEquals(null, StringUtils.truncateToNull(null, 5));
        assertEquals("1", StringUtils.truncateToNull("1", 5));
        assertEquals("12", StringUtils.truncateToNull("12", 5));
        assertEquals("123", StringUtils.truncateToNull("123", 5));
        assertEquals("1234", StringUtils.truncateToNull("1234", 5));
        assertEquals("12345", StringUtils.truncateToNull("12345", 5));
        assertEquals("12345", StringUtils.truncateToNull("123456", 5));
        assertEquals(null, StringUtils.truncateToNull("      ", 5));

        assertEquals(null, StringUtils.truncateToNull(null, 6));
        assertEquals(null, StringUtils.truncateToNull(null, 6));
        assertEquals("1", StringUtils.truncateToNull("1", 6));
        assertEquals("12", StringUtils.truncateToNull("12", 6));
        assertEquals("123", StringUtils.truncateToNull("123", 6));
        assertEquals("1234", StringUtils.truncateToNull("1234", 6));
        assertEquals("12345", StringUtils.truncateToNull("12345", 6));
        assertEquals("123456", StringUtils.truncateToNull("123456", 6));
        assertEquals(null, StringUtils.truncateToNull("      ", 6));

        assertEquals(null, StringUtils.truncateToNull(null, 7));
        assertEquals(null, StringUtils.truncateToNull(null, 7));
        assertEquals("1", StringUtils.truncateToNull("1", 7));
        assertEquals("12", StringUtils.truncateToNull("12", 7));
        assertEquals("123", StringUtils.truncateToNull("123", 7));
        assertEquals("1234", StringUtils.truncateToNull("1234", 7));
        assertEquals("12345", StringUtils.truncateToNull("12345", 7));
        assertEquals("123456", StringUtils.truncateToNull("123456", 7));
        assertEquals(null, StringUtils.truncateToNull("      ", 7));
    }

    /** Test the trim() method. */
    @Test public void testTrim() {
        assertEquals(null, StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim(" "));
        assertEquals("", StringUtils.trim("\t"));
        assertEquals("", StringUtils.trim("\n"));
        assertEquals("", StringUtils.trim("   \n  \t\t    \t"));
        assertEquals("A", StringUtils.trim("A"));
        assertEquals("A", StringUtils.trim(" A"));
        assertEquals("A", StringUtils.trim("A "));
        assertEquals("A", StringUtils.trim(" A "));
        assertEquals("A", StringUtils.trim(" \t   \n A \t\t  "));
        assertEquals("A \tB", StringUtils.trim(" \t   \n A \tB\t  "));
    }

    /** Test the trimToNull() method. */
    @Test public void testTrimToNull() {
        assertEquals(null, StringUtils.trimToNull(null));
        assertEquals(null, StringUtils.trimToNull(""));
        assertEquals(null, StringUtils.trimToNull(" "));
        assertEquals(null, StringUtils.trimToNull("\t"));
        assertEquals(null, StringUtils.trimToNull("\n"));
        assertEquals(null, StringUtils.trimToNull("   \n  \t\t    \t"));
        assertEquals("A", StringUtils.trimToNull("A"));
        assertEquals("A", StringUtils.trimToNull(" A"));
        assertEquals("A", StringUtils.trimToNull("A "));
        assertEquals("A", StringUtils.trimToNull(" A "));
        assertEquals("A", StringUtils.trimToNull(" \t   \n A \t\t  "));
        assertEquals("A \tB", StringUtils.trimToNull(" \t   \n A \tB\t  "));
    }

    /** Test the rtrim() method. */
    @Test public void testRtrim() {
        assertEquals(null, StringUtils.rtrim(null));
        assertEquals("", StringUtils.rtrim(""));
        assertEquals("", StringUtils.rtrim(" "));
        assertEquals("", StringUtils.rtrim("\t"));
        assertEquals("", StringUtils.rtrim("\n"));
        assertEquals("", StringUtils.rtrim("   \n  \t\t    \t"));
        assertEquals("A", StringUtils.rtrim("A"));
        assertEquals(" A", StringUtils.rtrim(" A"));
        assertEquals("A", StringUtils.rtrim("A "));
        assertEquals(" A", StringUtils.rtrim(" A "));
        assertEquals(" \t   \n A", StringUtils.rtrim(" \t   \n A \t\t  "));
        assertEquals(" \t   \n A \tB", StringUtils.rtrim(" \t   \n A \tB\t  "));
    }

    /** Test the rtrimToNull() method. */
    @Test public void testRtrimToNull() {
        assertEquals(null, StringUtils.rtrimToNull(null));
        assertEquals(null, StringUtils.rtrimToNull(""));
        assertEquals(null, StringUtils.rtrimToNull(" "));
        assertEquals(null, StringUtils.rtrimToNull("\t"));
        assertEquals(null, StringUtils.rtrimToNull("\n"));
        assertEquals(null, StringUtils.rtrimToNull("   \n  \t\t    \t"));
        assertEquals("A", StringUtils.rtrimToNull("A"));
        assertEquals(" A", StringUtils.rtrimToNull(" A"));
        assertEquals("A", StringUtils.rtrimToNull("A "));
        assertEquals(" A", StringUtils.rtrimToNull(" A "));
        assertEquals(" \t   \n A", StringUtils.rtrimToNull(" \t   \n A \t\t  "));
        assertEquals(" \t   \n A \tB", StringUtils.rtrimToNull(" \t   \n A \tB\t  "));
    }

    /** Test the matches() method. */
    @Test public void testMatches() {
        assertFalse(StringUtils.matches(null, null));
        assertFalse(StringUtils.matches("string", null));
        assertFalse(StringUtils.matches(null, Pattern.compile("A")));
        assertFalse(StringUtils.matches("B", Pattern.compile("A")));
        assertTrue(StringUtils.matches("A", Pattern.compile("A")));
    }

    /** Test replaceAll(). */
    @Test public void testReplaceAll() {
        assertEquals(null, StringUtils.replaceAll(null, Pattern.compile("^\\s+"), ""));
        assertEquals(null, StringUtils.replaceAll("   Ken", null, ""));
        assertEquals("Ken", StringUtils.replaceAll("   Ken", Pattern.compile("^\\s+"), ""));
        assertEquals("Ken", StringUtils.replaceAll("   Ken", Pattern.compile("^\\s+"), null));
    }

    /** Test the contains() method. */
    @Test public void testContains() {
        assertFalse(StringUtils.contains(null, null));
        assertFalse(StringUtils.contains("string", null));
        assertFalse(StringUtils.contains(null, "A"));
        assertFalse(StringUtils.contains("a", "A"));
        assertFalse(StringUtils.contains("B", "A"));
        assertTrue(StringUtils.contains("A", "A"));
        assertFalse(StringUtils.contains("whatever", "AT"));
        assertFalse(StringUtils.contains("whAtever", "AT"));
        assertTrue(StringUtils.contains("whATever", "AT"));

        assertFalse(StringUtils.contains(null, null, true));
        assertFalse(StringUtils.contains("string", null, true));
        assertFalse(StringUtils.contains(null, "A", true));
        assertFalse(StringUtils.contains("a", "A", true));
        assertFalse(StringUtils.contains("B", "A", true));
        assertTrue(StringUtils.contains("A", "A", true));
        assertFalse(StringUtils.contains("whatever", "AT", true));
        assertFalse(StringUtils.contains("whAtever", "AT", true));
        assertTrue(StringUtils.contains("whATever", "AT", true));

        assertFalse(StringUtils.contains(null, null, false));
        assertFalse(StringUtils.contains("string", null, false));
        assertFalse(StringUtils.contains(null, "A", false));
        assertTrue(StringUtils.contains("a", "A", false));
        assertFalse(StringUtils.contains("B", "A", false));
        assertTrue(StringUtils.contains("A", "A", false));
        assertTrue(StringUtils.contains("whatever", "AT", false));
        assertTrue(StringUtils.contains("whAtever", "AT", false));
        assertTrue(StringUtils.contains("whATever", "AT", false));
    }

    /** Test the startsWith() method. */
    @Test public void testStartsWith() {
        assertFalse(StringUtils.startsWith(null, null));
        assertFalse(StringUtils.startsWith("string", null));
        assertFalse(StringUtils.startsWith(null, "A"));
        assertFalse(StringUtils.startsWith("a", "A"));
        assertFalse(StringUtils.startsWith("B", "A"));
        assertTrue(StringUtils.startsWith("A", "A"));
        assertFalse(StringUtils.startsWith("whatever", "AT"));
        assertFalse(StringUtils.startsWith("whAtever", "AT"));
        assertFalse(StringUtils.startsWith("whATever", "AT"));
        assertFalse(StringUtils.startsWith("whATever", "what"));
        assertFalse(StringUtils.startsWith("whATever", "What"));
        assertFalse(StringUtils.startsWith("whATever", "whatever"));
        assertTrue(StringUtils.startsWith("whATever", "whAT"));
        assertTrue(StringUtils.startsWith("whATever", "whATever"));

        assertFalse(StringUtils.startsWith(null, null, true));
        assertFalse(StringUtils.startsWith("string", null, true));
        assertFalse(StringUtils.startsWith(null, "A", true));
        assertFalse(StringUtils.startsWith("a", "A", true));
        assertFalse(StringUtils.startsWith("B", "A", true));
        assertTrue(StringUtils.startsWith("A", "A", true));
        assertFalse(StringUtils.startsWith("whatever", "AT", true));
        assertFalse(StringUtils.startsWith("whAtever", "AT", true));
        assertFalse(StringUtils.startsWith("whATever", "AT", true));
        assertFalse(StringUtils.startsWith("whATever", "what", true));
        assertFalse(StringUtils.startsWith("whATever", "What", true));
        assertFalse(StringUtils.startsWith("whATever", "whatever", true));
        assertTrue(StringUtils.startsWith("whATever", "whAT", true));
        assertTrue(StringUtils.startsWith("whATever", "whATever", true));

        assertFalse(StringUtils.startsWith(null, null, false));
        assertFalse(StringUtils.startsWith("string", null, false));
        assertFalse(StringUtils.startsWith(null, "A", false));
        assertTrue(StringUtils.startsWith("a", "A", false));
        assertFalse(StringUtils.startsWith("B", "A", false));
        assertTrue(StringUtils.startsWith("A", "A", false));
        assertFalse(StringUtils.startsWith("whatever", "AT", false));
        assertFalse(StringUtils.startsWith("whAtever", "AT", false));
        assertFalse(StringUtils.startsWith("whATever", "AT", false));
        assertTrue(StringUtils.startsWith("whATever", "what", false));
        assertTrue(StringUtils.startsWith("whATever", "What", false));
        assertTrue(StringUtils.startsWith("whATever", "whatever", false));
        assertTrue(StringUtils.startsWith("whATever", "whAT", false));
        assertTrue(StringUtils.startsWith("whATever", "whATever", false));
    }

    /** Test toUpperCase(). */
    @Test public void testToUpperCase() {
        assertEquals(null, StringUtils.toUpperCase(null));
        assertEquals("", StringUtils.toUpperCase(""));
        assertEquals("   ", StringUtils.toUpperCase("   "));
        assertEquals("A", StringUtils.toUpperCase("a"));
        assertEquals("AB", StringUtils.toUpperCase("aB"));
        assertEquals("AB", StringUtils.toUpperCase("AB"));
        assertEquals(" AB ", StringUtils.toUpperCase(" AB "));
    }

    /** Test convertLineEndings(). */
    @SuppressWarnings("deprecation")
    @Test public void testConvertLineEndings() {
        assertEquals(null, StringUtils.convertLineEndings(null));
        assertEquals("", StringUtils.convertLineEndings(""));
        assertEquals("abc", StringUtils.convertLineEndings("abc"));

        assertEquals("\r\n", StringUtils.convertLineEndings("\r\n"));
        assertEquals("1\r\n", StringUtils.convertLineEndings("1\r\n"));

        if (StringUtils.LINE_ENDING.equals("\n")) {
            assertEquals("\n", StringUtils.convertLineEndings("\n"));
            assertEquals("1\n", StringUtils.convertLineEndings("1\n"));
            assertEquals("1\nabc\nde", StringUtils.convertLineEndings("1\nabc\nde"));
            assertEquals("1\r\nabc\nde", StringUtils.convertLineEndings("1\r\nabc\nde"));
        } else if (StringUtils.LINE_ENDING.equals("\r\n")) {
            assertEquals("\r\n", StringUtils.convertLineEndings("\n"));
            assertEquals("1\r\n", StringUtils.convertLineEndings("1\n"));
            assertEquals("1\r\nabc\r\nde", StringUtils.convertLineEndings("1\r\nabc\nde"));
        }
    }

    /** Test splitToList(). */
    @Test public void testSplitToList() {
        List<String> result = null;

        // this is really just verifying the behavior of String.split()
        // for a few obvious cases, just so our own interface is well-specified

        result = StringUtils.splitToList(null, "/");
        assertTrue(result.isEmpty());

        result = StringUtils.splitToList("", "/");
        assertEquals(1, result.size());
        assertEquals("", result.get(0));

        result = StringUtils.splitToList("AAA", "/");
        assertEquals(1, result.size());
        assertEquals("AAA", result.get(0));

        result = StringUtils.splitToList("A/B", "/");
        assertEquals(2, result.size());
        assertEquals("A", result.get(0));
        assertEquals("B", result.get(1));

        result = StringUtils.splitToList("A/B/", "/");
        assertEquals(2, result.size());
        assertEquals("A", result.get(0));
        assertEquals("B", result.get(1));

        result = StringUtils.splitToList("/A/B", "/");
        assertEquals(3, result.size());
        assertEquals("", result.get(0));
        assertEquals("A", result.get(1));
        assertEquals("B", result.get(2));

        result = StringUtils.splitToList("/A/B/", "/");
        assertEquals(3, result.size());
        assertEquals("", result.get(0));
        assertEquals("A", result.get(1));
        assertEquals("B", result.get(2));
    }

    /** Test splitToList() with ignoreEmpty=true. */
    @Test public void testSplitToListIgnoreEmpty() {
        List<String> result = null;

        // this is really just verifying the behavior of String.split()
        // for a few obvious cases, just so our own interface is well-specified

        result = StringUtils.splitToList(null, "/", true);
        assertTrue(result.isEmpty());

        result = StringUtils.splitToList("", "/", true);
        assertTrue(result.isEmpty());

        result = StringUtils.splitToList("AAA", "/", true);
        assertEquals(1, result.size());
        assertEquals("AAA", result.get(0));

        result = StringUtils.splitToList("A/B", "/", true);
        assertEquals(2, result.size());
        assertEquals("A", result.get(0));
        assertEquals("B", result.get(1));

        result = StringUtils.splitToList("A/B/", "/", true);
        assertEquals(2, result.size());
        assertEquals("A", result.get(0));
        assertEquals("B", result.get(1));

        result = StringUtils.splitToList("/A/B", "/", true);
        assertEquals(2, result.size());
        assertEquals("A", result.get(0));
        assertEquals("B", result.get(1));

        result = StringUtils.splitToList("/A/B/", "/", true);
        assertEquals(2, result.size());
        assertEquals("A", result.get(0));
        assertEquals("B", result.get(1));
    }

    /** Test splitLines(). */
    @Test public void testSplitLines() {
        List<String> list = StringUtils.splitLines(null);
        assertEquals(0, list.size());

        list = StringUtils.splitLines("");
        assertEquals(0, list.size());

        list = StringUtils.splitLines("blech");
        assertEquals(1, list.size());
        assertEquals("blech", list.get(0));

        list = StringUtils.splitLines("one\r\ntwo");
        assertEquals(2, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two", list.get(1));

        list = StringUtils.splitLines("one\rtwo");
        assertEquals(2, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two", list.get(1));

        list = StringUtils.splitLines("one\ntwo");
        assertEquals(2, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two", list.get(1));

        list = StringUtils.splitLines("one\r\ntwo\r\nthree");
        assertEquals(3, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two", list.get(1));
        assertEquals("three", list.get(2));

        list = StringUtils.splitLines("one\rtwo\rthree");
        assertEquals(3, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two", list.get(1));
        assertEquals("three", list.get(2));

        list = StringUtils.splitLines("one\ntwo\nthree");
        assertEquals(3, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two", list.get(1));
        assertEquals("three", list.get(2));

        list = StringUtils.splitLines("one\r\ntwo\nthree\rfour\r\nfive");
        assertEquals(3, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two\nthree\rfour", list.get(1));
        assertEquals("five", list.get(2));

        list = StringUtils.splitLines("one\rtwo\r\nthree\nfour\rfive");
        assertEquals(4, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two", list.get(1));
        assertEquals("\nthree\nfour", list.get(2));
        assertEquals("five", list.get(3));

        list = StringUtils.splitLines("one\ntwo\r\nthree\rfour\nfive");
        assertEquals(4, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two\r", list.get(1));
        assertEquals("three\rfour", list.get(2));
        assertEquals("five", list.get(3));
    }

}
