////////////////////// Aviya Sela, ID No. 302221403 //////////////////////
 

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class TimeTest {

	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";

	public static void main(String[] args) {

		String srcFileName, toFileName;
		int bufferSize;
		boolean bOverwrite = false;

		int startPnt = 0;

		// Validates number of arguments	
		int numOfArgs = args.length;

		if (numOfArgs == 4 & args[0].equals("/force")) {
			bOverwrite = true;
			startPnt = 1;
		} else if (numOfArgs == 3) {
			bOverwrite = false; 
		} else {
			System.out.println(USAGE);
			System.exit(1);
		}

		// Gets the Arguments 
		srcFileName = args[startPnt];
		toFileName = args[startPnt + 1];
		bufferSize = 0;

		// Validates bufferSize

		try {
			bufferSize = Integer.parseInt(args[startPnt + 2]);
		} catch (Exception e) {			// bufferSize does not consist of digits
			System.err.println(USAGE);	
			System.exit(2);
		}

		if (bufferSize < 1) {			// bufferSize is not positive
			System.out.println(USAGE);
			System.exit(3);
		}

		// Start file copy
		long startTime = System.currentTimeMillis();

		boolean success = copyFile(srcFileName, toFileName, bufferSize, bOverwrite);

		// If copied successfully, returns the total time  
		if (success) {
			long endTime = System.currentTimeMillis();
			System.out.println("File " + srcFileName + " was copied to " + toFileName + "\n" 
					+ "Total time: " + (endTime - startTime) + "ms");
		} else {
			System.exit(4);
		}
	}

	/**
	 * Copies a file to a specific path, using the specified buffer size.
	 *
	 * @param srcFileName File to copy
	 * @param toFileName Destination file name
	 * @param bufferSize Buffer size in bytes
	 * @param bOverwrite If file already exists, overwrite it
	 * @return true when copy succeeds, false otherwise
	 */
	public static boolean copyFile(String srcFileName,
			String toFileName,
			int bufferSize,
			boolean bOverwrite) {

		File dest = new File(toFileName);

		// Validates destination file path 
		if (! bOverwrite & dest.exists() || dest.isDirectory()) {
			System.out.println("Copy Failed: " + srcFileName + " already exists");
			return false; 
		}  

		// Sets the ground for try phrase
		FileReader inputStream = null;
		FileWriter outputStream = null;
		char[] buffer = new char[bufferSize];
		int n;

		try {
			inputStream = new FileReader(srcFileName);
			outputStream = new FileWriter(toFileName);

			// Copies file one buffer at a time
			while ((n = inputStream.read(buffer, 0, bufferSize)) > 0) {
				outputStream.write(buffer, 0, n);
			}
		} catch (Exception e) {
			System.out.println("Copy Failed: " + e.getMessage());
			return false; 
		} finally {

			// Closes streams
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				System.err.println("I/O Streams didn't close properly: " + e.getMessage());
				return false; 
			}
		}
		return true;			// True unless returned False sometime until now
	}
}
