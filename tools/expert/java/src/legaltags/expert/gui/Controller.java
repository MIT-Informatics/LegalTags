package legaltags.expert.gui;


import java.util.List;
import java.util.function.Function;

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
	private ActionListener addDataListener;
	private ActionListener builtinListener;
	
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
		addDataListener = new ActionListener() {
			public void actionPerformed (ActionEvent actionEvent) {
				addData();
			}
		};
		view.getAddDataButton().addActionListener(addDataListener);
		builtinListener = new ActionListener() {
			public void actionPerformed (ActionEvent actionEvent) {
				runBuiltin();
			}
		};
		view.getBuiltinButton().addActionListener(builtinListener);
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
	private void addData () {
		// get what is in the add data text field
		String assertion = view.getAddDataField().getText();
		// add it to the model
		model.addData(assertion);
		// clear the text field
		view.getAddDataField().setText(null);
	}
	private void runBuiltin () {
		System.out.println("Running built in query...");
		// get what is in the builtin query dropdown
		Function<JIPEngine, List<String>> f = module.queries.get(0).getValue();
		String result = model.callFunction(f);
		System.out.println("Your result is " + result);
		view.getBuiltinResult().setText(result);
	}

}
	
