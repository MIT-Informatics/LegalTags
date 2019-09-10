## Formalizations

The following formalizations are defined in subdirectories:

- `cmr` provides a formalization of the Code of Massachusetts Regulation regarding privacy of personal information.
- `ferpa` provides a formalization the Family Educational Rights and Privacy Act.
- `harvard` is an example module of local policy combining the FERPA and CMR formalizations.
- `std` defines formalizations involving license generation.   
## Prolog files

These Prolog files specify how to determine whether actions are in scope, permitted, or denied under a given legislation or policy's formalization. 
- `cmr/cmr.pro`
- `ferpa/ferpa.pro`
- `harvard/harvard.pro`

## License Terms files

License term files describe license terms (i.e., terms to include in automatically generated licenses), along with information about when they should be included in a license, and where in the license they should appear if included.

The format of a license term file is a CSV file, where the first line of the CSV file is column headers. The columns that are used by the tools current include the following:

- 1st column: LicenseTermID. A unique identifier for the license term.

- 3rd column: Template section. Which section of the license template should the term appear in, if it is included. The permissible values for this column will depend on the license template.

- 4th column: Markdown text. Text of the license term, in markdown. This is the text that will be included in the license. This text may include placeholders for text that will be supplied by the user or the tool, e.g., "`[dataUser:supplied:FERPA:studyStartDate]'".

- 6th column: Condition. This is a boolean formula that indicates when the license term should be included in the license. Many of the formula are just simple "primitive conditions", i.e., a single condition that the user or a tool tells the license generator whether that condition is met, for example `data:FERPA:InScope`. Boolean formulas include negation (`NOT`), conjunction (`AND`), disjunction (`OR`), and parentheses (`(` and `)`) to indicate priority.
