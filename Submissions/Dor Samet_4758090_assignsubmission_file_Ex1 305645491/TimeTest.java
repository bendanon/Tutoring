import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class TimeTest {
	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	private static final String errorFileIsDirectory = " file is a directory";
	private static final String errorFileExists = "Error: File already exists";
	private static final String errorIO = "Error: There has been an I/O Error. This should not happen, but it did...";
	private static final String errorClose = "Error: There has been an error closing one of the streams:";
	private static String fileFrom;
	private static String fileTo;
	private static boolean force;
	private static int bufferLength;
	
	public static void main(String[] args) { 
		// Check arguments for usage
		if (args.length > 4 || args.length < 3) {
			System.out.println(USAGE);
            System.exit(0);
		}
		
		// Check if not forced or forced
		if (args.length == 3) {
			fileFrom = args[0];
			fileTo = args[1];
			bufferLength = Integer.parseInt(args[2]);
			force = false;
		} else {
			fileFrom = args[1];
			fileTo = args[2];
			bufferLength = Integer.parseInt(args[3]);
			force = true;
		}
		
		long startTime = System.currentTimeMillis();
		if (copyFile(fileFrom, fileTo, bufferLength, force)) { 
			long endTime = System.currentTimeMillis(); 
			System.out.println("File " + fileFrom + " was copied to " + fileTo);
			System.out.println("Total time: " + (endTime - startTime));
		}
	} 
	/**
	 * Copies a file to a specific path, using the specified buffer size. 
	 * 
	 * @param srcFileName File to copy 
	 * @param toFileName Destination file name 
	 * @param bufferSize Buffer size in bytes 
	 * @param bOverwrite If file already exists, overwrite it 
	 * @return true when copy succeeds, false otherwise */ 
	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {
		FileReader in = null;
		FileWriter out = null;
		File fromFile = new File(srcFileName);
		File toFile = new File(toFileName);
		
		if (fromFile.isDirectory()) {
			System.out.println("Error: " + srcFileName + errorFileIsDirectory);
			return false;
		} else if (toFile.isDirectory()) {
			System.out.println("Error: " + toFileName + errorFileIsDirectory);
			return false;
		}
		
		if (toFile.exists() && !toFile.isDirectory() && !bOverwrite) {
			System.out.println(errorFileExists);
			return false;
		}
		try {
			char[] buffer = new char[bufferSize];
			in = new FileReader(new File(srcFileName));
			out = new FileWriter(toFile);
			int c;
			while((c = in.read(buffer, 0, bufferSize)) > 0) {
				out.write(buffer, 0, c);
			}
			return true;
		} catch (IOException e) {
			System.out.println(errorIO);
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println(errorClose + " IN!");
				}
			}
			
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					System.out.println(errorClose + " OUT!");
				}
			}
		}
	}
}
