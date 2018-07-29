package licensecreation.conditions;

import java.util.Set;

/**
 * Represents the negation of a condition.
 *
 */
public class NegatedCondition extends Condition {
	private final Condition c;
	
	public NegatedCondition(Condition c) {
		this.c = c;
	}
	
	@Override
	public boolean isSatisfied(Set<PrimitiveCondition> conds) {
		return !c.isSatisfied(conds);
	}

	@Override
	public void addAllPrimitiveConds(Set<PrimitiveCondition> conds) {
		c.addAllPrimitiveConds(conds);
	}

	
	
	@Override
	public String toString() {
		boolean needParens = !(c instanceof PrimitiveCondition) && !(c instanceof NegatedCondition);
		if (needParens) {
			return "NOT (" + c.toString() + ")";
		}
		return "NOT " + c.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NegatedCondition other = (NegatedCondition) obj;
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		return true;
	}

}
