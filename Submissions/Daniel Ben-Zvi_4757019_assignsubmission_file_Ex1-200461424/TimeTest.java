import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*  Receives source file path, target file path, and buffer size
 	as command line arguments.
 	Adding the /force argument ('/verbose') forces overwriting 
 	of an existing file.
 	
 	Print out the time it took to copy the file.
*/
public class TimeTest {
	
	public static void main(String[] args) throws IOException {
		boolean copySuccessfull = false;
		String srcName, outName;
		int buff;
		boolean booOverwrite;
		
		// Parse and check arguments
		if (args[0].equals("/verbose")){ //In case we are given the /force argument.
			srcName = args[1];
			outName = args[2];
			 buff = Integer.parseInt(args[3]);
			 booOverwrite = true;
		}else{ // No /force argument given.
			srcName = args[0];
			outName = args[1];
			buff = Integer.parseInt(args[2]);
			booOverwrite = false;
			
			// Check if file already exists.
			 File f = new File(outName);
				if(f.exists() && !f.isDirectory()){
					System.out.println("Error: File already exists!");
					return;
				}
		}		
		
		// measure start time
		long startTime = System.currentTimeMillis();
		
		//Copy file
		copySuccessfull = copyFile(srcName, outName, buff, booOverwrite);
		
		// measure end time
		long endTime = System.currentTimeMillis();
		
		// Print results
		if(copySuccessfull){
			System.out.printf("File %s was copied to %s\n", srcName, outName);
			System.out.printf("Total time: %dms\n",(endTime - startTime));
		}else{
			System.out.println("Copy unsuccessfull");
		}
		
		return;
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
	public static boolean copyFile(String srcFileName,
			String toFileName,
			int bufferSize,
			boolean bOverwrite) throws IOException{
		
		FileReader inputStream = null;
		FileWriter outputStream = null;
		int len;
		try{
			// create I/O streams and char array of the given buffer size.
			inputStream = new FileReader(srcFileName);
			outputStream = new FileWriter(toFileName);
			char[] buff = new char[bufferSize];
			
			// read from inputStream and write into file using outputStream.
			while ((len = inputStream.read(buff, 0, bufferSize)) > 0){
				outputStream.write(buff);
			}
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
			return false;
		} finally {
			
			// Close streams
			try { 
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				System.err.println("Error closing input stream");
				return false;
			}
			try { 
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				System.err.println("Error closing output stream");
				return false;
			}
		}
		return true;
	}
}



