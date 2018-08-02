package licensecreation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import licensecreation.conditions.Condition;
import licensecreation.conditions.PrimitiveCondition;

import com.opencsv.CSVReader;

/**
 * A <code>LicenseTemplate</code> contains all of the information
 * required to generate a license agreement, including the template
 * of the license text, the set of possible {@link LicenseTerm}s, and the set
 * of {@link Condition}s that determine which terms are included.
 *
 */
public class LicenseTemplate {
	/**
	 * This condition string identifies terms that should be used if there is no other term in the section.
	 */
	private static final Object DEFAULT_IF_EMPTY_CONDITION = "data:default:ifNoOther";

	/**
	 * The text of the license template. 
	 * Contains placeholders that will be replaced during instatiation.
	 */
	public final String licenseTemplateText;

	/**
	 * The sets of all terms and their conditions, not just the included ones.
	 * They are included in the license or excluded based on the map supplied in
	 * activateTerms()
	 * 
	 */
	private final Map<String, LicenseTerm> terms;
	
	/**
	 * The primitive conditions that are used to determine whether license terms
	 * are included. This is a map from the string name of the primitive
	 * condition to the <code>PrimitiveCondition</code> itself.
	 */
	private final Map<String, PrimitiveCondition> primConditions;
	
	/**
	 * Human-readable description of user-supplied inputs.
	 */
	private final Map<String, String> inputDescriptions;
	
	/**
	 * Suitable defaults for of user-supplied inputs.
	 */
	private final Map<String, String> inputDefaults;

	/**
	 * Appropriate default text for each license section, if that section is empty.
	 */
	private final Map<LicenseSection, String> defaultTextIfEmpty;
	
	private LicenseTemplate(String licenseTemplateText, Set<LicenseTerm> terms, Set<PrimitiveCondition> primConditions, Map<LicenseSection, String> defaultTextIfEmpty,
			Map<String,String> inputDescriptions, Map<String, String> inputDefaults) {
		this.terms = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		for (LicenseTerm t : terms) {
			if (this.terms.containsKey(t.uid)) {
				throw new IllegalArgumentException("Duplicate term identifier: " + t);
			}
			this.terms.put(t.uid, t);
		}
		this.primConditions = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		for (PrimitiveCondition c : primConditions) {
			this.primConditions.put(c.uid, c);
		};
		this.licenseTemplateText = licenseTemplateText;
		this.defaultTextIfEmpty = defaultTextIfEmpty;
		this.inputDescriptions = inputDescriptions;
		this.inputDefaults = inputDefaults;

	}

	/**
	 * Create a <code>LicenseTemplate</code>, using the specified arguments for the input files.
	 * 
	 * @param licenseTermsFile the license terms, and conditions under which the terms will be included
	 * @param templateFile the license template, a markdown file containing place holders that will be replaced with license terms
	 * @param inputDescriptorFile inputs that are required in order to fill in additional info in the license (e.g., name of recipient)
	 * @return
	 * @throws IOException
	 */
	public static LicenseTemplate createLicenseTemplate(List<InputStream> licenseTermsFiles, InputStream templateFile, List<InputStream> inputDescriptorFiles) throws IOException {
		Set<LicenseTerm> terms = new LinkedHashSet<>();
		Set<PrimitiveCondition> primConds = new LinkedHashSet<>();
		Map<LicenseSection, String> defaultTextIfEmpty = new LinkedHashMap<>();

		// open and read the license terms files.
		for (InputStream licenseTermsFile : licenseTermsFiles) {
			try (CSVReader reader = new CSVReader(new InputStreamReader(licenseTermsFile))) {
				// skip the first line of the csv, which is just headers
				reader.readNext();
				String [] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					// nextLine[] is an array of values from the csv line
					String id = nextLine[0];
					String law = nextLine[1];
					String section = nextLine[2];
					String termText = nextLine[3];
					String condition = nextLine[5];


					// Lookup the license section
					LicenseSection cat = LicenseSection.lookup("[" + section + "]");
					if (cat == null) {
						throw new RuntimeException("Can't find section provided for term " + id + " with section " + section);
					}

					// Strip any enclosing quotations around the termText
					char first = termText.charAt(0);
					char last = termText.charAt(termText.length()-1);
					if ((first == '"' || first == '\u0093' || first == '\u201c') &&
							(last == '"' || last == '\u0094' || last == '\u201d')) {
						termText = termText.substring(1, termText.length() - 1);
					}

					if (condition.equals(DEFAULT_IF_EMPTY_CONDITION)) {
						// These entries are text that should be used
						// if there is no active term in the given
						// section.
						defaultTextIfEmpty.put(cat, termText);
					}
					else {				
						Condition cond = Condition.parseCondition(condition);
						cond.addAllPrimitiveConds(primConds);


						LicenseTerm term = new LicenseTerm(id, termText, cat, cond);
						terms.add(term);
					}
				}
			}
		}
		
		// Read in the default license agreement file
		ByteArrayOutputStream baos = new ByteArrayOutputStream();				
		byte[] buffer = new byte[1024];
		int read = 0;
		while ((read = templateFile.read(buffer, 0, buffer.length)) != -1) {
			baos.write(buffer, 0, read);
		}		
		baos.flush();		
		String licenseTemplate = new String(baos.toByteArray(), "UTF-8");
				
		// Read in the input descriptions
		Map<String, String> inputDescriptions = new LinkedHashMap<>();
		Map<String, String> inputDefaults = new LinkedHashMap<>();
		
		for (InputStream inputDescriptorFile : inputDescriptorFiles) {
			try (CSVReader reader = new CSVReader(new InputStreamReader(inputDescriptorFile))) {
				// skip the first line of the csv, which is just headers
				reader.readNext();

				String [] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					// nextLine[] is an array of values from the csv line
					String placeholder = nextLine[0];
					String description = nextLine[1];
					String defalt = nextLine[2];
					placeholder = "[" + placeholder + "]";
					inputDescriptions.put(placeholder, description);
					inputDefaults.put(placeholder, defalt);

				}
			}
		}
		
		return new LicenseTemplate(licenseTemplate, terms, primConds, defaultTextIfEmpty, inputDescriptions, inputDefaults);

	}

	/**
	 * Gets the list of LicenseTerms
	 * @return a List of LicenseTerms.
	 */
	public List<LicenseTerm> getTerms() {
		return new ArrayList<>(this.terms.values());
	}

	/**
	 * Gets the LicenseTerm for the corresponding key, if any.
	 * @return LicenseTerm
	 */
	public LicenseTerm getTerm(String termKey) {
		return this.terms.get(termKey);
	}

	/**	
	 * Gets the set of primitive conditions that are mentioned in the template
	 * @return 
	 */
	public Set<PrimitiveCondition> getPrimitiveConditions() {
		Set<PrimitiveCondition> s = new LinkedHashSet<>(primConditions.values());
		return s;
	}

	/**
	 * Takes a list of Condition ids and returns the ids of terms which
	 * are activated by those conditions.
	 * @param conditions the condition list.
	 * @return A list of term ids.
	 */
	public List<LicenseTerm> getSatisfiedTerms(Collection<PrimitiveCondition> conditions) {
		Set<PrimitiveCondition> s;
		if (conditions instanceof Set) {
			s = (Set<PrimitiveCondition>) conditions;
		}
		else {
			s = new HashSet<>(conditions);
		}
		List<LicenseTerm> list = new ArrayList<>();
		for (LicenseTerm term : terms.values()) {
			if (term.isSatisfied(s)) {
				list.add(term);
			}
		}
		return list;
	}

	/**
	 * Finds and returns all placeholders which contains the string ":supplied:"
	 * within brackets.
	 * @param license the license text which contains placeholders
	 * @return the list of placeholders
	 */
	public Set<String> findSuppliedInfo() {
		Set<String> info = new LinkedHashSet<>();
		Pattern p = Pattern.compile("\\[[^\\[\\]]*:supplied:[^\\[\\]]*\\]");
		Matcher m = p.matcher(licenseTemplateText);
		while (m.find()) {
			info.add(m.group(0));
		}
		return info;
	}

	public String getInputDescription(String placeholder) {
		return this.inputDescriptions.get(placeholder);
	}
	public String getInputDefault(String placeholder) {
		return this.inputDefaults.get(placeholder);
	}
	public String getDefaultIfEmptyText(LicenseSection cat) {
		String s = this.defaultTextIfEmpty.get(cat);
		return s == null ? "" : s;
	}

}
