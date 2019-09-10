package legaltags.expert.gui;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPTermParser;
import java.util.List;
import java.util.UUID;

/* 
 * Entity class
 * 
 * Represents a single entity (i.e., a person, dataset, etc) governed by the 
 * expert system. Has a unique ID used in the prolog engine, a human readable name,
 * and the list of relations the entity is part of.
 * 
 * Provides functions to operate on relations the entity is part of.
 * 
 */

abstract class Entity {
	// public human readable display name
	public String name;
	
	// random unique id, canonicalized to be valid Prolog 
	String pid;
	void makeID () {
		String uid = UUID.randomUUID().toString();
		pid = "lt2019".concat(uid.replaceAll("-", ""));
	}

	// List of relations that this entity is a member of
	List<Relation> relations;
	
	// function to add this entity to a JIPEngine
	public JIPEngine addToEngine (JIPEngine jip) {
		JIPTermParser parser = jip.getTermParser();
		for (int i = 0; i < relations.size(); i++) {
			Relation r = relations.get(i);
			String assertion = r.toString();
			jip.assertz(parser.parseTerm(assertion));
		}
		return jip;
	}
	public void addRelation (Relation r) {
		relations.add(r);
	}
	public Boolean hasRelation (Relation r) {
		String pred = r.predicate;
		for (int i = 0; i < relations.size(); i++) {
    		if (pred.equals(relations.get(i).predicate)) {
    			return Boolean.TRUE;
    		}
    	}
    	return Boolean.FALSE;
	}
	public void removeRelation (Relation r) {
		String pred = r.predicate;
		for (int i = 0; i < relations.size(); i++) {
    		if (pred.equals(relations.get(i).predicate)) {
    			relations.remove(i);
    			System.out.println("Removed relation");
    		}
    	}
	}
	public Boolean equals(Entity e) {
		return this.pid.equals(e.pid);
	}
}