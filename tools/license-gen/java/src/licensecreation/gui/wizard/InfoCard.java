package licensecreation.gui.wizard;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class InfoCard implements WizardCard {
	protected JPanel comp;

	protected String text;
	
	protected String nextKey;
	protected Wizard wiz;
	
	protected boolean isFirstCard, isFinalCard;
	protected Runnable nextRunnable = null;
	
	
	public InfoCard(Wizard wiz, String text, String nextKey) {
		this.wiz = wiz;
		this.text = text;
		this.nextKey = nextKey;
	}

	
	public void setIsFirstCard() {
		this.isFirstCard = true;
	}
	
	public void setIsFinalCard() {
		this.isFinalCard = true;
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
		c.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		JLabel l = new JLabel("<html>" + this.text + "</html>");
		c.add(l);
	    
		c.validate();

		this.comp = c;
	}

	@Override
	public void backRequested() {
		wiz.goBack();
	}

	@Override
	public void nextRequested() {
		if (nextRunnable != null) {
			nextRunnable.run();
		}
		else {
			wiz.setActiveCard(this.nextKey);
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

	public void setNextAction(Runnable runnable) {
		this.nextRunnable = runnable;
	}
	
}