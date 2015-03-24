/** Maor Zadunaisky 305028300 */

import java.io.*;

public class TimeTest {

	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	private static final String FILE_NOT_FOUND = "Error: File not found!";
	private static final String FAILED_TO_CREATE_FILE = "Error: Failed to create file";
	private static final String TARGET_ALREADY_EXISTS = "Error: Target file is already exist. turn on [/force] flag!";
	private static final String INVALID_BUFFER = "Error: Invalid buffer. Must be a positive number!";
	private static final String FAILED_TO_COPY = "Error: Copy Failed!";

	public static void main(String[] args) throws IOException {

		// Check arguments
		if (args.length < 3 || args.length > 4) {
			printMsg(USAGE, true);
		}

		int index = 0; // location of parameters
		boolean forceFlag = false;
		if (args[index].equalsIgnoreCase("/force")) {
			forceFlag = true;
			index = 1;
		}

		// Set sourceFile parameter
		File sourceFile = null;
		try {
			sourceFile = new File(args[index]);
			if (!(sourceFile.exists() && !sourceFile.isDirectory())) {
				printMsg(FILE_NOT_FOUND, true);
			}
		} catch (Exception exception) {
			printMsg(FILE_NOT_FOUND, true);
		}

		// Set targetFile parameter
		index++;
		String targetFileName = args[index];
		File targetFile = null;
		try {
			targetFile = new File(targetFileName);
			if (!(targetFile.exists() && !targetFile.isDirectory())) {
				try {
					// Create file
					targetFile.getParentFile().mkdirs();
                    System.out.println("GOT HERE");
					targetFile.createNewFile();
				} catch (Exception exception) {
					printMsg(FAILED_TO_CREATE_FILE, true);
				}
			} else if ((targetFile.exists() && !targetFile.isDirectory()) && !forceFlag) {
				printMsg(TARGET_ALREADY_EXISTS, true);
			}
		} catch (Exception exception) {
			printMsg(FILE_NOT_FOUND, true);
		}

		// Set buffer parameter
		index++;
		String bufferParam = args[index];
		int bufferSize = 0;
		if (tryParseInt(bufferParam)) {
			bufferSize = Integer.parseInt(bufferParam);
			if (bufferSize <= 0) {
				printMsg(INVALID_BUFFER, true);
			}
		} else {
			printMsg(INVALID_BUFFER, true);
		}

		// Copy
		long startTime = System.currentTimeMillis();
		boolean copySucceed = copyFile(sourceFile.getName(), targetFile.getName(), bufferSize, forceFlag);
		long endTime = System.currentTimeMillis();

		if (copySucceed) {
			String message = "File " + sourceFile.getName() + " was copied to " + targetFile.getName() + "\nTotal time: " + (endTime - startTime);
			printMsg(message, false);
		} else {
			printMsg(FAILED_TO_COPY, false);
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
	 * @throws IOException
	 */
	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) 
			throws IOException {

		FileReader inputStream = null;
		FileWriter outputStream = null;

		try {
			inputStream = new FileReader(srcFileName);
			outputStream = new FileWriter(toFileName);
			char[] buffer = new char[bufferSize];
			while ((inputStream.read(buffer, 0, bufferSize)) != -1) {
				outputStream.write(buffer, 0, bufferSize);
			}
		} catch (Exception exception) {
			printMsg(exception.getMessage(), false);
			return false;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception exception) {
				printMsg(exception.getMessage(), false);
				return false;
			}
		}

		return true;
	}

	/**
	 * Prints message to user
	 * 
	 * @param message The message to print
	 * @param exit If true, close the application
	 */
	private static void printMsg(String message, boolean exit) {
		System.out.println(message);
		if (exit) {
			System.exit(0);
		}
	}

	private static boolean tryParseInt(String value)  
	{  
		try  {  
			Integer.parseInt(value);  
			return true;  
		} catch(NumberFormatException exception)  {  
			return false;  
		}  
	}
}
