`license-gen` is a tool for automatically generating data-use agreements.

It is implemented in mostly in Java. The Java code invokes pandoc ([https://pandoc.org/](https://pandoc.org/)) to generate an output PDF from markdown-versions of licenses. Pandoc uses [LaTeX](https://www.latex-project.org/) to generate PDF files.

The subdirectory `java` contains the Java source code (and compiled class files).

The subdirectory `resources` contains some of the input files to the Java program, including the descriptions of input fields, markdown-based license templates (that contain placeholders that will be replaced by license terms), LaTeX template for Pandoc, etc.


## Requirements

- Pandoc must be installed and on the path

- LaTeX must be installed and on the path. LaTeX is invoked by Pandoc.

- Pandoc uses a LaTeX file to create a PDF from the generated markdown license. One such template is the file `pandoc_latex_template.tex` in the resources directory. If you would like to support digital signatures, your LaTeX installation must have the [digsig package](http://home.htp-tel.de/lottermose2/tex/dist/digsig.sty) installed.

## Building

There is a simple [Apache Ant](https://ant.apache.org/) `build.xml` file in this directory, and so the Java code can be compiled by executing `ant compile`.
	
## Running
 
The classes in the package `licensecreation.main` are the entry points for executing the program.
 
If you would like a GUI, you can run `LaunchGUI`, for example, as follows.

```
java -cp java/bin:libs/commons-cli-1.4.jar:libs/opencsv-3.7.jar \
     licensecreation.main.LaunchGUI \
     -terms ../../formalizations/src/ferpa/license_text.csv \
            ../../formalizations/src/cmr/license_text.csv \
     -template resources/markdown_license_template.txt \
     -inputs resources/inputs.csv \
     -o outputlicense.pdf \
     -pdt resources/pandoc_latex_template.tex
```

The arguments specify which license terms to use (here, the FERPA license terms: `../../formalizations/ferpa/license_text.csv`), which markdown template to use (`resources/markdown_license_template.txt`), a description of the user-supplied inputs (`resources/inputs.csv`), the Pandoc LaTeX template that is used to convert the markdown license into a PDF file (`resources/pandoc_latex_template.tex`), and an output file (`outputlicense.pdf`). In general, you can provide multiple licence term files and multiple input description files.

An alternative GUI, which asks the user questions based on the FERPA and CMR legislation, can be launched with the class `LaunchDecisionTreeWizard`:

```
java -cp java/bin:libs/commons-cli-1.4.jar:libs/opencsv-3.7.jar \
     licensecreation.main.LaunchDecisionTreeWizard \
     -terms ../../formalizations/ferpa/license_text.csv \
            ../../formalizations/cmr/license_text.csv \
     -template resources/markdown_license_template.txt \
     -inputs resources/inputs.csv \
     -o outputlicense.pdf \
     -pdt resources/pandoc_latex_template.tex
```

Finally, a command-line-only execution is provided by the `Main` class. In addition to the `-terms`, `-template`, `-inputs`, `-o`, and `-pdt` arguments, this class takes a `-cond <condlist>` argument, where `<condlist>` is a comma-separated list of conditions (which will be used to select the license terms to include in the license). It also takes zero or more `-subst <key=value>` arguments, which can be used to provide substitutions for user-supplied values. For example:

```
java -cp java/bin:libs/commons-cli-1.4.jar:libs/opencsv-3.7.jar \
     licensecreation.main.Main \
     -terms ../../formalizations/ferpa/license_text.csv \
            ../../formalizations/cmr/license_text.csv \
     -template resources/markdown_license_template.txt \
     -inputs resources/inputs.csv \
     -o outputlicense.pdf \
     -pdt resources/pandoc_latex_template.tex \
	 -conds data:FERPA:inScope,data:FERPA:PII,data:FERPA:auditException \
	 -subst "dataUser:supplied:name=Stephen Chong" \
	 -subst "[repository:supplied:name]=Harvard Dataverse"
```

	
