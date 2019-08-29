package legaltags.expert.gui;

import com.ugos.jiprolog.engine.JIPEngine;
import java.util.List;
import java.util.ArrayList;
import javafx.util.Pair;
import legaltags.expert.main.Main;

public class Module {
	public String name;
	public List<String> prologFilePaths;

	// List of built in queries and the prolog query to run for each one
	public List<Pair<String, String>> queries = 
			new ArrayList<Pair<String, String>>();

	public String[] getQueryStrings () {
		String[] choices = new String[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			choices[i] = queries.get(i).getKey();
		}
		return choices;
	}
	
	// Return a JIPEngine, initialized with the Prolog source files
	public JIPEngine getEngine () {
		JIPEngine jip = new JIPEngine();
		ClassLoader cl = Main.class.getClassLoader();
		for (String fp : prologFilePaths) {
			jip.consultStream(cl.getResourceAsStream(fp), fp);
		}
		return jip;
	}
	
	// java list of constants from the Prolog formalization
	public List<Constant> constants;
	// base relations from the Prolog formalization
	public List<Relation> baseRelations;
	// list of entities that are initially loaded
	public List<Entity> entities;
}