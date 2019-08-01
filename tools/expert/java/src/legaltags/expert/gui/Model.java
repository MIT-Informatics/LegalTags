package legaltags.expert.gui;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;
import com.ugos.jiprolog.engine.JIPVariable;

import javafx.util.Pair;

import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
/* Model class
 * Internal representation of Prolog formalization.
 * The internal state.engine has a list of "entities"-- datasets, 
 * people, repos, etc, as well as the JIPEngine. 
 * 
 */

public class Model {
	private State state; 
	private Module module;
	private List<EntityTableModel> tableModels;
	// a model can be seeded with a JIPEngine or a Module
	public Model () {
		state.engine = new JIPEngine ();
	}
	public Model (JIPEngine s) {
		this.state.engine = s;
	}
	public Model (Module m) {
		module = m;
		state = new State(m);
		tableModels = new ArrayList<EntityTableModel>();
	}
	public void addTable(EntityTableModel t) {
		tableModels.add(t);
	}
	public State getState() {
		return state;
	}
	public void updateTables() {
		// remake any tables associated with the model
		for (int i = 0; i < tableModels.size(); i++) {
			tableModels.get(i).updateTable();
		}
	}
	public void updateEngine() {
		// rebuild the JIP Engine
		state.buildEngine();
	}
	public String askQuery (String s) {
		JIPTerm query = null;
		JIPTerm jipSolution;
		List<Pair<String, JIPTerm>> jipSolutions = new ArrayList<Pair<String, JIPTerm>>();
		List<String> solutions = new ArrayList<String>();
		String solution = new String();
		// try to parse the query
		try {
			JIPTermParser parser = state.engine.getTermParser(); 
			query = parser.parseTerm(s); 
			// print any variables in the query
			JIPVariable[] queryVars = query.getVariables();
			for (JIPVariable v : queryVars) {
				System.out.println("name = " + v.getName());
				System.out.println("v = " + v.getValue());
			}
			JIPQuery jipQuery = state.engine.openSynchronousQuery(query);
			
			// Loop while there is another solution 
			while (jipQuery.hasMoreChoicePoints() ) {
				jipSolution = jipQuery.nextSolution();
				if (jipSolution != null) {
					JIPVariable[] vs = jipSolution.getVariables();
					if (vs != null) {
						for (JIPVariable v : vs) {
							jipSolutions.add(new Pair<String, JIPTerm>(v.getName(), v.getValue()));						
							System.out.println("solution var name = " + v.getName());
							System.out.println("solution var val = " + v.getValue());
						}
					}
				}
				System.out.println("Solution found: " + jipSolution);
			}
			// lookup the human readable names of the prolog solutions
			// create solutions in the form "X = foo" 
			for (Pair<String, JIPTerm> sol : jipSolutions) {
				String name = state.pid2Name(sol.getValue().toString());
				solutions.add(sol.getKey() + " = " + name);
			}
			solution = String.join("\n", solutions);
		} 
		// syntax error in the query
		catch (JIPSyntaxErrorException ex) {
			System.out.println("Syntax error.");
			solution = "Syntax error";
		} 
		return solution;
	}
	public void addData (Entity e) {
		state.addEntity(e);
	}
	
	// custom TableModel for tables of entities
	// seeded with class representing dataset, person, or repo etc
	// the first column are the datasets etc. themselves
	// each other column is a Y/N representing whether a predicate holds
	// true for that entity
	@SuppressWarnings("serial")
	class EntityTableModel extends AbstractTableModel {
		private List<Relation> cols;
		private List<Entity> data;
		private Class<?> kind;
		public EntityTableModel (Class<?> c) {
			kind = c;
			makeCols();
			makeData();
			tableModels.add(this);
		}
		public boolean isCellEditable(int row, int col) {
			return true;
	    }
		public void makeData () {
			data = new ArrayList<Entity>();
			for (int i = 0; i < state.entities.size(); i++) {
				if (state.entities.get(i).getClass() == kind) {
					data.add(state.entities.get(i));
				}
			}
		}
		private void makeCols () {
			cols = new ArrayList<Relation>();
			for (int i = 0; i < module.baseRelations.size(); i++) {
				// select base relation which include an entity of the 
				// chosen type as the first argument
				if (module.baseRelations.get(i).types.get(0) == kind) {
					cols.add(module.baseRelations.get(i));
				}
			}
		}
		public void updateTable () {
			makeData();
			fireTableDataChanged();
		}
		public int getRowCount() {
	        return data.size();
	    }
		public int getColumnCount() {
			return cols.size() + 1;
		}

	    public String getColumnName(int col) {
	    	if (col == 0) {
		    	if (kind == Dataset.class) {
					return "Dataset";
				} else if (kind == Person.class) {
					return "Person";
				} else if (kind == Repo.class) {
					return "Repository";
				}
	    	}
	    	return cols.get(col - 1).predicate;
	    }
	    // first column is strings (names), others are booleans
	    public Class<?> getColumnClass(int col) {
	    	if (col == 0) {
	    		return String.class;
	    	}
	    	return Boolean.class;
	    }
	    public Object getValueAt(int row, int col) {
	    	// first column is the list of entities
	    	if (col == 0) {
	    		return data.get(row).name;
	    	}
	    	// lookup whether the predicate holds for this entity
	        return lookup(data.get(row), cols.get(col - 1));
	    }
	    public void setValueAt(Object value, int row, int col) {
	    	Entity e = data.get(row);
	    	// editing the entity name
	    	if (col == 0) {
	    		data.get(row).name = (String) value;
	    	}
	    	else {
		    	// add or edit the appropriate relation to the edited entity
		    	String predicate = module.baseRelations.get(col - 1).predicate;
		    	List<Entity> args = Arrays.asList(data.get(row));
		    	Relation r = new Relation(predicate, args);
		    	if (value.equals(Boolean.TRUE)) {
		    		e.addRelation(r);
		    	} else if (value.equals(Boolean.FALSE)) {
		    		System.out.println("Removing relation");
		    		e.removeRelation(r);
		    	} 
	    	}
            state.updateEntity(e);
            updateTable();
        }
	    private Boolean lookup (Entity e, Relation r) {
	    	return e.hasRelation(r);
	    }
	}
	
}