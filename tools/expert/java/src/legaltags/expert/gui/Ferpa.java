package legaltags.expert.gui;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;
import com.ugos.jiprolog.engine.JIPVariable;

import javafx.util.Pair;

import java.util.List;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.Arrays;

public class Ferpa extends Module {
	public Ferpa() {
		name = "Ferpa";
		Function<JIPEngine, List<String>> getDatasets = (JIPEngine jip) ->
		{
			List<String> datasets = new ArrayList<String>();
			JIPTerm jipSolution;
			JIPTermParser parser = jip.getTermParser(); 
			JIPTerm query = parser.parseTerm("ferpa_datasetInScope(DS)."); 
			JIPQuery jipQuery = jip.openSynchronousQuery(query);
			while (jipQuery.hasMoreChoicePoints() ) { 
				jipSolution = jipQuery.nextSolution();
				if (jipSolution != null) {
					JIPVariable[] vs = jipSolution.getVariables();
					if (vs != null) {
						for (JIPVariable v : vs) {
							datasets.add(v.getValue().toString(jip));					
						}
					}
				}
			}
			return datasets;
		};
		queries = 
				Arrays.asList(
					new Pair <String, Function<JIPEngine, List<String>>> 
						("Datasets", getDatasets)
				);
		constants = Arrays.asList(
				"ferpa_license_notice",
				"ferpa_license_purpose",
				"ferpa_license_scope",
				"ferpa_license_duration",
				"ferpa_license_information",
				"ferpa_license_authorizedRepresentative",
				"ferpa_license_irb",
				"ferpa_license_auditException",
				"ferpa_license_authorizedUse",
				"general_license_researchProposal",
				"general_license_minimumPersonnel",
				"general_license_minimumInformation",
				"general_license_dataDestruction",
				"general_license_regulatoryCompliance",
				"general_license_appropriateSecurity");
	}
}