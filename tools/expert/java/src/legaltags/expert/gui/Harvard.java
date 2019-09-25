package legaltags.expert.gui;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;

/* CMR module
 * 
 * Provides built-ins for formalization of the FERPA legislation.
 * 
 */
public class Harvard extends Module {
	public Harvard() {
		name = "Harvard";
		prologFilePaths = Arrays.asList("common.pro", "ferpa/ferpa.pro", "cmr/cmr.pro", "harvard/harvard.pro");
		entities = new ArrayList<Entity>();
		queries = Arrays.asList(
				new AbstractMap.SimpleEntry<String, String> 
					("Datasets in scope (FERPA)", "ferpa_datasetInScope(DS)."),
				new AbstractMap.SimpleEntry<String, String> 
					("Datasets in scope (CMR)", "cmr_dataSubjectsInScope(DS)."),
				new AbstractMap.SimpleEntry<String, String> 
					("Datasets with identifiable information", "ferpa_identifiable(DS)."),
				new AbstractMap.SimpleEntry<String, String>
					("Secure repositories", "cmr_secure(R)."),
				new AbstractMap.SimpleEntry<String, String>
					("Can HarvardDataverse accept data from Alice into data2015 unconditionally?", 
					"permitted(harvard, accept(HarvardDataverse, data2015, Alice, []), 2)."),
				new AbstractMap.SimpleEntry<String, String> 
					("Can HarvardDataverse accept data from Alice into data2015 with transmission and storage encrypted?", 
					"permitted(harvard, accept(HarvardDataverse, data2015, Alice, [cmr_StorageEncrypted,cmr_TransmissionEncrypted]), 2)."),
				new AbstractMap.SimpleEntry<String, String>
					("Can HarvardDataverse accept data from Bob into data2015 with transmission and storage encrypted?", 
					"permitted(harvard, accept(HarvardDataverse, data2015, Bob, [cmr_StorageEncrypted,cmr_TransmissionEncrypted]), 2).")
				);
		constants = Arrays.asList(
				new Constant("cmr_StorageEncrypted"),
				new Constant("cmr_TransmissionEncrypted"),
				new Constant("ferpa_license_notice"),
				new Constant("ferpa_license_purpose"),
				new Constant("ferpa_license_scope"),
				new Constant("ferpa_license_duration"),
				new Constant("ferpa_license_information"),
				new Constant("ferpa_license_authorizedRepresentative"),
				new Constant("ferpa_license_irb"),
				new Constant("ferpa_license_auditException"),
				new Constant("ferpa_license_authorizedUse"),
				new Constant("general_license_researchProposal"),
				new Constant("general_license_minimumPersonnel"),
				new Constant("general_license_minimumInformation"),
				new Constant("general_license_dataDestruction"),
				new Constant("general_license_regulatoryCompliance"),
				new Constant("general_license_appropriateSecurity"));
		
		baseRelations = Arrays.asList(
				new Relation("cmr_dataSubjectsInScope"),
				new Relation("cmr_personalInformation"),
				new Relation("cmr_nonPublicInformation"),
				new Relation("ferpa_datasetInScope"),
				new Relation("ferpa_pii"),
				new Relation("ferpa_directoryInfo"),
				new Relation("ferpa_allConsented"),
				new Relation("ferpa_studiesException"),
				new Relation("ferpa_auditException"),
				
				new Relation("cmr_depositorInScope"),
				
				new Relation("cmr_hasUserAuthentication"),
				new Relation("cmr_hasUserAccessControl"),
				new Relation("cmr_hasWrittenSecurityPlan"),
				new Relation("cmr_monitorsSystem"),
				new Relation("cmr_patchesSystem"),
				new Relation("cmr_trainsEmployees"),
				new Relation("cmr_regularAudits"),
				new Relation("cmr_reportsBreachesToDataOwner"),
				new Relation("cmr_reportsBreachesToDataSubjects"),
				new Relation("cmr_reportsBreachesToGovt"),
				new Relation("cmr_destroysRecords"));
		
		// first nine base relations refer to datasets,
		// cmr_depositorInScope refers to a DD and a DS,
		// the rest refer to repositories
		for (int i = 0; i < baseRelations.size(); i++) {
			if (i < 9) {
				baseRelations.get(i).setTypes(Arrays.asList(new Dataset("").getClass()));
			}
			else if (i == 9) {
				baseRelations.get(i).setTypes(
						Arrays.asList(new Person("").getClass(), new Dataset("").getClass()));
			}
			else {
				baseRelations.get(i).setTypes(Arrays.asList(new Repo("").getClass()));
			}
		}
		// sample person
		Entity alice = new Person("Alice");
		Entity bob = new Person("Bob");
		Entity carol = new Person("Carol");
		
		// sample dataset
		Entity data2015 = new Dataset("data2015");	
		data2015.addRelation(new Relation("cmr_dataSubjectsInScope", 
				Arrays.asList(data2015)));
		data2015.addRelation(new Relation("cmr_personalInformation", 
				Arrays.asList(data2015)));
		data2015.addRelation(new Relation("cmr_nonPublicInformation", 
				Arrays.asList(data2015)));
		data2015.addRelation(new Relation("cmr_depositorInScope", 
				Arrays.asList(alice, data2015)));
		data2015.addRelation(new Relation("ferpa_datasetInScope", 
				Arrays.asList(data2015)));
		data2015.addRelation(new Relation("ferpa_pii", 
				Arrays.asList(data2015)));
		data2015.addRelation(new Relation("ferpa_allConsented", 
				Arrays.asList(data2015)));
		alice.addRelation(new Relation("cmr_depositorInScope", 
				Arrays.asList(alice, data2015)));
			
		Entity data2017 = new Dataset("data2017");
		data2017.addRelation(new Relation("ferpa_datasetInScope", 
				Arrays.asList(data2017)));
		data2017.addRelation(new Relation("ferpa_pii", 
				Arrays.asList(data2017)));
		
		Entity data2019 = new Dataset("data2019");	
		data2019.addRelation(new Relation("ferpa_datasetInScope", 
				Arrays.asList(data2019)));
		data2019.addRelation(new Relation("ferpa_pii", 
				Arrays.asList(data2019)));
		data2019.addRelation(new Relation("ferpa_allConsented", 
				Arrays.asList(data2019)));
		data2019.addRelation(new Relation("ferpa_studiesException", 
				Arrays.asList(data2019)));
		data2019.addRelation(new Relation("ferpa_auditException", 
				Arrays.asList(data2019)));
		
		// sample repository
		Entity harvardDataverse = new Repo("HarvardDataverse");		
		harvardDataverse.addRelation(new Relation("cmr_hasUserAuthentication", 
				Arrays.asList(harvardDataverse)));
		harvardDataverse.addRelation(new Relation("cmr_hasUserAccessControl", 
				Arrays.asList(harvardDataverse)));
		harvardDataverse.addRelation(new Relation("cmr_hasWrittenSecurityPlan", 
				Arrays.asList(harvardDataverse)));
		harvardDataverse.addRelation(new Relation("cmr_monitorsSystem", 
				Arrays.asList(harvardDataverse)));
		harvardDataverse.addRelation(new Relation("cmr_patchesSystem", 
				Arrays.asList(harvardDataverse)));
		harvardDataverse.addRelation(new Relation("cmr_trainsEmployees", 
				Arrays.asList(harvardDataverse)));
		harvardDataverse.addRelation(new Relation("cmr_regularAudits", 
				Arrays.asList(harvardDataverse)));
		harvardDataverse.addRelation(new Relation("cmr_reportsBreachesToDataOwner", 
				Arrays.asList(harvardDataverse)));
		harvardDataverse.addRelation(new Relation("cmr_reportsBreachesToDataSubjects", 
				Arrays.asList(harvardDataverse)));
		harvardDataverse.addRelation(new Relation("cmr_reportsBreachesToGovt", 
				Arrays.asList(harvardDataverse)));
		harvardDataverse.addRelation(new Relation("cmr_destroysRecords", 
				Arrays.asList(harvardDataverse)));
		
		entities.add(alice);
		entities.add(bob);
		entities.add(carol);
		entities.add(data2015);
		entities.add(data2017);
		entities.add(data2019);
		entities.add(harvardDataverse);
		entities.add(alice);
	}
	
}
