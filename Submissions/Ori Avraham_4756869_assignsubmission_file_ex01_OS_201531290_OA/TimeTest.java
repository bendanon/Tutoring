//package TimeTest;

// EX_01_OS 201531290 O.A
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTest {

	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";

	public static void main(String[] args) {

		// Fields
		int argsLength = args.length;
		long startTime;
		int bufferSize = -1;
		String inputFile = null;
		String outputFile = null;
		boolean forceFlag = false;

		// Check for bad usage
		if (argsLength < 3 || (argsLength > 3 && !args[0].equals("/force"))) {
			System.out.println(USAGE);
			return;
		}

		// Check force flag
		forceFlag = (argsLength > 3 && args[0].equals("/force")) ? true : false;
		if (forceFlag) {
			inputFile = args[1];
			outputFile = args[2];
			bufferSize = Integer.parseInt(args[3]);
		} else {
			inputFile = args[0];
			outputFile = args[1];
			bufferSize = Integer.parseInt(args[2]);
		}

		// check buffer size
		if (bufferSize < 1) {
			System.out.println("Bad buffer size");
			return;
		}
		// copy file
		startTime = System.currentTimeMillis();
		if (!copyFile(inputFile, outputFile, bufferSize, forceFlag)) {
			return;
		}

		// copy success
		long endTime = System.currentTimeMillis();
		System.out.println("File: " + inputFile + " was copied to "
				+ outputFile);
		System.out.println("Total time: " + (endTime - startTime) + "ms");
	}

	public static boolean copyFile(String inputFile, String outputFile,
			int bufferSize, boolean forceFlag) {

		File inFile = new File(inputFile);
		File outFile = new File(outputFile);

		FileReader inFileReader = null;
		FileWriter outFileWriter = null;
		int readBufferData;

		// CHECK FILES
		if (!forceFlag && outFile.exists()) {
			System.out
					.println("File already exist! use /force if you want to overwrite it.");
			return false;
		}
		if (forceFlag && outFile.exists() && outFile.isDirectory() && outFile.canWrite()) {
			System.out
					.println("Bad out file");
			return false;
		}

		if (!inFile.canRead() || !inFile.exists() || inFile.isDirectory()) {
			System.out.println("Bad source file.");
			return false;
		}

		// (try) Copying
		try {
			inFileReader = new FileReader(inputFile);
			outFileWriter = new FileWriter(outputFile);

			// Using char[] to store data
			char[] buffer = new char[bufferSize];

			// Read Data
			while ((readBufferData = inFileReader.read(buffer, 0, bufferSize)) != -1) {
				outFileWriter.write(buffer, 0, readBufferData);
				outFileWriter.flush();
			}

			// Close streams
			inFileReader.close();
			outFileWriter.close();
		} catch (IOException e) {

			// COPYING FAILED
			System.out.println("Error while copying");
			return false;
		}

		// File copied successfully
		return true;
	}
}
