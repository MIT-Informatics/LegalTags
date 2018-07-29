package licensecreation.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import licensecreation.LicenseInstantiation;
import licensecreation.LicenseTemplate;
import licensecreation.conditions.PrimitiveCondition;
import markdown.PandocRenderer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Class to run the renderer. This class does not use a GUI, but requires all
 * information required for rendering to be passed on the command line.
 * Subclasses of this class launch various GUIs.
 * 
 * @author snchong
 *
 */
public class Main {
	public static void main(String[] args) {
		new Main().execute(args);
	}
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

		String[] conds = cmd.getOptionValues("conds");
		System.out.println(Arrays.asList(conds));

		try {
			LicenseTemplate template = LicenseTemplate.createLicenseTemplate(licenseTermsFiles, templateFile, descriptorFiles);
			PandocRenderer renderer = new PandocRenderer();
			if (cmd.hasOption("pdt")) {
				renderer.setPandocTemplate(cmd.getOptionValue("pdt"));
			}			
			
			Set<PrimitiveCondition> knownPrimConds = template.getPrimitiveConditions();
			
			Set<PrimitiveCondition> primConds = new LinkedHashSet<>();
			for (String cond : conds) {
				PrimitiveCondition c = PrimitiveCondition.createPrimitiveCondition(cond);
				primConds.add(c);
				if (!knownPrimConds.contains(c)) {
					System.out.println("Warning: condition '" + cond + "' is not used in the license terms");
				}
			}
			
			LicenseInstantiation linst = new LicenseInstantiation(template, primConds, true);

			Map<String, String> substs = new LinkedHashMap<>();
			Properties props = cmd.getOptionProperties("subst");
			Enumeration<Object> e = props.keys();
			while (e.hasMoreElements()) {
				String k = (String) e.nextElement();
				substs.put(k,  props.getProperty(k));
			}
			
			linst.addAdditionalSubsitution(substs);
			
			// check if there are any missing placeholders
			List<String> missingPlaceholders = linst.getMissingPlaceholders();
			if (!missingPlaceholders.isEmpty()) {
				System.out.println("Warning: no substitutions provided for:");				
			}
			for (String missing : missingPlaceholders) {
				System.out.print("    " + missing);
				String deflt = template.getInputDefault(missing);
				if (deflt != null) {
					System.out.print(" \t(using default of '" + deflt + " ')");
					linst.addAdditionalSubsitution(missing,  deflt);
				}
				System.out.println();
			}
			
			
			final String markdown = linst.renderMarkdown();
			renderer.render(markdown, outputFile);
			
		} catch (IOException e) {
			System.err.println("Unable to use files: " + e.getMessage());
			System.exit(3);			
		}		
	}

	protected void addInputFileOptions(Options options) {
		options.addOption(Option.builder("template").hasArg()
				.argName("filename").required()
				.desc("the license template file, in markdown format").build());
		options.addOption(Option.builder("pdt").longOpt("pandoctemplate")
				.hasArg().argName("filename")
				.desc("the pandoc LaTeX template file to generate the PDF")
				.build());

		options.addOption(Option.builder("terms").hasArg().required()
				.numberOfArgs(Option.UNLIMITED_VALUES).argName("filename(s)")
				.desc("the license terms file(s), in CSV format").build());

		options.addOption(Option.builder("inputs").hasArg().required()
				.numberOfArgs(Option.UNLIMITED_VALUES).argName("filename(s)")
				.desc("the input descriptor file(s), in CSV format").build());

	}
	
	protected void addOutputFileOptions(Options options) {
		options.addOption("o","output", true, "the output file (will be PDF)");		
		options.getOption("o").setRequired(true);
		options.getOption("o").setArgName("filename");
	}
	protected void addConditionOptions(Options options) {
		Option condOpt = new Option("conds", "comma-separated list of the conditions used to generate the license");
		condOpt.setArgs(Option.UNLIMITED_VALUES);
		condOpt.setArgName("condlist");
		condOpt.setValueSeparator(',');
		
		options.addOption(condOpt);
	}

	protected void addSubstOptions(Options options) {
		Option property = Option
				.builder("subst")
				.argName("key=value")
				.numberOfArgs(2)
				.valueSeparator()
				.desc("an input substitution, of the from inputKey=value, e.g., \"dataUser:supplied:name=Stephen Chong\"")
				.build();
				
		options.addOption(property);		
	}
	
	protected Options constructOptions() {
		// set up the command line options.
		Options options = new Options();
		addInputFileOptions(options);
		addOutputFileOptions(options);
		addConditionOptions(options);
		addSubstOptions(options);

		options.addOption("h", "help", false, "Show this help message");
		
		return options;
	}
	
	protected CommandLine parseArguments(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();
		boolean usageError = false;
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {			
			usageError = true;
		}

		if (cmd != null && cmd.hasOption("h")) {
			printHelp(options);
			System.exit(0);
		}
		
		if (!usageError) {
			for (Option opt : options.getOptions()) {
				if (opt.isRequired() && !cmd.hasOption(opt.getOpt())) {
					System.err.println("Option -" + opt.getOpt() + " is required.");
					usageError = true;
				}
			}
		}
		if (usageError) {
			System.out.println("Usage error!");			
			printHelp(options);
			System.exit(1);
		}
		return cmd;
	}

	private void printHelp(final Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(this.getClass().getSimpleName(), options);		
	}

	

}
