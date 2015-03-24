/* NAME: Itamar Apel. ID: 301848842*/

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class TimeTest {

	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";

	public static void main(String[] args) {

		boolean bOverwrite = false;
		int lenOfArgs = args.length;

		// Check if the number of arguments are 4
		if (lenOfArgs == 4) {
			if (args[0].equals("/force")) {
				bOverwrite = true;
			} else {
				System.err.println(USAGE);
				return;
			}
		}
		
		// End the application if the number of arguments are illegal 
		if ((lenOfArgs < 3) || (lenOfArgs > 4)) {
			System.err.println(USAGE);
			return;
		}

		// get the number size and check if is legal
		int bufferSize = 0;
		try {
			bufferSize = Integer.parseInt(args[lenOfArgs - 1]);
		} catch (NumberFormatException e) {
			System.err.println("Error: The given buffer_size is not a number");
			return;
		}
		if (bufferSize < 1) {
			System.err.println("Error: The given buffer_size should be greater than 0");
			return;
		}

		String srcFileName = args[lenOfArgs - 3];
		String toFileName = args[lenOfArgs - 2];

		
		long startTime = System.currentTimeMillis();
		
		// copy file form sorce file to target file and end the application if not Success 
		boolean res = copyFile(srcFileName, toFileName, bufferSize, bOverwrite);
		if (!res) {
			return;
		}
	
		long endTime = System.currentTimeMillis();

		// print the result.
		System.out.println("File " + srcFileName + " txt was copied to " + toFileName);
		System.out.println("Total time: " + (endTime - startTime) + "ms");

	}

	/**
	 * Copies a file to a specific path, using the specified buffer size.
	 * 
	 * @param srcFileName  File to copy
	 * @param toFileName Destination file name
	 * @param bufferSize Buffer size in bytes
	 * @param bOverwrite If file already exists, overwrite it
	 * @return true when copy succeeds, false otherwise
	 */
	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {

		FileReader sourceFile = null;
		FileWriter targetFile = null;

		// check if allow override and if the file is exist. if not return error message.
		if (!bOverwrite) {
			File file = new File(toFileName);
			if (file.exists()) {
				System.err.println("Error: the destention file exist, you should use '/force' for override");
				return false;
			}
		}

		// Copy file from source file to target file. if not success print error message. 
		try {
			sourceFile = new FileReader(srcFileName);
			targetFile = new FileWriter(toFileName);
			char[] buffer = new char[bufferSize];
			int numOfChar;
			
			// read the input from the source file.
			while ((numOfChar = sourceFile.read(buffer, 0, bufferSize)) != -1) {
				// write to the target file.
				targetFile.write(buffer, 0, numOfChar);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			
			// close the streams. if not success print error message
		} finally {
			try {
				if (sourceFile != null) {
					sourceFile.close();
				}
				if (targetFile != null) {
					targetFile.close();
				}
			} catch (Exception e2) {
				System.out.println("Error: proble, while trying close the streams");

			}
		}
		return true;
	}
}
