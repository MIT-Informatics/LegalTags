package licensecreation.gui.wizard;

import java.awt.Component;

public interface WizardCard {
	Component getComponent();

	void backRequested();

	void nextRequested();

	boolean isFirstCard();

	boolean isFinalCard();
}