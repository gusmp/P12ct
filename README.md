
1.What is P12ct?
================

This is tiny tool for modify the alias and password of a PKC#12 in one step.

2.Requirements
===============

It is only required Java 1.6 or upper.
Maven is required in case of wating compile the source code.

3.Compile
=========

NOTE: There are binary files ready to run in distribution folder.

As any other maven project just type:

mvn clean package -DskipTests=true

Now copy the jar 'original-p12ct.jar' into distribution folder wih the name 'p12ct.jar'.

4.Run P12ct
===========

Just execute:

run.bat (this file is available in the distribution folder)

or

java -cp bcprov-jdk15-1.46.jar -jar p12ct.jar

NOTE: p12ct requires Bouncy Castle. So the file bcprov-jdk15-1.46.jar must be in same folder as p12ct.jar. If not, change the parameter cp

4.Usage
=======

P12ct is an interative tool. So just run the application an follow the steps
