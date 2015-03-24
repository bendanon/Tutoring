/* Ex1
 * Daniel Dror
 * 204078653
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTest {
	public static void main(String args[]){
		boolean force = false;
		int index = 0;
		String sourceFile;
		String targetFile;
		int numByte = 0;

		// Check if the input is valid and set the variables
		int argsLen = args.length;
		if ((argsLen != 4) && (argsLen != 3)){
			System.out.println("wrong input");
			return;
		}
		if (argsLen == 4){
			if (!args[0].equals("/force")){
				System.out.println("wrong input");
				return;
			}
			else{
				force = true;
				index++;
			}
		}

		sourceFile = args[index];
		targetFile = args[++index];
		numByte = Integer.parseInt(args[++index]);

		if ((new File(targetFile).exists()) && !force){
			System.out.println("The file is already exsists");
			return;
		}

		// Set variable to be the start time and copy the file
		long startTime = System.currentTimeMillis();
		boolean copied = copyFile(sourceFile, targetFile, numByte, force);
		// If file is successfully copied, set variable to be the end time and calculate the total run time.
		if (copied) 
		{
			long endTime = System.currentTimeMillis();
			System.out.println("File " + sourceFile + " was copied to " + targetFile);
			System.out.println("Total time : " + (endTime - startTime));
		}
		else{
			System.out.println("Faild to copy the file");
		}
	}

	/**
	* Copies a file to a specific path, using the specified buffer size.
	*
	* @param srcFileName File to copy
	* @param toFileName Destination file name
	* @param bufferSize Buffer size in bytes
	* @param bOverwrite If file already exists, overwrite it
	* @return true when copy succeeds, false otherwise
	*/
	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {

		FileReader reader = null;
		FileWriter writer = null;
		int numCharRead; 

		try {
			reader = new FileReader(srcFileName);
			writer = new FileWriter(toFileName);
			char[] buffer = new char[bufferSize];

			// Copy from the source file to the target file  
			while ((numCharRead = reader.read(buffer, 0, bufferSize)) > 0) {
				writer.write(buffer, 0, numCharRead);
			}
			return true;

			//catch exceptions
		}catch(FileNotFoundException e){
			System.out.println("Error: " + e.getMessage());
			return false;
		}catch (IOException e){
			System.out.println("Error: " + e.getMessage());
			return false;
		}

		// close all the streams
		finally {
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
	}
}