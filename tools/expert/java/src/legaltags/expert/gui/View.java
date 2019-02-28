package legaltags.expert.gui;


import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JComboBox;
import javax.swing.JTextArea;


public class View {
	
	private JFrame frame;
	private JTextField addDataPrologField;
	private JButton addDataButton;
	
	private JTextField queryPrologField;
	private JTextArea queryPrologResults;
	private JButton queryButton;
	
	private JComboBox<String> builtinQueries;
	private JTextArea builtinResults;
	private JButton builtinButton;
	
	public View (Module module) {
		// Create swing UI components 
		
		// components to enter data
		addDataPrologField = new JTextField(26);
		addDataButton = new JButton("Add Data");
		
		// components to query with Prolog
		queryPrologField = new JTextField(26);
		queryPrologResults = new JTextArea(1, 20);
		queryButton = new JButton("Enter Prolog Query");
		
		// Add the dropdown for built in queries
		String[] choices = module.getQueryStrings();
		builtinQueries = new JComboBox<String>(choices);
		builtinResults = new JTextArea(5, 20);
		builtinButton = new JButton("Go");

		// Set the view layout
		JPanel addDataPanel = new JPanel();
		addDataPanel.add(addDataPrologField);
		addDataPanel.add(addDataButton);
		
		SpringLayout layout = new SpringLayout();
		JPanel queryPanel = new JPanel(layout);
		queryPanel.add(queryPrologField);
		queryPanel.add(queryPrologResults);
		queryPanel.add(queryButton);
		queryPanel.add(builtinQueries);
		queryPanel.add(builtinResults);
		queryPanel.add(builtinButton);
		
		// set layout constraints
		// layout Prolog query components in a row
		layout.putConstraint(SpringLayout.WEST, queryButton, 10,
                SpringLayout.WEST, queryPanel);
		layout.putConstraint(SpringLayout.NORTH, queryButton, 10,
                SpringLayout.NORTH, queryPanel);
		layout.putConstraint(SpringLayout.NORTH, queryPrologField, 10,
                SpringLayout.NORTH, queryPanel);
		layout.putConstraint(SpringLayout.NORTH, queryPrologResults, 10,
                SpringLayout.NORTH, queryPanel);
		// space out Prolog query components
		layout.putConstraint(SpringLayout.WEST, queryPrologField, 10, 
				SpringLayout.EAST, queryButton);
		layout.putConstraint(SpringLayout.WEST, queryPrologResults, 10, 
				SpringLayout.EAST, queryPrologField);
		// layout built in query components below Prolog query components
		layout.putConstraint(SpringLayout.WEST, builtinQueries, 10,
                SpringLayout.WEST, queryPanel);
		layout.putConstraint(SpringLayout.NORTH, builtinQueries, 10, 
				SpringLayout.SOUTH, queryButton);
		layout.putConstraint(SpringLayout.NORTH, builtinButton, 10, 
				SpringLayout.SOUTH, queryButton);
		layout.putConstraint(SpringLayout.NORTH, builtinResults, 10, 
				SpringLayout.SOUTH, queryButton);
		// layout built in query components in a row
		layout.putConstraint(SpringLayout.WEST, builtinButton, 10, 
				SpringLayout.EAST, builtinQueries);
		layout.putConstraint(SpringLayout.WEST, builtinResults, 10, 
				SpringLayout.EAST, builtinButton);
		// layout size of overall panel
		layout.putConstraint(SpringLayout.EAST, queryPanel, 5,
				SpringLayout.EAST, queryPrologResults);
		layout.putConstraint(SpringLayout.SOUTH, queryPanel, 5,
                SpringLayout.SOUTH, builtinQueries);
		
		// combine query panel and panel for adding data
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                queryPanel, addDataPanel);
		// Display it all in a scrolling window and make the window appear
		frame = new JFrame(module.name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(splitPane);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public JTextField getAddDataField () {
		return addDataPrologField;
	}
	public JButton getAddDataButton () {
		return addDataButton;
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
}

