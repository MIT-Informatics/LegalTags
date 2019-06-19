package legaltags.expert.gui;


import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import darrylbu.renderer.VerticalTableHeaderCellRenderer;

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
		datasetModel = model.new EntityTableModel(Dataset.class);
		datasetTable = new JTable (datasetModel);
		JScrollPane scrollPane = new JScrollPane(datasetTable);
		addDatasetButton = new JButton("Add Dataset");
		datasetPanel.add(scrollPane);
		datasetPanel.add(addDatasetButton);
		tabbedPane.addTab("Datasets", datasetPanel);
		
		// make the column headers vertical
		TableCellRenderer headerRenderer = new VerticalTableHeaderCellRenderer();
		Enumeration<TableColumn> columns = datasetTable.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
		   columns.nextElement().
		         setHeaderRenderer(headerRenderer);
		}
		
		// tab to query with Prolog
		JPanel queryPanel = new JPanel();
		String[] queryChoices = module.getQueryStrings();
		queryDropdown = new JComboBox<String>(queryChoices);
		loadQueryButton = new JButton("Load Query");
		runQueryButton = new JButton("Run Query");
		queryField = new JTextArea(5, 20);
		resultsField = new JTextArea(5, 20);
		queryPanel.add(queryDropdown);
		queryPanel.add(loadQueryButton);
		queryPanel.add(queryField);
		queryPanel.add(runQueryButton);
		queryPanel.add(resultsField);
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

