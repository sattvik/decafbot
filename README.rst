The Decaffeinated Robot: Demo Source
====================================

:Author: Daniel Solano Gómez

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

Clojure
```````

Clojure_, a Lisp, is a dynamic and functional programming language that helps
you write correct concurrent programs with a rich set of immutable persistent
data structures and a software transactional memory to help manage mutable
state.

The base directory for the Clojure implementation is in ``jvm-lang/clojure``,
and the source code for the demo can be found in ``src/clojure``.

.. _Clojure: http://www.clojure.org

Additional prerequisites
~~~~~~~~~~~~~~~~~~~~~~~~

In addition to the basic prerequisites, you will also need Ant_, the build tool
used by default on Android.

.. _Ant: http://ant.apache.org/

Building and installing
~~~~~~~~~~~~~~~~~~~~~~~

To build the package, simply use ``ant debug``.  To install the demo to a
running emulator or an attached device use ``ant install``.





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
