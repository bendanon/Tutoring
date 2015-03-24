// Name: Or Brand
// ID: 302521034

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTest {
	// Messages Constants 
	private static final String USAGE = "Usage: Invalid input";
	private static final String NOFILE = "Usage: File not found";
	private static final String ALLREADY_EXISTS = "Can't write, File already exists";
	private static final String EXCEPTION = "Error coping file";
	private static final String BUFFER_ERROR = "Usage: Invalid buffer size";

	public static void main(String[] args) { 

		// Check input length
		if (args.length > 4 || args.length < 3) {
			System.out.println(USAGE);
			return;
		}

		// Check if there is /force in input, if so, shift the number of the inputs by 1
		String force = args[0];
		boolean isForce = true;
		int shift = 1;
		if (!force.equals("/force")) {
			isForce = false;
			shift = 0;
		}

		// takes the other arguments
		String fileToTakeFrom = args[0 + shift];
		String fileToWriteTo = args[1 + shift];

		// Parsing the string of buffer size to int.
		// if the string is not a number - exit the program.
		int bufferLength;
		try {
			bufferLength = Integer.parseInt(args[2 + shift]);
		} catch (NumberFormatException e) {
			System.out.println(BUFFER_ERROR);
			return;
		}

		// Checks if the buffer size is valid
		if (bufferLength <= 0) {
			System.out.println(BUFFER_ERROR);
			return;
		}


		// Checks if input file exists
		File inputFile = new File(fileToTakeFrom);
		if (!inputFile.exists() || inputFile.isDirectory()) {
			System.out.println(NOFILE);
			return;
		}

		// Runs copyFile and check times
		long startTime = System.currentTimeMillis();
		if (copyFile(fileToTakeFrom, fileToWriteTo, bufferLength, isForce)) { 
			long endTime = System.currentTimeMillis(); 
			System.out.println("File " + fileToTakeFrom + " was copied to " + fileToWriteTo);
			System.out.println("Total time: " + (endTime - startTime) + "ms");
		}
	}

	/** 
	 * Copies a file to a specific path, using the specified buffer size.
	 *  
	 * @param srcFileName File to copy 
	 * @param toFileName Destination file name 
	 * @param bufferSize Buffer size in bytes 
	 * @param bOverwrite If file already exists, overwrite it 
	 * @return true when copy succeeds, false otherwise */ 

	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {

		// Checks if the file exists and no overwrite command received
		if (!bOverwrite) {
			File f = new File(toFileName);
			if (f.exists() && (!f.isDirectory())){
				System.out.println(ALLREADY_EXISTS);
				return false;
			}
		}

		// Creating input and output files according to arguments
		FileReader in = null;
		FileWriter out = null;
		try {
			char[] buffer = new char[bufferSize];
			in = new FileReader(srcFileName);
			out = new FileWriter(toFileName);
			int c;

			// reads from the input file and write to the output file
			while ((c = in.read(buffer, 0, bufferSize)) > 0) {
				out.write(buffer, 0, c);
			}

		} catch (Exception e) {
			System.out.println(EXCEPTION);
			return false;

			// closing all relevant files
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(EXCEPTION);
					return false;
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(EXCEPTION);
					return false;
				}
			}
		}
		return true;
	}
}
