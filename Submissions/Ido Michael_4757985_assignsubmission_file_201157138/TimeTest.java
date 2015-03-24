//201157138


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TimeTest {

	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	private static final String force = "/verbose";
	private static boolean overWrite = false;
	private static String sourcePath = null;
	private static String targetPath = null;
	private static int buffSize = 0;

	/**
	 * A program which reads 3/4 arguments as usage from command line. the
	 * program uses copyFile and then alerts on process time
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		int argsNum = 0;

		// Check command line arguments validity
		if ((args.length > 4) || (args.length < 3)
				|| (!(args[0].equals(force)) && args.length == 4)) {
			System.err.println(USAGE);
		}

		// In case of /verbose tag forward the counter and parse rest of args
		if (args.length == 4) {
			overWrite = true;
			argsNum++;
		}
		sourcePath = args[argsNum++]; // 0/1
		targetPath = args[argsNum++]; // 1/2
		try {
			buffSize = (Integer.parseInt(args[argsNum++]) / 4); // 2/3
		} catch (NumberFormatException nfe) {
			System.err.println("Buffer error");
		}

		// copy file call + check start and end time (only if true)
		long startTime = System.currentTimeMillis();
		if (copyFile(sourcePath, targetPath, buffSize, overWrite)) {
			long endTime = System.currentTimeMillis();
			System.out.println("File " + sourcePath + " was copied to "
					+ targetPath);
			System.out.println("Total time: " + (endTime - startTime) + "ms");
		}
	}

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
	 * @throws Exception
	 */
	public static boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, boolean bOverwrite) {
		int len;
		FileInputStream in = null;
		FileOutputStream out = null;
		File inputFile = new File(sourcePath);
		File outputFile = new File(targetPath);
		try {
			// Check for valid buffer size
			if (bufferSize < 0) {
				throw new Exception("Error with buffer size");
			}
			
			// Check validity of input file and readability
			if ((!inputFile.exists()) || (!inputFile.canRead())
					|| (inputFile.isDirectory())) {
				throw new Exception("Input file should be txt file");
			}

			// Check validity of output file in case no /verbose and file exist
			// throw Exception
			if (outputFile.exists() && !overWrite) {
				throw new Exception("Output file exist + no /verbose");
			}
			in = new FileInputStream(sourcePath);
			out = new FileOutputStream(outputFile);
			byte[] buff = new byte[bufferSize];

			// Read until end of file (0 bits have been read)
			while ((len = in.read(buff, 0, bufferSize)) > 0) {
				out.write(buff, 0, len);
				out.flush();
			}
			return true;
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			return false;

			// Close Streams
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				System.err.println("Error closing streams");
			}
		}
	}
}
