// Tom Medalya 305266843

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTest {

	// Messages Constants 
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size"; 
	private static boolean overWrite = false;
	private static String pathOfFile, pathToWrite;
	//	private static String pathToWrite;
	private static int givenBufferSize;
	static boolean done;

	public static void main(String[] args) { 
		// Check arguments 
		if (args == null || args.length < 3 || args.length > 4){
			System.out.println(USAGE);
			System.exit(1);
		}

		if (args.length == 3){
			pathOfFile = args[0];
			pathToWrite = args[1];
			givenBufferSize = Integer.parseInt(args[2]);
		}else{
			if (args[0].equals("\\force")){
				overWrite = true;
			}else{
				System.out.println(USAGE);
				//System.exit(1);
			}
			pathOfFile = args[1];
			pathToWrite = args[2];
			givenBufferSize = Integer.parseInt(args[3]);//  args[2];
		}

		// copy file 
		long startTime = System.currentTimeMillis(); 

		done = copyFile(pathOfFile, pathToWrite, givenBufferSize, overWrite);

		// copy logic 
		long endTime = System.currentTimeMillis(); 

		if (done){
			System.out.println("File " + pathOfFile + " was copied to " + pathToWrite + "\nTotal time: " + (endTime - startTime) + "ms");
		}

	} 

	/** Copies a file to a specific path, using the specified buffer size. 
	 * @param srcFileName File to copy 
	 * @param toFileName Destination file name 
	 * @param bufferSize Buffer size in bytes 
	 * @param bOverwrite If file already exists, overwrite it 
	 * @return true when copy succeeds, false otherwise 
	 */ 
	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {
		//initialize variables 
		char[] textFromFile = null;
		FileReader fileToRead = null;
		FileWriter fileToWrite = null;
		File toFile = null, fromFile = null;
		int numberOfReadChar = 0;
		
		
		try {
			// try to open the file if not succeed throw exception
			fromFile = new File(srcFileName);
			if (fromFile.exists() && !fromFile.isDirectory()){
				fileToRead = new FileReader(fromFile);
			}else{
				System.out.println("Error: source file does not exist");
				return false;
			}
			
			
			// check if the file is exist if not create one if it is check either to overwrite if or not.
			toFile = new File(toFileName);
			if (toFile.exists() && !toFile.isDirectory()){
				if (bOverwrite){
					toFile.delete();
				}else{
					System.out.println("Error: file is exist and you didn't add a force command ");
					return false;
				}
			}
			toFile.createNewFile();
			fileToWrite = new FileWriter(toFile);
			textFromFile = new char[bufferSize];
			
			// read the file and write the copy.
			while(true){
				numberOfReadChar = fileToRead.read(textFromFile, 0, bufferSize);
				// if it is the end of the file return true
				if(numberOfReadChar == -1){
					return true;
				}
				// if there were less than the given bufferSize copy the rest of the line and return true
				if (numberOfReadChar < bufferSize){
					char[] textFromFileCut = new char[numberOfReadChar];
					System.arraycopy( textFromFile, 0, textFromFileCut, 0, numberOfReadChar );
					fileToWrite.write(textFromFileCut, 0, numberOfReadChar);
					return true;
				}
				fileToWrite.write(textFromFile, 0, numberOfReadChar);
			}
		} catch (IOException e) {
			// print the error message and exit the program
			System.out.println("Error: " + e.getMessage());
			return false;
		}finally{
			// close the files if they are still open
			try {
				if (fileToRead != null){
					fileToRead.close();
				}

				if (fileToWrite != null){
					fileToWrite.close();
				}

			} catch (IOException e) {
				// write error if there was while closing the file
				System.out.println("Error: IOException when closing the file");
				return false;

			}
		}
	}
}
