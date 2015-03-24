// Snir Marian 302637046

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TimeTest {

	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";

	public static void main(String[] args) {
		Boolean result = false;
		long startTime = 0;
		long endTime = 0;
		int argsLength = args.length;
		// check the number of the arguments
		if (argsLength < 3 || argsLength > 4) {
			System.out.println(USAGE);
		} else {
			int num;
			//there are 3 arguments
			if (argsLength == 3) {
				try {
					//check if the buffer size is numeric positive
					num = Integer.parseInt(args[2]);
					if (num <= 0) {
						System.out.println("the buffer size is negative");
					} else {
						//try to copy the file
						startTime = System.currentTimeMillis();
						result = copyFile(args[0], args[1],
								Integer.valueOf(args[2]).intValue(), false);
						endTime = System.currentTimeMillis();
					}
				} catch (NumberFormatException e) {
					System.out
							.println("Please enter a number to the buffer size");
				}
			//there are 4 arguments	
			} else {
				try {
					//check if the buffer size is numeric positive
					num = Integer.parseInt(args[3]);
					if (num < 0) {
						System.out.println("The buffer size is negative");
					} else {
						// check if the first argument is /force
						if (args[0].equals("/force")) {
							startTime = System.currentTimeMillis();
							//try to copy the file
							result = copyFile(args[1], args[2], Integer
									.valueOf(args[3]).intValue(), true);
							endTime = System.currentTimeMillis();
						} else {
							System.out.println("The force flag is not /force");
						}

					}
				} catch (NumberFormatException e) {
					System.out
							.println("Please enter a number to the buffer size");
				}
			}
		}
		//print the result
		if (result) {
			if (argsLength == 3) {
				System.out.println("File " + args[0] + " was copied to "
						+ args[1]);
			} else {
				System.out.println("File " + args[1] + " was copied to "
						+ args[2]);
			}
			System.out.println("Total time: " + (endTime - startTime) + "ms");
		}
	}

	/**
	 * Copies a file to a specific path, using the specified buffer size. // *
	 * // * @param srcFileName File to copy // * @param toFileName Destination
	 * file name // * @param bufferSize Buffer size in bytes // * @param
	 * bOverwrite If file already exists, overwrite it // * @return true when
	 * copy succeeds, false otherwise
	 */

	public static boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, boolean bOverwrite) {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		//check if to do overwrite
		if (bOverwrite == false) {
			//check if the target exists
			if (new File(toFileName).exists()) {
				System.out
						.println("There is no force flag, and the file exists");
				return false;
			}
		}
		try {
			inputStream = new FileInputStream(srcFileName);
			outputStream = new FileOutputStream(toFileName);
			byte[] array = new byte[bufferSize];
			int len;
			// copy the file
			while ((len = inputStream.read(array, 0, bufferSize)) != -1) {
				outputStream.write(array, 0, len);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Please enter a correct path");
			return false;
		} catch (IOException e) {
			System.out.println("error "+ e);
			return false;
		// close the streams
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					return false;
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					return false;
				}
			}
		}
		return true;
	}
}
