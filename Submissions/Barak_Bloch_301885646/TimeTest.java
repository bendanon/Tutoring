//package ex1OS;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TimeTest {

	private static final String USAGE = "Usage: TimeTest [/force] source_file target_file buffer_size";
	private static boolean m_flag;
	private static String sourceFilePath;
	private static String targetFilePath;
	private static int bufferSize;

	public static void main(String[] args) {
		boolean isValid;
		// Check validity
		isValid = checkValidity(args);
		if (!isValid) {
			System.exit(1);
		}
		// Copying
		long startTime = System.currentTimeMillis();
		if (copyFile(sourceFilePath, targetFilePath, bufferSize, m_flag)) {
			// copy logic
			long endTime = System.currentTimeMillis();
			System.out.println("Time of copying was "
					+ (endTime - startTime) + "ms");
		} else {
			System.out.println("Error: Copy wasn't complete");
		}
	}

	public static boolean checkValidity(String[] args) {
		
		int counter = 0;
		if (args.length == 3 || args.length == 4) {
			if (args.length == 4) {
				if (!args[counter++].equals("/force")) {
					System.err.println("Invalid command: " + args[counter - 1]);
					return false;
				}
				
				m_flag = true;

			}
			
			sourceFilePath = args[counter++];
			targetFilePath = args[counter++];
			if (m_flag == false) {
				if (new File(targetFilePath).exists()) {
					System.out.println("Error: file exists & no flag");
					return false;
				}
			}
			
			try {
				bufferSize = Integer.parseInt(args[counter]);
				// Zero or less
				if (bufferSize < 1) {
					System.err.println("Invalid size");
					return false;
				}
			} catch (NumberFormatException e) {
				System.err.println("Size supposed to be a number");
				return false;
			}
			//Valid 
			return true;

		}
		System.err.println(USAGE);
		return false;
	}

	/*
	 * Copies a file to a specific path, using the specified buffer size. * *
	 * 
	 * @param srcFileName File to copy * @param toFileName Destination file name
	 * 
	 * @param bufferSize Buffer size in bytes
	 * 
	 * @param bOverwrite If file already exists, overwrite it
	 * 
	 * @return true when copy succeeds, false otherwise
	 */

	public static boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, boolean bOverwrite) {
		
		FileInputStream reader = null;
		FileOutputStream writer = null;
		int charsLength;
		
		// Copying
		try {
			reader = new FileInputStream(sourceFilePath);
			writer = new FileOutputStream(targetFilePath);
			byte[] bufferReader = new byte[bufferSize];
			while ((charsLength = reader
					.read(bufferReader, 0, bufferSize)) > 0) {
				writer.write(bufferReader, 0, charsLength);
			}
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
			return false;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				System.err.println("Error: " + e.getMessage());
				return false;
			}
		}
		//Copied
		return true;

	}
}
