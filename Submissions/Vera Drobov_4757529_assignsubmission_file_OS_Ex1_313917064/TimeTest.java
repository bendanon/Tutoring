/**
 * 
 * @author 313917064
 * 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTest {

	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";

	public static void main(String[] args) {

		String force = null;
		String source = null;
		String target = null;
		int bufSize = 0;
		boolean rtrn = false;
		long startTime = 0;
		int numArgs = args.length;

		// Check arguments
		if (numArgs != 3 && numArgs != 4) {
			// Incorrect number of arguments
			System.out.println(USAGE);
		} else if (numArgs == 4) {
			// Parse the given arguments
			force = args[0];
			source = args[1];
			target = args[2];
			bufSize = Integer.parseInt(args[3]);

			if (!force.equals("/force")) {
				// Incorrect Force flag
				System.out.println(USAGE);

			} else {
				// copy file and overwrite the existing target file
				startTime = System.currentTimeMillis();
				rtrn = copyFile(source, target, bufSize, true);
			}
		} else {
			// Parse the given arguments
			source = args[0];
			target = args[1];
			bufSize = Integer.parseInt(args[2]);

			// copy file
			startTime = System.currentTimeMillis();
			rtrn = copyFile(source, target, bufSize, false);
		}
		// copy logic
		long endTime = System.currentTimeMillis();

		// Determines if the file was successfully copied
		if (rtrn) {
			// print out the time it took to copy the file
			System.out.println(source + " was copied to " + target);
			// Calculate the time it took to copy the file
			long time = (endTime - startTime);

			System.out.println("Total time: " + time + "ms");
		} else {
			System.out.println("The file could not be copied");
		}
	}

	/**
	 * * Copies a file to a specific path, using the specified buffer size.
	 * 
	 * @param srcFileName
	 *            File to copy * @param toFileName Destination file name
	 * @param bufferSize
	 *            Buffer size in bytes
	 * @param bOverwrite
	 *            If file already exists, overwrite it
	 * @return true when copy succeeds, false otherwise
	 */
	public static boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, boolean bOverwrite) {

		FileReader inputStream = null;
		FileWriter outputStream = null;
		File dest = new File(toFileName);
		char[] cbuf = new char[bufferSize];

		try {
			try {
				// Creates an InputStreamReader
				inputStream = new FileReader(srcFileName);
			} catch (FileNotFoundException e) {
				System.out
						.println("File does not exist or for some other reason cannot be opened for reading");
				return false;
			}
			try {
				// Determine if the flag /force is true, if yes overwrite the
				// existing file, else error and return false
				if (bOverwrite || (!bOverwrite && !dest.exists())) {
					// Creates an OutputStreamWriter
					outputStream = new FileWriter(dest);
				} else if (dest.exists()) {
					System.out
							.println("Cannot overwrite existing file without flag");
					return false;
				}
			} catch (IOException e) {
				System.out
						.println("If the file exists but is a directory rather than a regular file, "
								+ "				does not exist but cannot be created, or cannot be opened for any other reason");
				return false;
			}
			/*try {
				// read input from the input steam until reaches -1(EOF) to the
				// char buffer
				int nL = 0;
				while ((nL = inputStream.read(cbuf, 0, bufferSize)) != -1) {
					System.out.println("*");
					// write from the char buffer to the output stream
					outputStream.write(cbuf, 0, nL);
				}
			} catch (IOException e) {
				System.out.println("An I/O error occurs");
				return false;
			}*/
		} finally {
			if (inputStream != null) {
				try {
					// Closes the stream and releases any system resources
					// associated with it.
					inputStream.close();
				} catch (IOException e) {
					System.out.println("An I/O error occurs");
					return false;
				}
			}
			if (outputStream != null) {
				try {
					// Closes the stream, flushing it first.
					outputStream.close();
				} catch (IOException e) {
					System.out.println("An I/O error occurs");
					return false;
				}
			}
		}
		return true;
	}
}
