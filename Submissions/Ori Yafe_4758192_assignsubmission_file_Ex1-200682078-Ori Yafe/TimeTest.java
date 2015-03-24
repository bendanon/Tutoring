import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class TimeTest {

	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	private static final String BUFFER_SIZE_INVALID = "ERROR:BufferSize is not number";
	private static final String BUFFER_SIZE_TOO_LOW = "ERROR:BufferSize need to be greater then 1";
	private static final String RESULT_PATTERN = "File %s was copied to %s\nTotal time: %dms";
	private static final String ERROR_CLOSING_STREAM = "ERROR:close stream";
	private static final String FILE_EXISTS = "Error: Target file exists. Use the /force parameter in order to";
	public static void main(String[] args) { 
		// Check arguments
		int Len = args.length;
		if ((Len < 3) || (Len > 4)) {
			System.err.println(USAGE);
			return;
		}
		boolean bOverwrite = false;
		if ((Len == 4)) {
			if (!args[0].equals("/force")) {
				System.err.println(USAGE);
				return;
			}
			bOverwrite = true;
		}
		// Check buffer size
		String strBufferSize = args[Len - 1];
		int bufferSize = 0;
		try {
			bufferSize = Integer.parseInt(strBufferSize);
		} catch (NumberFormatException e) {
			System.err.println(BUFFER_SIZE_INVALID);
			return;
		}
		if (bufferSize < 1) {
			System.err.println(BUFFER_SIZE_TOO_LOW);
			return;
		}

		String srcFileName = args[Len - 3];
		String toFileName = args[Len - 2];

		long startTime = System.currentTimeMillis();
		// Copy file
		boolean res = copyFile(srcFileName, toFileName, bufferSize, bOverwrite);
		if (!res) {
			// Couldn't copy the file
			return;
		}
		long endTime = System.currentTimeMillis();

		// Print results to user
		System.out.println(String.format(RESULT_PATTERN, srcFileName,
				toFileName, endTime - startTime));
	}
		
	// copy file
	long startTime = System.currentTimeMillis();
	// copy logic
	long endTime = System.currentTimeMillis();
	/**
	* Copies a file to a specific path, using the specified buffer size. *
	* @param srcFileName File to copy
	* @param toFileName Destination file name
	* @param bufferSize Buffer size in bytes
	* @param bOverwrite If file already exists, overwrite it
	* @return true when copy succeeds, false otherwise
	*/
	public static boolean copyFile(String srcFileName, String toFileName,
	int bufferSize, boolean bOverwrite) {
		FileReader in = null;
		FileWriter out = null;
		try {
			in = new FileReader(srcFileName);
            out = new FileWriter(toFileName);
            int length;
            if(!bOverwrite) {
            	File f = new File(toFileName);
            	if (f.exists()) {
            		System.err.println(FILE_EXISTS);
            		return false;
            	}
            }
            char[] buff = new char[bufferSize];
			
			while ((length = in.read(buff, 0, bufferSize)) > 0) {
				out.write(buff, 0, length);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return false;
		} finally {
			// Close all resources.
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				System.err.println(ERROR_CLOSING_STREAM);
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				System.err.println(ERROR_CLOSING_STREAM);
			}
		}

		return true;
	}
}
