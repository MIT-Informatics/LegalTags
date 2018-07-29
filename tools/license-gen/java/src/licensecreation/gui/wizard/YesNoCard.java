package licensecreation.gui.wizard;

import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

public class YesNoCard implements WizardCard {
	protected JPanel comp;

	protected String question;
	protected String trueString = "Yes";
	protected String falseString = "No";
	protected Boolean answer;
	protected float fontSize = 18.0f;
	
	protected String trueNextKey, falseNextKey;
	protected Wizard wiz;
	
	protected boolean isFirstCard, isFinalCard;
	protected Runnable nextRunnable = null;
	
	
	public YesNoCard(Wizard wiz, String question, String nextKey) {
		this.wiz = wiz;
		this.question = question;
		this.trueNextKey = nextKey;
		this.falseNextKey = nextKey;
	}

	public YesNoCard(Wizard wiz, String question, String trueNextKey, String falseNextKey) {
		this.wiz = wiz;
		this.question = question;
		this.trueNextKey = trueNextKey;
		this.falseNextKey = falseNextKey;
	}

	public void setTrueString(String trueString) {
		this.trueString = trueString;
	}

	public void setFalseString(String falseString) {
		this.falseString = falseString;
	}
	
	public void setIsFirstCard() {
		this.isFirstCard = true;
	}
	
	public void setIsFinalCard() {
		this.isFinalCard = true;
	}
	
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	@Override
	public Component getComponent() {
		if (comp == null) {
			initComponent();
		}
		return this.comp;
	}

	protected void initComponent() {
		JPanel c = new JPanel();
		Font font = c.getFont();
		font = font.deriveFont(this.fontSize);
		c.setFont(font);
		c.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		JLabel l = new JLabel("<html>" + this.question + "</html>");
		l.setFont(font);
		c.add(l);
		// Set up radio buttons
	    //Create the radio buttons.
	    JRadioButton trueButton = new JRadioButton(new AbstractAction(trueString) {
			private static final long serialVersionUID = -3382536738004241252L;

			@Override
			public void actionPerformed(ActionEvent e) {
				answer = Boolean.TRUE;
			}});

	    trueButton.setMnemonic('y');
	    trueButton.setFont(font);
	    JRadioButton falseButton = new JRadioButton(new AbstractAction(falseString) {
			private static final long serialVersionUID = 8157968879467564622L;

			@Override
			public void actionPerformed(ActionEvent e) {
				answer = Boolean.FALSE;
			}});
	    falseButton.setMnemonic('n');
	    falseButton.setFont(font);
	    
	    //Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    group.add(trueButton);
	    group.add(falseButton);
	    
	    
	    c.add(Box.createVerticalStrut(30));
	    
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
		radioPanel.add(trueButton);
		radioPanel.add(falseButton);

		c.add(radioPanel);
	    
		c.validate();

		this.comp = c;
	}

	@Override
	public void backRequested() {
		wiz.goBack();
	}

	@Override
	public void nextRequested() {
		if (this.answer != null) {
			if (nextRunnable != null) {
				nextRunnable.run();
			}
			else {
				wiz.setActiveCard(this.answer.booleanValue() ? this.trueNextKey : this.falseNextKey);
			}
		}			
	}

	@Override
	public boolean isFirstCard() {
		return this.isFirstCard;
	}

	@Override
	public boolean isFinalCard() {
		return this.isFinalCard;
	}

	public void setNextCards(String trueNext, String falseNext) {
		this.trueNextKey = trueNext;
		this.falseNextKey = falseNext;
	}

	public void setNextAction(Runnable runnable) {
		this.nextRunnable = runnable;
	}

	public Boolean getAnswer() {
		return this.answer;
	}
	
}