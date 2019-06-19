package legaltags.expert.gui;

import com.ugos.jiprolog.engine.JIPEngine;
import java.util.List;
import java.util.ArrayList;

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
	// build the JIPEngine from the entity list
	public void buildEngine () {
		// for each entity, add the associated prolog to the engine
		for (int i = 0; i < entities.size(); i++) {
			engine = entities.get(i).addToEngine(engine);
		}
	}
	public State (Module m) {
		// consult the source files for the associated module
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
}