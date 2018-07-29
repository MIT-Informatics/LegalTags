package licensecreation.conditions;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * A logical predicate that indicates the conditions under which a license term should be included
 * in a license.
 *
 */
public abstract class Condition {

	/**
	 * Give a set of PrimitiveConditions (i.e., the set of 
	 * primitive conditions that hold),
	 * is this condition satisfied? That is, should a 
	 * license term with this condition be included in the license?
	 * @param conds
	 * @return
	 */
	public abstract boolean isSatisfied(Set<PrimitiveCondition> conds);

	/**
	 * Add to conds all the PrimitiveConditions that appear in this Condition.
	 * @return
	 */
	public abstract void addAllPrimitiveConds(Set<PrimitiveCondition> conds);

	/**
	 * Parse the string s, returning the Condition that the string represents.
	 * 
	 * Currently handles disjuncts of conjuncts,
	 *  e.g., "data:FERPA:inScope AND data:CMR:identifiable OR data:FERPA:PII"
	 * which would be represented as a DisjunctiveCondition. Note that we do not
	 * currently handle parentheses.
	 * 
	 * @param s
	 * @return
	 */
	public static Condition parseCondition(String s) {
		//System.err.println("\nParsing condition " + s);
		StringTokenizer t = new StringTokenizer(s, " \t\n\r\f()", true);
		Queue<String> tokens = new ArrayDeque<>();
		while (t.hasMoreTokens()) {
			String tok = t.nextToken();
			if (tok.trim().isEmpty()) {
				// it's just white space.
				continue;
			}
			tokens.add(tok);
		}
		Condition x = parseDisjunctCondition(tokens);
		if (!tokens.isEmpty()) {
			throw new IllegalArgumentException("Can't parse");
		}
		return x;
	}
	
	/**
	 * Internal method for recursive descent parsing.
	 * @param tokens
	 * @return
	 */
	private static Condition parseDisjunctCondition(Queue<String> tokens) {
		if (tokens.isEmpty()) {
			throw new IllegalArgumentException("Can't parse");
		}
		Set<Condition> disjuncts = new LinkedHashSet<>();
		
		while (true) {
			disjuncts.add(parseConjunctCondition(tokens));
		
			String s = tokens.peek();
			if ("AND".equalsIgnoreCase(s)) {
				throw new IllegalArgumentException("Can't parse");
			}
			if ("OR".equalsIgnoreCase(s)) {
				tokens.remove();
				continue;
			}
			// otherwise we are done with parsing the disjunct
			break;
		}
		if (disjuncts.isEmpty()) {
			throw new IllegalArgumentException("Can't parse");
		}
		
		if (disjuncts.size() == 1) {
			return disjuncts.iterator().next();
		}
		return new DisjunctiveCondition(disjuncts);
	}

	/**
	 * Internal method for recursive descent parsing.
	 * 
	 * @param tokens
	 * @return
	 */
	private static Condition parseConjunctCondition(Queue<String> tokens) {
		if (tokens.isEmpty()) {
			throw new IllegalArgumentException("Can't parse");
		}
		Set<Condition> conjuncts = new LinkedHashSet<>();
		
		while (true) {
			conjuncts.add(parseBaseCondition(tokens));

			String s = tokens.peek();
			if ("NOT".equalsIgnoreCase(s)) {
				// We have something like "a AND b NOT c".
				// Treat the "NOT" like "AND NOT"
				continue;
			}
			if ("AND".equalsIgnoreCase(s)) {
				tokens.remove();
				continue;
			}
			// otherwise we are done with parsing the conjunct
			break;
		}
		if (conjuncts.size() == 1) {
			return conjuncts.iterator().next();
		}
		return new ConjunctiveCondition(conjuncts);
	}

	/**
	 * Internal method for recursive descent parsing.
	 * 
	 * @param tokens
	 * @return
	 */
	private static Condition parseBaseCondition(Queue<String> tokens) {
		String s = tokens.remove();
		if ("(".equalsIgnoreCase(s)) {
			Condition c = parseDisjunctCondition(tokens);
			s = tokens.remove();
			if (!")".equalsIgnoreCase(s)) {
				throw new IllegalArgumentException("Can't parse, expected closing paren");					
			}
			return c; 
		}
		else if ("NOT".equalsIgnoreCase(s)) {
			Condition c = parseBaseCondition(tokens);
			return new NegatedCondition(c);
		}
		else if (TrueCondition.isTrue(s)) {
			return TrueCondition.INSTANCE;				
		}
		else {
			// should be a primitive
			if ("AND".equalsIgnoreCase(s) || "OR".equalsIgnoreCase(s)) {
				throw new IllegalArgumentException("Can't parse");					
			}
				
			return createPrimitiveCondition(s);
		}
	
	}


	/**
	 * A memoization map from Strings to PrimitiveConditions, so that we can use
	 * reference-equality for equality of PrimitiveConditions.
	 */
	private static Map<String, PrimitiveCondition> memo = new HashMap<>();
	public static PrimitiveCondition createPrimitiveCondition(String s) {
		PrimitiveCondition c = memo.get(s);
		if (c == null) {
			c = new PrimitiveCondition(s);
			memo.put(s, c);
		}
		return c;
	}

}
