package legaltags.expert.gui;

import com.ugos.jiprolog.engine.JIPEngine;
import java.util.List;
import java.util.ArrayList;
import javafx.util.Pair;
import java.util.function.Function;

public class Module {
	public String name;

	// List of built in queries and the action (function) to execute for each one
	// the type is a list of (string, JIPEngine -> string list) pairs
	// the string is displayed in the drop down menu of built in queries, 
	// and on click runs the function on the current state returning a list of strings
	public List<Pair<String, Function<JIPEngine, List<String>>>> queries = 
			new ArrayList<Pair<String, Function<JIPEngine, List<String>>>>();
	
	public String[] getQueryStrings () {
		String[] choices = new String[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			choices[i] = queries.get(i).getKey();
		}
		return choices;
	}
	
	// Prolog source files
	public List<String> sourceFiles = new ArrayList<String>();
}