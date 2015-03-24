/*
 * Operating Systems Course - Spring 2015
 * Efi Arazi School of Computer Science - IDC Herzliya
 * 
 * School Solution - Exercise 1
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * The TimeTest class is intended for copying files.
 * The class has a static method 'copyFile' that copies a file with a given buffer size
 * and a main method that allows running a copy from command line. When running from
 * command line, the application prints the time it took to perform the copy.
 */
public class TimeTest {

	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [%s] src dest buffSize";
	private static final String FORCE = "/force";
	private static final String MSG_TIME = "Total time: %dms";
	private static final String ERR_DIR_EXISTS = "A directory with destination file name already exists";
	private static final String ERR_FILE_EXISTS = "File already exists: ";
	private static final String ERR_FILE_NOT_FOUND = "File/path was not found";
	private static final String ERR_IO_EXCEPTION = "Some I/O error has occured";
	private static final String ERR_UNKNOWN = "Unknown error: ";
	private static final String ERR_CLOSING = "Error closing streams";
	private static final String ERR_INVALID_BUFFER = "Invalid value for buffer size";
	
	/**
	 * Main method.
	 * Reads command line arguments and calls copyFile to do the copy.
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		// Check number of arguments
		int startArg = 0;
		boolean bForce = false;
        
		if (args.length < 3 || args.length > 4 || ((bForce = args[0].equals(FORCE)) && args.length != 4)) {
			System.out.println(String.format(USAGE, FORCE));
			return;
		}
		if (bForce)
			startArg++;
		
		// Read the rest of the arguments
		String src = args[startArg++];
		String dest = args[startArg++];
		
		// Read and parse buffer size
		int buffSize = parseBuffer(args[startArg++]);
		if (buffSize <= 0) {
			System.out.println(ERR_INVALID_BUFFER);
			return;
		}
		
		// Do the copy (and measure the time)
		long startTime = System.currentTimeMillis();
		if (copyFile(src, dest, buffSize, bForce)) {
			long endTime = System.currentTimeMillis();
			System.out.println(String.format(MSG_TIME, endTime - startTime));
		}
	}
	
	/**
	 * Parses a string with buffer size.
	 * 
	 * @param bufSizeStr Buffer size string argument
	 * @return buffer size or -1 when input is invalid
	 */
	private static int parseBuffer(String bufSizeStr) {
		try {
			return Integer.parseInt(bufSizeStr);
		}
		catch (NumberFormatException nfe) {
			return -1;
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
		
		// Check buffer size
		if (bufferSize < 0) {
			System.out.println(ERR_INVALID_BUFFER);
			return false;
		}
		
		// Check if destination file exists or if there is a directory with this name
		File dest = new File(toFileName);

		if (dest.isDirectory()) {
			// A directory with such name already exists, cannot overwrite whatsoever
			
			System.out.println(ERR_DIR_EXISTS);
			
			return false;
		}
		if (!bOverwrite && dest.exists()) {
			// A file with such name already exists and bOverwrite is false - cannot continue
			System.out.println(ERR_FILE_EXISTS + toFileName);
			
			return false;
		}
		
		// Copy the file
		RandomAccessFile reader = null;
		RandomAccessFile writer = null;
		try {
			reader = new RandomAccessFile(srcFileName, "r");
			writer = new RandomAccessFile(toFileName, "rw");
			
			// Copy the file using a buffer
			// Using len to determine how many bytes were actually read each iteration
			byte[] buff = new byte[bufferSize];
			int len;
			// As long as len > 0 we might have more bytes to read
			while ((len = reader.read(buff, 0, bufferSize)) > 0) {
				// Write read buffer - make sure you write only the amount of bytes that were actually read
				writer.write(buff, 0, len);
			}
			// Copy succeeded
			return true;
		}
		catch (FileNotFoundException fne) {
			System.out.println(ERR_FILE_NOT_FOUND);
		}
		catch (IOException ioe) {
			System.out.println(ERR_IO_EXCEPTION);
		}
		catch (Exception e) {
			System.out.println(ERR_UNKNOWN + e.getMessage());
		}
		finally {
			// We should close file handles if they were opened
			// Even if an exception was thrown we should make sure we close these handles
			try {
				if (reader != null)
					reader.close();
				if (writer != null)
					writer.close();
				}
			catch (Exception e) {
				System.out.println(ERR_CLOSING);
			}
		}
		return false;
	}
}
