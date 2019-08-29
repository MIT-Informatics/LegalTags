package legaltags.expert.gui;

import java.util.ArrayList;

/* 
 * Representation of a person
 * Subclass of a legaltag Entity
 * 
 */

public class Person extends Entity {
	public Person(String n) {
		name = n;
		makeID();
		relations = new ArrayList<Relation>();
	}
}