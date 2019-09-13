`Expert` is an expert reasoning system that takes formalizations of (privacy-relevant aspects of) legislation, regulation, and practices, and answers data repository relevant queries. For example, given a dataset with these characteristics, is the repository allowed to accept the data?

## Structure

The structure of this directory is as follows.

- `prolog-src` contains Prolog source code for the expert system.

- `java` contains Java source code for the expert system as well as a README with code documentation. The Java code provides a GUI front end for querying the logic program as well as a command line utility.

## Dependencies

The tool depends on Java.

This tool relies on the [JIProlog](https://www.jiprolog.com/) library. JIProlog is released under a ALGPL v3.0 license, and is *not* contained in the distribution of this software. To use this tool, you must download the `.jar` file and place it in the `libs` directory. The following command will do this.

```
mkdir -p libs && \
  mkdir temp_gnuprolog && \
  cd temp_gnuprolog && \
  wget http://ftp.gnu.org/gnu/gnuprologjava/gnuprologjava-0.2.6.zip && \
  unzip -q -n gnuprologjava-0.2.6.zip && \
  mv *.jar ../libs && \
  cd .. && \
  rm -rf temp_gnuprolog
```
TODO make sure the library is included in the classpath before compiling.
## Compiling

There is a simple [Apache Ant](https://ant.apache.org/) `build.xml` file in this directory, and so the Java code can be compiled by executing `ant build`.

## Instructions

Command line only testing can be run with the following command:

```
java -cp java/bin:libs/jiprolog-4.1.6.1.jar:prolog-src:../../formalizations/src \
legaltags.expert.Main
```

A Java graphical user interface can be launched with the following command:

```
java -cp java/bin:libs/jiprolog-4.1.6.1.jar:prolog-src:../../formalizations/src \
legaltags.expert.main.LaunchGUI
```
