//ex1_operating-system_304844939
//package ex1_os;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TimeTest {
	private static final String ARGUMENTS_NUM = "Error: minimum 3 arguments needed; src file, trg file, buffer num";
	private static final String BUFFER_ARG_IS_NEGATIV = "Error: buffer number needs to be positive";
	private static final String SOURCE_NOT_EXISTS = "Error: source file doesn't exists";
	private static final String TARGET_ALREADY_EXISTS = "Error: write /force to rewrite file";
	private static final String UNABLE_TO_CREATE_FILE = "Error: Unable to create the file";
	private static final String COPY_ERROR = "Error: a problem has occurred";

	public static void main(String[] args) throws IOException {
		boolean force = false;
		File sourceFile;
		File targetFile;
		//check arguments number
		if(args.length < 3){
			System.out.println(ARGUMENTS_NUM);
			System.exit(0);
		}
		int bufferSize = -1 ;
		int index = 0;
		//check if index 1 = /force
		if (args[0].equalsIgnoreCase("/force")) {
			index++;
			force = true;
		}
		//read source file path
		sourceFile = new File(args[index]);
		if (!sourceFile.exists() && sourceFile.isDirectory()) {
			System.out.println(SOURCE_NOT_EXISTS);
			System.exit(0);
		}
		//read source file path
		targetFile = new File(args[index+1]);
		if (!targetFile.exists() && targetFile.isDirectory()) {
			try {
				targetFile.getParentFile().mkdirs();
				targetFile.createNewFile();
			} catch (Exception e) {
				System.out.println(UNABLE_TO_CREATE_FILE);
			}
		} else if ((targetFile.exists() && !targetFile.isDirectory()) && !force) {
			System.out.println(TARGET_ALREADY_EXISTS);
			System.exit(0);
		}
		try{
			bufferSize = Integer.parseInt(args[index + 2]);
		} catch (NumberFormatException e){
			System.out.println(ARGUMENTS_NUM);
		}
		if (bufferSize <= 0){
			System.out.println(BUFFER_ARG_IS_NEGATIV);
			System.exit(0);
		}
		//start copy
		long currentTime = System.currentTimeMillis(); 
		boolean copyOk = copyFile(sourceFile.toString(), targetFile.toString(), bufferSize, force);
		long currentTime2 = System.currentTimeMillis();
		if (copyOk) {
			System.out.println("File " + sourceFile.toString() + " was copied to " + targetFile.toString());
			System.out.println("Total time: " + (currentTime2 - currentTime));
		}else{
			System.out.println(COPY_ERROR);
		}
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
	 * @param bOverwrite
	 *            If file already exists, overwrite it
	 * @return true when copy succeeds, false otherwise
	 * @throws IOException 
	 */
	public static boolean copyFile(String srcFileName,
			String toFileName,
			int bufferSize,
			boolean bOverwrite) throws IOException {
		FileReader inputStream = null;
		FileWriter outputStream = null;

		try {
			inputStream = new FileReader(srcFileName);
			outputStream = new FileWriter(toFileName);

			char[] car = new char[bufferSize];
			while ((inputStream.read(car, 0, bufferSize)) != -1) {
				outputStream.write(car, 0, bufferSize);
			}
		} catch (FileNotFoundException e) {
			return false;
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

