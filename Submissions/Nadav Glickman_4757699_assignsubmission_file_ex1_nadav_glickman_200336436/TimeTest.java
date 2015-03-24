/*
 * Operating Systems, Ex1
 * Nadav Glickman, 200336436
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class TimeTest {
	private static final String USAGE = "Usage: java TimeTest [/force] source_file"
			+ " target_file buffer_size";
	private static final String FORCE_FLAG = "/force";
	private static final String ERROR_INVALID_BUFFER = "Invalid value. Buffer may be"
			+ " a positive integer only.";
	private static final String ERROR_IO = "I/O error occured.";
	private static final String ERROR_CLOSING = "Error closing the stream.";
	
	public static void main(String[] args) {
		boolean forceOverwrite = false;
		String srcFilePath = "";
		String toFilePath = "";
		int bufferSize;
		
		// Check arguments
		int numOfArgs = args.length;
		if (numOfArgs < 3 || numOfArgs > 4) {
			System.out.println(USAGE);
			return;
		}
		
		// If 3 arguments received, assume no /force flag and assign the arguments
		// to the declared variables.
		if (numOfArgs == 3) {
			srcFilePath = args[0];
			toFilePath = args[1];
			// Make sure the third argument is a number
			try {
				bufferSize = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				System.out.println(USAGE);
				return;
			}
		// If 4 arguments received - check if the first is the /force flag,
		// else close the program.
		} else {
			if (args[0].equals(FORCE_FLAG)) {
				forceOverwrite = true;
			} else {
				System.out.println(USAGE);
				return;
			}
			// Assign arguments to declared variables.
			srcFilePath = args[1];
			toFilePath = args[2];
			// Make sure the fourth argument is a number
			try {
				bufferSize = Integer.parseInt(args[3]);
			} catch (NumberFormatException e) {
				System.out.println(USAGE);
				return;
			}		
		}
				
		// Copy file
		long startTime = System.currentTimeMillis();
		
		// If copy was successful, print what was copied and how long it took.
		if (copyFile(srcFilePath, toFilePath, bufferSize, forceOverwrite)) {
			long endTime = System.currentTimeMillis();
			System.out.println("File " + srcFilePath + " was copied to " + toFilePath
					+ "\nTotal time: " + (endTime - startTime) + "ms");
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
	public static boolean copyFile(String srcFileName, String toFileName, 
			int bufferSize, boolean bOverwrite) {
		
		FileReader inputFileReader = null;
		FileWriter outputFileWriter = null;
		File outputFile;
		
		// Check for positive buffer size.
		if (bufferSize < 0) {
			System.out.println(ERROR_INVALID_BUFFER);
			return false;
		}
		
		// Initialize a buffer of size bufferSize / 2 - it will store
		// as many chars as possible under the bufferSize limitation.
		// bufferSize is given as bytes (8 bit) and chars take up 2 bytes (16 bit).
		char[] buffer = new char[bufferSize / 2];
		int numOfCharsRead;
		
		outputFile = new File(toFileName);
		
		// Check if given path is a directory
		if (outputFile.isDirectory()) {
			System.out.println("Error: Destination file " + toFileName + " is"
					+ " a directory.");
			return false;
		}
		// Check if the file already exists. If it does and the /force
		// flag wasn't used, print an error and return false.
		if (!bOverwrite && outputFile.exists()) {
			System.out.println("Error: Destination file " + toFileName + 
					" already exists. To overwrite, use the /force flag.");
			return false;
		}
		
		try {			
			inputFileReader = new FileReader(srcFileName);
			outputFileWriter = new FileWriter(toFileName);
			
			// Read the file, buffer.size chars at a time, and write them to
			// the target file.
			numOfCharsRead = inputFileReader.read(buffer, 0, bufferSize / 2);
			while (numOfCharsRead != -1) {
				outputFileWriter.write(buffer, 0, numOfCharsRead);
				numOfCharsRead = inputFileReader.read(buffer, 0, bufferSize / 2);
			}
		// If source file is not found, print an error message and return false.
		} catch (FileNotFoundException e) {
			System.out.println("Error: File " + srcFileName + " not found.\n" + USAGE);
			return false;
		// If destination file cannot be created properly, or is a directory, print
		// an error message and return false.
		} catch (IOException e) {
			System.out.println(ERROR_IO);
			return false;
			
		// Close the FileReader and FileWriter. If an error occurs when closing,
		// print an error message and return false.
		} finally {
			try {
				if (inputFileReader != null) {
					inputFileReader.close();
				}
				if (outputFileWriter != null) {
					outputFileWriter.close();
				}
			} catch (IOException e) {
				System.out.println(ERROR_CLOSING);
				return false;
			}
		}
		return true;
	}
}
