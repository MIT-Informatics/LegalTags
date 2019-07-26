package legaltags.expert.gui;

import com.ugos.jiprolog.engine.JIPEngine;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/* 
 * Internal representation of Prolog formalization.
 * The internal state has a list of "entities"-- datasets, 
 * people, repos, etc, as well as the JIPEngine. 
 * 
 */

public class State {
	JIPEngine engine;
	// list of entities
	List<Entity> entities;
	// a state has a starting module (ferpa, cmr, etc.)
	Module module;
	// build the JIPEngine from the entity list
	public void buildEngine () {
		// can't subtract entities from an engine easily, so build it anew
		engine = module.getEngine();
		// for each entity, add the associated prolog to the engine
		for (int i = 0; i < entities.size(); i++) {
			engine = entities.get(i).addToEngine(engine);
		}
	}
	public State (Module m) {
		// consult the source files for the associated module
		module = m;
		engine = m.getEngine();
		entities = m.entities;
		// initialize the engine
		buildEngine();
	}
	public void addEntity (Entity e) {
		entities.add(e);
	}
	// update an entity. if it does not exist, insert it.
	public void updateEntity (Entity e) {
		for (int i = 0; i < entities.size(); i++) {
			if (e.equals(entities.get(i))) {
				entities.set(i, e);
				System.out.println("Entity updated");
				return;
			}
		}
		entities.add(e);
	}
	public Boolean hasEntity (Entity e) {
		for (int i = 0; i < entities.size(); i++) {
			if (e.equals(entities.get(i))) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	public void addToEntity (Entity e, Relation r) {
		for (int i = 0; i < entities.size(); i++) {
			if (e.equals(entities.get(i))) {
				e.addRelation(r);
			}
		}
	}
	// convert prolog id to human readable name
	// if name is not found, return the input unchanged
	public String pid2Name (String id) {
		for (int i = 0; i < entities.size(); i++) {
			if (id.equals((entities.get(i).pid))) {
				return entities.get(i).name;
			}
		}
		return id;
	}
	// uses regex to return any prolog IDs in s
	public List<String> getPIDs (String s) {
		List<String> l = new ArrayList<String> ();
		String patternString = "\blt2019[a-zA-Z0-9_]*";
	    Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(s);
        while(matcher.find()) {
            l.add(s.substring(matcher.start(), matcher.end()));
        }
		return l;
	}
	// replace any prolog IDs in s with their names
	public String replacePIDs (String s) {
		String patternString = "\blt2019[a-zA-Z0-9_]*";
	    Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(s);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
        	System.out.println(matcher.group(1));
        	String repString = pid2Name(matcher.group(1));
        	matcher.appendReplacement(sb, repString);
        }
        matcher.appendTail(sb);
        return(sb.toString());
	}
}