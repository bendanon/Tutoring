// Ofer Berkovich, ID 039860390

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 */

/**
 * @author oferberko
 * 
 */
public class TimeTest {

	private static final String USAGE = "Usage: java TimeTest [/force] "
			+ "source_file target_file buffer_size";

	/**
	 * check that the arguments are legal, and test how long it takes to copy
	 * a file to a new one
	 * @param args
	 */
	public static void main(String[] args) {
		int buf, len = args.length;
		long startTime, endTime;
		boolean succes, force = false;
		String source, dest;

		// Check arguments length
		if (len < 3 || len > 4) {
			System.err.println(USAGE);
			return;
		}

		// Check that the last argument is an integer
		try {
			buf = Integer.parseInt(args[len - 1]);
		} catch (NumberFormatException e) {
			System.err.println(USAGE);
			return;
		}

		// Check that the first argument is valid
		if (args[0].equals("/force") && len == 4) {
			force = true;
		} else if (!args[0].equals("/force") && len == 4) {
			System.err.println(USAGE);
			return;
		}

		// copy file, and check the time it took
		source = args[len - 3];
		dest = args[len - 2];
		startTime = System.currentTimeMillis();
		succes = copyFile(source, dest, buf, force);
		endTime = System.currentTimeMillis();
		if (succes) {
			System.out.println("File " + source + " was copied to " + dest);
			System.out.println("Total time: " + (endTime - startTime) + "ms");
		} else {
			System.err.println("failure occured during file copy");
		}
	}

	/**
	 * Copies a file to a specific path, using the specified buffer size. *
	 * 
	 * @param srcFileName
	 *            File to copy
	 * @param toFileName
	 *            Destination file name
	 * @param bufferSize
	 *            Buffer size in bytes
	 * @param bOverwrite
	 *            If file already exists, overwrite it
	 * @return true when copy succeeds, false otherwise
	 */
	public static boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, boolean bOverwrite) {

		FileReader inputStream = null;
		FileWriter outputStream = null;
		char[] cBuf = new char[bufferSize];

		// Check that the source file exists and readable
		File in = new File(srcFileName);
		if (!in.exists() || in.isDirectory() || !in.canRead()) {
			System.err.println("file: '" + srcFileName + "' isn't readble");
			return false;
		}

		// If the destination file exists, check that we can overwrite it
		File out = new File(toFileName);
		if (!bOverwrite && out.exists()) {
			System.err.println("/force required to overwrite: " + toFileName);
			return false;
		}

		// If the destination file doesn't exist, check that the path is valid
		String fullPath = out.getAbsolutePath();
		String path = fullPath.
				substring(0, fullPath.lastIndexOf(File.separator));
		File fPath = new File(path);
		if (!fPath.isDirectory()) {
			System.err.println("the path " + path + " is illegal");
			return false;
		}

		// start copying
		try {
			inputStream = new FileReader(srcFileName);
			outputStream = new FileWriter(toFileName);
			while (inputStream.read(cBuf, 0, bufferSize) != -1) {
				outputStream.write(cBuf);
			}
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				System.err.println("unable to close stream");
			}
		}
		return true;
	}
}
