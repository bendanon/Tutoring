//ID 200628733

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTest {


	// Messages Constants
	private static final String USAGE = "Usage: TimeTest [/force] source_file target_file buffer_size";
	public static void main(String[] args) throws IOException {
		String source_file = null, target_file = null;
		int bufferSize = 0;
		int overwriteInt = 0;		//overwriteInt Will be used to read the parameters well.
									//We use this variable for avoiding from using always " (int) overwrite" code.
		boolean overwrite = false;
		
		//validate right usage
		if ((args.length != 3) && (args.length !=4)){
			System.out.println("Error. Invalid usage");
			System.out.println(USAGE);
			System.exit(0);
		}
		
		//does force enabled?
		if (args[0].equals("/force")){ //[/force] option enabled
				overwrite = true;
				overwriteInt = 1;
		}
		
		/* Initialize paramaters; According to the USAGE.
		 * We use {overwriteInt} to read the parameters from the right place. 
		 */
		try {
			source_file = args[0 + overwriteInt];
			target_file = args[1 + overwriteInt];
			bufferSize =  Integer.parseInt(args[2 + overwriteInt]);
		} catch (Exception e) {
	        //throw new IllegalArgumentException("Error. Invalid usage\n" + USAGE);
			System.out.println("Error. Invalid usage");
			System.out.println(USAGE);
			System.exit(0);
		}

		
		
		long startTime = System.currentTimeMillis();
		boolean b = copyFile(source_file, target_file, bufferSize, overwrite);
		//if copyFile succeed, calculate the runing time. else - exit;
		if (b){
			long endTime = System.currentTimeMillis();
			System.out.println("Buffer size:" + bufferSize + ", Total time:" + (endTime - startTime));
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
	 * @throws IOException 
	*/
	public static boolean copyFile(String srcFileName,	String toFileName,	int bufferSize,	boolean bOverwrite) throws IOException {
		FileReader in = null;
		File outputFile =new File(toFileName);
		FileWriter out=null;
		
		if (outputFile.exists() && !outputFile.isDirectory() && !bOverwrite) {
			// throw new IllegalArgumentException(toFileName +
			// " already exists. You should use [/force] option, or choose another target file");
			System.out
					.println("\nError:\n"
							+ toFileName
							+ " already exists. You should use [/force] option, or choose another target file\n"
							+ USAGE);
			return false;
		} else {
			outputFile.createNewFile();
		}
		
		int len;
		int offset = 0;
		try {
			in = new FileReader(new File(srcFileName)); //Will be used to read from a file.
			out = new FileWriter(outputFile); //Will be used to write into a file.
			char[] cbuff = new char[bufferSize]; //buffer size, as suppiled in "buffer" parameter.
			/*
			 * While there is data to read, read it, and write out into {out}.
			 */
			while ((len = in.read(cbuff, offset , bufferSize)) > 0) {
				out.write(cbuff, 0, len);
				out.flush();
			}	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		finally{
			//Close FileReader & FileWriter.
			in.close();
			out.close();
		}

	return true;
	}
}
