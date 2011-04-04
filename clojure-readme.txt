Clojure for Android(tm)
=======================

Building
--------

In order to build Clojure with Android support, you will most likely need to
copy the included 'local.properties.example' file to 'local.properties' and
edit the following properties:

  android.enabled  Must be set to 'true' to enable Android support.

  android.version  The version of the Android SDK with which to build.  This
                   should be a number.  The minimum supported version is 7.

  android.sdk.dir  The root directory of your Android SDK installation.

You may optionally define the property 'proguard.jar' and set it to the
location of ProGuard's 'proguard.jar'.  This will enable the use of
ProGuard to perform a better minification of the bundled dx.jar, resulting in
a smaller Clojure jar file.


New Jar files
-------------

When Android support is enabled, Ant will build a couple extra files of
interest to Android developers:

clojure-nosrc.jar:
  Just like 'clojure.jar', but without source files.

clojure-dex.jar:
  A version of 'clojure-nosrc.jar' with all classes compiled into a Dalvik
  executable.  This file may be used with one of Android's class loaders.


Legal stuff
-----------

Copyright © 2011 Sattvik Software & Technology Resources
All rights reserved.

The use and distribution terms for this software are covered by the Eclipse
Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
be found in the file epl-v10.html at the root of this distribution.  By using
this software in any fashion, you are agreeing to be bound by the terms of
this license.  You must not remove this notice, or any other, from this
software.

Android is a trademark of Google Inc. Use of this trademark is subject to
Google Permissions.


Original readme
---------------

 *   Clojure
 *   Copyright (c) Rich Hickey. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.

Docs: http://clojure.org
Feedback: http://groups.google.com/group/clojure


To run:  java -cp clojure-${VERSION}.jar clojure.main

To build locally with Ant:  ant


Maven 2 build instructions:

  To build:  mvn package 
  The built JARs will be in target/

  To build without testing:  mvn package -Dmaven.test.skip=true

  To build and install in local Maven repository:  mvn install

  To build a ZIP distribution:  mvn package -Pdistribution
  The built .zip will be in target/


--------------------------------------------------------------------------
This program uses the ASM bytecode engineering library which is distributed
with the following notice:

Copyright (c) 2000-2005 INRIA, France Telecom
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.

3. Neither the name of the copyright holders nor the names of its
   contributors may be used to endorse or promote products derived from
   this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
THE POSSIBILITY OF SUCH DAMAGE.
