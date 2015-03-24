//ex1_304993447 - Tamar Shore

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TimeTest {


		// Messages Constants of the main method
		private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
		private static final String FORCE = "/force";
		private static final String TIME_MESSAGE = "File %s was copied to %s\nTotal time: %dms";
		// Messages Constants of the copyFile method 
		private static final String NEGATIVE_BUFFERSIZE = "BufferSize should be positive number";
		private static final String ILLEGAL_ARG = "Mode argument isn't valid";
		private static final String FILE_NOT_FOUND = "File wasn't found";
		private static final String IO_ERROR = "Some I/O error has occured";
		private static final String CLOSING_STREAM_ERROR = "The stream can't be closed";
	
		
		public static void main(String[] args) {
			
			boolean checkForce = false;
			int index = 0;
			int bufferSize;
			checkForce = args[0].equals(FORCE);
			// Check arguments - should be 3 if no overwrite command requested 
			if (/*((args.length == 4) && (checkForce = args[0].equals(FORCE))) ||*/ (args.length < 3 )|| (args.length > 4)){
				System.out.println(String.format(USAGE));
				return;
			}
			
			//If there is an overwrite request, start setting the args from index 1 else from 0 
			if (checkForce){
				index++;
			}
			
			//Set arguments
			String srcFileName = args[index];
			index++;
			String toFileName = args[index];
			index++;
			try {
				bufferSize = Integer.parseInt(args[args.length -1]);
			} catch (NumberFormatException e) {
				System.out.println(NEGATIVE_BUFFERSIZE);
				return;
			}
			
			
			
			// Copy file
			long startTime = System.currentTimeMillis();
			if (copyFile(srcFileName, toFileName, bufferSize, checkForce)) {
				long endTime = System.currentTimeMillis();
				System.out.println(String.format(TIME_MESSAGE,srcFileName, toFileName, endTime - startTime));
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
			
			File destFile = new File(toFileName);
			//Checks if toFileName exists and bOverWrite is false
			if(destFile.exists() && !bOverwrite){
				System.out.println();
			}
			//Checks if the given bufferSize is negative
			if(bufferSize < 0){
				System.out.println(NEGATIVE_BUFFERSIZE);
				return false;
			}
			
			RandomAccessFile reader = null;
			RandomAccessFile writer = null;
			
			
			try {
				reader = new RandomAccessFile(srcFileName, "r");
				writer = new RandomAccessFile(toFileName, "rw");
				byte[] buffer = new byte[bufferSize];
				int size = 0;
				
				//Copies the content of the srcFileName to toFileName
				while((size = reader.read(buffer, 0, bufferSize)) > 0){
					writer.write(buffer, 0, size);
				}
				
				//After the content was successfully copied, return true
				return true;
				
			//If the mode argument is not valid
			} catch (IllegalArgumentException e) {
				System.out.println(ILLEGAL_ARG);
			//If some error occurs while opening or creating the file
			} catch (FileNotFoundException e) {
				System.out.println(FILE_NOT_FOUND);
			//If some I/O error occurs
			} catch (IOException e) {
				System.out.println(IO_ERROR);
			} finally {
				//Closes this random access file stream and releases any system resources associated with the stream
				try {
					if (reader != null)
						reader.close();
					if (writer != null)
						writer.close();
					}
				//If the stream can't be closed for some reason
				catch (Exception e) {
					System.out.println(CLOSING_STREAM_ERROR);
				}
			}
			//Returns false if file copy failed
			return false;
		}
		
}

