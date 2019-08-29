package legaltags.expert.gui;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.util.Pair;

public class Cmr extends Module {
	public Cmr() {
		name = "cmr";
		prologFilePaths = Arrays.asList("common.pro", "cmr/cmr.pro");
		entities = new ArrayList<Entity>();
		queries = Arrays.asList(
				new Pair<String, String> 
					("Secure repositories", "cmr_secure(R).")
				);
		constants = Arrays.asList(
				new Constant("cmr_StorageEncrypted"),
				new Constant("cmr_TransmissionEncrypted"));
		
		baseRelations = Arrays.asList(
				new Relation("cmr_dataSubjectsInScope"),
				new Relation("cmr_personalInformation"),
				new Relation("cmr_nonPublicInformation"),
				
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
		
		// first three base relations refer to datasets,
		// cmr_depositorInScope refers to a DD and a DS,
		// the rest refer to repos
		for (int i = 0; i < baseRelations.size(); i++) {
			if (i < 3) {
				baseRelations.get(i).setTypes(Arrays.asList(new Dataset("").getClass()));
			}
			else if (i == 3) {
				baseRelations.get(i).setTypes(
						Arrays.asList(new Person("").getClass(), new Dataset("").getClass()));
			}
			else {
				baseRelations.get(i).setTypes(Arrays.asList(new Repo("").getClass()));
			}
		}
		// sample person
		Entity steveC = new Person("steveC");
		
		// sample dataset
		Entity data2015 = new Dataset("data2015");	
		data2015.addRelation(new Relation("cmr_dataSubjectsInScope", 
				Arrays.asList(data2015)));
		data2015.addRelation(new Relation("cmr_personalInformation", 
				Arrays.asList(data2015)));
		data2015.addRelation(new Relation("cmr_nonPublicInformation", 
				Arrays.asList(data2015)));
		data2015.addRelation(new Relation("cmr_depositorInScope", 
				Arrays.asList(steveC, data2015)));
		steveC.addRelation(new Relation("cmr_depositorInScope", 
				Arrays.asList(steveC, data2015)));
			
		// sample repository
		Entity harvardDataverse = new Repo("harvardDataverse");		
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
		
		entities.add(data2015);
		entities.add(harvardDataverse);
		entities.add(steveC);
	}
	
}
