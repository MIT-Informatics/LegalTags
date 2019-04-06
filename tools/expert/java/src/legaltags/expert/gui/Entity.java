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
/* 
 * Representation of an entity
 * 
 */

public class Entity {
	// public human readable display name
	String name;
	// internal unique representation
	String id;
	// either person, repo, dataset
	String type;
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
}