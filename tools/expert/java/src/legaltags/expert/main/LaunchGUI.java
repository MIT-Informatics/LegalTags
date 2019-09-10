package legaltags.expert.main;

import legaltags.expert.gui.*;
import javax.swing.*;

/**
 * This class provides a main method to launch a GUI interface for the
 * LegalTag expert tool.
 *
 */

public class LaunchGUI extends Main {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
		});
	}
	
	public static void createAndShowGUI() throws Exception {
		// module hard coded to Harvard for now
        Module module = new Harvard();
		Model model = new Model(module);
        View view = new View(module, model); 
        Controller controller = new Controller(model,view,module);
        controller.addListeners();
	}
	
}
