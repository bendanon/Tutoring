/*
 * Exercise number      :    1.A
 * File Name            :    TimeTest.java
 * Name (First Last)    :    Yonatan Karp
 * Student ID           :    300070760
 * Email                :    ykarp@post.idc.ac.il
 */

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class copy text file into a new text file, and count the time it took
 * until to copy ended.
 * 
 * @author Yonatan Karp
 */
public class TimeTest {

	// The usage message for this program.
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";

	private static final String OVERWRITE_FLAG = "/force";

	// the source file
	private static File mSourceFile;
	// the target file
	private static File mTargetFile;
	// deside whether or not overwrite is allowed (according to the arguments
	// input), by default overwrite is disabled.
	private static boolean mOverwrite = false;
	// contains the size of the buffer.
	private static int mBufferSize;

	/**
	 * The main method of the program, running the flow and prints the results
	 * to the screen.
	 * 
	 * @param args
	 *            the source path, target path, buffer size and if overwrite is
	 *            enabled (use -h for more info)
	 */
	public static void main(String[] args) {

		// if not enough arguments was received (or too many) print USAGE
		// message and exit the program
		if (args.length < 3 || args.length > 4 || args[0].equals("-h")) {
			System.out.println(USAGE);
			return;
		}

		try {
			// if the arguments are legal copy the file
			if (validateArguments(args)) {
				String srcFilePath = mSourceFile.getAbsolutePath();
				String targetFilePath = mTargetFile.getAbsolutePath();
				long startTime = System.currentTimeMillis();
				boolean isSucceed = copyFile(srcFilePath, targetFilePath,
						mBufferSize, mOverwrite);
				long endTime = System.currentTimeMillis();

				// if the copy succeed print a message. (error message will appear
				// from System.Error stream
				if (isSucceed) {
					// print the results of the run
					System.out.println("File " + srcFilePath + " was copied to "
							+ targetFilePath);
					System.out.println("Total time: " + (endTime - startTime)
							+ "ms");
				}
			}
		} catch(Exception e) {
			System.err.println("Exception was thrown.");
		}
	}

	/**
	 * This function check the given arguments of the program (check if the
	 * source file exists)
	 * 
	 * @param args
	 * @return
	 */
	private static boolean validateArguments(String[] args) {

		int index = 0;
		if (args[0].equals(OVERWRITE_FLAG)) {
			mOverwrite = true;
			++index;
		}

		mSourceFile = new File(args[index]);
		mTargetFile = new File(args[index + 1]);

		try {
			mBufferSize = Integer.parseInt(args[index + 2]);
		} catch (NumberFormatException e) {
			System.err.println("Buffer size must be natural number.");
			return false;
		}

		boolean isSourceExists = mSourceFile.exists();
		boolean isTargetExists = mTargetFile.exists();

		try {
			// if not exist create new file
			if (!isTargetExists) {
				mTargetFile.createNewFile();
				// checks if the file exists but overwrite is disabled.
			}
		} catch (IOException e) {
			System.err.println("Couldn't create target file.");
			return false;
		}

		if (!isSourceExists) {
			System.err.println("Source file (" + args[index]
					+ ") doesn't exists.");
			return false;
		}

		return true;
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
	 * @param bOverWrite
	 *            If file already exists, overwrite it
	 * @return true when copy succeed false otherwise.
	 */
	public static boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, boolean bOverWrite) {

		if (bufferSize <= 0) {
			System.err.println("Buffer size must be greater then zero.");
			return false;
		}

		boolean isTargetExists = mTargetFile.exists();
		if (isTargetExists && !mOverwrite) {
			System.err
					.println("Target file already exists and overwrite doesn't enabled.");
			return false;
		}

		// the buffer of the current run (according to the given buffer size)
		char[] buffer = new char[bufferSize];

		// Declare streams to input and destination files
		FileReader inputStream = null;
		FileWriter outputStream = null;

		try {
			// open streams to the files
			inputStream = new FileReader(srcFileName);
			outputStream = new FileWriter(toFileName);

			// read to the buffer and write the buffer to the destination until
			// the end of the file and write the actual number of readed bytes.
				int numOfReadedBytes = inputStream.read(buffer, 0, bufferSize);
				do {
					outputStream.write(buffer, 0, numOfReadedBytes);
					numOfReadedBytes = inputStream.read(buffer, 0, bufferSize);
				} while (numOfReadedBytes != -1);
		} catch (IOException e) {
			// if copy failed for some reason return false
			System.err.println("I/O Exception has been thrown");
			return false;
		} finally {
			try {
				// closing the streams
				if (inputStream != null)
					inputStream.close();
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e) { }
		}
		return true;
	}
}
