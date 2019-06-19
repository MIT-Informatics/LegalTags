package legaltags.expert.gui;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPTermParser;
import java.util.List;
import java.util.UUID;

/* 
 * Representation of an entity
 * 
 */

abstract class Entity {
	// public human readable display name
	public String name;
	// internal Prolog readable representation
	String id;
	// unique internal representation
	String uid;
	void makeID () {
		uid = UUID.randomUUID().toString();
		id = "lt".concat(uid.replaceAll("-", "").toLowerCase());
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
		return this.uid.equals(e.uid);
	}
}