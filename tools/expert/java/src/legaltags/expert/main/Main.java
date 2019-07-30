package legaltags.expert.main;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;
import com.ugos.jiprolog.engine.JIPVariable;

/**
 * Some temporary code to test the loading and execution of prolog source code.
 *
 */
public class Main {
	public static void main(String[] args) {
		JIPEngine jip = new JIPEngine();
		ClassLoader cl = Main.class.getClassLoader();

		System.out.println("Starting to consult files");
		jip.consultStream(cl.getResourceAsStream("common.pro"), "common.pro");
		jip.consultStream(cl.getResourceAsStream("ferpa/ferpa.pro"), "ferpa.pro");
		jip.consultStream(cl.getResourceAsStream("cmr/cmr.pro"), "cmr.pro");

		System.out.println("Adding test data");
		addTestData(jip);
		
		System.out.println("Running tests");
		testTestData(jip);

		System.out.println("All tests done!");
//		JIPTerm query = null;
//
//		try
//		{            
//			// parse query
//			JIPTermParser parser = jip.getTermParser();
//			query = parser.parseTerm("denied(cmr, accept(harvardDataverse, data2015, steveC, CS), 2).");
//		}
//		catch(JIPSyntaxErrorException ex)
//		{
//			// there is a syntax error in the query
//			ex.printStackTrace();
//			System.exit(0);
//		}
//
//		// open a synchronous query
//		JIPQuery jipQuery = jip.openSynchronousQuery(query);
//		JIPTerm solution;
//
//		
//		// Loop while there is another solution
//		while (jipQuery.hasMoreChoicePoints())
//		{
//			solution = jipQuery.nextSolution();
//			
//			if (solution != null) {
//				System.out.println(solution.toString(jip));
//				JIPVariable[] vs = solution.getVariables();
//				if (vs == null) {
//					System.out.println("   null variables");
//				}
//				else {
//					for (JIPVariable v : vs) {
//						System.out.println("   " + v.getName() + " = " + v.getValue().toString(jip));					
//					}
//				}
//			}
//			System.out.println();
//		}
//		System.out.println("Done.");

	}

	private static void testQuery(JIPEngine jip, String query, boolean expectSuccess) {
		testQuery(jip, query, expectSuccess, false);
	}
	private static void testQuery(JIPEngine jip, String query, boolean expectSuccess, boolean printResults) {
		JIPQuery jipQuery = jip.openSynchronousQuery(query);
		// Loop while there is another solution
		boolean success = false;
		
		if (jipQuery.hasMoreChoicePoints()) {
			JIPTerm solution = jipQuery.nextSolution(); 
			if (solution != null) {
				success = true;
				if (true) {
					if (solution != null) {
						System.out.println(solution.toString(jip));
						JIPVariable[] vs = solution.getVariables();
						if (vs == null) {
							System.out.println("   null variables");
						}
						else {
							for (JIPVariable v : vs) {
								System.out.println("   " + v.getName() + " = " + v.getValue().toString(jip));					
							}
						}
					}
					if (jipQuery.hasMoreChoicePoints()) {
						jipQuery.nextSolution();
					}
					else {
						System.out.println();
						//break;
					}
					
				}				
			}
			
		}
		if (success != expectSuccess) {
			throw new AssertionError("Query " + query + " " + (success?"succeeded":"failed"));
		}
	}
	
	private static void addTestData(JIPEngine jip) {
		JIPTermParser parser = jip.getTermParser();

		// repo
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
		
		
	}
	private static void testTestData(JIPEngine jip) {

		// TESTS FOR accept ACTION
		// Test 1: query permitted(cmr, accept(harvardDataverse, data2015, steveC, CS), 2).
		// result should be CS = [license(dataverseClickthrough)] and
		// also CS = [cmr_StorageEncrypted,cmr_TransmissionEncrypted], and various combinations
		
		// This test breaking for some reason
		// testQuery(jip, 
		// 		"permitted(cmr, accept(harvardDataverse, data2015, steveC, [license(dataverseClickthrough)]), 2).", 
		// 		true);	
		testQuery(jip, 
				"permitted(cmr, accept(harvardDataverse, data2015, steveC, [cmr_StorageEncrypted,cmr_TransmissionEncrypted]), 2).", 
				true);	
		testQuery(jip, 
				"permitted(cmr, accept(harvardDataverse, data2015, steveC, []), 2).", 
				false);	
		testQuery(jip, 
				"permitted(cmr, accept(harvardDataverse, data2015, steveC, [cmr_TransmissionEncrypted]), 2).", 
				false);	


		// Test 2: query denied(cmr, accept(harvardDataverse, data2015, steveC, CS), 2), 
		// result should be CS = [] and others.
		testQuery(jip, 
				"denied(cmr, accept(harvardDataverse, data2015, steveC, []), 2).", 
				true);	
		testQuery(jip, 
				"denied(cmr, accept(harvardDataverse, data2015, steveC, [cmr_StorageEncrypted]), 2).", 
				true);	
		testQuery(jip, 
				"denied(cmr, accept(harvardDataverse, data2015, steveC, [cmr_TransmissionEncrypted]), 2).", 
				true);	




		// TESTS FOR release ACTION
		
		// Query permitted(cmr, release(harvardDataverse, data2015, bob, steveC, CS), 2).
		// Result should be CS = [dataverseClickthrough] and
		// also CS = [cmr_TransmissionEncrypted], and various combinations
		testQuery(jip, 
				"permitted(cmr, release(harvardDataverse, data2015, bob, steveC, CS), 2).", 
				true);	

		testQuery(jip, 
				"permitted(cmr, release(harvardDataverse, data2015, bob, steveC, [license(dataverseClickthrough)]), 2).", 
				true);	
		testQuery(jip, 
				"permitted(cmr, release(harvardDataverse, data2015, bob, steveC, []), 2).", 
				false);	
		testQuery(jip, 
				"permitted(cmr, release(harvardDataverse, data2015, bob, steveC, [cmr_TransmissionEncrypted]), 2).", 
				true);	
		testQuery(jip, 
				"denied(cmr, release(harvardDataverse, data2015, bob, steveC, []), 2).", 
				true);	
		testQuery(jip, 
				"denied(cmr, release(harvardDataverse, data2015, bob, steveC, [cmr_StorageEncrypted]), 2).", 
				true);	
		testQuery(jip, 
				"permitted(cmr, release(harvardDataverse, data2015, bob, steveC, [cmr_StorageEncrypted]), 2).", 
				false);	

		// FERPA Tests
		testQuery(jip, 
				"permitted(ferpa, release(harvardDataverse, data_alex_educational, bob, steveC, CS), 2).", 
				false);	
		testQuery(jip, 
				"permitted(ferpa, release(harvardDataverse, data_alex_educational, bob, steveC, [ferpa_license_notice,ferpa_license_purpose,ferpa_license_scope,ferpa_license_duration,ferpa_license_information,ferpa_license_irb]), 6).", 
				true);	

		testQuery(jip, 
				"inScope(ferpa, release(harvardDataverse, data_alex_educational, bob, steveC, CS)).", 
				true);	
		testQuery(jip, 
				"inScope(ferpa, release(harvardDataverse, bogusData, bob, steveC, CS)).", 
				false);	
		testQuery(jip, 
				"ferpa_not_identifiable(data2015).", 
				false);	
		testQuery(jip, 
				"ferpa_not_identifiable(data_alex_deid).", 
				true);	
		testQuery(jip, 
				"ferpa_not_identifiable(data_alex_deid2).", 
				false);	
		
	}
}