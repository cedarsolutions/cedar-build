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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Test class that we can use for testing serialization.
 * We intentionally include only getters to test the reflection behavior.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@XmlRootElement(name = "test")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestClass implements Serializable {

    private static final long serialVersionUID = 1L;

    private String string;
    private Date date;
    private Integer integer;
    private Long longinteger;
    private List<String> list;

    // There *must* a no-args constructor for the underlying JAXB/GSON functionality to work.
    public TestClass() {
    }

    public TestClass(String string, Date date, Integer integer, Long longinteger) {
        this(string, date, integer, longinteger, null);
    }

    public TestClass(String string, Date date, Integer integer, Long longinteger, List<String> list) {
        this.string = string;
        this.date = date;
        this.integer = integer;
        this.longinteger = longinteger;
        this.list = list;
    }

    public String getString() {
        return string;
    }

    public Date getDate() {
        return date;
    }

    public Integer getInteger() {
        return integer;
    }

    public Long getLonginteger() {
        return longinteger;
    }

    public List<String> getList() {
        return this.list;
    }

}
