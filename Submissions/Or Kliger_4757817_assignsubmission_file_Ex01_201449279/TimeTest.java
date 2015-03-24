// Or Kliger, 201449279

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTest {

	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";

	public static void main(String[] args) {

		boolean result = false;
		long startTime = System.currentTimeMillis();

		switch (args.length) {
		case 3: // Force flag is not given
			result = copyFile(args[0], args[1], Integer.parseInt(args[2]), false);
			break;
		case 4: // Force flag is given
			// Check if the flag is in correct convention
			if (!args[0].equals("/force")) {
				System.out.println(USAGE);
				break;
			}
			result = copyFile(args[1], args[2], Integer.parseInt(args[3]), true);
			break;
		default: // Incorrect Number of input 
			System.out.println(USAGE);
		}
		
		Long endTime = System.currentTimeMillis();
		
		// Print The Time Result if Copy Was Succeeded
		if (result) {
			System.out.println("Total time: " + (endTime - startTime));
		} else {
			System.out.println("Copy was Failed");
		}
	}

	/**
	 * Copies a file to a specific path, using the specified buffer size.
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

		File srcFile = new File(srcFileName);
		File destFile = new File(toFileName);
		
		// Return Error if file exists and cannot be overwritten
		if ((destFile.exists()) && (bOverwrite == false)) {
			System.out.println("Cannot Overwrite the Destination File");
			return false;
		}

		// Copy the file
		FileReader in = null;
		FileWriter out = null;
		char[] buffer = new char[bufferSize];
		try {
			in = new FileReader(srcFile);
			out = new FileWriter(destFile);
			while (in.read(buffer, 0, bufferSize) != -1) {
				out.write(buffer, 0, bufferSize);
			}
			return true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		
		return false;
	}
}
