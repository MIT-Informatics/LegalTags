package licensecreation.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import licensecreation.LicenseTemplate;
import licensecreation.gui.DecisionTree;
import markdown.PandocRenderer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class LaunchDecisionTreeWizard extends Main {
	public static void main(String[] args) {
		new LaunchDecisionTreeWizard().execute(args);
	}
	
	@Override
	protected Options constructOptions() {
		// set up the command line options.
		Options options = new Options();
		addInputFileOptions(options);
		addOutputFileOptions(options);
		return options;
	}
	
	@Override
	public void execute(String[] args) {	
		Options options = this.constructOptions();
		
		CommandLine cmd = parseArguments(options, args);
		
		
		InputStream templateFile = null;
		List<InputStream> licenseTermsFiles = new ArrayList<>();
		List<InputStream> descriptorFiles = new ArrayList<>();
		try {
			templateFile = new FileInputStream(new File(cmd.getOptionValue("template")));
			for (String lt : cmd.getOptionValues("terms")) {
				licenseTermsFiles.add(new FileInputStream(new File(lt)));
			}
			for (String id : cmd.getOptionValues("inputs")) {
				descriptorFiles.add( new FileInputStream(new File(id)));
			}
		} catch (FileNotFoundException e) {
			System.err.println("Unable to find file: " + e.getMessage());
			System.exit(2);
		}
		
		File outputFile = new File(cmd.getOptionValue("o"));


		try {
			LicenseTemplate template = LicenseTemplate.createLicenseTemplate(licenseTermsFiles, templateFile, descriptorFiles);
			PandocRenderer renderer = new PandocRenderer();
			if (cmd.hasOption("pdt")) {
				renderer.setPandocTemplate(cmd.getOptionValue("pdt"));
			}			
			new DecisionTree(template, renderer, outputFile);
		} catch (IOException e) {
			System.err.println("Unable to use files: " + e.getMessage());
			System.exit(3);			
		}
	}

}
