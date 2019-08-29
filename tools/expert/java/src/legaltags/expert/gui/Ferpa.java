package legaltags.expert.gui;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;

public class Ferpa extends Module {
	public Ferpa() {
		name = "Ferpa";
		prologFilePaths = Arrays.asList("common.pro", "ferpa/ferpa.pro");
		entities = new ArrayList<Entity>();
		queries = Arrays.asList(
				new Pair<String, String> 
					("Datasets in scope", "ferpa_datasetInScope(DS)."),
				new Pair<String, String> 
					("Datasets with identifiable information", "ferpa_identifiable(DS).")
				);
		
		constants = Arrays.asList(
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
				new Relation("ferpa_datasetInScope"),
				new Relation("ferpa_pii"),
				new Relation("ferpa_directoryInfo"),
				new Relation("ferpa_allConsented"),
				new Relation("ferpa_studiesException"),
				new Relation("ferpa_auditException"));
		
		// set the types of variables in all base relations to dataset
		for (Relation r : baseRelations) {
			r.setTypes(Arrays.asList(new Dataset("").getClass()));
		}
		
		// sample data
		Entity data2015 = new Dataset("data2015");
		data2015.addRelation(new Relation("ferpa_datasetInScope", 
				Arrays.asList(data2015)));
		data2015.addRelation(new Relation("ferpa_pii", 
				Arrays.asList(data2015)));
		data2015.addRelation(new Relation("ferpa_allConsented", 
				Arrays.asList(data2015)));
		
		Entity data_alex_educational = new Dataset("data_alex_educational");	
		data_alex_educational.addRelation(new Relation("ferpa_datasetInScope", 
				Arrays.asList(data_alex_educational)));
		data_alex_educational.addRelation(new Relation("ferpa_pii", 
				Arrays.asList(data_alex_educational)));
		data_alex_educational.addRelation(new Relation("ferpa_allConsented", 
				Arrays.asList(data_alex_educational)));
		data_alex_educational.addRelation(new Relation("ferpa_studiesException", 
				Arrays.asList(data_alex_educational)));
		data_alex_educational.addRelation(new Relation("ferpa_auditException", 
				Arrays.asList(data_alex_educational)));
		
		Entity data_alex_deid = new Dataset("data_alex_deid");
		data_alex_deid.addRelation(new Relation("ferpa_datasetInScope", 
				Arrays.asList(data_alex_deid)));
		data_alex_deid.addRelation(new Relation("ferpa_pii", 
				Arrays.asList(data_alex_deid)));
		
		Entity data_alex_deid2 = new Dataset("data_alex_deid2");
		data_alex_deid2.addRelation(new Relation("ferpa_datasetInScope", 
				Arrays.asList(data_alex_deid2)));
		data_alex_deid2.addRelation(new Relation("ferpa_pii", 
				Arrays.asList(data_alex_deid2)));
		
		Entity steveC = new Person("steveC");
		Entity bob = new Person("bob");
		Entity harvardDataverse = new Repo("harvardDataverse");

		entities.add(data2015);
		entities.add(data_alex_educational);
		entities.add(data_alex_deid);
		entities.add(data_alex_deid2);
		entities.add(bob);
		entities.add(steveC);
		entities.add(harvardDataverse);
	}
	
}
