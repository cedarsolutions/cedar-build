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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Filesystem-related utilities.
 * This class is duplicated from CedarCommon for build purposes.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class FilesystemUtils {

    /** Normalize a path, so it uses all '/' characters. */
    public static String normalize(String path) {
        if (path == null) {
            return null;
        } else if (path.length() == 0) {
            return "";
        } else {
            File file = new File(path);
            return file.getPath().replace("\\", "/");
        }
    }

    /**
     * Join a list of filesystem elements together, kind of like Python's os.path.join().
     * @param elements  List of elements to be joined.
     * @return Complete path, with elements separated by '/' characters.
     */
    public static String join(String ... elements) {
        StringBuffer buffer = new StringBuffer();

        if (elements.length > 0) {
            int index = 0;
            for (String element : elements) {
                if (element != null) {
                    if (index++ > 0) {
                        buffer.append("/");
                    }
                    buffer.append(element);
                }
            }
        }

        return buffer.toString();
    }

    /**
     * Get the base name for a path, like Python's os.path.basename().
     * @param path    Path to operate on
     * @return Base name for the path, possibly null.
     */
    public static String getBasename(String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        } else {
            File file = new File(path);
            return StringUtils.trimToNull(file.getName());
        }
    }

    /**
     * Get the directory name for a path, like Python's os.path.dirname().
     * @param path    Path to operate on
     * @return Directory name for the path, possibly null.
     */
    public static String getDirname(String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        } else {
            File file = new File(path);
            String parent = file.getParent();
            return parent == null ? null : parent.replace("\\", "/");
        }
    }

    /**
     * Copy a file into a directory, overwriting the file if it exists.
     * @param sourceFilePath  Source file path
     * @param targetDirPath   Target directory path
     * @throws RuntimeException If there is a problem with the filesystem operation.
     */
    public static void copyFileToDir(String sourceFilePath, String targetDirPath) {
        try {
            File sourceFile = new File(sourceFilePath);
            String targetFilePath = join(targetDirPath, sourceFile.getName());
            File targetFile = new File(targetFilePath);
            copyFile(sourceFile, targetFile);
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy directory: " + e.getMessage(), e);
        }
    }

    /**
     * Copy a file, overwriting the target if it exists.
     * @param sourceFilePath  Source file path
     * @param targetFilePath  Target file path
     * @throws RuntimeException If there is a problem with the filesystem operation.
     */
    public static void copyFile(String sourceFilePath, String targetFilePath) {
        File sourceFile = new File(sourceFilePath);
        File targetFile = new File(targetFilePath);
        copyFile(sourceFile, targetFile);
    }

    /**
     * Copy a file, overwriting the target if it exists.
     * @param sourceFile  Source file
     * @param targetFile  Target file
     * @throws RuntimeException If there is a problem with the filesystem operation.
     */
    public static void copyFile(File sourceFile, File targetFile) {
        FileChannel sourceChannel = null;
        FileChannel targetChannel = null;

        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }

            sourceChannel = new FileInputStream(sourceFile).getChannel();
            targetChannel = new FileOutputStream(targetFile).getChannel();
            targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy file: " + e.getMessage(), e);
        } finally {
            close(sourceChannel);
            close(targetChannel);
        }
    }

    /**
     * Create a file in an existing directory.
     * @param filePath  Path of the file to create
     * @throws RuntimeException If there is a problem with the filesystem operation.
     */
    public static void createFile(String filePath) {
        try {
            new File(filePath).createNewFile();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create file: " + e.getMessage(), e);
        }
    }

    /**
     * Remove a file, if it exists.
     * @param filePath  Path of the file to remove
     * @throws RuntimeException If there is a problem with the filesystem operation.
     */
    public static void removeFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("Failed to remove file.");
            } else if (fileExists(filePath)) {
                throw new RuntimeException("Failed to remove file.");
            }
        }
    }

    /**
     * Indicate whether a file exists.
     * @param filePath  Path of the file to check
     * @return True if the file exists, false otherwise.
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    /**
     * Create a directory including all parent directories, like 'mkdir -p'.
     * @param dirPath  Path of the directory to create
     * @throws RuntimeException If there is a problem with the filesystem operation.
     */
    public static void createDir(String dirPath) {
        File dirFile = new File(dirPath);
        if (!dirFile.mkdirs() && !dirFile.exists()) {
            throw new RuntimeException("Failed to create directory " + dirPath + ".");
        }
    }

    /**
     * Remove an empty directory, if it exists.
     * @param dirPath   Path of the directory to remove.
     * @throws RuntimeException If there is a problem with the filesystem operation.
     */
    public static void removeEmptyDir(String dirPath) {
        removeDir(dirPath, false);
    }

    /**
     * Remove a directory, if it exists.
     * If recursive=false, the directory must be empty.
     * @param dirPath    Path of the directory to remove.
     * @param recursive  Whether the directory should be removed recursively.
     * @throws RuntimeException If there is a problem with the filesystem operation.
     */
    public static void removeDir(String dirPath, boolean recursive) {
        File dirFile = new File(dirPath);
        if (!recursive) {
            if (dirFile.exists()) {
                if (!dirFile.delete()) {
                    throw new RuntimeException("Failed to remove directory.");
                } else if (dirExists(dirPath)) {
                    throw new RuntimeException("Failed to remove directory.");
                }
            }
        } else {
            removeDirRecursive(dirFile);
            if (dirExists(dirPath)) {
                throw new RuntimeException("Failed to remove directory.");
            }
        }
    }

    /**
     * Recursively remove a directory and all of its contents.
     * @param dirFile   Directory to remove
     */
    private static void removeDirRecursive(File dirFile) {
        if (dirFile.exists()) {
            File[] targetFiles = dirFile.listFiles();
            for (int i = 0; i < targetFiles.length; i++) {
                if (targetFiles[i].isDirectory()) {
                    removeDirRecursive(targetFiles[i]);
                } else {
                    targetFiles[i].delete();
                }
            }

            dirFile.delete();
        }
    }

    /**
     * Indicates whether a directory exists.
     * @param dirPath   Path of directory to check.
     * @return True if the directory exists, false otherwise.
     */
    public static boolean dirExists(String dirPath) {
        File dirFile = new File(dirPath);
        return dirFile.exists() && dirFile.isDirectory();
    }

    /**
     * Writes string contents to a file, replacing the file if it already exists.
     * @param filePath  Path of the file to write
     * @param contents  String contents to be written to the file
     * @throws RuntimeException If there is a problem with the filesystem operation.
     */
    public static void writeFileContents(String filePath, String contents) {
        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(filePath);
            stream.write(StringUtils.getBytes(contents));
        } catch (Exception e) {
            throw new RuntimeException("Failed to write file: " + e.getMessage(), e);
        } finally {
            close(stream);
        }
    }

    /**
     * Writes string contents to a file, replacing the file if it already exists.
     * @param filePath  Path of the file to write
     * @param contents  Set of lines to be written to the file
     * @throws RuntimeException If there is a problem with the filesystem operation.
     */
    public static void writeFileContents(String filePath, List<String> contents) {
        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(filePath);
            for (String line : contents) {
                stream.write(StringUtils.getBytes(line));
                stream.write(StringUtils.LINE_ENDING_BYTES);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to write file: " + e.getMessage(), e);
        } finally {
            close(stream);
        }
    }

    /**
     * Get the contents of a file.
     * @param filePath  Path of the file to read
     * @return Contents of the file as a list of strings.
     * @throws RuntimeException If there is a problem with the filesystem operation.
     */
    public static List<String> getFileContents(String filePath) {
        String line = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        List<String> lines = new ArrayList<String>();

        try {
            fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);

            do {
                line = bufferedReader.readLine();
                if (line != null) {
                    lines.add(line);
                }
            } while (line != null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + e.getMessage(), e);
        } finally {
            close(bufferedReader);
            close(fileReader);
        }

        return lines;
    }

    /**
     * Get the contents of a file.
     * @param filePath  Path of the file to read
     * @return Contents of the file as a single multi-line string.
     * @throws RuntimeException If there is a problem with the filesystem operation.
     */
    public static String getFileContentsAsString(String filePath) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        StringBuffer buffer = new StringBuffer();

        try {
            fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);

            int character = -1;
            while ((character = bufferedReader.read()) != -1) {
                buffer.append((char) character);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + e.getMessage(), e);
        } finally {
            close(bufferedReader);
            close(fileReader);
        }

        return buffer.toString();
    }

    /**
     * Get a list of the files and directories immediately within a directory.
     * @param dirPath  Directory to operate on
     * @return List of files and directories within the directory.
     * @throws RuntimeException If there is a problem with the filesystem operation.
     */
    public static List<String> getDirContents(String dirPath) {
        return getDirContents(dirPath, false);
    }

    /**
     * Get a list of the files and directories within a directory.
     * @param dirPath    Directory to operate on
     * @param recursive  Recursively dig through all subdirectories
     * @return List of files and directories within the directory.
     * @throws RuntimeException If there is a problem with the filesystem operation.
     */
    public static List<String> getDirContents(String dirPath, boolean recursive) {
        try {
            List<String> contents = new ArrayList<String>();

            File dirFile = new File(dirPath);
            if (!dirFile.exists() || !dirFile.isDirectory()) {
                throw new RuntimeException("Directory does not exist.");
            }

            File[] files = dirFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                contents.add(file.getName());
                if (file.isDirectory()) {
                    if (recursive) {
                        List<String> subdir = getDirContents(file.getCanonicalPath(), recursive);
                        for (String entry : subdir) {
                            String path = join(file.getName(), entry);
                            contents.add(path);
                        }
                    }
                }
            }

            return contents;
        } catch (Exception e) {
            throw new RuntimeException("Error getting dir contents: " + e.getMessage(), e);
        }
    }

    /**
     * Unzips the passed-in zip file.
     * @param zipFilePath   Path to the zip file on disk
     * @param targetDirPath Target directory that the zip contents should be written into
     * @see <a href="http://stackoverflow.com/questions/3321842/poor-performance-of-javas-unzip-utilities">Stack Overflow</a>
     */
    @SuppressWarnings("unchecked")
    public static final void unzip(String zipFilePath, String targetDirPath) {
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFilePath);
            Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                java.io.File f = new java.io.File(targetDirPath, entry.getName());
                if (entry.isDirectory()) { // if its a directory, create it
                    continue;
                }

                if (!f.exists()) {
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                }

                InputStream is = zip.getInputStream(entry); // get the input stream
                OutputStream os = new java.io.FileOutputStream(f);
                try {
                    byte[] buf = new byte[4096];
                    int r;
                    while ((r = is.read(buf)) != -1) {
                        os.write(buf, 0, r);
                    }
                } finally {
                    close(os);
                    close(is);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error unzipping file: " + e.getMessage(), e);
        } finally {
            close(zip);
        }
    }

    /** Close a reader, ignoring errors. */
    private static void close(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) { }
        }
    }

    /** Close an input stream, ignoring errors. */
    private static void close(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) { }
        }
    }

    /** Close an output stream, ignoring errors. */
    private static void close(OutputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) { }
        }
    }

    /** Close a file channel, ignoring errors. */
    private static void close(FileChannel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) { }
        }
    }

    /** Close a zip file channel, ignoring errors. */
    private static void close(ZipFile zip) {
        if (zip != null) {
            try {
                zip.close();
            } catch (IOException e) { }
        }
    }

}
