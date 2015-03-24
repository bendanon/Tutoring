import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



public class TimeTest {

	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] srcFileName, toFileName, bufferSize.";
	private static final String BUFFER = "Error: buffer size value is unvalid";
	private static final String COPY_RUN_TIME = "Copy run time: %dms";
	private static final String FILE_ALREADY_EXISTS = "Error: file already exists. By adding '/force' will be forced to override";
	private static final String CLOSING_STREAM = "Error closing stream.";

	public static void main(String[] args) { 

		boolean bOverwrite = false;
		// Check arguments
		if((args.length>4) || (args.length<3)){
			System.err.print(USAGE);
			return;
		}
		if(args.length == 4){
			if(!args[0].equals("/force")){
				System.err.print(USAGE);
				return;
			}
			bOverwrite = true;
		}
		// matching the arguments
		String srcFileName = args[args.length - 3];
		String toFileName = args[args.length - 2];

		// checking for valid buffer size
		int bufferSize = 0;
		String strBufSize = args[args.length - 1];
		try {
			bufferSize = Integer.parseInt(strBufSize);
		} catch (NumberFormatException e) {
			System.err.print(BUFFER);
			return;
		} if (bufferSize <= 0) {
			System.err.print(BUFFER);
			return;
		}

		// copy file
		long startTime = System.currentTimeMillis();
		if (copyFile(srcFileName, toFileName, bufferSize, bOverwrite)) {
			long endTime = System.currentTimeMillis();
			System.out.println(String.format( COPY_RUN_TIME, endTime - startTime));
		}	
	}


	/**
	 * Copies a file to a specific path, using the specified buffer size. *
	 * @param srcFileName File to copy
	 * @param toFileName Destination file name
	 * @param bufferSize Buffer size in bytes
	 * @param bOverwrite If file already exists, overwrite it
	 * @return true when copy succeeds, false otherwise
	 */


	public static boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, boolean bOverwrite) {

		FileReader src = null;
		FileWriter dest = null;

		// if the file exists - display an error message.
		if (!bOverwrite) {
			File checkFile = new File(toFileName);
			if(checkFile.exists()) {
				System.err.print(FILE_ALREADY_EXISTS);
				return false;
			}
		} 

		try {
			/*
			 * Initializing:
			 * src <-- srcFileName
			 * dst <-- toFileName 
			 */
			src = new FileReader(srcFileName);
			dest =  new FileWriter(toFileName);
			char[] bufferArr = new char[bufferSize];

			/*
			 * As long as there is more file to copy from the source to the destination -
			 * continue.
			 */
			int readFile;
			while ((readFile = src.read(bufferArr, 0, bufferSize)) >= 1) {
				dest.write( bufferArr, 0, readFile);
			}
		} catch (IOException e) {
			System.err.print("Error:" + e.getMessage());
			return false;
		}
		// Have to make sure to always close all files
		finally {
			try {
				if (src != null) {
					src.close();
				}
			} catch (IOException e) {
				System.err.print(CLOSING_STREAM);
			}
			try {
				if (dest != null) {
					dest.close();
				}
			} catch (IOException e) {
				System.err.print(CLOSING_STREAM);
			}
		}
		return true;
	}
}
