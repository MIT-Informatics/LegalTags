package licensecreation;

import java.util.Set;

import licensecreation.conditions.Condition;
import licensecreation.conditions.PrimitiveCondition;

/**
 * A term which may or may not be included in any given license. Contains
 * the text of the term, its unique identifier, the {@link LicenseSection}
 * in which it belongs, 
 * and the {@link Condition}s which activate it.
 * @author obasi42
 *
 */
public class LicenseTerm {
	/**
	 * A human-readable unique identifier for the license term. e.g., "license:preamble:FERPA:data"
	 */
	final String uid;
	
	/**
	 * The text for the term, which will be put into the license when the license is generated.
	 */
	final String text;
	
	/**
	 * The appropriate section for this license term
	 */
	final LicenseSection lsect;

	/**
	 * The condition under which this term should be included in a clicense.
	 */
	final Condition cond;

	LicenseTerm(String uid, String text, LicenseSection lsect, Condition cond) {
		this.uid = uid;
		this.text = text;
		this.lsect = lsect;
		this.cond = cond;
	}
	
	public String getUID() {
		return this.uid;
	}
	
	/**
	 * Give a set of PrimitiveConditions (i.e., the set of 
	 * primitive conditions that hold),
	 * should this 
	 * license term be included in the license?
	 * @param conds
	 * @return
	 */
	public boolean isSatisfied(Set<PrimitiveCondition> conds) {
		return cond.isSatisfied(conds);
	}

	@Override
	public String toString() {
		return uid;
	}

	public Condition getCondition() {
		return cond;
	}
	
}