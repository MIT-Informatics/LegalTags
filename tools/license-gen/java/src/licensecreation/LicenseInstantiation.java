package licensecreation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import licensecreation.conditions.PrimitiveCondition;

/**
 * This class contains a reference to a {@link LicenseTemplate}, and contains
 * substitutions for placeholders and user data. This class also contains enough
 * information to select the relevant terms from the {@link LicenseTemplate},
 * and can produce a PDF of the instantiated license template.
 *
 * Here is the lifecycle for an object of this class: 1. Create object 2. Call
 * setActiveTerms or setActivePrimitiveConditions one or more times. 3. Provide
 * any additional substitutions (via addAdditionalSubstitution). 4. Call renderMarkdown
 *
 */
public class LicenseInstantiation {
	/**
	 * The license template that we are instantiating.
	 */
	private final LicenseTemplate template;

	/**
	 * The set of LicenseTerms that are "active", i.e., that should be included.
	 */
	private final Set<LicenseTerm> activeTerms;

	/**
	 * Additional substitutions
	 */
	private Map<String, String> additionalSubstitutions;

	/**
	 * Instantiates the license template with the given collection of LicenseTerms.
	 * 
	 * @param template
	 * @param selectedTerms
	 */
	public LicenseInstantiation(LicenseTemplate template, Collection<LicenseTerm> selectedTerms) {
		this.template = template;
		this.additionalSubstitutions = new LinkedHashMap<>();
		this.activeTerms = new LinkedHashSet<>();
		this.activeTerms.addAll(selectedTerms);
	}

	/**
	 * Instantiates the license template by selected the LicenseTerms whose condition is satisfied
	 * by the given collection of PrimitiveConditions.
	 * 
	 * @param template
	 * @param primConditions
	 * @param dummy
	 */
	public LicenseInstantiation(LicenseTemplate template, Collection<PrimitiveCondition> primConditions, boolean dummy) {
		this.template = template;
		this.additionalSubstitutions = new LinkedHashMap<>();
		this.activeTerms = new LinkedHashSet<>();
		activeTerms.addAll(template.getSatisfiedTerms(primConditions));
		
	}

	/**
	 * Add additional substitutions that will be performed during rendering.
	 * @param placeholder
	 * @param value
	 */
	public void addAdditionalSubsitution(String placeholder, String value) {
		placeholder = placeholder.trim();
		if (!placeholder.startsWith("[")) {
			placeholder = "["+placeholder;
		}
		if (!placeholder.endsWith("]")) {
			placeholder = placeholder + "]";
		}
		this.additionalSubstitutions.put(placeholder, value);
	}
	public String getAdditionalSubstitution(String placeholder) {
		return this.additionalSubstitutions.get(placeholder);
	}


	/**
	 * Add additional substitutions that will be performed during rendering.
	 * @param substs
	 */
	public void addAdditionalSubsitution(Map<String, String> substs) {
		for (String k : substs.keySet()) {
			this.addAdditionalSubsitution(k, substs.get(k));
		}
	}

	/**
	 * Get all placeholders that have not been filled in. 
	 * 
	 */
	public List<String> getMissingPlaceholders() {
		String text = this.substituteSectionPlaceholders();

		List<String> placeholders = new ArrayList<>();
		Pattern p = Pattern.compile("\\[[^\\]]*\\]");
		Matcher m = p.matcher(text);
		while (m.find()) {
			if (!placeholders.contains(m.group(0))) {
				placeholders.add(m.group(0));
			}
		}
		return placeholders;
	}

	/**
	 * Perform the substitutions of the section placeholders, returning the
	 * resulting string.
	 */
	private String substituteSectionPlaceholders() {
		if (activeTerms == null) {
			return template.licenseTemplateText;
		}
		// The substitutions for license categories.
		Map<LicenseSection, List<String>> genSubstitutions = new HashMap<>();

		for (LicenseTerm term : activeTerms) {
			List<String> catTermList = genSubstitutions.get(term.lsect);
			if (catTermList == null) {
				catTermList = new ArrayList<>();
				genSubstitutions.put(term.lsect, catTermList);
			}
			catTermList.add(term.text);
		}

		// Text-replace all of the categories
		String licenseText = template.licenseTemplateText;
		boolean madeSubst;
		do {
			madeSubst = false;
			for (LicenseSection cat : LicenseSection.values()) {
				if (licenseText.contains(cat.getPlaceholder())) {
					madeSubst = true;
					if (genSubstitutions.containsKey(cat)) {
						licenseText = licenseText.replace(cat.getPlaceholder(),
								cat.concatenate(genSubstitutions.get(cat)));
					} else {
						licenseText = licenseText.replace(cat.getPlaceholder(),
								template.getDefaultIfEmptyText(cat));
					}
				}
			}
		} while (madeSubst);

		// Text-replace all of the user-supplied information
		for (String placeholder : additionalSubstitutions.keySet()) {
			licenseText = licenseText.replace(placeholder, additionalSubstitutions.get(placeholder));
		}
		
		return licenseText;
	}

	public String renderMarkdown() {
		return substituteSectionPlaceholders();
	}

	public LicenseTemplate getTemplate() {
		return this.template;
	}
}
