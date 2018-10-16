`Expert` is an expert reasoning system that takes formalizations of (privacy-relevant aspects of) legislation, regulation, and practices, and answers data repository relevant queries. For example, given a dataset with these characteristics, is the repository allowed to accept the data?


## Structure

The structure of this directory is as follows.

- `prolog-src` contains Prolog source code for the expert system.

- `java` contains Java source code for the expert system. The Java code provides a GUI front end for querying the logic program.

## Dependencies

This tool relies on the [GNU Java Prolog](https://www.gnu.org/software/gnuprologjava/) library, which is a Java implementation of ISO Prolog. GNU Java Prolog is released under a LGPL v3 license, and i s *not* contained in the distribution of this software. To use this tool, you must download the `.jar` file and place it in the `libs` directory. The following command will do this.

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

## Compiling

TODO

## Instructions

TODO: How to use the GUI, and use the formalizations. 
