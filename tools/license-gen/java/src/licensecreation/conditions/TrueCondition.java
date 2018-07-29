package licensecreation.conditions;

import java.util.Set;

/**
 * The condition that is always true.
 *
 */
public class TrueCondition extends Condition {
	public static final TrueCondition INSTANCE = new TrueCondition();
	
	public static boolean isTrue(String s) {
		return "TRUE".equalsIgnoreCase(s) ||
				"data:default:always".equalsIgnoreCase(s);
	}
	private TrueCondition() {
	}
	
	@Override
	public boolean isSatisfied(Set<PrimitiveCondition> conds) {
		return true;
	}

	
	@Override
	public void addAllPrimitiveConds(Set<PrimitiveCondition> s) {
		// no primitives to add
	}


	@Override
	public String toString() {
		return "TRUE";
	}

	@Override
	public int hashCode() {
		return 39187;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}	

}
