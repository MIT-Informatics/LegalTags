package legaltags.expert.gui;


import java.util.ArrayList;

/* 
 * A constant
 * Subclass of a legal tag Entity
 * 
 */

public class Constant extends Entity {
	public Constant(String n) {
		name = n;
		makeID();
		relations = new ArrayList<Relation>();
	}
}