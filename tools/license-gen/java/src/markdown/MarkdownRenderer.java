package markdown;

import java.io.File;

/**
 * This abstract class provides a mechanism to render a markdown string
 * outputing the result to the specified file. Different renderers may render in
 * different formats, e.g., producing a PDF file, or producing an HTML file.
 *
 */
public abstract class MarkdownRenderer {
	protected static boolean isOsX(String osName) {
		return osName.contains("OS X");
	}

	protected static boolean isWindows(String osName) {
		return osName.contains("Windows");
	}

	/**
	 * Subclasses render the document, writing out to a file if needed.
	 * 
	 * Note that this method may be executed in a non-Swing thread.
	 * 
	 * @throws RuntimeException if there was an error in rederning the license.
	 */
	public abstract void render(String markdown, File outputFile);

}
