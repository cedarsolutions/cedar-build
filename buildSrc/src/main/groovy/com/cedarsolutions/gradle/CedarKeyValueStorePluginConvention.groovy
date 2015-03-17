// vim: set ft=groovy ts=4 sw=4:
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// *              C E D A R
// *          S O L U T I O N S       "Software done right."
// *           S O F T W A R E
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Copyright (c) 2013 Kenneth J. Pronovici.
// * All rights reserved.
// *
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the Apache License, Version 2.0.
// * See LICENSE for more information about the licensing terms.
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Author   : Kenneth J. Pronovici <pronovic@ieee.org>
// * Language : Gradle (>= 1.7)
// * Project  : Common Gradle Build Functionality
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package com.cedarsolutions.gradle

import org.gradle.api.Project
import java.util.Map
import java.util.HashMap


/** 
 * Plugin convention for cedarKeyValueStore. 
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class CedarKeyValueStorePluginConvention {

    /** Project tied to this convention. */
    private Project project;

    /** Create a convention for a project. */
    public CedarKeyValueStorePluginConvention(Project project) {
        this.project = project;
    }

    /** Set a value in the shared cache. */
    def setCacheValue(key, value) {
        KeyValueStore.getInstance().setCacheValue(key, value)
    }

    /** Get a value from the shared cache. */
    def getCacheValue(key) {
        KeyValueStore.getInstance().getCacheValue(key)
    }

    /** Singleton key/value store that is shared across instances of the plugin. */
    private static class KeyValueStore {

        /** Singleton instance. */
        private static KeyValueStore INSTANCE;

        /** List of classes which have been registered, as recorded by the singleton. */
        private Map cache;

        /** Default constructor is private so class cannot be instantiated. */
        private KeyValueStore() {
            this.cache = new HashMap();
        }

        /** Get an instance of this class to use. */
        public static synchronized KeyValueStore getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new KeyValueStore();
            }

            return INSTANCE;
        }

        /** Set a value in the shared cache. */
        def setCacheValue(key, value) {
            this.cache.put(key, value);
        }

        /** Get a value from the shared cache. */
        def getCacheValue(key) {
            this.cache.get(key)
        }
    }

}

