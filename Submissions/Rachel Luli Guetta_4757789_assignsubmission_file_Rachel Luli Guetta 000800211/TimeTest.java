import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class TimeTest {

	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";

	public static void main(String[] args) {

		//Check arguments
		if ((args.length != 3) && (args.length != 4)) {
			System.out.println("Invalid number of parameters, please try again." + "\n" + USAGE);
		}

		else {

			//Initialize parameters to copyFile

			boolean force = false;
			int bufferSize = 0;

			//Check if the user set "force"
			if (args.length == 4) {
				if (!args[0].equals("/force")) {
					System.out.println("Wrong parameters, please try again." + "\n" + USAGE);
					System.exit(-1);
				}
				else {
					force = true;
				}
			}

			//Check if the bufferSize is valid
			try {
				bufferSize = Integer.parseInt(args[args.length - 1]);
			} catch (NumberFormatException e1) {
				System.err.println("Invalid parameter: bufferSize must be a number, please try again." + "\n" + USAGE);
			}

			String srcFilePath = args[args.length - 3]; 
			String targetFilePath = args[args.length - 2];

			//Start copying the file
			long startTime = System.currentTimeMillis();

			boolean copy = copyFile(srcFilePath, targetFilePath, bufferSize, force);

			long endTime = System.currentTimeMillis();
			
			long totalTime = endTime - startTime;
			
			//If the copy has succeeded
			if (copy) {
				System.out.println("File " + srcFilePath + " was copied to " + targetFilePath);
				System.out.println("Total time: " + totalTime + "ms");
			} 
		}

	}

	/**
	 * Copies a file to a specific path, using the specified buffer size. *
	 * @param srcFileName File to copy
	 * @param toFileName Destination file name
	 * @param bufferSize Buffer size in bytes
	 * @param bOverwrite If file already exists, overwrite it
	 * @return true when copy succeeds, false otherwise 
	 * @throws IllegalAccessException 
	 */
	public static boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, boolean bOverwrite) {

		FileReader reader = null;
		FileWriter writer = null;

		File srcFile = new File(srcFileName);

		//Check if the srcFile exists
		if (!srcFile.isFile()) {
			System.out.println("Couldn't find the srcFile, please try again.");
			return false;
		}
	
		File toFile = new File(toFileName);

		if (!toFile.exists()) {
			try {
				toFile.createNewFile();
			} catch (IOException e) {
				System.out.println("Couldn't create the target file, please try again.");
			}	
		} else {
			if (!bOverwrite) {
				System.out.println("The destination file exists, add a force flag if you want to overwrite it.");
				return false;
			}
		}

		try {
			
			reader = new FileReader(srcFile);
			writer = new FileWriter(toFile);
			
			char[] buffer = new char[bufferSize];
			int c;
			
			while ((c = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, c);
			}
			
		} catch (IOException e) {
			System.err.println("An error occured, couldn't copy the file.");
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}

				if (writer != null) {
					writer.close();
				}

			} catch (IOException e) {
				System.err.println("An error occured, couldn't close the files.");
			}

		}

		return true;

	}

}
