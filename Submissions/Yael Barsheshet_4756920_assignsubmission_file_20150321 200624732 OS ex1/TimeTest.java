/* 
 * Assignment number 	: 	1
 * File Name			:	TimeTest.java
 * name (First Last)	:	Yael Barsheshet
 * Student ID			:	200624732
 * Email				:	yaelbarsheshet7@gmail.com
 */


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTest {

	// Messages Constants 
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	private static final String BUFFERERR = "Buffer_size is illegal";
	private static final String BNOTINT = "Does not contain a parsable integer";
	private static final String BNOTPOS = "Buffer size must be positive";
	private static final String COPYERR = "An error occurred while trying to copy the file";
	private static final String OVERWRITE = "Can't overWrite - the target file is already exist and forceFlag is false";
	
	
	
	public static void main(String[] args) { 
		
		int argsLength = args.length;
		boolean forceFlag = false;
		int argStartPoint = 0;
				
		// check arguments 
		if (argsLength < 3 || argsLength > 4) {
			// The command line arguments is incorrect
			System.err.println(USAGE);
			return;
		}
		if (argsLength==4) {
			if (!(args[0].equals("/force"))) {
				// The command line arguments is incorrect
				System.err.println(USAGE);
				return;
			} else {
				// The command line arguments contain a verbose flag
				forceFlag = true;
				argStartPoint = 1;
			}
		}
		
		// get paths for source file and target file
		String sourceFilePath = args[argStartPoint];
		String targetFilePath =  args[argStartPoint+1];
		
		// get buffer size and check the it is correct 
		int bufferSize = 0;
		try {
			bufferSize = Integer.parseInt(args[argStartPoint+2]);			
		} catch(NumberFormatException e) {
			System.err.println(BUFFERERR + " " + BNOTINT);
			return;
		}
		if (bufferSize < 1) {
			System.err.println(BUFFERERR + " " + BNOTPOS);
			return;
		}
		
		// start time
		long startTime = System.currentTimeMillis(); 
		// copy file
		boolean resultCopyFile = copyFile(sourceFilePath, targetFilePath, bufferSize, forceFlag);
		// end time
		long endTime = System.currentTimeMillis();
		
		if(!resultCopyFile) {
			System.err.println(COPYERR);
			return;
		}
		// calculate total time  
		long totalTime = endTime - startTime;
		System.out.println("File" + sourceFilePath + " was copied to " + targetFilePath +  
							System.lineSeparator() + " Total time: " + totalTime + "ms" );
	} 
	
	/** Copies a file to a specific path, using the specified buffer size.
	 * 	@param srcFileName File to copy
	 * 	@param toFileName Destination file name 
	 * 	@param bufferSize Buffer size in bytes
	 * 	@param bOverwrite If file already exists, overwrite it 
	 * 	@return true when copy succeeds, false otherwise
	 */ 
	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {
			
			FileReader inputStream = null;
	        FileWriter outputStream = null;
	        char[] cbuf = new char[bufferSize];

	        try {
	        	
	        	// check if the target file is already exist and forceFlag is false
	        	if(!bOverwrite) {
	        		File f = new File(toFileName);
	        		if(f.exists() && !f.isDirectory()) { 
	        			System.err.println(OVERWRITE);
	        			return false;
	        		}
	        	}
	        	
	            inputStream = new FileReader(srcFileName);
	            outputStream = new FileWriter(toFileName);

	            //The number of characters read, or -1 if the end of the stream has been reached
	            while ((inputStream.read(cbuf, 0, bufferSize)) != -1) {
	                outputStream.write(cbuf, 0, bufferSize);
	            }
	        // Handling IOExceptions
	        } catch(IOException e) {
	        	System.err.println("Error: " + e.getMessage());
	        	return false;
	        // Closing input and output streams
			} finally {
	            if (inputStream != null) {
	                try {
						inputStream.close();
					} catch (IOException e) {
						System.err.println("Error occurs while closing input stream ERR: " + e.getMessage());
					}
	            }
	            if (outputStream != null) {
	                try {
						outputStream.close();
					} catch (IOException e) {
						System.err.println("Error occurs while closing output stream. ERR: " + e.getMessage());
					}
	            }
	        }
	     return true;
	}
}
