/**
 * @author Alex Maybaum
 * @ID 337597520 */

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

public class TimeTest {
	// Message Constants
	private static final String USAGE = "Usage: Please enter input parameters correctly.";
	private static final String FILE_EXISTS = "File already exists: Please use /force flag to overwrite existing file.";
	
	public static void main(String[] args) throws IOException {
		// initialize variables
		boolean overwrite;
		String source;
        String destination;
		int num_of_chars;
		
		try {
		// check arguments
			if (args[0].equals("/force") && args.length == 4) {
				overwrite = true;
				source = args[1];
				destination = args[2];
				num_of_chars = Integer.parseInt(args[3]);
			} else if (args.length == 3) {
				overwrite = false;
				source = args[0];
				destination = args[1];
				num_of_chars = Integer.parseInt(args[2]);
			} else {
				throw new IllegalArgumentException(USAGE);
			}
		} catch (IllegalArgumentException e) {
    		System.err.println(USAGE);
    		return;
    	}
		// copy file - start clock timer
		long startTime = System.currentTimeMillis();
		// copy logic
		boolean copy_was_successful = copyFile(source, destination, num_of_chars, overwrite);
		// end copy - finish clock timer
		long endTime = System.currentTimeMillis();
		// calculate total time to copy
		long totalTime = endTime - startTime;
		// print output
        if (copy_was_successful) {
    		System.out.println(String.format("Success!  The file %s was copied to %s", source, destination));
            System.out.println(String.format("Total time: %d ms", totalTime));
        }
	}
	
	/**
	* Copies a file to a specific path, using the specified buffer size. *
	* @param srcFileName File to copy
	* @param toFileName Destination file name
	* @param bufferSize Buffer size in bytes
	* @param bOverwrite If file already exists, overwrite it
	* @return true when copy succeeds, false otherwise */
	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {
		// initialize buffer and offset
		char[] buffer = new char[bufferSize];
        int offset = 0;
        // initialize input/output streams
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;
		// check if file exists
		File f = new File(toFileName);
		if (f.exists() && !bOverwrite) {
			System.err.println(FILE_EXISTS);
        	return false;
		}
          
        try {
        	// construct input/output streams
        	inputStream = new BufferedReader(new FileReader(srcFileName));
        	outputStream = new PrintWriter(new FileWriter(toFileName));
        	// read and write characters to I/O stream using buffer
            while ((inputStream.read(buffer, offset, bufferSize)) > 0) {
            	outputStream.write(buffer);
            }  
        }
        catch (IOException e) {
        	System.err.println(USAGE);
        	return false;
        } 
        finally {
        	try {
        		// close input stream
        		if (inputStream != null) {
        			inputStream.close();
        		}
        		// close output stream
        		if (outputStream != null) {
        			outputStream.close();
        		}
        	}
        	catch (IOException e) {
        		System.err.println("Error closing input/output streams");
        		return false;
        	}
        }
		return true;
	}
}