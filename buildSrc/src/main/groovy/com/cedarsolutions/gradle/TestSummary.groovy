// vim: set ft=groovy ts=4 sw=4:
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// *              C E D A R
// *          S O L U T I O N S       "Software done right."
// *           S O F T W A R E
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Copyright (c) 2013-2015 Kenneth J. Pronovici.
// * All rights reserved.
// *
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the Apache License, Version 2.0.
// * See LICENSE for more information about the licensing terms.
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Author   : Kenneth J. Pronovici <pronovic@ieee.org>
// * Language : Gradle (>= 2.5)
// * Project  : Common Gradle Build Functionality
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package com.cedarsolutions.gradle

import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

/**
 * Test summary listener that prints out test results when tests are executed.
 * This makes the output legible in Eclipse; normally, the output isn't printed because Eclipse as a dumb terminal.
 * @see http://forums.gradle.org/gradle/topics/how_to_print_a_maven_like_test_summary_on_the_console
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class TestSummary implements TestListener {

    long passed;
    long failed;
    long skipped;

    public void beforeSuite(TestDescriptor suite) {
        passed = 0;
        failed = 0;
        skipped = 0;
    }

    public void afterSuite(TestDescriptor suite, TestResult result) {
        // We get duplicate notifications; the one we want looks like "Gradle Test Run :test:clienttest"
        if (suite.getName().startsWith("Gradle Test Run") && this.getTotal() > 0) {
            System.out.printf("*** Test results: passed=%d, failed=%d, skipped=%d%n", passed, failed, skipped);
        }
    }

    public void beforeTest(TestDescriptor test) {
    }

    public void afterTest(TestDescriptor test, TestResult result) {
        passed += result.getSuccessfulTestCount();
        failed += result.getFailedTestCount();
        skipped += result.getSkippedTestCount();
    }

    public int getTotal() {
        return this.passed + this.failed + this.skipped;
    }

}

