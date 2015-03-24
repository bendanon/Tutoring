import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
/**
 * OS_Ex1 - TimeTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~
 * Name: Yafim Vodkov
 * ID: 308973882
 *
 */
public class TimeTest {
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	
	public static void main (String[] args) throws IOException{

		boolean success;
		
		//declare relevant parameters
		String srcFileName = null, toFileName = null;
		int bufferSize = 0;
		boolean  overwriteFlag = args[0].equals("/force");
		
		//copy file
		long startTime = System.currentTimeMillis();

		if (args.length < 3 || args.length > 4 || (overwriteFlag && args.length != 4)) {
			System.err.println(USAGE);
			return;
		}
		
		int i = 0;
		
		if (overwriteFlag)
			i++;
		
		srcFileName = args[i++];
		toFileName = args[i++];
		
		try {
		  bufferSize = Integer.parseInt(args[i++]);
				
		} catch (NumberFormatException e) {
			System.out.println(USAGE);
			return;
		}
		
		success = copyFile(srcFileName, toFileName, bufferSize, overwriteFlag);
		
		//copy logic
		long endTime = System.currentTimeMillis();	
		
		if (success){
			long totalTime = endTime - startTime;
			System.out.println("File " + srcFileName + " was copied to " 
								+ toFileName + "\nTotal time: " + totalTime);
		}
		
	}
	/** 
	 * Copies a file to a specific path, using the specified buffer size.  
	 * 
	 * @param srcFileName File to copy  
	 * @param toFileName Destination file name  
	 * @param bufferSize Buffer size in bytes  
	 * @param bOverwrite If file already exists, overwrite it  
	 * @return true when copy succeeds, false otherwise  */ 
	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {

		RandomAccessFile input = null;
		RandomAccessFile output = null;
		
		if (new File(toFileName).exists() && !bOverwrite){
			System.out.println(toFileName + " exists. \nNo overwrite permissions.");
			return false;
		}
			

		try {
			//create buffer
			byte[] cbuf = new byte[bufferSize];
			
			// write to new file
			output = new RandomAccessFile(toFileName, "rw");
			input = new RandomAccessFile(srcFileName, "r");
			
			int numOfBytesToRead;
			while ((numOfBytesToRead = input.read(cbuf, 0, bufferSize))> 0){
				output.write(cbuf, 0,numOfBytesToRead);
			}

			
		} catch (FileNotFoundException e) {
			System.err.println("No File was found");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.err.println(USAGE);
		}finally {
			if (input != null || output != null)
				try {
					input.close();
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return true;
	}
}
