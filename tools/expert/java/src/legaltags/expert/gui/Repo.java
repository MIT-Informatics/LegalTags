package legaltags.expert.gui;

import java.util.ArrayList;

/* 
 * Representation of a repository
 * Subclass of a legaltag Entity
 * 
 */

public class Repo extends Entity {
	public Repo(String n) {
		name = n;
		makeID();
		relations = new ArrayList<Relation>();
	}
}