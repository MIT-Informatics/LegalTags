package licensecreation.conditions;

import java.util.Set;

/**
 * A <code>PrimitiveCondition</code> represents a proposition. These are facts about the external
 * world that we need.
 * @author snchong
 *
 */
public class PrimitiveCondition extends Condition {
	/**
	 * Unique human-readable identifier for the condition.
	 */
	public final String uid;

	PrimitiveCondition(String uid) {
		this.uid = uid;
	}
	
	@Override
	public boolean isSatisfied(Set<PrimitiveCondition> conds) {
		return conds.contains(this);
	}

	
	@Override
	public void addAllPrimitiveConds(Set<PrimitiveCondition> s) {
		s.add(this);
	}


	@Override
	public String toString() {
		return uid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
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
		PrimitiveCondition other = (PrimitiveCondition) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}

}
