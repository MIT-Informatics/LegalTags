package markdown;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.ProcessBuilder.Redirect;
import java.util.Map;


public class PandocRenderer extends MarkdownRenderer {
	/**
	 * String of the command for pandoc.
	 */
	 String pandocExecutable;
	
	/**
	 * String of the LaTeX template file to pass in to pandoc
	 */
	String pandocTemplate;
	
	/**
	 * Additional paths to add to the path when executing pandoc. 
	 */
	String[] additionalPaths;
	
	public PandocRenderer() {
		this(null, null,  null);
	}
	public PandocRenderer(String pandocExecutable, String pandocTemplate, String[] additionalPaths) {
		this.setPandocExecutable(pandocExecutable);
		this.setPandocTemplate(pandocTemplate);
		this.setAdditionalPaths(additionalPaths);
	}
	public void setPandocExecutable(String pandocExecutable) {
		String osName = System.getProperty ("os.name");
		if (pandocExecutable == null && isOsX(osName)) {
			this.pandocExecutable = "/usr/local/bin/pandoc";
		}
		else if (pandocExecutable != null) {
			this.pandocExecutable = pandocExecutable;
		}
		else {
			this.pandocExecutable = "pandoc";
		}		
	}
	
	public void setPandocTemplate(String pandocTemplate) {
		if (pandocTemplate != null) {
			this.pandocTemplate = pandocTemplate;
		}
		else {
			this.pandocTemplate = "pandoc_latex_template.tex";
		}		
	}

	public void setAdditionalPaths(String[] additionalPaths) {
		String osName = System.getProperty ("os.name");
		
		if (additionalPaths != null) {
			this.additionalPaths = additionalPaths;
		}
		else if (isWindows(osName)) {
			this.additionalPaths = new String[] { "c:\\Program Files (x86)\\MiKTeX 2.9\\miktex\\bin" };
		}
		else if (isOsX(osName)) {
			this.additionalPaths = new String[] {  "/Library/TeX/texbin", "/usr/local/bin" };
		}
		else {
			this.additionalPaths = null;
		}
	}
	
	@Override
	public void render(String s, File outputFile) {
		if (outputFile == null) {
			throw new IllegalArgumentException("No output file selected. PDF not generated.");
		}

		
		
		String[] cmdarray = {
				this.pandocExecutable,
				"--from=markdown",
				"--to=latex",
				"-s",
				"--template="+this.pandocTemplate,
				//"--verbose",
				"--output=" + outputFile.getAbsolutePath()
		};
		//System.out.println("Output file is " + outputFile.getAbsolutePath());
		ProcessBuilder pb = new ProcessBuilder(cmdarray);
		//pb.redirectError(Redirect.INHERIT);
		pb.redirectOutput(Redirect.INHERIT);
		Map<String, String> env = pb.environment();

		// Add additional paths to $PATH
		{
			if (this.additionalPaths != null && this.additionalPaths.length > 0) {
				String path = env.get("PATH");

				if (path == null) {
					path = "";
				}

				for (String addPath : this.additionalPaths) {
					if (path.length() > 0) {
						path += File.pathSeparator;
					}
					path += addPath;				
				}

				env.put("PATH", path);
			}
		}

		try {
			Process p = pb.start();
			PrintWriter bos = new PrintWriter(p.getOutputStream());
			bos.print(s);
			bos.flush();
			bos.close();			
			int result = p.waitFor();			
			//System.out.println("finished: " + result);
			

			if (result != 0) {
				// convert the error stream to a string

				final int bufferSize = 1024;
				final char[] buffer = new char[bufferSize];
				final StringBuilder out = new StringBuilder();
				Reader in = new InputStreamReader(p.getErrorStream(), "UTF-8");
				for (; ; ) {
					int rsz = in.read(buffer, 0, buffer.length);
					if (rsz < 0)
						break;
					out.append(buffer, 0, rsz);
				}
				throw new RuntimeException("Unable to render PDF using Pandoc: " + out.toString());
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}


}
