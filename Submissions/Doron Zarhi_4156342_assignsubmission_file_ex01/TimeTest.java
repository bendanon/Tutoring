/**
 * Simple file copier written as an assignment for HW #1 in OS course.
 * @author Doron Zarhi (301796470)
 */

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class TimeTest {
	
	// Constants
	private static final String USAGE_FORCE_FLAG = "/force";
	private static final int ARGV_WITHOUT_FORCE_LENGTH = 3;
	private static final int ARGV_WITH_FORCE_LENGTH = 4;
	
	// Error Constants
	private static final int ERR_BAD_USAGE = 1;
	private static final int ERR_FAILED_COPY = 2;
	
	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	
	public static void main(String[] args) {

		// Check arguments
		if (!((args.length == ARGV_WITHOUT_FORCE_LENGTH) || 
				((args.length == ARGV_WITH_FORCE_LENGTH) &&
						args[0].toLowerCase().matches(USAGE_FORCE_FLAG)))) {
			System.out.println(USAGE);
			System.exit(ERR_BAD_USAGE);
		}

		// Parsing the arguments
		
		boolean success = false;
		boolean overwrite = false;
		String srcFileName = "";
		String dstFileName = "";
		int bufferSize = 0;
		
		try {
			if (args.length == ARGV_WITH_FORCE_LENGTH) {
				srcFileName = args[1];
				dstFileName = args[2];
				bufferSize = Integer.parseInt(args[3]);
				overwrite = true;
			} else {
				srcFileName = args[0];
				dstFileName = args[1];
				bufferSize = Integer.parseInt(args[2]);
				overwrite = false;
			}
			
		} catch (NumberFormatException e) {
			System.out.println(USAGE);
			System.exit(ERR_BAD_USAGE);
		}
		
		if (bufferSize <= 0) {
			System.out.println(USAGE);
			System.exit(ERR_BAD_USAGE);
		}

		long startTime = System.currentTimeMillis();
		success = copyFile(srcFileName, dstFileName, bufferSize, overwrite);
		long endTime = System.currentTimeMillis();
		
		if (success) {
			System.out.printf("File %s was copied to %s\n", 
					srcFileName, dstFileName);
			System.out.printf("Total time: %sms\n", endTime - startTime);
		} else {
			System.out.println("ERROR: Copy failed.");
			System.exit(ERR_FAILED_COPY);
		}
	}
	
	/**
	 * Copies a file to a specific path, using the specified buffer size.
	 *
	 * @param srcFileName 	File to copy
	 * @param toFileName 	Destination file name
	 * @param bufferSize 	Buffer size in bytes
	 * @param bOverwrite 	If file already exists, overwrite it
	 * @return true 		When copy succeeds, false otherwise
	 */
	public static boolean copyFile(String srcFileName,
		 String toFileName,
		 int bufferSize,
		 boolean bOverwrite) {
		
		// Check for the overwrite option
		
		File file = new File(toFileName);
		
		if (file.exists() && !bOverwrite) {
			return false;
		}

		// Start actual process
		
		FileReader reader = null;
		FileWriter writer = null;
		char[] buffer = new char[bufferSize];
		boolean retVal = true;
		
		try {
			
			// Open/create target file
			writer = new FileWriter(toFileName);

			// Open source file
			reader = new FileReader(srcFileName);

			int ret = -1;
			
			// Read first chunk
			ret = reader.read(buffer, 0, bufferSize);

			while (-1 != ret) {
				
				// Write current chunk
				writer.write(buffer, 0, ret);
				
				// Read next chunk
				ret = reader.read(buffer, 0, bufferSize);
				
			}
		
		} catch (IOException e) {
			
			retVal = false;
			
		} finally {
			
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					// Ignore
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e1) {
					// Ignore
				}
			}
			
		}
		
		return retVal;
	}
}
