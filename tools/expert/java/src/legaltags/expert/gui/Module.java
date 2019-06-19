package legaltags.expert.gui;

import com.ugos.jiprolog.engine.JIPEngine;
import java.util.List;
import java.util.ArrayList;
import javafx.util.Pair;
import legaltags.expert.main.Main;

import java.util.function.Function;

public class Module {
	public String name;

	// List of built in queries and the query to run for each one
	
	public List<Pair<String, String>> queries = 
			new ArrayList<Pair<String, String>>();
	public String[] getQueryStrings () {
		String[] choices = new String[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			choices[i] = queries.get(i).getKey();
		}
		return choices;
	}
	
	// Prolog source files
	public List<String> sourceFiles = new ArrayList<String>();
	// Initialize a JIPEngine with the Prolog source files
	// TODO: figure out how to modularize this, right now it just loads all prolog files
	public JIPEngine getEngine () {
		JIPEngine jip = new JIPEngine();
		ClassLoader cl = Main.class.getClassLoader();
		jip.consultStream(cl.getResourceAsStream("common.pro"), "common.pro");
		jip.consultStream(cl.getResourceAsStream("ferpa/ferpa.pro"), "ferpa.pro");
		jip.consultStream(cl.getResourceAsStream("cmr/cmr.pro"), "cmr.pro");
		return jip;
	}
	
	// java list of constants from the Prolog formalization
	public List<Constant> constants;
	// base relations from the Prolog formalization
	// 
	public List<Relation> baseRelations;
	// initial data to load
	public List<Entity> entities;
}