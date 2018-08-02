package licensecreation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Each section of the license which takes automated input has its
 *  own enum, which contains the placeholder to search for and replace
 *  in the license text, a default replacement if no terms are provided,
 *  and a custom {@link ListJoiner} to determine how to concatenate terms.
 *  @author obasi42
 */
public enum LicenseSection {
	RELEVANT_LAWS ("[TERMS:RELEVANT LAWS]", "", new SimpleListJoiner("; ", "; and ", "")),
	
	PREAMBLE ("[TERMS:PREAMBLE]", "", new SimpleListJoiner(";\n\n", "; and\n\n", ".")),
	
	LICENSE ("[TERMS:LICENSE]", "**License.** ", new SimpleListJoiner(" ")),
	
	DEFINITIONS ("[TERMS:DEFINITIONS]", "**Definitions.**\n\n", new SortListJoiner("\n\n")),
	
	USE ("[TERMS:USE]", "**Restrictions on use.** The Data Recipient shall:\n\n", new SimpleListJoiner(";\n", "; and\n", ".\n").setPrefix("a) ")),
	
	PERMITTED_USES ("[TERMS:PERMITTED USES]", "", new SimpleListJoiner("\n\n")),
	
	CONFIDENTIALITY ("[TERMS:CONFIDENTIALITY]", "**Data confidentiality.** The Data Recipient shall:\n\n", new SimpleListJoiner(";\n", "; and\n", ".\n").setPrefix("a) ")),
	
	IRB ("[TERMS:IRB]", "**Institutional review board (IRB).** ", new SimpleListJoiner("\n\n")),
	
	AGENT ("[TERMS:AGENT]", "", new SimpleListJoiner("\n\n")),
	
	SECURITY ("[TERMS:SECURITY]", "**Data security.** The Data Recipient shall:\n\n", new SimpleListJoiner(";\n", "; and\n", ".\n").setPrefix("a) ")),
	
	BREACH_REPORTING ("[TERMS:BREACH REPORTING]", "**Data breach.** The Data Recipient shall:\n\n", new SimpleListJoiner(";\n", "; and\n", ".\n").setPrefix("a) ")),
	
	DESTRUCTION ("[TERMS:DESTRUCTION]", "When destroying records from the Data:\n", new SimpleListJoiner("\n\n")),
	
	SHARING ("[TERMS:SHARING]", "**Data sharing.**\n", new SimpleListJoiner("\n\n")),
	
	ATTRIBUTION ("[TERMS:ATTRIBUTION]", "**Attribution.** ", new SimpleListJoiner("\n\n"));

	/**
	 * Provide lookup from the placeholder string to the LicenseSection
	 */
	private static Map<String, LicenseSection> memo = null;

	/**
	 * The placeholder text in the license template where we will put the terms in this section.
	 */
	private final String placeholder;
	
	/**
	 * The heading for this section. This string should be empty if no heading is needed, otherwise it is markdown text.
	 */
	private final String heading;
	
	/**
	 * The way that a list of strings (i.e., terms) should be combined into markdown text for inclusion in the license. 
	 */
	private final ListJoiner listJoiner;

	LicenseSection(String ph, String heading, ListJoiner listJoiner) {
		this.placeholder = ph;
		this.heading = heading;
		this.listJoiner = listJoiner;
	}
	public String getPlaceholder() {
		return this.placeholder;
	}
	public String getHeading() {
		return this.heading;
	}

	/**
	 * Given a placeholder string, get the Licensesection associatged with it.
	 * @param ph
	 * @return
	 */
	public static synchronized LicenseSection lookup(String ph) {
		if (memo == null) {
			memo = new HashMap<>();
			for (LicenseSection c : LicenseSection.values()) {
				memo.put(c.placeholder, c);
			}
		}
		return memo.get(ph);
	}

	/**
	 * Turns a list of terms into a Pandoc markdown formatted String, based on the license section.
	 * @param list
	 * @return
	 */
	public String concatenate(List<String> list) {
		StringBuffer sb = new StringBuffer();
		sb.append(getHeading());
		this.listJoiner.join(sb, list);
		return sb.toString();
	}
		
	/**
	 *  An interface for concatenating a List of Strings.
	 */
	private static interface ListJoiner {
		public void join(StringBuffer sb, List<String> list);
	}
	
	/**
	 * Simply joins a List of Strings using a separator defined in the constructor.
	 * Allows for one, two, or three different, case-specific separators.
	 */
	private static class SimpleListJoiner implements ListJoiner {
		protected final String normalSeparator;
		protected final String secondLastSeparator;
		protected final String lastSeparator;
		protected String prefix;
		
		SimpleListJoiner(String sep) {
			this.normalSeparator = sep;
			this.secondLastSeparator = null;
			this.lastSeparator = null;
		}
		
		SimpleListJoiner(String sep, String lastSep) {
			this.normalSeparator = sep;
			this.secondLastSeparator = null;
			this.lastSeparator = lastSep;
		}

		SimpleListJoiner(String sep, String secondLastSep, String lastSep) {
			this.normalSeparator = sep;
			this.secondLastSeparator = secondLastSep;
			this.lastSeparator = lastSep;
		}

		SimpleListJoiner setPrefix(String prefix) {
			this.prefix = prefix;
			return this;
		}
		
		@Override
		public void join(StringBuffer sb, List<String> list) {
			for (int i = 0; i < list.size(); i++) {
				if (prefix != null) {
					sb.append(prefix);
				}
				sb.append(list.get(i));
				String sep = normalSeparator;
				if (secondLastSeparator != null && i == list.size()-2) {
					sep = secondLastSeparator;
				}
				if (lastSeparator != null && i == list.size() - 1) {
					sep = lastSeparator;	
				}	
				sb.append(sep);
			}			
		}
	}		
	
	private static class SortListJoiner extends SimpleListJoiner {
		public SortListJoiner(String sep) {
			super(sep);
		}

		@Override
		public void join(StringBuffer sb, List<String> list) {
			Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
			super.join(sb, list);
		}
	}
}
