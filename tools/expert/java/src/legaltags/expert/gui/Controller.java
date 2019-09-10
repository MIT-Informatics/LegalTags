package legaltags.expert.gui;


import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/* Controller class (see MVC framework)
 * 
 * Contains listeners for all interactive pieces of GUI.
 * Coordinates between model (internal state) and view (GUI components).
 * 
 */

public class Controller {
	private Module module;
	private Model model;
	private View view;
	private ActionListener addDatasetListener;
	private ActionListener addPersonListener;
	private ActionListener addRepoListener;
	private TableModelListener entityChangeListener;
	
	private ActionListener runQueryListener;
	private ActionListener loadQueryListener;
	
	public Controller (Model model, View view, Module module) {
		this.model = model;
		this.view = view;
		this.module = module;
	}
	
	// add action listeners for query entry and adding data
	public void addListeners () {
		// rebuild the prolog engine when changes are made to entity tables
		entityChangeListener = new TableModelListener() {
			public void tableChanged (TableModelEvent e) {
				model.updateEngine();
			}
		};
		view.getDatasetTable().getModel().addTableModelListener(entityChangeListener);
		view.getPersonTable().getModel().addTableModelListener(entityChangeListener);
		view.getRepoTable().getModel().addTableModelListener(entityChangeListener);
		// add a data set on button press
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
		// add a repository on button press
		addRepoListener = new ActionListener() {
			public void actionPerformed (ActionEvent actionEvent) {
				Repo r = new Repo("Name");
				model.addData(r);
				// update the table(s) when changes are made to the state
				model.updateTables();
			}
		};
		view.getAddRepoButton().addActionListener(addRepoListener);
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
		System.out.println("Your query is: " + query);
		System.out.println("Running query ...");
		String result = model.askQuery(query);
		// display the result to the text field
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
	
