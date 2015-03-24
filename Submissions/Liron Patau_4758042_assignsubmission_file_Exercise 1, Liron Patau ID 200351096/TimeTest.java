//Liron Patau 200351096

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class TimeTest {

	public static void main(String[] args) throws IOException {
		String Usage = "Usage: java TimeTest [/force] source_file target_file buffer_size";
		Boolean overWrite = false;
		String inputPath;
		String outputPath;
		int bufferSize;


		//checks the arguments
		if (args.length != 3 && args.length != 4) {
			System.err.println(Usage);
		} else {
			if (args.length == 3) {
				inputPath = args[0];
				outputPath = args[1];
				bufferSize =Integer.parseInt(args[2]);
			} else {
				if (args[0].equalsIgnoreCase("/force")) {
					overWrite = true;
				}
				inputPath = args[1];
				outputPath = args[2];
				bufferSize =Integer.parseInt(args[3]);
			}
			//end of argument checking

			//copy file
			long startTime = System.currentTimeMillis();
			Boolean res = copyFile(inputPath , outputPath , bufferSize , overWrite);
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			if (res) {
				System.out.println("file " + inputPath + " was copied to " + outputPath);
				System.out.println("Total time: " + totalTime + "ms");
			}

		}
	}

	/** * Copies a file to a specific path, using the specified buffer size.
	 *@param srcFileName File to copy
	 *@param toFileName Destination file name
	 *@param bufferSize Buffer size in bytes
	 *@param bOverwrite If file already exists, overwrite it
	 *@return true when copy succeeds, false otherwise */

	private static Boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, Boolean bOverWrite) throws IOException {
		FileReader inputStream = null;
		FileWriter outputStream = null;
		File out, in;
		
		//Checks if the input file exists and readable
		in = new File(srcFileName);
		if (!in.exists() || !in.canRead()) {
			System.err.println("Input file doesn't exists or isn't readable!");
			return false;
		}
		
		//checks the write condition
		out = new File(toFileName);
		if (!bOverWrite && out.exists()) {
			System.err.println("Overwrite nedded - file exists!");
			return false;
		}
		try {
			inputStream = new FileReader(srcFileName);
			outputStream = new FileWriter(toFileName);
			char[] c = new char[bufferSize];

			while(inputStream.read(c, 0 , bufferSize) != -1) {
				outputStream.write(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
