package legaltags.expert.gui;


import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
	private ActionListener addDatasetListener;
	private ActionListener addPersonListener;
	private TableModelListener datasetChangeListener;
	
	private ActionListener runQueryListener;
	private ActionListener loadQueryListener;
	
	public Controller (Model model, View view, Module module) {
		this.model = model;
		this.view = view;
		this.module = module;
	}
	
	// add action listeners for query entry and adding data
	public void addListeners () {
		// rebuild the prolog engine when changes are made to datasets
		datasetChangeListener = new TableModelListener() {
			public void tableChanged (TableModelEvent e) {
				model.updateEngine();
			}
		};
		view.getDatasetTable().getModel().addTableModelListener(datasetChangeListener);
		// add a dataset on button press
		addDatasetListener = new ActionListener() {
			public void actionPerformed (ActionEvent actionEvent) {
				Dataset ds = new Dataset("New Dataset");
				model.addData(ds);
				// update the table(s) when changes are made to the state
				model.updateTables();
			}
		};
		view.getAddDatasetButton().addActionListener(addDatasetListener);
		// add a person on button press
		addPersonListener = new ActionListener() {
			public void actionPerformed (ActionEvent actionEvent) {
				Person p = new Person("Name");
				model.addData(p);
				// update the table(s) when changes are made to the state
				model.updateTables();
			}
		};
		view.getAddPersonButton().addActionListener(addPersonListener);
		// run a query on button press
		runQueryListener = new ActionListener() {
			public void actionPerformed (ActionEvent actionEvent) {
				runQuery();
			}
		};
		view.getRunQueryButton().addActionListener(runQueryListener);
		// load built in queries into the query text field
		loadQueryListener = new ActionListener() {
			public void actionPerformed (ActionEvent actionEvent) {
				loadQuery();
			}
		};
		view.getLoadQueryButton().addActionListener(loadQueryListener);
		
	}
	
	// run whatever is in the query field as a Prolog query
	private void runQuery () {
		// get what is in the query text field
		String query = view.getQueryField().getText();
		System.out.println("Your query was: " + query);
		System.out.println("Running query ...");
		String result = model.askQuery(query);
		System.out.println("Your result is " + result);
		// display the result to the result text field
		view.getResultsField().setText(result);
	}
	// load one of the built in queries in to the query text field
	private void loadQuery () {
		// find which of the dropdown queries is selected
		int ind = view.getQueryDropdown().getSelectedIndex();
		String query = module.queries.get(ind).getValue();
		view.getQueryField().setText(query);
	}

}
	
