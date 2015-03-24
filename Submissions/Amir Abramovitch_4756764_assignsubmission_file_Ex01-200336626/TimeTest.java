/**
 * OS Ex01
 * By Amir Abramovitch
 * ID 200336626
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Remark:
 * For the most precise timing, I would've timed copyFile's while loop.
 * But in the PDF you requested to put the timing in the main() method
 * and time the call to copyFile(), so I did. 
 * @author amir
 *
 */

public class TimeTest {

	// Messages Constants
	private static final String USAGE 		= "Usage: java TimeTest [/force] source_file target_file buffer_size";
	private static final String BADSIZE 	= "Error: bad buffer_size (please enter a positive number)";
	private static final String BADSRCFILE 	= "Error: source_file does not exist";
	private static final String BADDSTFILE 	= "Error: target_file exists. Either use '/force' or a different file";
	private static final String IOERROR 	= "Error: an I/O error has occured";
	
	public static void main(String[] args) {
		
		String srcFilename, dstFilename;
		int bufferSize;
		boolean bOverwrite;
		
		// Make sure we have enough arguments
		if (args.length < 3 || (args.length == 4 && !args[0].equals("/force"))) {
			System.out.println(USAGE);
			return;
		}
		
		// Make sure buffer_size is legit
		try {
			bufferSize = Integer.parseInt(args[args.length-1]);
			if (bufferSize < 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println(BADSIZE);
			return;
		}
		
		// Parse arguments
		if (args.length == 4) {
			bOverwrite = true;
			srcFilename = args[1];
			dstFilename = args[2];
		} else {
			bOverwrite = false;
			srcFilename = args[0];
			dstFilename = args[1];
		}
		
		// Check '/force' flag
		if (!bOverwrite) {
			File f = new File(dstFilename);
			if (f.exists() && !f.isDirectory()) {
				System.out.println(BADDSTFILE);
				return;
			}
		}
		
		// Start the clock
		long startTime = System.currentTimeMillis();
		
		// Copy the file
		boolean success = copyFile(srcFilename, dstFilename, bufferSize, bOverwrite);
		
		// Stop the clock
		long endTime = System.currentTimeMillis();
		
		// Print the result
		if (success) {
			System.out.printf("File %s was copied to %s\nTotal time: %dms\n", srcFilename, dstFilename, endTime-startTime);
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
	public static boolean copyFile(String srcFilename, String dstFilename, int bufferSize, boolean bOverwrite) {
		
		FileReader inputStream = null;
        FileWriter outputStream = null;

        try {
        	
        	// I/O streams
        	inputStream = new FileReader(srcFilename);
            outputStream = new FileWriter(dstFilename);

            // I/O loop
            int bytesRead;
            char[] cbuf = new char[bufferSize];
            while ((bytesRead = inputStream.read(cbuf, 0, bufferSize)) != -1) {
            	outputStream.write(cbuf, 0, bytesRead);
            }
            
        } catch (FileNotFoundException e) {
			System.out.println(BADSRCFILE);
			return false;
		} catch (IOException e) {
			System.out.println(IOERROR);
			return false;
		} finally {
            if (inputStream != null) {
                try {
					inputStream.close();
				} catch (IOException e) {}
            }
            if (outputStream != null) {
                try {
					outputStream.close();
				} catch (IOException e) {}
            }
        }
		
		return true;
	}

}
