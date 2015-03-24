/* TOMER DEAN - 200691202 */

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;

import com.sun.org.omg.CORBA.ExceptionDescription;
import com.sun.tools.javac.util.Convert;

public class TimeTest {

	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";

	public static void main(String[] args) throws Exception { // Check arguments

		// check param
		if (args.length < 3 || args.length > 4)
		{
			System.out.println(USAGE);
			return;
		}
		
		// temp
		String _src = args[1];
		String _to = args[2];
		int _buffer = 1024;//Integer.parseInt(args[3]);
		boolean _overwrite = (args[0].equals("/force"));

		// start timer
		long startTime = System.currentTimeMillis();

		// do actual logic
		boolean status = copyFile(_src, _to, _buffer, _overwrite);

		// end timer
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;

		if (status == true) {
			System.out.format("File %s was copied to %s", _src, _to);
			System.out.println("");
			System.out.format("Total time: %dms", totalTime);
		}

	}

	/**
	 * 
	 * Copies a file to a specific path, using the specified buffer size. *
	 * 
	 * 
	 * 
	 * @param srcFileName
	 * 
	 *            File to copy
	 * 
	 * @param toFileName
	 * 
	 *            Destination file name
	 * 
	 * @param bufferSize
	 * 
	 *            Buffer size in bytes
	 * 
	 * @param bOverwrite
	 * 
	 *            If file already exists, overwrite it
	 * 
	 * @return true when copy succeeds, false otherwise
	 */

	public static boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, boolean bOverwrite) throws Exception {

		// define new buffers
		FileReader inputStream = null;
		FileWriter outputStream = null;

		// check if file exists
		File f = new File(toFileName);
		if (f.exists() == true && !bOverwrite) {
			System.out.println("Not copied");
			return false;
		}

		try {
			// convert to streams
			inputStream = new FileReader(srcFileName);
			outputStream = new FileWriter(toFileName);

			int read = 0;
			// define buffer size
			char[] data = new char[bufferSize];

			// start reading
			while ((read = inputStream.read(data, 0, bufferSize)) > 0) {
				outputStream.write(data, 0, read);
			}

		} catch (Exception ex) {
			System.out.println(ex);
			return false;
		} finally {
			// close everything
			if (inputStream != null) {
				inputStream.close();
			}

			if (outputStream != null) {
				outputStream.close();
			}
		}

		return true;
	}
}
