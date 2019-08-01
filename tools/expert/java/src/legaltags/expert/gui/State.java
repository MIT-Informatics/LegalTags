package legaltags.expert.gui;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPTermParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
	// a state has a starting module (ferpa, cmr, etc.)
	Module module;
	// mapping between prolog ids and human readable names
	Map<String, String> map;
	// list of entities
	Map<String, Entity> entities;
	
	public State (Module m) {
		// consult the source files for the associated module
		module = m;
		engine = m.getEngine();
		// add each initial entity to the internal list
		entities = new HashMap<String, Entity>();
		for (Entity e : m.entities) {
			entities.put(e.pid, e);
		}
		// initialize the engine
		buildEngine();
	}
	
	// build the JIPEngine from the entity list
	public void buildEngine () {
		// can't subtract entities from an engine easily, so build it anew
		engine = module.getEngine();
		// for each entity, add the associated prolog to the engine
		for (Entity e : entities.values()) {
		    engine = e.addToEngine(engine);
		}	
			
		JIPTermParser parser = engine.getTermParser();
		
		// testing this...
		engine.assertz(parser.parseTerm("cmr_hasUserAuthentication(harvardDataverse)."));
		engine.assertz(parser.parseTerm("cmr_hasUserAccessControl(harvardDataverse)."));
		engine.assertz(parser.parseTerm("cmr_hasWrittenSecurityPlan(harvardDataverse)."));
		engine.assertz(parser.parseTerm("cmr_monitorsSystem(harvardDataverse)."));
		engine.assertz(parser.parseTerm("cmr_patchesSystem(harvardDataverse)."));
		engine.assertz(parser.parseTerm("cmr_trainsEmployees(harvardDataverse)."));
		engine.assertz(parser.parseTerm("cmr_regularAudits(harvardDataverse)."));
		engine.assertz(parser.parseTerm("cmr_reportsBreachesToDataOwner(harvardDataverse)."));
		engine.assertz(parser.parseTerm("cmr_reportsBreachesToDataSubjects(harvardDataverse)."));
		engine.assertz(parser.parseTerm("cmr_reportsBreachesToGovt(harvardDataverse)."));
		engine.assertz(parser.parseTerm("cmr_destroysRecords(harvardDataverse)."));

		// data set
		engine.assertz(parser.parseTerm("cmr_depositorInScope(steveC, data2015)."));
		engine.assertz(parser.parseTerm("cmr_dataSubjectsInScope(data2015)."));
	    engine.assertz(parser.parseTerm("cmr_personalInformation(data2015)."));
	    engine.assertz(parser.parseTerm("cmr_nonPublicInformation(data2015)."));
								 
		// license
		engine.assertz(parser.parseTerm("licenseRequires(dataverseClickthrough, cmr_StorageEncrypted)."));
		engine.assertz(parser.parseTerm("licenseRequires(dataverseClickthrough, cmr_TransmissionEncrypted)."));

		// these last two are actually institutional policy. Have an example institutional policy file? Need an examples directory.
		engine.assertz(parser.parseTerm("ferpaSufficientEps(0.8)."));		
		engine.assertz(parser.parseTerm("ferpa_not_identifiable(DS) :- derivedFrom(DS, _DSOrig, dptool(Params)), member([epsilon , EPS], Params), ferpaSufficientEps(FEps), EPS =< FEps."));
	}
	public Iterator<Entity> getEntityIterator () {
		return entities.values().iterator();
	}
	public Boolean hasEntity (Entity e) {
		return entities.containsKey(e.pid);
	}
	public void addEntity (Entity e) {
		entities.put(e.pid, e);
	}
	// update an entity. if it does not exist, error.
	public void updateEntity (Entity e) {
		if (hasEntity(e)) {
			addEntity(e);
		} else {
			System.out.println("Error: key not found when updating");
		}
	}
	
	public void addToEntity (Entity e, Relation r) {
		if (hasEntity(e)) {
			e.addRelation(r);
			updateEntity(e);
		} else {
			System.out.println("Error: key not found when updating");
		}
	}
	// convert prolog id to human readable name
	// if name is not found, return the input unchanged
	public String pid2Name (String pid) {
		Entity e = entities.get(pid);
		if (e != null) {
			return e.name;
		}
		System.out.printf("Key not found: %s\n", pid);
		return pid;
	}
	// convert a name into its prolog id
	// if no corresponding pid exists, return input unchanged
	public String name2pid (String name) {
		// currently, we iterate through keys and return the first one 
		// mapping to the given name.
		for (Map.Entry<String, Entity> entry : entities.entrySet()) {
	        if (name.equals(entry.getValue().name)) {
	        	return entry.getKey();
	        }
	    }
	    return name;
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