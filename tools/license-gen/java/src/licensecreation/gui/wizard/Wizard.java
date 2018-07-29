package licensecreation.gui.wizard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

/**
 * Implements a customizable GUI Wizard. That is, it handles a notion of
 * "cards", the user clicks "Next" and "Previous" to move forwards and backwards
 * on the cards. When the final card is reached, the "Next" button changes to
 * "Finish", which when clicked will trigger an action.
 * 
 * @author snchong
 *
 */
public class Wizard {

	private final JDialog wiz;

	private final JPanel cardPanel;
	private final CardLayout cardLayout;

	private final Map<String, WizardCard> cards;

	private String activeCard;
	private LinkedList<String> activeStack = new LinkedList<>();

	private String finishButtonText = "Finish";
	private String nextButtonText = "Next >";

	private JButton backButton;
	private JButton nextButton;

	public Wizard(JFrame owner, boolean modal, String title) {
		this.wiz = new JDialog(owner, title, modal);
		this.wiz.setPreferredSize(new Dimension(500, 800));
		this.cards = new LinkedHashMap<>();

		this.wiz.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		JPanel buttonPanel = new JPanel();
		Box buttonBox = new Box(BoxLayout.X_AXIS);

		cardPanel = new JPanel();
		cardPanel.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));

		cardLayout = new CardLayout();
		cardPanel.setLayout(cardLayout);

		backButton = new JButton(new AbstractAction("< Back") {
			private static final long serialVersionUID = 6669020259875530875L;

			@Override
			public void actionPerformed(ActionEvent e) {
				cards.get(activeCard).backRequested();
			}

		});
		nextButton = new JButton(new AbstractAction("Next >") {
			private static final long serialVersionUID = -6666009946581100782L;

			@Override
			public void actionPerformed(ActionEvent e) {
				cards.get(activeCard).nextRequested();
			}

		});

		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.add(new JSeparator(), BorderLayout.NORTH);

		buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
		buttonBox.add(backButton);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(nextButton);
		// buttonBox.add(Box.createHorizontalStrut(30));
		// buttonBox.add(cancelButton);
		buttonPanel.add(buttonBox, java.awt.BorderLayout.EAST);

		wiz.getContentPane().add(cardPanel, BorderLayout.CENTER);
		wiz.getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

	}

	public void setExitOnClose() {
		this.wiz.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public RootPaneContainer getRootPane() {
		return this.wiz;
	}

	public void addCard(String key, WizardCard card) {
		if (this.activeCard == null) {
			this.activeCard = key;
		}
		this.cards.put(key, card);
		this.cardPanel.add(card.getComponent(), key);
		wiz.pack();

	}

	public void setActiveCard(String key) {
		this.setActiveCard(key, true);
	}

	public void setActiveCard(String key, boolean pushOnStack) {
		if (key != null && cards.containsKey(key)) {
			if (!key.equals(this.activeCard)) {
				if (pushOnStack) {
					this.activeStack.addLast(this.activeCard);
				}
				this.activeCard = key;
			}
			this.cardLayout.show(cardPanel, key);
			if (cards.get(key).isFinalCard()) {
				this.nextButton.setText(finishButtonText);
			} else {
				this.nextButton.setText(nextButtonText);
			}
			this.backButton.setEnabled(!cards.get(key).isFirstCard());

		}
	}

	public void goBack() {
		if (!activeStack.isEmpty()) {
			String key = activeStack.removeLast();
			setActiveCard(key, false);
		}
	}

	public void show() {
		// wiz.validate();
		setActiveCard(this.activeCard);
		wiz.pack();
		wiz.setVisible(true);
	}

	// Some simple test code.
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// JFrame frame = new JFrame("Legal Tags Demo Wizard");
				// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				Wizard w = new Wizard(null, false, "Simple Wizard Test");
				w.setExitOnClose();

				YesNoCard card1 = new YesNoCard(w, "Is this thing on?",
						"TEST2", null);
				card1.setIsFirstCard();
				YesNoCard card2 = new YesNoCard(w, "Hello?", null, "TEST");
				card2.setIsFinalCard();

				// Add content to the window.
				w.addCard("TEST", card1);
				w.addCard("TEST2", card2);

				// Display the window.
				w.show();
			}
		});

	}

}