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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

/**
 * JAXB utilities that operate on a cached JAXB context.
 *
 * <p>
 * The process of establishing a JAXB context can be pretty slow.  So, we want
 * to avoid having to do it more often than necessary.  This class keeps an
 * internal cache of contexts that have already been established.  It also
 * provides some utility methods to simplify marshalling and unmarshalling.
 * </p>
 *
 * <p>This class is duplicated from CedarCommon for build purposes.</p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class JaxbUtils {

    /** Singleton instance. */
    private static JaxbUtils INSTANCE;

    /** Map from class name to generated JAXB context. */
    private Map<String, JAXBContext> contextMap = new HashMap<String, JAXBContext>();

    /** Default constructor is private so class cannot be instantiated. */
    private JaxbUtils() {
    }

    /** Get an instance of this class to use. */
    public static synchronized JaxbUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JaxbUtils();
        }

        return INSTANCE;
    }

    /**
     * Get a JAXB context for the indicated class.
     * @param className  Name of the class to get a context for
     * @return JAXB context for the class.
     */
    public JAXBContext getJaxbContext(String className) {
        try {
            Class clazz = Class.forName(className);
            return this.getJaxbContext(clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error obtaining JAXB context: " + e.getMessage(), e);
        }
    }

    /**
     * Get a JAXB context for the indicated class.
     * @param clazz  Class to get a context for.
     * @return JAXB context for the class.
     */
    public synchronized <T> JAXBContext getJaxbContext(Class<T> clazz) {
        if (!this.contextMap.containsKey(clazz.getName())) {
            try {
                JAXBContext context = JAXBContext.newInstance(clazz);
                this.contextMap.put(clazz.getName(), context);
            } catch (Exception e) {
                throw new RuntimeException("Error obtaining JAXB context: " + e.getMessage(), e);
            }
        }

        return this.contextMap.get(clazz.getName());
    }

    /**
     * Marshal an object of type T, creating XML.
     * @param <T>  Type of the object
     * @param value  Value to marshal.
     * @return Generated XML.
     */
    public <T> String marshalDocument(T value) {
        try {
            JAXBContext context = this.getJaxbContext(value.getClass());

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);

            StringWriter writer = new StringWriter();
            marshaller.marshal(value, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException("Error marshalling XML: " + e.getMessage(), e);
        }
    }

    /**
     * Unmarshal XML, creating an object of type T.
     * @param <T>  Type of the object
     * @param type Type of the object to unmarshal
     * @param xml XML to use as source
     * @return Object of type T, unmarshalled from the XML.
     */
    public <T> T unmarshalDocument(Class<T> type, String xml) {
        return unmarshalDocument(type, xml, true);
    }

    /**
     * Unmarshal XML, creating an object of type T.
     * @param <T>  Type of the object
     * @param type Type of the object to unmarshal
     * @param xml XML to use as source
     * @param validate  Whether to validate the unmarshalling step
     * @return Object of type T, unmarshalled from the XML.
     */
    public <T> T unmarshalDocument(Class<T> type, String xml, boolean validate) {
        try {
            JAXBContext context = this.getJaxbContext(type);

            StringReader reader = new StringReader(xml);
            Source source = new StreamSource(reader);

            Unmarshaller unmarshaller = context.createUnmarshaller();

            // If an adapter fails, we don't get an unmarshal exception.
            // Instead, we have to register and interrogate a handler.
            // See also: http://java.net/jira/browse/JAXB-537
            ValidationEventCollector eventHandler = new ValidationEventCollector();
            unmarshaller.setEventHandler(eventHandler);

            JAXBElement<T> element = unmarshaller.unmarshal(source, type);

            if (validate) {
                if (eventHandler.hasEvents()) {
                    StringBuffer buffer = new StringBuffer();

                    buffer.append("Error unmarshalling XML: found validation error(s)");
                    for (ValidationEvent event : eventHandler.getEvents()) {
                        buffer.append("\n\t -> ");
                        buffer.append(event.getMessage());
                    }

                    throw new RuntimeException(buffer.toString());
                }
            }

            return element.getValue();
        } catch (JAXBException e) {
            throw new RuntimeException("Error unmarshalling XML: " + e.getMessage(), e);
        }
    }
}
