package legaltags.expert.gui;


import java.util.List;
import java.util.function.Function;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.ugos.jiprolog.engine.JIPEngine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/* Controller class
 * 
 * Coordinates between model and view.
 */

public class Controller {
	private Module module;
	private Model model;
	private View view;
	private ActionListener queryListener;
	private ActionListener addDatasetListener;
	private ActionListener builtinListener;
	private TableModelListener datasetChangeListener;
	
	public Controller (Model model, View view, Module module) {
		this.model = model;
		this.view = view;
		this.module = module;
	}
	
	// add action listeners for query entry and adding data
	public void addListeners () {
		queryListener = new ActionListener() {
			public void actionPerformed (ActionEvent actionEvent) {
				runQuery();
			}
		};
		view.getQueryButton().addActionListener(queryListener);
		builtinListener = new ActionListener() {
			public void actionPerformed (ActionEvent actionEvent) {
				runBuiltin();
			}
		};
		view.getBuiltinButton().addActionListener(builtinListener);
		// rebuild the prolog engine when changes are made to datasets
		datasetChangeListener = new TableModelListener() {
			public void tableChanged (TableModelEvent e) {
				model.updateEngine();
			}
		};
		view.getDatasetTable().getModel().addTableModelListener(datasetChangeListener);
		addDatasetListener = new ActionListener() {
			public void actionPerformed (ActionEvent actionEvent) {
				Dataset ds = new Dataset("New Dataset");
				model.addData(ds);
				// update the table(s) when changes are made to the state
				model.updateTables();
			}
		};
		view.getAddDatasetButton().addActionListener(addDatasetListener);
	}
	
	private void runQuery () {
		System.out.println("Running query ...");
		// get what is in the query text field
		String query = view.getQueryField().getText();
		System.out.println("Your query was " + query);
		String result = model.askQuery(query);
		System.out.println("Your result is " + result);
		// display the result to the result text field
		view.getQueryResult().setText(result);
	}
	
	private void runBuiltin () {
		System.out.println("Running built in query...");
		// get what is in the builtin query dropdown
		int ind = view.getBuiltinDropdown().getSelectedIndex();
		String query = module.queries.get(ind).getValue();
		String result = model.askQuery(query);
		System.out.println("Your result is " + result);
		view.getBuiltinResult().setText(result);
	}

}
	
