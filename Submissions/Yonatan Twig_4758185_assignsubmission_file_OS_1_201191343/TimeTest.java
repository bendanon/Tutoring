// Yonatan Twig - 201191343
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTest {

	// Exceptions
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	private static final String SOURCE_IS_NOT_EXISTSING = "Error: source file not found";
	private static final String TARGET_ALREADY_EXISTS = "Error: target file already exists. turn on [/force] in order to overwrite";
	private static final String PROBLEM_DURING_COPY = "Problem During Copying Process";
	private static final String CLOSING_STREAMS_EXCEPTION = "Streams can not be closed";
	private static final String ILLEGAL_BUFFER_SIZE = "Error: buffer has to be positive";
	private static final String CHECK_PATH = "Error: Check the path Entered ";

	public static void main(String[] args) throws IOException {
		boolean force = false;
		File source;
		File target;

		if (args.length < 3) {
			System.out.println(USAGE);
			System.exit(0);
		}
		int bufferSize = -1;
		int i = 0;

		// checks whether the first argument is force
		if (args[0].equalsIgnoreCase("/force")) {
			i = 1;
			force = true;
		}
		source = new File(args[i]);		// check for source validity
		if (!(source.exists() && !source.isDirectory())) {
			System.out.println(SOURCE_IS_NOT_EXISTSING);
			System.out.println(USAGE);
			System.exit(0);
		}
		target = new File(args[i + 1]);		// check for target validity
		if (!(target.exists() && !target.isDirectory())) {
			try {
				target.getParentFile().mkdirs();
				target.createNewFile();
			} catch (Exception e) {
				System.out.println(CHECK_PATH);
				System.out.println(USAGE);
			}
		} else if ((target.exists() && !target.isDirectory()) && !force) {
			System.out.println(TARGET_ALREADY_EXISTS);
			System.out.println(USAGE);
			System.exit(0);
		}

		try {
			bufferSize = Integer.parseInt(args[i + 2]);
		} catch (NumberFormatException e) {
			System.out.println(USAGE);
		}

		if (bufferSize <= 0) {
			System.out.println(ILLEGAL_BUFFER_SIZE);
			System.exit(0);
		}

		// copy process
		long current_time = System.currentTimeMillis();
		boolean done = copyFile(source.toString(), target.toString(),
				bufferSize, force);
		long endTime = System.currentTimeMillis();
		if (done) {
			System.out.println("File " + source.toString() + " was copied to "
					+ target.toString());
			System.out.println("Total time: " + (endTime - current_time));
		} else {
			System.out.println(PROBLEM_DURING_COPY);
		}
	}

	/**
	 * Copies a file to a specific path, using the specified buffer size.
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
	 * @throws IOException
	 */
	public static boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, boolean bOverwrite) throws IOException {

		FileReader inputStream = null;
		FileWriter outputStream = null;

		try {

			// Copy process
			inputStream = new FileReader(srcFileName);
			outputStream = new FileWriter(toFileName);

			char[] buffer = new char[bufferSize];
			while ((inputStream.read(buffer, 0, bufferSize)) != -1) {
				outputStream.write(buffer, 0, bufferSize);
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (Exception e) {
			System.out.println(PROBLEM_DURING_COPY);
			return false;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception ex) {
				System.out.println(CLOSING_STREAMS_EXCEPTION);
				return false;
			}
		}
		return true;
	}

}