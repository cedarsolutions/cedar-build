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

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;


/**
 * General string utilities.
 *
 * <p>
 * This class includes methods that cannot be translated by the GWT
 * compiler.  Back-end or pure-Java functionality should use this class.
 * GWT client code should use GwtStringUtils instead.
 * </p>
 * 
 * <p>This class is duplicated from CedarCommon for build purposes.</p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public final class StringUtils {

    /** Proper line ending on this platform. */
    public static final String LINE_ENDING = System.getProperty("line.separator");

    /** Proper line ending on this platform. */
    public static final byte[] LINE_ENDING_BYTES = toByteArray(LINE_ENDING);

    /**
     * Convert an integer value to a byte value.
     * @param value  Integer value to operate on
     * @return Byte value, possibly truncated.
     */
    private static byte toByte(int value) {
        return (byte) (value & 0xFF);
    }

    /**
     * Convert a string into a byte array by converting each character to a byte.
     * @param value  String value to operate on
     * @return Byte array created based on input string.
     */
    public static byte[] toByteArray(String value) {
        return toByteArray(value, value.length());
    }

    /**
     * Convert a string into a byte array by converting each character to a byte.
     * @param value  String value to operate on
     * @param bytes  Number of bytes to convert
     * @return Byte array created based on input string.
     */
    public static byte[] toByteArray(String value, int bytes) {
        byte[] result = new byte[bytes];

        for (int i = 0; i < bytes; i++) {
            result[i] = toByte(value.charAt(i));
        }

        return result;
    }

    /**
     * Convert a string to a byte array, using US-ASCII or the platform default charset.
     * @param value  String value to operate on
     * @return Byte array generated based on the input string, always non-null.
     */
    public static byte[] getBytes(String value) {
        try {
            return value == null ? new byte[0] : value.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            return value.getBytes(); // use platform default
        }
    }

    /**
     * Convert a byte array to a string, using US-ASCII or the platform default charset.
     * @param value  Byte array to operate on
     * @return String generated based on the passed-in data, always non-null.
     */
    public static String getString(byte[] value) {
        try {
            return value == null ? "" : new String(value, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            return new String(value); // use platform default
        }
    }

    /**
     * Chop the last character off a string, like Perl's chop().
     * @param value  String value to operate on
     * @return Input value with last character chopped off.
     */
    public static String chop(String value) {
        if (value == null) {
            return null;
        } else if (value.length() == 0 || value.length() == 1) {
            return "";
        } else {
            return value.substring(0, value.length() - 1);
        }
    }

    /**
     * Indicates whether a string is null or empty.
     * @param value  String value to operate on
     * @return True if string value is null or empty when trimmed, false otherwise.
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * Indicates whether two strings are equal.
     * This method is null-safe: null strings are equivalent to empty strings.
     * @param left   First string to compare
     * @param right  Second string to compare
     * @return True if the strings are equal, false otherwise.
     */
    public static boolean equals(String left, String right) {
        return equals(left, right, true);
    }

    /**
     * Indicates whether two strings are equal up to and including a certain number of bytes.
     * This method is null-safe: null strings are equivalent to empty strings.
     * @param left  First string to compare
     * @param right Second string to compare
     * @param max   Maximum number of bytes to check
     * @return True if the strings are equal, false otherwise.
     */
    public static boolean equals(String left, String right, int max) {
        left = left == null ? "" : left.length() < max ? left : left.substring(0, max);
        right = right == null ? "" : right.length() < max ? right : right.substring(0, max);
        return equals(left, right, true);
    }

    /**
     * Indicates whether two strings are equal, optionally case-insensitive.
     * This method is null-safe: null strings are equivalent to empty strings.
     * @param left          First string to compare
     * @param right         Second string to compare
     * @param caseSensitive Whether the check should be case-sensitive
     * @return True if the strings are equal, false otherwise.
     */
    public static boolean equals(String left, String right, boolean caseSensitive) {
        left = left == null ? "" : left;
        right = right == null ? "" : right;
        return caseSensitive ? left.equals(right) : left.equalsIgnoreCase(right);
    }

    /**
     * Substring method that safely deals with null strings and an end index that is too large.
     * If the passed-in string is null, then the result will always be null.
     * The start index must always be valid, except when the string is null.
     * If the passed-in end index is past the end of the string, then the end of the string is used.
     * @param value       String value to operate on
     * @param startIndex  Start index
     * @param endIndex    End index
     * @return Substring, possibly null.
     */
    public static String substring(String value, int startIndex, int endIndex) {
        if (value == null) {
            return null;
        } else {
            endIndex = value.length() < endIndex ? value.length() : endIndex;
            return value.substring(startIndex, endIndex);
        }
    }

    /**
     * Safe substring method that safely deals with null strings.
     * If the passed-in string is null, then the result will always be null.
     * The start index must always be valid, except when the string is null.
     * @param value       String value to operate on
     * @param startIndex  Start index
     * @return Substring, possibly null.
     */
    public static String substring(String value, int startIndex) {
        return value == null ? null : value.substring(startIndex);
    }

    /**
     * Truncate a string to a maximum length.
     * A null string will always be truncated to an empty string.
     * @param value  String value to operate on
     * @param max    Maximum length to allow
     * @return String truncated to the maximum length.
     */
    public static String truncate(String value, int max) {
        if (value == null || max == 0) {
            return "";
        } else {
            if (value.length() <= max) {
                return trim(value);
            } else {
                return trim(substring(value, 0, max));
            }
        }
    }

    /**
     * Truncate a string to a maximum length.
     * A null string will always be truncated to a null value.
     * @param value  String value to operate on
     * @param max    Maximum length to allow
     * @return String truncated to the maximum length.
     */
    public static String truncateToNull(String value, int max) {
        if (value == null || max == 0) {
            return null;
        } else {
            if (value.length() <= max) {
                return trimToNull(value);
            } else {
                return trimToNull(substring(value, 0, max));
            }
        }
    }

    /**
     * Trim a string value (null-safe).
     * @param value  String value to operate on
     * @return Trimmed value, possibly null.
     */
    public static String trim(String value) {
        return value == null ? null : value.trim();
    }

    /**
     * Trim a string value, returning null if the trimmed string is empty.
     * @param value  String value to operate on
     * @return Trimmed value, possibly null.
     */
    public static String trimToNull(String value) {
        String result = trim(value);
        return result == null || result.length() == 0 ? null : result;
    }

    /**
     * Right-trim a string value (null-safe).
     * @param value  String value to operate on
     * @return Trimmed value, possibly null.
     */
    public static String rtrim(String value) {
        return value == null ? null : value.replaceAll("\\s+$", "");
    }

    /**
     * Right-trim a string value (null-safe).
     * @param value  String value to operate on
     * @return Trimmed value, possibly null.
     */
    public static String rtrimToNull(String value) {
        String result = rtrim(value);
        return result == null || result.length() == 0 ? null : result;
    }

    /**
     * Equivalent of string.contains(), but is null-safe.
     * @param value     String value to operate on
     * @param substring Substring to check for
     * @return True if string contains the substring, false otherwise.
     */
    public static boolean contains(String value, String substring) {
        return contains(value, substring, true);
    }

    /**
     * Equivalent of string.contains(), but is null-safe and optionally case-insensitive.
     * @param value         String value to operate on
     * @param substring     Substring to check for
     * @param caseSensitive Whether the check should be case-sensitive
     * @return True if string contains the substring, false otherwise.
     */
    public static boolean contains(String value, String substring, boolean caseSensitive) {
        if (value == null || substring == null) {
            return false;
        } else {
            if (!caseSensitive) {
                value = value.toUpperCase();
                substring = substring.toUpperCase();
            }

            return value.contains(substring);
        }
    }

    /**
     * Equivalent of String.startsWith(), but is null-safe.
     * @param value      String value to operate on
     * @param substring  Substring to check for
     * @return True if string starts with the substring, false otherwise.
     */
    public static boolean startsWith(String value, String substring) {
        return startsWith(value, substring, true);
    }

    /**
     * Equivalent of String.startsWith(), but is null-safe and optionally case-insensitive.
     * @param value         String value to operate on
     * @param substring     Substring to check for
     * @param caseSensitive Whether the check should be case-sensitive
     * @return True if string starts with the substring, false otherwise.
     */
    public static boolean startsWith(String value, String substring, boolean caseSensitive) {
        if (value == null || substring == null) {
            return false;
        } else {
            if (!caseSensitive) {
                value = value.toUpperCase();
                substring = substring.toUpperCase();
            }

            return value.startsWith(substring);
        }
    }

    /**
     * Equivalent of String.toUpperCase(), but is null-safe.
     * @param value  String value to operate on
     * @return String converted to upper case, or null if input is null.
     */
    public static String toUpperCase(String value) {
        return value == null ? null : value.toUpperCase();
    }

    /**
     * Convert an input stream to a string.
     * @param stream  Input stream to operate on
     * @return String read from the input stream
     */
    public static String convertStreamToString(InputStream stream) {
        return convertStreamToString(stream, null);
    }

    /**
     * Convert an input stream to a string.
     * @param stream  Input stream to operate on
     * @param charset Name of the charset to use, or null to use the default
     * @return String read from the input stream
     * @see <a href="http://stackoverflow.com/questions/309424/in-java-how-do-a-read-convert-an-inputstream-in-to-a-string">StackOverflow</a>
     */
    public static String convertStreamToString(InputStream stream, String charset) {
        // According to the StackOverflow author: the reason this works is because
        // Scanner iterates over tokens in the stream, and in this case we separate
        // tokens using "beginning of the input boundary" (\A) thus giving us only one
        // token for the entire contents of the stream.
        try {
            if (stream == null) {
                return "";
            } else {
                if (charset == null) {
                    return new Scanner(stream).useDelimiter("\\A").next();
                } else {
                    return new Scanner(stream, charset).useDelimiter("\\A").next();
                }
            }
        } catch (NoSuchElementException e) {
            // This happens if the stream is empty
            return "";
        }
    }

    /**
     * Equivalent of string.matches(pattern), but allows compiled patterns.
     * Null strings or null patterns never match.
     * @param value    String value to operate on
     * @param pattern  Pattern to check for
     * @return True if pattern matches, false otherwise.
     */
    public static boolean matches(String value, Pattern pattern) {
        if (value == null || pattern == null) {
            return false;
        } else {
            return pattern.matcher(value).matches();
        }
    }

    /**
     * Equivalent of string.replaceAll(pattern, replacement), but allows compiled patterns.
     * Null strings or null patterns just result in a null string.
     * @param value       String value to operate on
     * @param pattern     Pattern to check for in string
     * @param replacement Replacement, where pattern is found
     * @return Resulting string with replacement substituted for pattern.
     */
    public static String replaceAll(String value, Pattern pattern, String replacement) {
        if (value == null || pattern == null) {
            return null;
        } else {
            replacement = replacement == null ? "" : replacement;
            return pattern.matcher(value).replaceAll(replacement);
        }
    }

    /**
     * Fix line endings, converting newlines into the platform line ending.
     * The conversion tries to avoid replacing "\r\n" with "\r\r\n" where "\r\n" is the line ending.
     * @param value  String value to operate on
     * @return String with newlines converted to the proper system line ending, possibly null.
     * @deprecated I'm not convinced that this works properly on non-Windows systems
     */
    @Deprecated
    public static String convertLineEndings(String value) {
        return value == null ? null : value.replaceAll(LINE_ENDING, "\n").replaceAll("\n", LINE_ENDING);
    }

    /**
     * Split a string but return a list rather than a String[].
     * Note that an empty string gets you a list with 1 element, which is consistent with String.split().
     * @param value  String value to operate on
     * @param regex  Regular expression to split with
     * @return List of split strings, possibly empty.
     */
    public static List<String> splitToList(String value, String regex) {
        return splitToList(value, regex, false);
    }

    /**
     * Split a string but return a list rather than a String[].
     * Use ignoreEmpty=false to get behavior consistent with String.split().
     * @param value        String value to operate on
     * @param regex        Regular expression to split with
     * @param ignoreEmpty  Whether to ignore empty values
     * @return List of split strings, possibly empty.
     */
    public static List<String> splitToList(String value, String regex, boolean ignoreEmpty) {
        List<String> result = new ArrayList<String>();

        if (value != null) {
            String[] split = value.split(regex);
            if (split != null) {
                for (String element : split) {
                    if (ignoreEmpty) {
                        if (element.length() > 0) {
                            result.add(element);
                        }
                    } else {
                        result.add(element);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Split a string into a list of lines, using the ending on the first line to determine the expected format.
     * @param value  String value to operate on
     * @return List of strings, possibly empty.
     */
    public static List<String> splitLines(String value) {
        final String lf = "\n";
        final String cr = "\r";
        final String crlf = cr + lf;

        List<String> list = new ArrayList<String>();

        if (value != null && value.length() > 0) {
            int crLfIndex = value.indexOf(crlf);
            int lfIndex = value.indexOf(lf);
            int crIndex = value.indexOf(cr);

            int smallest = Integer.MAX_VALUE;
            smallest = crIndex != -1 && crIndex < smallest ? crIndex : smallest;
            smallest = lfIndex != -1 && lfIndex < smallest ? lfIndex : smallest;
            smallest = crLfIndex != -1 && crLfIndex < smallest ? crLfIndex : smallest;

            String pattern =  null;
            if (smallest == crLfIndex) {
                pattern = crlf;
            } else if (smallest == lfIndex) {
                pattern = lf;
            } else if (smallest == crIndex) {
                pattern = cr;
            }

            if (pattern == null) {
                list.add(value);
            } else {
                for (String element : value.split(pattern)) {
                    list.add(element);
                }
            }
        }

        return list;
    }
}
