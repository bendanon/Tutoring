/**
 * File copying time tester 
 * Operating Systems - Ex01
 * 
 * @author Idan Freiberg
 * @id 	   302939178
 */
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTest {
	// Messages
	private static final String USAGE 	  = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	private static final String ERROR_MSG = "ERROR: File copy failed, make sure source_file exists";

	// Force flag
	private static final String forceFlag = "/force";
	
	// Error codes
	private static final int ERROR_BAD_USAGE   = 1;
	private static final int ERROR_COPY_FAILED = 2;

	/**
	 * Copies a file to a specific path, using the specified buffer size. *
	 * 
	 * @param srcFileName
	 *            File to copy
	 * @param toFileName
	 *            Destination file name
	 * @param bufferSize
	 *            Buffer size in bytes
	 * @param bOverwrite
	 *            If file already exists, overwrite it
	 * @return true when copy succeeds, false otherwise
	 */
	public static boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, boolean bOverwrite) {

		// Check that bufferSize is valid
		if (bufferSize <= 0) {
			return false;
		}

		File outFile = new File(toFileName);

		// Check if target file already exists
		if (outFile.exists() && !bOverwrite) {
			return false;
		}

		boolean status = true;
		char buf[] = new char[bufferSize];
		int bytesRead = 0;
		FileWriter fWriter = null;
		FileReader fReader = null;

		// Copy source file in 'bufferSize' chunks
		try {
			fReader = new FileReader(srcFileName);
			fWriter = new FileWriter(toFileName);

			// Read the first chunk from sourceFile
			bytesRead = fReader.read(buf, 0, bufferSize);

			// Continue reading the file and writing it out to target
			while (bytesRead != -1) {
				fWriter.write(buf, 0, bytesRead);
				bytesRead = fReader.read(buf, 0, bufferSize);
			}
		} catch (IOException e) {
			status = false;
		} finally {
			if (fReader != null) {
				try {
					fReader.close();
				} catch (IOException e) {
					// Ignore
				}
			}
			if (fWriter != null) {
				try {
					fWriter.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}

		return status;
	}

	public static void main(String args[]) {

		boolean overwrite = false;
		String srcFilePath = null;
		String targetFilePath = null;
		int bufferSize = 0;

		// Read command line arguments
		if (args.length == 3) {
			srcFilePath = args[0];
			targetFilePath = args[1];
			try {
				bufferSize = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				// Error
				System.out.println(USAGE);
				System.exit(ERROR_BAD_USAGE);
			}
		} else if (args.length == 4 && args[0].toLowerCase().equals(forceFlag)) {
			overwrite = true;
			srcFilePath = args[1];
			targetFilePath = args[2];
			try {
				bufferSize = Integer.parseInt(args[3]);
			} catch (NumberFormatException e) {
				// Error
				System.out.println(USAGE);
				System.exit(ERROR_BAD_USAGE);
			}
		} else {
			// Error
			System.out.println(USAGE);
			System.exit(ERROR_BAD_USAGE);
		}

		// Copy file
		long startTime = System.currentTimeMillis();
		boolean copySucceeded = copyFile(srcFilePath, targetFilePath, bufferSize, overwrite);
		long endTime = System.currentTimeMillis();

		// Print result
		if (copySucceeded) {
			System.out.printf("File %s was copied to %s\n", srcFilePath, targetFilePath);
			System.out.printf("Total Time: %dms\n", endTime - startTime);
		} else {
			System.out.println(ERROR_MSG);
			System.exit(ERROR_COPY_FAILED);
		}

	}
}
