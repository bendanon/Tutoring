
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TimeTest {

	// Messages Constants 
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";


	public static void main(String[] args) throws Exception {
		int count = 0;
		boolean force = false;
		// Check arguments to see if the input of the user
		// is correct and if the source file exist and if force is required.
		if (args.length != 3 && args.length != 4) {
			throw new Exception(USAGE);
		}
		if (args.length == 4) {
			force = true;
			count++;
		}
		long startTime = System.currentTimeMillis();
		String sourceFilePath = args[count++];
		String targetFilePath = args[count++];
		// Copy logic, because the input is in bytes and the method
		// receives int type i divide by 4 the number i get after parseInt
		copyFile(sourceFilePath, targetFilePath, (Integer.parseInt(args[count]) / 4), force);
		long endTime = System.currentTimeMillis();
		long res = endTime - startTime;
		System.out.println(res);
	}
	/** 
	 * Copies a file to a specific path, using the specified buffer size. 
	 * 
	 * @param srcFileName File to copy 
	 * @param toFileName Destination file name 
	 * @param bufferSize Buffer size in bytes 
	 * @param bOverwrite If file already exists, overwrite it 
	 * @return true when copy succeeds, false otherwise 
	 * @throws Exception 
	 */ 
	public static boolean copyFile(String srcFileName, 
			String toFileName, 
			int bufferSize, 
			boolean bOverwrite) throws Exception {
		// Creating the streams which the data is going to go 
		// through them plus the source and target files.
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		File sourceFile = new File(srcFileName);
		File targetFile = new File(toFileName);
		int len;
		// Making sure all the conditions for the copy stands.
		try {
			if (bufferSize < 0) {
				throw new Exception("buffer size is negative");
			}

			if (!sourceFile.exists()) {
				throw new Exception("source file does not exists");
			}
			if (sourceFile.isDirectory()) {
				throw new Exception("source file should be a file!");
			}
			if ((targetFile.exists() && !bOverwrite)) {
				throw new Exception("overwrite is unallowed");
			}

			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(targetFile);
			// Creating a buffer with the size received
			byte[]buff = new byte[bufferSize];
			// Reading bytes data according to buffer size
			while((len = inputStream.read(buff, 0, bufferSize)) > 0) {
				outputStream.write(buff, 0, len);
				outputStream.flush();
			}
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		//If one of the streams is somehow open closes them.
		finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				System.err.println("Error: " + e.getMessage());
				return false;
			}
		}
		return true;

	}
}


