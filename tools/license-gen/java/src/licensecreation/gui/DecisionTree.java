package licensecreation.gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import licensecreation.LicenseInstantiation;
import licensecreation.LicenseTemplate;
import licensecreation.conditions.PrimitiveCondition;
import licensecreation.gui.wizard.Wizard;
import licensecreation.gui.wizard.YesNoCard;
import markdown.MarkdownRenderer;

public class DecisionTree {
	private static final String CARD_1 = "CARD1";
	private static final String CARD_2 = "CARD2";
	private static final String CARD_3 = "CARD3";
	private static final String CARD_4 = "CARD4";
	private static final String CARD_5 = "CARD5";
	private static final String CARD_6 = "CARD6";
	private static final String CARD_7 = "CARD7";
	private static final String CARD_8 = "CARD8";
	private static final String CARD_9 = "CARD9";
	private static final String CARD_10 = "CARD10";
	private static final String CARD_11 = "CARD11";
	private static final String CARD_12 = "CARD12";
	private static final String CARD_13 = "CARD13";
	
	private YesNoCard card1;
	private YesNoCard card2;
	private YesNoCard card3;
	private YesNoCard card4;
	private YesNoCard card5;
	private YesNoCard card6;
	private YesNoCard card7;
	private YesNoCard card8;
	private YesNoCard card9;
	private YesNoCard card10;
	private YesNoCard card11;
	private YesNoCard card12;
	private YesNoCard card13;
	
	private final LicenseTemplate template;
	private final MarkdownRenderer renderer;
	
	
	private static Wizard w;

	
	public DecisionTree(LicenseTemplate template, MarkdownRenderer renderer, File outputFile) throws IOException {
		this.template = template;
		this.renderer = renderer;
		lastFile = outputFile;
		
		w = new Wizard(null, false, "License Generation Demo");
		w.setExitOnClose();
		
		// set up the questions
		
		
		card1 = new YesNoCard(w, 
					"Do the data contain personal information about a Massachusetts resident, defined by 201 C.M.R. 17.00 as the following combination of information about a Massachusetts resident obtained from a non-public source: (1) first name or first initial, (2) last name, and (3) Social Security number, driver's license number, state-issued ID number, or financial account number?",
					CARD_2, CARD_6
					/*Yes: condition data:CMR:identifiable is TRUE & continue to next question
					  No: skip remaining CMR questions*/);
		card1.setIsFirstCard();
		
		card2 = new YesNoCard(w, 
				"Is the data recipient a person, corporation, partnership, or government agency?",
				CARD_3
				/*Yes: condition dataRecipient:CMR:inScopeStatute is TRUE & continue to next question
	              No: continue to next question*/);

		card3 = new YesNoCard(w, 
				"Is the data recipient a person, corporation, or partnership (but not a Massachusetts government agency) that will own, license, receive, store, maintain, process, or have access to the personal information (as defined by 201 CMR 17.00) in the data in connection with the provision of goods or services or in connection with employment?",
				CARD_4
				/*Yes: condition dataRecipient:CMR:inScopeRegulation is TRUE & continue to next question
				  No: continue to next question*/);
		
		card4 = new YesNoCard(w, 
				"Will the data recipient own or license data containing personal information about a Massachusetts resident (as defined by 201 C.M.R. 17.00)?",
				CARD_6, CARD_5
				/*
				 Yes: condition dataRecipient:CMR:owns is TRUE & continue to FERPA questions
				No: continue to next question
               */);

		card5 = new YesNoCard(w, 
				"Will the data recipient maintain, but not own or license, data containing personal information about a Massachusetts resident (as defined by 201 C.M.R. 17.00)?",
				CARD_6
				/*
					Yes: condition dataRecipient:CMR:maintains is TRUE & continue to next question
					No: continue to next question
               */);

		card6 = new YesNoCard(w, 
				"Do the data contain information from the education records of an educational agency or institution receiving funds from the US Department of Education?",
				CARD_7, CARD_13
				/*
					Yes: condition data:FERPA:inScope is TRUE & continue to next question
					No: skip remaining FERPA questions
               */);
		
		card7 = new YesNoCard(w, 
				"Is the data provider an educational agency or institution that receives funding from the US Department of Education?",
				CARD_8
				/*
					Yes: condition dataProvider:FERPA:inScope is TRUE & continue to next question
					No: continue to next question
               */);

		card8 = new YesNoCard(w, 
				"Is the data recipient an educational agency or institution that receives funding from the US Department of Education?",
				CARD_9
				/*
					Yes: condition dataRecipient:FERPA:inScope is TRUE & continue to next question
					No: continue to next question
               */);

		card9 = new YesNoCard(w, 
				"Do the data contain personally identifiable information as defined by FERPA?",
				CARD_10, CARD_13
				/*
					Yes: condition data:FERPA:PII is TRUE & continue to next question
					No: skip remaining FERPA questions
               */);

		card10 = new YesNoCard(w, 
				"Are all data containing FERPA PII shared with the signed consent of the data subjects?",
				CARD_11
				/*
					Yes: condition data:FERPA:allConsented is TRUE & continue to next question
					No: continue to next question
               */);

		card11 = new YesNoCard(w, 
				"Are all data containing FERPA PII shared pursuant to a DUA authorizing release under the FERPA audit and evaluation exception?",
				CARD_12
				/*
					Yes: condition data:FERPA:auditException is TRUE & continue to next question
					No: continue to next question
               */);

		card12 = new YesNoCard(w, 
				"Are all data containing FERPA PII shared pursuant to a DUA authorizing release under the FERPA studies exception?",
				CARD_13
				/*
					Yes: condition data:FERPA:studiesException is TRUE & continue to next question
					No: continue to next question
               */);

		card13 = new YesNoCard(w, 
				"Are there some restrictions on the release of the data?",
				null
				/*
					Yes: condition data:tag:YELLOW is TRUE & end
					No: end
               */);
		card13.setNextAction(new Runnable() {
			@Override
			public void run() {
				// compute the conditions
				computeConditions();
			} });
		card13.setIsFinalCard();

		w.addCard(CARD_1, card1);
		w.addCard(CARD_2, card2);
		w.addCard(CARD_3, card3);
		w.addCard(CARD_4, card4);
		w.addCard(CARD_5, card5);
		w.addCard(CARD_6, card6);
		w.addCard(CARD_7, card7);
		w.addCard(CARD_8, card8);
		w.addCard(CARD_9, card9);
		w.addCard(CARD_10, card10);
		w.addCard(CARD_11, card11);
		w.addCard(CARD_12, card12);
		w.addCard(CARD_13, card13);
		
		w.show();
	}
	
	private void computeConditions() {
		Set<PrimitiveCondition> conditions = new LinkedHashSet<>();
		if (Boolean.TRUE.equals(card1.getAnswer())) {
			conditions.add(PrimitiveCondition.createPrimitiveCondition("data:CMR:identifiable"));
		}
		if (Boolean.TRUE.equals(card2.getAnswer())) {
			conditions.add(PrimitiveCondition.createPrimitiveCondition("dataRecipient:CMR:inScopeStatute"));
		}
		if (Boolean.TRUE.equals(card3.getAnswer())) {
			conditions.add(PrimitiveCondition.createPrimitiveCondition("dataRecipient:CMR:inScopeRegulation"));
		}
		if (Boolean.TRUE.equals(card4.getAnswer())) {
			conditions.add(PrimitiveCondition.createPrimitiveCondition("dataRecipient:CMR:owns"));
		}
		if (Boolean.TRUE.equals(card5.getAnswer())) {
			conditions.add(PrimitiveCondition.createPrimitiveCondition("dataRecipient:CMR:maintains"));
		}
		if (Boolean.TRUE.equals(card6.getAnswer())) {
			conditions.add(PrimitiveCondition.createPrimitiveCondition("data:FERPA:inScope"));
		}
		if (Boolean.TRUE.equals(card7.getAnswer())) {
			conditions.add(PrimitiveCondition.createPrimitiveCondition("dataProvider:FERPA:inScope"));
		}
		if (Boolean.TRUE.equals(card8.getAnswer())) {
			conditions.add(PrimitiveCondition.createPrimitiveCondition("dataRecipient:FERPA:inScope"));
		}
		if (Boolean.TRUE.equals(card9.getAnswer())) {
			conditions.add(PrimitiveCondition.createPrimitiveCondition("data:FERPA:PII"));
		}
		if (Boolean.TRUE.equals(card10.getAnswer())) {
			conditions.add(PrimitiveCondition.createPrimitiveCondition("data:FERPA:allConsented"));
		}
		if (Boolean.TRUE.equals(card11.getAnswer())) {
			conditions.add(PrimitiveCondition.createPrimitiveCondition("data:FERPA:auditException"));
		}
		if (Boolean.TRUE.equals(card12.getAnswer())) {
			conditions.add(PrimitiveCondition.createPrimitiveCondition("data:FERPA:studiesException"));
		}
		if (Boolean.TRUE.equals(card13.getAnswer())) {
			conditions.add(PrimitiveCondition.createPrimitiveCondition("data:tag:YELLOW"));
		}
		
		Set<PrimitiveCondition> knownConditions = template.getPrimitiveConditions();
		StringBuffer sb = new StringBuffer();
		for (PrimitiveCondition pc : conditions) {
			if (!knownConditions.contains(pc)) {
				if (sb.length() > 0) {
					sb.append("\n");
				}
				sb.append(pc);
			}
		}
		if (sb.length() > 0) {
			JOptionPane.showMessageDialog(w.getRootPane().getContentPane(), 
					"<html>The following conditions do not appear in the license terms:\n" + sb.toString() + "\n\nKnown conditions are: "+ knownConditions, 
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
		
		System.out.println("After the wizard, we have the following set of conditions:");
		for (PrimitiveCondition pc : conditions) {
			System.out.println("  " + pc);
		}
		
		LicenseInstantiation templateInst = new LicenseInstantiation(template, conditions, true);		

		if (!new GetUserInputs(templateInst, w.getRootPane().getContentPane()).createAndShow()) {
			return;
		}
				
		
		final String markdown = templateInst.renderMarkdown();
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
							JOptionPane.showMessageDialog(w.getRootPane().getContentPane(), new JLabel(e.getMessage()), "License Render Error", JOptionPane.PLAIN_MESSAGE);
						}
					});

				}								
			}
		};
		new Thread(renderThread).start();


	}
	
	private File lastFile;

	/**
	 * Ask the user for a filename, via a JFileChooser
	 * @return
	 */
	private File getFile() {
	    JFileChooser chooser = new JFileChooser(lastFile);
	    chooser.setSelectedFile(lastFile);
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF files", "pdf");

	    chooser.setFileFilter(filter);
	    chooser.setAcceptAllFileFilterUsed(false);
	    
	    if (!SwingUtilities.isEventDispatchThread()) {
	    	throw new Error("Wrong thread!!!");
	    }
	    	
	    
	    if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
	    	lastFile = chooser.getSelectedFile();
	       System.out.println("Chose to save as: " +
	            lastFile);
	       
	       return lastFile;
	    }
	    return null;
	}

}
