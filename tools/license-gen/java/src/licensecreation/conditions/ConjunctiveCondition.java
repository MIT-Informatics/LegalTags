package licensecreation.conditions;

import java.util.Iterator;
import java.util.Set;

/**
 * Represents the conjunction of one or more conditions.
 *
 */
public class ConjunctiveCondition extends Condition {

	private final Set<Condition> conjuncts;
	
	public ConjunctiveCondition(Set<Condition> conjuncts) {
		this.conjuncts = conjuncts;	
		if (conjuncts == null || conjuncts.isEmpty()) {
			throw new IllegalArgumentException("Must have at least one conjunct");
		}

	}
	
	
	@Override
	public boolean isSatisfied(Set<PrimitiveCondition> conds) {
		// A conjunctive condition is satisfied if all of its conjuncts
		// are satisfied.
		for (Condition c : this.conjuncts) {
			if (!c.isSatisfied(conds)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void addAllPrimitiveConds(Set<PrimitiveCondition> s) {
		for (Condition c : this.conjuncts) {
			c.addAllPrimitiveConds(s);
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();		
		Iterator<Condition> iter = conjuncts.iterator();
		while (iter.hasNext()) {
			Condition c = iter.next();
			boolean needParens = !(c instanceof PrimitiveCondition) && !(c instanceof NegatedCondition);
			if (needParens) {
				sb.append('(');
			}
			sb.append(c.toString());
			if (needParens) {
				sb.append(')');
			}
			if (iter.hasNext()) {
				sb.append(" AND ");
			}
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((conjuncts == null) ? 0 : conjuncts.hashCode());
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
		ConjunctiveCondition other = (ConjunctiveCondition) obj;
		if (conjuncts == null) {
			if (other.conjuncts != null)
				return false;
		} else if (!conjuncts.equals(other.conjuncts))
			return false;
		return true;
	}
	
	

}
