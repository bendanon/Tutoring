/* 
 * 
 * NAME: Dvir Davidi
 * ID: 200372142
 * 
 * */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TimeTest {
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	private static final String FAILED_BUFFER_NUMBER = "Error: buffer_size is not a number";
	private static final String FAILED_BUFFER_SIZE = "Error: buffer_size should be greater than 0";
	private static final String RESULT_TOTAL_TIME = "Total time to copy it took: %dms";
	private static final String FAILED_TO_CLOSE = "Error: Could not close the file";
	private static final String FILE_CANNOT_OVERRIDE = "Error: cannot override %s";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Check the arguments.
		int argsLength = args.length;
		if ((args.length < 3) || (args.length > 4)) {
			System.err.println(USAGE);
			return;
		}

		// If the first argument is "/force".
		boolean bOverwrite = false;
		if (args.length == 4) {
			if (!(args[0].equals("/force"))) {
				System.err.println(USAGE);
				return;
			}
			bOverwrite = true;
		}

		// Check the rest of the parameter.
		int bufferSize;
		try {
			bufferSize = Integer.parseInt(args[argsLength - 1]);
		} catch (NumberFormatException e) {
			// If buffer size isn't an integer.
			System.err.println(FAILED_BUFFER_NUMBER);
			return;
		}

		if (bufferSize <= 0) {
			System.err.println(FAILED_BUFFER_SIZE);
			return;
		}

		String srcFileName = args[argsLength - 3];
		String toFileName = args[argsLength - 2];

		long startTime = System.currentTimeMillis();
		boolean copied = copyFile(srcFileName, toFileName, bufferSize,
				bOverwrite);
		long endTime = System.currentTimeMillis();

		if (copied) {
			System.out.printf(String.format(RESULT_TOTAL_TIME, endTime
					- startTime));

		} else {
			return;
		}

	}

	public static boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, boolean bOverwrite) {
		FileInputStream inputStr = null;
		FileOutputStream outputStr = null;
		boolean testSuccess = true;

		// In case we needed to override file that doesn't exits.
		if (!bOverwrite) {
			File f = new File(toFileName);
			if (f.exists()) {
				System.err.println(String.format(FILE_CANNOT_OVERRIDE,
						toFileName));
				return false;
			}
		}

		// Copy the data from the source to the target.
		try {
			inputStr = new FileInputStream(srcFileName);
			outputStr = new FileOutputStream(toFileName);
			byte[] buff = new byte[bufferSize];
			int byteLen;

			while ((byteLen = inputStr.read(buff, 0, bufferSize)) > 0) {
				outputStr.write(buff, 0, byteLen);
			}

		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
			return false;

		} finally {
			// Close.
			if (inputStr != null) {
				try {
					inputStr.close();
				} catch (IOException e) {
					System.err.println(FAILED_TO_CLOSE);
				}
			}
			if (outputStr != null) {
				try {
					outputStr.close();
				} catch (IOException e) {
					System.err.println(FAILED_TO_CLOSE);
				}
			}
		}

		return testSuccess;
	}
}
