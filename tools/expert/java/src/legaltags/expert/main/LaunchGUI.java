package legaltags.expert.main;

import legaltags.expert.gui.*;
import javax.swing.*;
/**
 * This class provides a main method to launch a GUI interface for the
 * expert tool.

 */

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPTermParser;
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
		// module hard coded to ferpa for now
        Module module = new Ferpa();
		Model model = new Model(module);
        View view = new View(module, model); 
        Controller controller = new Controller(model,view,module);
        controller.addListeners();
	}
	
	// set up the initial JIProlog Engine, these are constants for now
	private static JIPEngine initJIPEngine () {
		JIPEngine jip = new JIPEngine();
		ClassLoader cl = Main.class.getClassLoader();
		jip.consultStream(cl.getResourceAsStream("common.pro"), "common.pro");
		jip.consultStream(cl.getResourceAsStream("ferpa/ferpa.pro"), "ferpa.pro");
		jip.consultStream(cl.getResourceAsStream("cmr/cmr.pro"), "cmr.pro");
		return (addTestData(jip));
	}
	private static JIPEngine addTestData (JIPEngine jip) {
		JIPTermParser parser = jip.getTermParser();
		jip.assertz(parser.parseTerm("cmr_hasUserAuthentication(harvardDataverse)."));
		jip.assertz(parser.parseTerm("cmr_hasUserAccessControl(harvardDataverse)."));
		jip.assertz(parser.parseTerm("cmr_hasWrittenSecurityPlan(harvardDataverse)."));
		jip.assertz(parser.parseTerm("cmr_monitorsSystem(harvardDataverse)."));
		jip.assertz(parser.parseTerm("cmr_patchesSystem(harvardDataverse)."));
		jip.assertz(parser.parseTerm("cmr_trainsEmployees(harvardDataverse)."));
		jip.assertz(parser.parseTerm("cmr_regularAudits(harvardDataverse)."));
		jip.assertz(parser.parseTerm("cmr_reportsBreachesToDataOwner(harvardDataverse)."));
		jip.assertz(parser.parseTerm("cmr_reportsBreachesToDataSubjects(harvardDataverse)."));
		jip.assertz(parser.parseTerm("cmr_reportsBreachesToGovt(harvardDataverse)."));
		jip.assertz(parser.parseTerm("cmr_destroysRecords(harvardDataverse)."));
	
		// data set
		jip.assertz(parser.parseTerm("cmr_depositorInScope(steveC, data2015)."));
		jip.assertz(parser.parseTerm("cmr_dataSubjectsInScope(data2015)."));
	    jip.assertz(parser.parseTerm("cmr_personalInformation(data2015)."));
	    jip.assertz(parser.parseTerm("cmr_nonPublicInformation(data2015)."));
		jip.assertz(parser.parseTerm("ferpa_datasetInScope(data2015)."));
		jip.assertz(parser.parseTerm("ferpa_pii(data2015)."));
		jip.assertz(parser.parseTerm("ferpa_allConsented(data2015)."));
	
	
		// dataset that is released under ferpa under both studies and audit exceptions

		jip.assertz(parser.parseTerm("ferpa_datasetInScope(data_alex_educational)."));
		jip.assertz(parser.parseTerm("ferpa_pii(data_alex_educational)."));
		jip.assertz(parser.parseTerm("ferpa_allConsented(data_alex_educational)."));
		jip.assertz(parser.parseTerm("ferpa_studiesException(data_alex_educational)."));
		//jip.assertz(parser.parseTerm("ferpa_auditException(data_alex_educational)."));
								 
		// license
		jip.assertz(parser.parseTerm("licenseRequires(dataverseClickthrough, cmr_StorageEncrypted)."));
		jip.assertz(parser.parseTerm("licenseRequires(dataverseClickthrough, cmr_TransmissionEncrypted)."));
	
		
		// dataset that is derived from a ferpa_identifiable dataset
		jip.assertz(parser.parseTerm("derivedFrom(data_alex_deid, data_alex_educational, dptool([[epsilon,0.5],[blah,blah]]))."));
		jip.assertz(parser.parseTerm("derivedFrom(data_alex_deid2, data_alex_educational, dptool([[epsilon,0.9],[blah,blah]]))."));
		jip.assertz(parser.parseTerm("ferpa_pii(data_alex_deid)."));
		jip.assertz(parser.parseTerm("ferpa_pii(data_alex_deid2)."));
		jip.assertz(parser.parseTerm("ferpa_datasetInScope(data_alex_deid)."));
		jip.assertz(parser.parseTerm("ferpa_datasetInScope(data_alex_deid2)."));

		// these last two are actually institutional policy. Have an example institutional policy file? Need an examples directory.
		jip.assertz(parser.parseTerm("ferpaSufficientEps(0.8)."));		
		jip.assertz(parser.parseTerm("ferpa_not_identifiable(DS) :- derivedFrom(DS, _DSOrig, dptool(Params)), member([epsilon , EPS], Params), ferpaSufficientEps(FEps), EPS =< FEps."));
	
		return jip;
	}
	}
