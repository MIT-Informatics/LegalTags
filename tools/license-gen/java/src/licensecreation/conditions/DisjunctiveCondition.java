package licensecreation.conditions;

import java.util.Iterator;
import java.util.Set;

/**
 * Represents the disjunction of one or more conditions.
 *
 */
public class DisjunctiveCondition extends Condition {

	// Non empty set of disjuncts
	private final Set<Condition> disjuncts;
	
	public DisjunctiveCondition(Set<Condition> disjuncts) {
		this.disjuncts = disjuncts;
		if (disjuncts == null || disjuncts.isEmpty()) {
			throw new IllegalArgumentException("Must have at least one disjunct");
		}
	}
	
	
	@Override
	public boolean isSatisfied(Set<PrimitiveCondition> conds) {
		// A disjunctive condition is satisfied if any one of its disjuncts
		// is satisfied.
		for (Condition c : this.disjuncts) {
			if (c.isSatisfied(conds)) {
				return true;
			}
		}
		return false;
	}

	
	@Override
	public void addAllPrimitiveConds(Set<PrimitiveCondition> s) {
		for (Condition c : this.disjuncts) {
			c.addAllPrimitiveConds(s);
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();		
		Iterator<Condition> iter = disjuncts.iterator();
		while (iter.hasNext()) {
			Condition c = iter.next();
			sb.append(c.toString());
			if (iter.hasNext()) {
				sb.append(" OR ");
			}
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((disjuncts == null) ? 0 : disjuncts.hashCode());
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
		DisjunctiveCondition other = (DisjunctiveCondition) obj;
		if (disjuncts == null) {
			if (other.disjuncts != null)
				return false;
		} else if (!disjuncts.equals(other.disjuncts))
			return false;
		return true;
	}
	
	

}
