import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//ariel verber
//304952047

public class TimeTest {
	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";  
	public static void main(String[] args) {
		String fromFile = null, destFile = null;
		int buffer = 0;
		// Check arguments
		// check if the arguments number is 4 or 3. otherwise don't operate
		if(args.length == 4){
			//check buffer size

			try{
				buffer = Integer.parseInt(args[3]);
			}
			//throw error if buffer size is not valid
			catch(NumberFormatException e){
				System.out.println("bad buffer size");
				return;
			}
			//grab the file paths
			fromFile = args[1];
			destFile = args[2];

			//check if the file needed to be copied exists
			if(!FileExists(fromFile))
				System.out.println("Error. source_file not file");

			//make sure the first word is /force. otherwise the command is bad
			if(!args[0].equals("/force")){
				System.out.println(USAGE);
				return;
			}
			//check if the file in the address is valid to write upon
			if(!canWrite(new File(args[1])))
				System.out.println("Can't write into target_file.");
		}
		//if there's no force command
		else if(args.length == 3){
			//check if the buffer size is good

			try{
				buffer = Integer.parseInt(args[2]);
			}
			//if it's not good, throw an error
			catch(NumberFormatException e){
				System.out.println("bad buffer size");
				return;
			}
			//grab the file names
			fromFile = args[0];
			destFile = args[1];
			//make sure source file exists
			if(!FileExists(fromFile))
				System.out.println("Error. source_file not file");
			//make sure no destination file exists - overwrite is set to no.
			if(FileExists(destFile))
				System.out.println("Error. target_file already exists. add /force to override");

		}
		//if too many / too little arguments, throw error.
		else{
			System.out.println(USAGE);
			return;
		}
		// keep times
		long startTime = System.currentTimeMillis();
		//try to copy
		boolean didCopy = copyFile(fromFile, destFile, buffer, true) ;
		long endTime = System.currentTimeMillis();
		//if copied, write how much time it took.
		if(didCopy){
			System.out.println("Total Time: " +(endTime - startTime) +"ms");
		}
		else{
			System.out.println("An error occured while trying to copy the file.");
		}
	}  

	/**
	 * given a file, the program checks if it's possible to write over this file
	 * The checks performed: if the file exists, return its permissions.
	 * If the file does not exist, try to create a new file there and delete it.
	 * If anything failed, return false.
	 * @param file
	 * @return
	 */
	private static boolean canWrite(File file) {
		//if the file exists, return true if it can be written upon.
		if (file.exists()) {
			return file.canWrite();
		}
		else {
			//if it doesn't, test if a file can be created over it
			try {
				file.createNewFile();
				file.delete();
				return true;
			}
			catch (Exception e) {
				return false;
			}
		}
	}

	/**
	 * given a path, the function checks if it exists and it's not a directory.
	 * @param filePathString
	 * @return
	 */
	private static boolean FileExists(String filePathString) {
		File f = new File(filePathString);
		if(f.exists() && !f.isDirectory()){
			return true;
		}
		return false;
	}

	/**  
	 * * Copies a file to a specific path, using the specified buffer size.  *
	 * 
	 * * @param srcFileName File to copy  * @param toFileName Destination file name  
	 * * @param bufferSize Buffer size in bytes  
	 * * @param bOverwrite If file already exists, overwrite it  
	 * * @return true when copy succeeds, false otherwise  */ 

	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {  
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try{
			//try to create new input and output streams
			inputStream = new FileInputStream(srcFileName);
			outputStream = new FileOutputStream(toFileName);
			//keep a buffer array
			byte[] buffer = new byte[bufferSize];
			//as long as the input reads 'buffer' bytes
			while(inputStream.read(buffer) > 0)
				//write them into output
				outputStream.write(buffer);
		}
		catch(Exception e){
			//if any exception occured, exit and return false
			System.out.println("An error occured. check your parameters/files and try again.");
			return false;
		}
		finally{
			//close input and output streams
			if (inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					System.out.println("failed to close input stream");
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					System.out.println("failed to close output stream");
				}
			}
		}
		//if everything went well, return true.
		return true;
	}
}
