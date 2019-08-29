package legaltags.expert.gui;


import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import darrylbu.renderer.VerticalTableHeaderCellRenderer;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Enumeration;

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
	// Components for person tab
	private EntityTableModel personModel;
	private JTable personTable;
	private JButton addPersonButton;
	// Components for repository tab
	private EntityTableModel repoModel;
	private JTable repoTable;
	private JButton addRepoButton;
	// Components for combined query tab
	private JButton loadQueryButton;
	private JButton runQueryButton;
	private JTextArea queryField;
	private JTextArea resultsField;
	private JComboBox<String> queryDropdown;
	
	public View (Module module, Model model) {
		// tabbed pane to hold all the tabs
		JTabbedPane tabbedPane = new JTabbedPane();
		
		// Tab to view and modify the data sets
		JPanel datasetPanel = new JPanel();
		datasetPanel.setLayout(new BorderLayout());
		datasetModel = model.new EntityTableModel(Dataset.class);
		datasetTable = new JTable (datasetModel);
		JScrollPane datasetScrollPane = new JScrollPane(datasetTable);
		addDatasetButton = new JButton("Add Dataset");
		datasetPanel.add(datasetScrollPane, BorderLayout.CENTER);
		// put the button below the table
		datasetPanel.add(addDatasetButton, BorderLayout.PAGE_END);
		tabbedPane.addTab("Datasets", datasetPanel);
		
		// make the column headers vertical
		TableCellRenderer headerRenderer = new VerticalTableHeaderCellRenderer();
		Enumeration<TableColumn> columns = datasetTable.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
		   columns.nextElement().
		         setHeaderRenderer(headerRenderer);
		}
		
		// Tab to view and modify people
		JPanel personPanel = new JPanel();
		personPanel.setLayout(new BorderLayout());
		personModel = model.new EntityTableModel(Person.class);
		personTable = new JTable (personModel);
		JScrollPane personScrollPane = new JScrollPane(personTable);
		addPersonButton = new JButton("Add Person");
		personPanel.add(personScrollPane, BorderLayout.CENTER);
		// put the button below the table
		personPanel.add(addPersonButton, BorderLayout.PAGE_END);
		tabbedPane.addTab("People", personPanel);
		
		// Tab to view and modify repositories
		JPanel repoPanel = new JPanel();
		repoPanel.setLayout(new BorderLayout());
		repoModel = model.new EntityTableModel(Repo.class);
		repoTable = new JTable (repoModel);
		JScrollPane repoScrollPane = new JScrollPane(repoTable);
		addRepoButton = new JButton("Add Repository");
		repoPanel.add(repoScrollPane, BorderLayout.CENTER);
		// put the button below the table
		repoPanel.add(addRepoButton, BorderLayout.PAGE_END);
		tabbedPane.addTab("Repositories", repoPanel);
		
		// tab to query with Prolog
		JPanel queryPanel = new JPanel();
		queryPanel.setLayout(new GridBagLayout());
		String[] queryChoices = module.getQueryStrings();
		// set of constraints, re-specify for each component
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		
		queryDropdown = new JComboBox<String>(queryChoices);
		c.gridx = 0;
		c.gridy = 0;
		queryPanel.add(queryDropdown, c);
		
		loadQueryButton = new JButton("Load Query");
		c.gridx = 1;
		c.gridy = 0;
		queryPanel.add(loadQueryButton, c);
		
		queryField = new JTextArea(5, 20);
		queryField.setLineWrap(true);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		queryPanel.add(queryField, c);
		
		runQueryButton = new JButton("Run Query");
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		queryPanel.add(runQueryButton, c);
		
		resultsField = new JTextArea(5, 20);
		resultsField.setLineWrap(true);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 3;
		c.weightx = 1.0;
		queryPanel.add(resultsField, c);
		
		tabbedPane.addTab("Query", queryPanel);
		
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
	public JTextField getPrologField () {
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
	public JButton getAddPersonButton () {
		return addPersonButton;
	}
	public JComboBox<String> getBuiltinDropdown () {
		return builtinQueries;
	}
	public JComboBox<String> getQueryDropdown () {
		return queryDropdown;
	}
	public JTextArea getQueryField () {
		return queryField;
	}
	public JTextArea getResultsField () {
		return resultsField;
	}
	public JButton getLoadQueryButton () {
		return loadQueryButton;
	}
	public JButton getRunQueryButton () {
		return runQueryButton;
	}
}

