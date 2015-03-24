/*
 * NAME: Daniel Biton
 * ID: 302778352
 */

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTest {
	// output messages
	private static final String USAGE = "Usage: java TimeTest [/verbose] source_file target_file buffer_size";
	private static final String INVALID_SIZE = "Error: buffer_size is not a number";
	private static final String TOO_LOW = "Error: buffer_size should be positive, greater than 0";
	private static final String RESULT = "The file: %s, was copied to: %s.\nCopying time: %dms.";
	private static final String FILE_EXISTS = "Error: Target exists. Use the /verbose flag in-order to override";
	private static final String ERR_CLOSING_STREAM = "Error while closing the stream";

	public static void main(String[] args) {
		// checking arguments
		int numberOfArgs = args.length;
		if ((numberOfArgs < 3) || (numberOfArgs > 4)) {
			System.err.println(USAGE);
			return;
		}

		// checking for /verbose
		boolean bOverwrite = false;
		if (numberOfArgs == 4) {
			if (args[0].equals("/verbose")) {
				bOverwrite = true;
			} else {
				System.err.println(USAGE);
				return;
			}
		}

		// getting the buffer size
		int bufferSize;
		try {
			bufferSize = Integer.parseInt(args[numberOfArgs - 1]);
		} catch (NumberFormatException e) {
			System.err.println(INVALID_SIZE);
			return;
		}
		if (bufferSize < 1) {
			System.err.println(TOO_LOW);
			return;
		}

		// setting up variables
		String srcFileName = args[numberOfArgs - 3];
		String toFileName = args[numberOfArgs - 2];

		// saving the starting-time
		long startTime = System.currentTimeMillis();

		// copying the target file
		boolean result = copyFile(srcFileName, toFileName, bufferSize, bOverwrite);
		long endTime = System.currentTimeMillis();

		if (result) {
			// print results
			System.out.printf(RESULT, srcFileName, toFileName, endTime - startTime);
		}
	}

	/**
	 * the method that copies the source to the target path
	 * 
	 * @param srcFileName
	 *            The path of the source file
	 * @param toFileName
	 *            The path of the target file
	 * @param bufferSize
	 *            The buffer that the method will use to copy
	 * @param bOverwrite
	 *            The marker that says if we can overwrite
	 * @return
	 */
	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize,
			boolean bOverwrite) {

		// handle overwriting
		if (!bOverwrite) {
			File f = new File(toFileName);
			if (f.exists()) {
				System.err.println(FILE_EXISTS);
				return false;
			}
		}

		// setting up variables
		FileReader fileReader = null;
		FileWriter fileWriter = null;

		try {
			// setup copying variables
			fileReader = new FileReader(srcFileName);
			fileWriter = new FileWriter(toFileName);
			char[] buff = new char[bufferSize];
			int charsFilled;

			// the copying loop
			while ((charsFilled = fileReader.read(buff)) > 0) {
				fileWriter.write(buff, 0, charsFilled);
			}

		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
			return false;

		} finally {
			// close the reader and writer
			try {
				if (fileReader != null) {
					fileReader.close();
				}
			} catch (IOException e) {
				System.err.println(ERR_CLOSING_STREAM);
			}
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				System.err.println(ERR_CLOSING_STREAM);
			}
		}
		return true;
	}
}
