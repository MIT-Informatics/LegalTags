package licensecreation.gui;

import java.awt.Container;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import licensecreation.LicenseInstantiation;
import licensecreation.LicenseTemplate;
import licensecreation.LicenseTerm;
import licensecreation.conditions.PrimitiveCondition;
import markdown.MarkdownRenderer;

/**
 * Opens a GUI which allows the user to create a custom license agreement by
 * interacting with the {@link LicenseTemplate}. The user can either select from
 * individual terms in the "Select by License Term" tab or individual
 * conditions in the "Select by Condition" tab. After the user has filled in
 * all the required supplied info, a custom license agreement is generated.
 * @author obasi42
 *
 */
public class GetDataFromUserGUI {
	/**
	 * The license template we are dealing with.
	 */
	private final LicenseTemplate template;
	
	/**
	 * An instantiation of the license template.
	 */
	//private final LicenseInstantiation templateInst;
	
	/**
	 * A renderer to use for the markdown
	 * 
	 */
	MarkdownRenderer renderer;
	private JFrame frame;
	private JTextArea log;
	private File lastFile;

	
	public GetDataFromUserGUI(LicenseTemplate template, MarkdownRenderer renderer, File outputFile) {
		this.template = template;
		this.renderer = renderer;		

		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException 
						| InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					throw new Error(e);
				}
				GetDataFromUserGUI.this.lastFile = outputFile;

				GetDataFromUserGUI.this.createAndShowGUI();
			}
		});
	}

	/**
	 * Creates all of the components and adds them to the pane.
	 * @param pane the finalized pane.
	 */
	@SuppressWarnings("unchecked")
	private void addComponentsToPane() {
		// The tabbed pane holds the two tabs, one for license terms the other for condition terms
		JTabbedPane tabbedPane = new JTabbedPane();
		final JList<LicenseTerm> termList = createToggleSelectJList(template.getTerms());

		// Create the conditions panel
		{
			final JList<PrimitiveCondition> conditionList = createToggleSelectJList(new ArrayList<>(template.getPrimitiveConditions())); 
			Action createLicenseFromCondsAction = new AbstractAction("Create License Agreement") {
				@Override
				public void actionPerformed(ActionEvent e) {
					GetDataFromUserGUI.this.startLicenseCreation(null, conditionList.getSelectedValuesList());				
				} 

			};
			conditionList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					// if the selected conditions change, 
					// set the selected terms
					termList.clearSelection();		
					List<LicenseTerm> termsToSelect = template.getSatisfiedTerms(conditionList.getSelectedValuesList());

					for (LicenseTerm lt : termsToSelect) {
						termList.setSelectedValue(lt, false);
					}

				} });
			JPanel conditionPanel = createPanel(conditionList, createLicenseFromCondsAction);
			tabbedPane.add("Select by Condition", conditionPanel);			
		}

		// Create the License Term panel
		{
			Action createLicenseFromTermsAction = new AbstractAction("Create License Agreement") {
				@Override
				public void actionPerformed(ActionEvent e) {
					GetDataFromUserGUI.this.startLicenseCreation(termList.getSelectedValuesList(), null);
				} 

			};
			JPanel licenseTermPanel = createPanel(termList, createLicenseFromTermsAction);

			// Tool tip per row
			termList.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					@SuppressWarnings("unchecked")
					JList<LicenseTerm> l = (JList<LicenseTerm>)e.getSource();
					ListModel<LicenseTerm> m = l.getModel();
					int index = l.locationToIndex(e.getPoint());
					if( index > -1 ) {
						LicenseTerm t = m.getElementAt(index);
						l.setToolTipText("Inclusion of term "+t+":         " + t.getCondition().toString());
					}
				}
			});

			// Set the initial terms
			for (LicenseTerm lt : (List<LicenseTerm>) template.getSatisfiedTerms(Collections.EMPTY_LIST)) {
				termList.setSelectedValue(lt, false);
			}


			tabbedPane.add("Select by License Term", licenseTermPanel);
		}


		// Add the log and the tabbed pane into a split pane
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.add(tabbedPane);
		splitPane.add(new JScrollPane(log));
		Container pane = frame.getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.add(splitPane);
	}

	/**
	 * Creates a JPanel with a JList and a button.
	 * @param itemList The JList to be included.
	 * @param buttonActionCommand The text of the button, which also serves as
	 * its command id.
	 * @return The panel.
	 */
	private <E> JPanel createPanel(JList<E> itemList, Action a) {
		JButton startButton = new JButton(a);
		startButton.setEnabled(true);

		JScrollPane optionPane = new JScrollPane(itemList);

		JPanel startPanel = new JPanel();
		startPanel.add(startButton);

		JPanel panel = new JPanel();
		GridBagLayout lm = new GridBagLayout();
		panel.setLayout(lm);
		panel.add(optionPane);
		panel.add(startPanel);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER; // make this component the last in the row.
		lm.setConstraints(optionPane, gbc);		

		return panel;
	}

	/**
	 * Creates a JList from a list of Strings
	 * @param itemList the String list.
	 * @return A JList containing the strings in the provided list.
	 */
	private <E> JList<E> createToggleSelectJList(List<E> itemList) {
		@SuppressWarnings("unchecked")
		JList<E> termList = new JList<E>((E[])itemList.toArray());		

		ListSelectionModel lsm = new DefaultListSelectionModel() {
			private int i0 = -1;
			private int i1 = -1;
			public void setSelectionInterval(int index0, int index1) {
				if (i0 == index0 && i1 == index1) {
					if (getValueIsAdjusting()) {
						setValueIsAdjusting(false);
						toggleSelectionInterval(index0, index1);
					}
				} else {
					i0 = index0;
					i1 = index1;
					setValueIsAdjusting(false);
					toggleSelectionInterval(index0, index1);
				}
			}
			private void toggleSelectionInterval(int index0, int index1) {
				for (int i = index0; i <= index1; i++) {
					if (super.isSelectedIndex(i)) {
						super.removeSelectionInterval(i, i);
					} else {
						super.addSelectionInterval(i, i);
					}
				}
			}
		};
		termList.setSelectionModel(lsm);
		return termList;
	}

	private void startLicenseCreation(Collection<LicenseTerm> selectedTerms, Collection<PrimitiveCondition> selectedConds) {
		clearLog();
		
		LicenseInstantiation templateInst;
		if (selectedTerms != null) {
			templateInst = new LicenseInstantiation(template, selectedTerms);
		}
		else {
			templateInst = new LicenseInstantiation(template, selectedConds, true);
		}
		
		if (!new GetUserInputs(templateInst, this.frame).createAndShow()) {
			return;
		}


		final String markdown = templateInst.renderMarkdown();
		printToLog(markdown);			

		final File outputFile = getFile();
		// Now actually produce the rendered license, in a separate thread.
		Runnable renderThread = new Runnable() {

			@Override
			public void run() {

				try {
					renderer.render(markdown, outputFile);
					Desktop.getDesktop().open(outputFile);
				}
				catch (RuntimeException|IOException e) {
					// Some error message.
					SwingUtilities.invokeLater(new Runnable() {
						public void run()
						{
							JOptionPane.showMessageDialog(frame.getContentPane(), new JLabel(e.getMessage()), "License Render Error", JOptionPane.PLAIN_MESSAGE);
						}
					});

				}							
				
			}
		};
		new Thread(renderThread).start();

	}

	/**
	 * Create and set up the window.
	 */
	private void createAndShowGUI() {
		this.frame = new JFrame("Legal Tags");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create the log pane.
		log = new JTextArea(20, 50);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		log.setLineWrap(true);		
		
		// Add content to the window.
		this.addComponentsToPane();
		this.frame.getContentPane().validate();

		// Display the window.
		this.frame.pack();
		this.frame.setVisible(true);
	}

	/**
	 * Adds the provided text to the log.
	 * @param txt the text.
	 */
	private void printToLog(final String txt)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				log.append(txt);
			}
		});
	}

	/**
	 * Clears the log.
	 */
	private void clearLog()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				log.setText("");
			}
		});
	}


	/**
	 * Ask the user for a filename
	 * @return
	 */
	private File getFile() {
		JFileChooser chooser = new JFileChooser(this.lastFile);
		chooser.setSelectedFile(this.lastFile);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF files", "pdf");

		chooser.setFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);

		if (!SwingUtilities.isEventDispatchThread()) {
			throw new Error("Wrong thread!!!");
		}


		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			this.lastFile = chooser.getSelectedFile();

			System.out.println("Chose to save as: " +
					this.lastFile);

			return this.lastFile;
		}
		return null;
	}

}