/* Amit Carmeli
 * ID - 305282600
 */

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * TimeTest class created for copying files and print the time it took to copy them in ms.
 * TimeTest has a method which is the copying files method. it should receive:
 * srcFileName, toFileName, bufferSize, bOverwrite.
 * 
 * @author Amit Carmeli
 */

public class TimeTest {

	// Messages Constants
	private static final String USAGE_ERROR = "Usage: java TimeTest [/force] srcFileName, toFileName, bufferSize.";
	private static final String BUFFER_ERROR = "Error: the value of buffer size is not valid.";
	private static final String FILE_EXISTS_ERROR = "Error: the file exists. /force parameter will force to override";
	private static final String CLOSING_STREAM_ERROR = "Error closing stream.";
	private static final String COPY_TIME = "Total time: %dms";


	// main method which calculates the time to copy the file.
	public static void main(String[] args) {

		// Check arguments
		boolean bOverwrite = false;
		if ((args.length < 3) || (args.length > 4)) {
			System.err.print(USAGE_ERROR);
			return;
		}

		if ((args.length == 4)) {
			if (!args[0].equals("/force")) {
				System.err.print(USAGE_ERROR);
				return;
			}
			bOverwrite = true;
		}

		// keep reading the arguments
		String srcFileName = args[args.length - 3];
		String toFileName = args[args.length - 2];

		// buffer size checking
		int bufferSize = 0;
		String strBufferSize = args[args.length - 1];
		try {
			bufferSize = Integer.parseInt(strBufferSize);
		} catch (NumberFormatException e) {
			System.err.print(BUFFER_ERROR);
			return;
		}
		if (bufferSize <= 0) {
			System.err.print(BUFFER_ERROR);
			return;
		}

		// copy file
		long startTime = System.currentTimeMillis();
		if (copyFile(srcFileName, toFileName, bufferSize, bOverwrite)) {
			long endTime = System.currentTimeMillis();
			System.out.println(String.format(COPY_TIME, endTime - startTime));
		}
	}

	/** 
	 * Copies a file to a specific path, using the specified buffer size. 
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

		FileReader in = null;
		FileWriter out = null;

		// if the overwriting is not possible, and toFileName exists, display an error message.
		if (!bOverwrite) {
			File file = new File(toFileName);
			if(file.exists()) {
				System.err.print(FILE_EXISTS_ERROR);
				return false;
			}
		}

		// Copy from srcFileName to toFileName
		try {

			// array in given bufferSize length
			char[] buffArr = new char[bufferSize];

			// initialize the src and to files into in and out (fileReader and fileWriter)
			in = new FileReader(srcFileName);
			out = new FileWriter(toFileName);

			int leng;
			while ((leng = in.read(buffArr, 0, bufferSize)) >= 1) {
				out.write(buffArr, 0, leng);
			}

		} catch (IOException e) {
			System.err.print("Error:" + e.getMessage());
			return false;

		} finally {

			// Close all files
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				System.err.print(CLOSING_STREAM_ERROR);
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				System.err.print(CLOSING_STREAM_ERROR);
			}
		}
		return true;
	}
}