/*
 * Yuval Feldman 302122700
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class TimeTest {
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";

	public static void main(String[] args) throws Exception {

		// Set parameters.
		boolean force = false;
		int startingIndex = 0;

		// Check arguments - Check if user entered enough parameters.
		if (args.length < 3 || args.length > 4) {
			throw new Exception(USAGE);
		}


		// check if the method /force is active.
		if (args.length == 4) {
			force = true;
			startingIndex++;
		}
		// Check start time.
		long startTime = System.currentTimeMillis();

		// Copy the provided file to the provided destination.
		copyFile(args[startingIndex], args[startingIndex + 1], Integer.parseInt(args[startingIndex + 2]), force);

		// Check end time
		long endTime = System.currentTimeMillis();

		// Print resulting amount of time.
		System.out.println(endTime - startTime);
	}

	/**
	 * Copies a file to a specific path, using the specified buffer size.
	 *
	 * @param srcFileName File to copy
	 * @param toFileName  Destination file name
	 * @param bufferSize  Buffer size in bytes
	 * @param bOverwrite  If file already exists, overwrite it
	 * @return true when copy succeeds, false otherwise
	 */
	public static boolean copyFile(String srcFileName,
				       String toFileName,
				       int bufferSize,
				       boolean bOverwrite) throws Exception {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		byte[] bufferSizeByte;
		int read;
		File sourceFile = new File(srcFileName);
		File targetFile = new File(toFileName);
		try {
			if (bufferSize < 0) {
				// Throw an exception if the buffer size us negative.
				throw new Exception("Buffer size is negative!");

			} else if (!sourceFile.exists()) {
				// Throw an exception if the source file does not exist.
				throw new Exception("Source file does not exists!");
			} else if (sourceFile.isDirectory()) {
				// Throw an exception if the source file is not a file.
				throw new Exception("Source file should be a file!");
			} else if (targetFile.exists() && !bOverwrite) {
				// Throw an exception if the source file exists but overwrite is not enabled.
				throw new Exception("Cannot overwrite file!");
			} else {
				inputStream = new FileInputStream(sourceFile);
				outputStream = new FileOutputStream(targetFile);
				bufferSizeByte = new byte[bufferSize];

				// read the sourceFile and write it tot he targetFile.
				while ((read = inputStream.read(bufferSizeByte, 0, bufferSize)) > 0) {
					outputStream.write(bufferSizeByte, 0, read);
					outputStream.flush();
				}
			}
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
			return false;
		} finally {
			try {

				// If any connections are still open close them.
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				System.err.println("Error: " + e.getMessage());
				return false;
			}
		}

		return true;
	}
}



