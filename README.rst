The Decaffeinated Robot: Demo Source
====================================

:Author: Daniel Solano Gómez

.. contents::

Introduction
------------

This repository contains the source code for the various implementations of the
demo for my talk at Texas Linux Fest 2011, *The Decaffeinated Robot: Developing
on Android without Java*.


.. Note:: I am not a seasoned practitioner in all of these languages.  Feel
          free to submit a pull request if you find ways to improve these
          examples.  Also, any new implementations are also welcome.


Prerequisites
-------------

Android SDK
```````````

If you wish build any of the examples, at a minimum you will need:

1. A JDK, available from your Linux distribution or `directly from Oracle
   <http://java.sun.com/javase/downloads/index.jsp>`_
2. The Android SDK, available from the `Android Developers site
   <http://developer.android.com/sdk/index.html>`_

To install the Android SDK, `follow these instructions
<http://developer.android.com/sdk/installing.html>`_.  You can skip step 3
since none of these examples require you to use Eclipse.


Emulator or Android device
``````````````````````````

You can install and run the demo either onto a phone or to an emulated device.
To use your Android-based device, check the instructions on `‘Using Hardware
Devices’ <http://developer.android.com/guide/developing/device.html>`_.
Otherwise, to use the emulator read `‘Managing Virtual Devices’
<http://developer.android.com/guide/developing/devices/index.html>`_.

Git
```

Of course, to clone the repository, you will need Git_.

.. _Git: http://git-scm.com/

This repository
```````````````

You will need a local copy of the repository which you can get by issuing::

  git clone git://github.com/sattvik/decafbot.git


``ANDROID_SDK_HOME``
````````````````````

Finally, you should set the environment variable ``ANDROID_SDK_HOME`` to the
base directory of the Android SDK.


The implementations
-------------------

C (NDK)
```````

From the `Android Native Development Tools`__ site:

  The Android NDK is a companion tool to the Android SDK that lets you build
  performance-critical portions of your apps in native code. It provides
  headers and libraries that allow you to build activities, handle user input,
  use hardware sensors, access application resources, and more, when
  programming in C or C++. If you write native code, your applications are
  still packaged into an .apk file and they still run inside of a virtual
  machine on the device. The fundamental Android application model does not
  change.

  Using native code does not result in an automatic performance increase, but
  always increases application complexity. If you have not run into any
  limitations using the Android framework APIs, you probably do not need the
  NDK. Read What is the NDK? for more information about what the NDK offers and
  whether it will be useful to you.

The base directory for the NDK implementation is ``ndk``.  The Java source code
can be found in ``src`` and the C source code can be found in ``jni``.

.. __: http://developer.android.com/sdk/ndk/index.html


Additional prerequisites
~~~~~~~~~~~~~~~~~~~~~~~~

You will need the following additional tools to build and install the NDK
implementation of the demo:

* Ant_, the build tool used by default on Android

* The Native Development Tools (NDK), available at
  <http://developer.android.com/sdk/ndk/index.html>

To install the NDK, all you have to do is unpack it and make sure its base
directory is in your path.

.. _Ant: http://ant.apache.org

Building and installing
~~~~~~~~~~~~~~~~~~~~~~~

First, you must compile the C source files using::

  ndk-build

Now, you can build the package using:

  ant debug

To install the demo to a running emulator or an attached device use::

  ant install


Clojure
```````

From the Clojure_ web site:

  Clojure is a dynamic programming language that targets the Java Virtual
  Machine (and the CLR ). It is designed to be a general-purpose language,
  combining the approachability and interactive development of a scripting
  language with an efficient and robust infrastructure for multithreaded
  programming. Clojure is a compiled language - it compiles directly to JVM
  bytecode, yet remains completely dynamic. Every feature supported by Clojure
  is supported at runtime. Clojure provides easy access to the Java frameworks,
  with optional type hints and type inference, to ensure that calls to Java can
  avoid reflection.

  Clojure is a dialect of Lisp, and shares with Lisp the code-as-data
  philosophy and a powerful macro system. Clojure is predominantly a functional
  programming language, and features a rich set of immutable, persistent data
  structures. When mutable state is needed, Clojure offers a software
  transactional memory system and reactive Agent system that ensure clean,
  correct, multithreaded designs.

The base directory for the Clojure implementation is ``jvm-lang/clojure``, and
the source code for the demo can be found in ``src/clojure``.

.. _Clojure: http://www.clojure.org

Additional prerequisites
~~~~~~~~~~~~~~~~~~~~~~~~

In addition to the basic prerequisites, you will also need Ant_, the build tool
used by default on Android.

Building and installing
~~~~~~~~~~~~~~~~~~~~~~~

To build the package, simply use::

  ant debug

To install the demo to a running emulator or an attached device use::

  ant install


Mirah
`````

From the Mirah_ web site:

  Mirah is a new way of looking at JVM languages. In attempting to build a
  replacement for Java, we have followed a few guiding principals:

  * No runtime library

    Mirah does not impose any jar files upon you. YOU decide what your
    application’s dependencies should be.

  * Clean, simple syntax

    We have borrowed heavily from Ruby, but added static typing and minor
    syntax changes to support the JVM’s type system. The result is pleasing to
    the eye, but as powerful as Java.

  * Metaprogramming and macros

    Mirah supports various mechanisms for compile-time metaprogramming and
    macros. Much of the “open class” feel of dynamic languages is possible in
    Mirah.

  * No performance penalty

    Because Mirah directly targets the JVM’s type system and JVM bytecode, it
    performs exactly as well as Java.

The base directory for the Mirah implementation is ``jvm-lang/mirah``, and the
source code for the demo can be found in ``src``.

.. _Mirah: http://www.mirah.org/


Additional prerequisites
~~~~~~~~~~~~~~~~~~~~~~~~

In order to build and install the Mirah implementation, you will need:

* JRuby_ 1.6.0 or above

* Ant_, the build tool used by default on Android

* Mirah and Pindah_ [#]_, which you can install using ``gem`` as follows::

    gem install mirah
    gem install --version '= 0.1.0' pindah

You will also need to be sure the ``android`` executable from the SDK is in
your path.  For most shells, this can be accomplished using::

  export PATH=$PATH:"$ANDROID_SDK_HOME/tools"

.. _JRuby: http://www.jruby.org
.. _Pindah: https://github.com/mirah/pindah
.. [#] The recently released 0.1.1 version seems to have broken something.  I
       am going to look into it and submit a patch.

Building and installing
~~~~~~~~~~~~~~~~~~~~~~~

Once you have all of the required gems and your path properly set up, you
should be able to create a package using::

  rake debug

To install the demo to a running emulator or an attached device use::

  rake install


Ruby (Ruboto)
`````````````

From the Ruboto_ web site:

  ruboto-core is a framework for writing full Android apps in Ruby. It includes
  support libraries and generators for creating projects, classes, tests, and
  more.

The base directory for the Ruboto implementation is ``ruboto``, and the source
code for the demo can be found in ``assets/scripts``.

.. _Ruboto: http://ruboto.org/


Additional prerequisites
~~~~~~~~~~~~~~~~~~~~~~~~

In order to build and install the Ruby implementation, you will need:

* JRuby_ 1.6.0 or above

* Ant_, the build tool used by default on Android


Building and installing
~~~~~~~~~~~~~~~~~~~~~~~

Once you have all of the required programs properly set up, you should be able
to create a package using::

  rake debug

To install the demo to a running emulator or an attached device use::

  rake install


Scala
`````

From the Scala_ web site:

  Scala is a general purpose programming language designed to express common
  programming patterns in a concise, elegant, and type-safe way. It smoothly
  integrates features of object-oriented and functional languages, enabling
  Java and other programmers to be more productive. Code sizes are typically
  reduced by a factor of two to three when compared to an equivalent Java
  application.

The base directory for the Scala implementation is ``jvm-lang/scala``,
and the source code for the demo can be found in ``src/main/scala``.

.. _Scala: http://www.scala-lang.org

Additional prerequisites
~~~~~~~~~~~~~~~~~~~~~~~~

You will need the `simple-build-tool
<https://code.google.com/p/simple-build-tool/>`_.  Installation instructions
are available from the sbt wiki at
<https://code.google.com/p/simple-build-tool/wiki/Setup>.

Building and installing
~~~~~~~~~~~~~~~~~~~~~~~

Once you have set up your sbt script, you can build the demo package with::

  sbt update package-debug

To install the demo to the emulator, use::

  sbt install-emulator

To install the demo to an attached device, use::

  sbt install-device


Licenses
--------

This demo is licensed under a BSD-style license as follows:

| Copyright © 2011 Sattvik Software & Technology Resources, Ltd. Co.
| All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
   this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.
3. Neither the name of Sattvik Software & Technology Resources, Ltd. Co. nor
   the names of its contributors may be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.


Android
```````

Android is a trademark of Google Inc. Use of this trademark is subject to
Google Permissions.


Clojure
```````

This demo includes a compiled version of Clojure, which is licensed under the
Eclipse Public License 1.0.  The details of the Clojure license are available
in the file ``clojure-readme.txt``.

JRuby
`````

This demo includes compiled portions of JRuby, which is licensed under a tri
CPL/GPL/LGPL license.  Details are available in the files ``COPYING.JRUBY`` and
``LICENSE.RUBY``.
