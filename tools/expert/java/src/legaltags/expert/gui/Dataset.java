package legaltags.expert.gui;


import java.util.ArrayList;
/* 
 * Representation of a data set
 * Subclass of a legaltag Entity
 * 
 */

public class Dataset extends Entity {
	public Dataset(String n) {
		name = n;
		makeID();
		relations = new ArrayList<Relation>();
	}
}