This is the Cedar Build project, which provides Gradle plugins and other
functionality for use in a standardized build process.  The home page for 
Cedar Build is:

   https://github.com/cedarsolutions/cedar-build

Version 0.8.x is supported for Gradle 1:

```
   buildscript {
       repositories {
           mavenLocal()   // Local Maven repository
           mavenCentral() // Maven Central repository
       }

       dependencies {
           classpath "com.googlecode.cedar-common:cedar-build:0.8.17"
       }
   }
```

Version 0.9.x is supported for Gradle 2:

```
   buildscript {
       repositories {
           mavenLocal()   // Local Maven repository
           mavenCentral() // Maven Central repository
       }

       dependencies {
           classpath "com.googlecode.cedar-common:cedar-build:0.9.3"
       }
   }
```

Version 0.8.17 is the last release for Gradle 1. There may be releases
later than version 0.9.3 for Gradle 2.  Check Maven Central to be sure.

