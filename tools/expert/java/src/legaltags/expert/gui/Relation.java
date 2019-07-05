package legaltags.expert.gui;


import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
/* 
 * Java representation of a relation in the Prolog formalization
 * 
 */
import java.util.stream.Collectors;

public class Relation {
	String predicate;
	List<Entity> args;
	int nargs;
	List<Class<? extends Entity>> types;
	List<Pair<Entity, Class<? extends Entity>>> arg;
	
	public String toString () {
		List<String> strs = args.stream().map(e -> e.pid)
								.collect(Collectors.toList());
		return predicate + "(" + String.join(",", strs) + ")";
	}
	// a relation must be initialized with a predicate
	// it may also have the arguments, 
	// or both the list of types of the arguments and the arguments
	public Relation (String s) {
		predicate = s;
		args = new ArrayList<Entity>();
	}
	public Relation (String s, List<Entity> l) {
		predicate = s;
		args = l;
		nargs = args.size();
		types = new ArrayList<Class<? extends Entity>>();
		for (int i = 0; i < nargs; i++) {
			types.add(args.get(i).getClass());
		}
	}
		
	public Relation (String s, List<Entity> as, List<Class<? extends Entity>> ts) {
		predicate = s;
		types = ts;
		args = as;
		nargs = types.size();
	}
	public void setTypes (List<Class<? extends Entity>> ts) {
		types = ts;
	}
}