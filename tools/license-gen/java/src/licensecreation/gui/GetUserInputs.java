package licensecreation.gui;

import java.awt.Component;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import licensecreation.LicenseInstantiation;
import licensecreation.LicenseTemplate;

public class GetUserInputs {
	private final LicenseInstantiation templateInst;
	private final LicenseTemplate template;
	private final Component parent;
	public GetUserInputs(LicenseInstantiation templateInst, Component parent) {
		this.templateInst = templateInst;
		this.template = templateInst.getTemplate();
		this.parent = parent;
	}
	public boolean createAndShow() {
		boolean unsuppliedInfo, firstTime = true;
		// Check if there are any unsupplied placeholder terms in the generated license
		do {
			unsuppliedInfo = false;

			List<String> sups = templateInst.getMissingPlaceholders();
			//sups.sort(String.CASE_INSENSITIVE_ORDER);
			
			
			// XXX TODO: clean up this code
			final JComponent[] inputs = new JComponent[sups.size() * 2];
			int index = 0;
			for (String sup : sups) {				
				inputs[index] = new JLabel(template.getInputDescription(sup));
				index++;
				inputs[index] = new JTextField();
				if (templateInst.getAdditionalSubstitution(sup) != null) {
					((JTextField) inputs[index]).setText(templateInst.getAdditionalSubstitution(sup));
				} else {
					unsuppliedInfo = true;
					((JTextField) inputs[index]).setText(template.getInputDefault(sup));
				}
				index++;
			}
			// Show popup if there remains unsupplied information, or simply to confirm the user's choices.
			if (unsuppliedInfo || firstTime) {
				firstTime = false;
				int pressedOk = JOptionPane.showConfirmDialog(parent, inputs, "Supply Information", JOptionPane.OK_CANCEL_OPTION);
				if (pressedOk != JOptionPane.OK_OPTION) {
					return false;
				}
				index = 0;
				// put the info into the map which is passed to activateTerms() earlier in the loop
				for (String sup : sups) {
					index++;
					if (((JTextField) inputs[index]).getText().length() > 0) {
						templateInst.addAdditionalSubsitution(sup, ((JTextField) inputs[index]).getText());
					}
					index++;
				}
			}
		} while (unsuppliedInfo);
		return true;
		
	}

}
