package legaltags.expert.gui;

import javafx.util.Pair;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Ferpa extends Module {
	public Ferpa() {
		name = "Ferpa";
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
		// all of the base relations in this module refer to datasets
		List<Class<? extends Entity>> dsClassList =
				Arrays.asList(new Dataset("").getClass());
		for (int i = 0; i < baseRelations.size(); i ++) {
			baseRelations.get(i).setTypes(dsClassList);
		}
		
		
		
		entities = new ArrayList<Entity>();
		
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

		entities.add(data2015);
		entities.add(data_alex_educational);
		entities.add(data_alex_deid);
		entities.add(data_alex_deid2);
		entities.add(bob);
		entities.add(steveC);
	}
	
}
