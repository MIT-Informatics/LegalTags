package legaltags.expert.gui;


import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import legaltags.expert.gui.Model;
import legaltags.expert.gui.Model.EntityTableModel;

public class View {
	
	private JFrame frame;
	// Components for Prolog tab
	private JTextField queryPrologField;
	private JTextArea queryPrologResults;
	private JButton queryButton;
	// Components for built in queries tab
	private JComboBox<String> builtinQueries;
	private JTextArea builtinResults;
	private JButton builtinButton;
	// Components for dataset tab
	private EntityTableModel datasetModel;
	private JTable datasetTable;
	private JButton addDatasetButton;
	
	
	public View (Module module, Model model) {
		// tabbed pane to hold all the tabs
		JTabbedPane tabbedPane = new JTabbedPane();
		
		// Tab to view and modify the data sets
		JPanel datasetPanel = new JPanel();
		datasetModel = model.new EntityTableModel(Dataset.class);
		datasetTable = new JTable (datasetModel);
		JScrollPane scrollPane = new JScrollPane(datasetTable);
		addDatasetButton = new JButton("Add Dataset");
		datasetPanel.add(scrollPane);
		datasetPanel.add(addDatasetButton);
		tabbedPane.addTab("Datasets", datasetPanel);

		// Tab to enter data and query with Prolog
		queryPrologField = new JTextField(26);
		queryPrologResults = new JTextArea(1, 20);
		queryButton = new JButton("Enter Prolog Query");
		JPanel prologPanel = new JPanel();
		prologPanel.add(queryButton);
		prologPanel.add(queryPrologField);
		prologPanel.add(queryPrologResults);
		tabbedPane.addTab("Prolog", prologPanel);
		
		// Tab for built in queries
		JPanel builtinPanel = new JPanel();
		String[] choices = module.getQueryStrings();
		builtinQueries = new JComboBox<String>(choices);
		builtinResults = new JTextArea(5, 20);
		builtinButton = new JButton("Go");
		builtinPanel.add(builtinQueries);
		builtinPanel.add(builtinResults);
		builtinPanel.add(builtinButton);	
		tabbedPane.addTab("Built in Queries", builtinPanel);
		
		// Display it all in a scrolling window and make the window appear
		frame = new JFrame(module.name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(tabbedPane);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	// getter methods
	public JFrame getFrame () {
		return frame;
	}
	public JTextField getQueryField () {
		return queryPrologField;
	}
	public JButton getQueryButton () {
		return queryButton;
	}
	public JTextArea getQueryResult () {
		return queryPrologResults;
	}
	public JTextArea getBuiltinResult () {
		return builtinResults;
	}
	public JButton getBuiltinButton () {
		return builtinButton;
	}
	public JTable getDatasetTable () {
		return datasetTable;
	}
	public JButton getAddDatasetButton () {
		return addDatasetButton;
	}
}

