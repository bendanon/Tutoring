// Alejandro Stivelman
// ID: 321119505 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class TimeTest {

	// Messages Constants 
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	private static final String FORCE = "bad input! expected: /force";		
	private static final String BUF_SIZE = "bad input! expected: positive number and bigger than 0";
	private static final String TOTAL_TIME = "Total time: ";
	private static final String DIREC_EXISTS = "A directory with this name exists";
	private static final String FILE_EXISTS = "file exists but we have no permission to overwrite";
	private static final String FILE_NOT_EXISTS = "source file not exists";
	private static final String ERROR_IO = "IO error ";

	// entry point - main method
	public static void main(String[] args) {
		// Check arguments 
		if (args.length < 3 || args.length > 4) {
			System.out.println(USAGE);
			return;
		}
		//if we got 4 arguments we working in this way
		if (args.length == 4) {
			if (!(args[0].equals("/force"))) {
				System.out.println(FORCE);
				return;
			}
			try {
				int size = Integer.parseInt(args[3]);
				if (size <= 0) {
					System.out.println(BUF_SIZE);
					return;			
				}

				// copy file - now we already checked the copy because the args are good at this point
				// excpet the files that we will check in the boolean method next
				long startTime = System.currentTimeMillis();
				if (copyFile(args[1], args[2], size, true)) {
					long endTime = System.currentTimeMillis();
					System.out.println("File " + args[1] + " was copied to " + args[2]); 
					System.out.print(TOTAL_TIME);
					System.out.println(endTime - startTime + "ms");
				}
			} catch (NumberFormatException n) {
				System.out.println("not numeric value");
			}
		}
		//if we got 3 arguments we working in this way
		if (args.length == 3) {
			try {
				int size = Integer.parseInt(args[2]);
				if (size <= 0) {
					System.out.println(BUF_SIZE);
					return;			
				}
				// copy file - now we already checked the copy because the args are good at this point
				// excpet the files that we will check in the boolean method next
				long startTime = System.currentTimeMillis();
				if (copyFile(args[0], args[1], size, false)) {
					long endTime = System.currentTimeMillis();
					//in case everything is OK we prints the results
					System.out.println("File" + args[0] + "was copied to" + args[1]);  
					System.out.print(TOTAL_TIME);
					System.out.println(endTime - startTime + "ms");
				}		
			} catch (NumberFormatException n) {
				System.out.println("not numeric value");
			}
		}

	}
	/**
	 * Parameters:
	 * srcFile - the file we want to copy
	 * destination - the file name of the destination
	 * bufferSize -  size in bytes of the buffer
	 * overWrite - in case ii is true and the destination file exists we overwrite on it.
	 * returns true if the copy succeeds, false otherwise
	 */
	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {

		if (bufferSize < 0) {
			System.out.println(BUF_SIZE);
			return false;
		}
		// we want to write a file so we check if is not a directory and if it's exists,
		// if the boolean overWrite is false we can't do overwrite on this file and we fail with the copy.
		File fileDest = new File(toFileName);
		if (fileDest.isDirectory()) {
			System.out.println(DIREC_EXISTS);					
			return false;
		}
		// if bOverwrite is false and the file exists we don't overwrite
		if (!bOverwrite && fileDest.exists()) {
			System.out.println(FILE_EXISTS);
			return false;
		}
		//else we starting coping
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(srcFileName);
			out = new FileOutputStream(toFileName);
			int pos;
			byte[] temp = new byte[bufferSize];
			while ((pos = in.read(temp, 0, bufferSize)) != -1) {
				out.write(temp, 0, pos); 
			}
		//error in case the file source file don't exists
		} catch (FileNotFoundException fnf) {
			System.out.println(FILE_NOT_EXISTS);
			return false;
		}
		catch (IOException io) {
			System.out.println(ERROR_IO);
			return false;
		//closing the streams in case they are still working
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException io) {
					System.out.println(ERROR_IO);
					return false;
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException io) {
					System.out.println(ERROR_IO);
					return false;
				}
		}
		return true;
	}
}
