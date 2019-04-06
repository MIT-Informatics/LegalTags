package legaltags.expert.gui;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;
import com.ugos.jiprolog.engine.JIPVariable;
import java.util.List;
import java.util.ArrayList;
import javafx.util.Pair;
import java.util.function.Function;
/* Model class
 * Internal representation of Prolog formalization.
 * The internal state has a list of "entities"-- datasets, 
 * people, repos, etc, as well as the JIPEngine. 
 * 
 */

public class Model {
	private JIPEngine state; 
	public Model () {
		state = new JIPEngine ();
	}
	public Model (JIPEngine s) {
		this.state = s;
	}
	public JIPEngine getState() {
		return state;
	}
	public String askQuery (String s) {
		JIPTerm query = null;
		JIPTerm jipSolution;
		String solution = "";
		// try to parse the query
		try {             
			JIPTermParser parser = state.getTermParser(); 
			query = parser.parseTerm(s); 
			JIPQuery jipQuery = state.openSynchronousQuery(query);
			
			// Loop while there is another solution 
			// TODO: convert Prolog '.' functor to more readable form
			while (jipQuery.hasMoreChoicePoints() ) { 
				jipSolution = jipQuery.nextSolution();
				solution = solution + jipSolution + ". ";
				System.out.println("Solution found: " + jipSolution);
			} 
		} 
		catch (JIPSyntaxErrorException ex) { 
		 // there is a syntax error in the query 
			System.out.println("Syntax error.");
			// ex.printStackTrace(); 
		    // System.exit(0); 
			solution = "Syntax error";
		} 
		return solution;
	}
	public void addData (String s) {
		System.out.println("Adding data: " + s);
		JIPTermParser parser = state.getTermParser();
		state.assertz(parser.parseTerm(s));
	}
	public String callFunction (Function<JIPEngine, List<String>> f) {
		// apply the function to the current state, getting back list of strings
		List<String> results = f.apply(state);
		// newline separate the results into a single string
		return String.join("\n", results);
	}
}