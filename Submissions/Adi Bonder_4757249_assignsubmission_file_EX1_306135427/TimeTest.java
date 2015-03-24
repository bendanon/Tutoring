/*
 * Operating Systems - Exercise 1
 * ID: 306135427
 */


import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;


/*
 * This class copies a file to another file when is run from the command line.
 * Print the time it took to copy the file.
 */
public class TimeTest {

	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] src_file dest_file buff_size";
	private static final String Buff_SIZE_ERROR = "Buffer size error - invalid value";
	private static final String TIME = "Total time: ";
	private static final String EXISTS = "The file already exists: ";
	private static final String IOEX = "I/O Exeption error";
	private static final String GEN_ERR = "Error: ";
	private static final String CLOSE = "Stream did not close";
	private static final String FORCE = "/force";
	
	
	/*
	 * The main method. Reads and verifies the command line arguments.
	 * 
	 */
	public static void main(String[] args) {
		
		// Check arguments
		boolean forceFlag = args[0].equals(FORCE);
		int argPos = 0;
		if (args.length < 3 || args.length > 4) {
			System.out.println(USAGE);
			return;
		}
		
		if (forceFlag) {
			argPos++;
		}
		
		//Hold the input and output path arguments
		String inPath = args[argPos++];
		String outPath = args[argPos++];
		
		//Get and hold the buffer size
		int holdBuffer = getBuff(args[argPos++]);
		if (holdBuffer < 0) {
			System.out.println(Buff_SIZE_ERROR);
			return;
		}
		
		// Copy the file and measure the time
		long startTime = System.currentTimeMillis();
		if (copyFile(inPath, outPath, holdBuffer,forceFlag)) {
			long endTime = System.currentTimeMillis();
			System.out.println(TIME + (endTime - startTime) + "ms");
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
		
		// Check if the destination file exists
		File outPath = new File(toFileName);
		// If the file exists and the force flag is off - return
		if (outPath.exists() && !bOverwrite) {
			System.out.println(EXISTS + toFileName);
			return false;
		}
		
		// Perform the actual copying of the file
		RandomAccessFile inFile = null;
		RandomAccessFile outFile = null;
		
		try {
			inFile = new RandomAccessFile(srcFileName, "r");
			outFile = new RandomAccessFile(toFileName, "rw");
			
			// Use the buffer to copy the file
			byte[] buff = new byte[bufferSize];
			int len;
			
			// Use len to determine how many bytes to read
			while ((len = inFile.read(buff, 0, bufferSize)) > 0) {
				outFile.write(buff, 0, len);
			}
			// return true if the file is copied 
			return true;
			
		} catch (IOException e) {
			System.out.println(IOEX);
		}
		catch (Exception e1) {
			System.out.println(GEN_ERR + e1.getMessage());
		}
		finally {
			try {
				if (inFile != null) {
					inFile.close();
				}
				if (outFile != null) {
					outFile.close();
				}
			} catch (Exception e2) {
				System.out.println(CLOSE);
			}
		}
		return false;
	}
	
	/*
	 * The method receives the buffer size as a string.
	 * Returns the buffer size as an int or -1 if an error occurs.
	 */
	private static int getBuff(String buff) {
		try {
			return Integer.parseInt(buff);
		} catch (Exception e){
			return -1;
		}
	}
}
