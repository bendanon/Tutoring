/**
 * Eyal shoham ID 036936037
 */

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTest {

	public static void main(String[] args)  {
		String file1, file2;
		int bufSize=0;
		boolean overW, isCopy;
			if (args.length==3) {		// checks if the command line argument contain 3 parameters
				file1= args[0];					// the source file path
				file2= args[1];					// the target file path
				overW=false;					// force flag is false
				try {
					bufSize= Integer.parseInt(args[2]);		// buffer size in bytes
				}
				catch (NumberFormatException e) {
					System.err.println("Invalid buffer size");
					return;
				}
			}
			else if (args.length==4) {	// checks if the command line argument contain 4 parameters
				if (args[0].equals("/force")) {	// if the args[0] if '/force' (force flag is true) overwrite the target file
					overW=true;
				}
				else {
					System.err.println("Illegal Argument"); 
					return;
				}
				file1= args[1];					// the source file path
				file2 =args[2];					// the target file path
				try {
					bufSize= Integer.parseInt(args[3]);	// buffer size in bytes
				}
				catch (NumberFormatException e) {
					System.err.println("Invalid buffer size");
					return;
				}
			}
			else {
				System.err.println("Illegal Argument"); 
				return;
			}
		long startTime = System.currentTimeMillis();	
		isCopy= copyFile(file1, file2, bufSize, overW);
		long endTime = System.currentTimeMillis();
		if (isCopy) {
		System.out.println("File "+ file1+ " was copied to "+file2);
		System.out.println("Total Time "+(endTime - startTime)+ "ms");
		}
		else {
			System.err.println("file wasn't copied");
		}
	}

	
	/** * Copies a file to a specific path, using the specified buffer size. * 
	 * * @param srcFileName File to copy * @param toFileName Destination file name 
	 * * @param bufferSize Buffer size in bytes 
	 * * @param bOverwrite If file already exists, overwrite it *
	 *  @return true when copy succeeds, false otherwise */ 
	public static boolean copyFile(String srcFileName,
			String toFileName, int bufferSize, boolean bOverwrite) {
		int offset =0;
		boolean success=false;					// if the file was copied success= true
		char[] cbuf= new char[bufferSize];		// char buffer to read and write from
		FileReader inputStream = null;
        FileWriter outputStream = null;
        File outFile = new File(toFileName);
        if (!outFile.exists() && !bOverwrite) {	// create new file if a target file doesn't exist and the force flag is false
        	try {
        			outFile.createNewFile();
        	}
        	catch (IOException e) {
        		System.err.println("file already exist");
        		return false;
        	}
        }
        else if(outFile.exists() && !bOverwrite) {	// if the file exist but the force flag is false 
        	System.err.println("file already exist, /force is required to overwrite the targt file ");
        	return false;
        }

        try {
            inputStream = new FileReader(srcFileName);	// create new file reader with the source file
            outputStream = new FileWriter(toFileName);	// create new file writer with the target file
            int c;
            while ((c = inputStream.read(cbuf, offset, bufferSize)) != -1) {	// copy the file 
            		outputStream.write(cbuf,offset, c);
            }
            success=true;								// file was copied successfully 

        } 
        catch (IOException e) {
        	System.err.println("unable to read or write, path of the source file or target file is illegal or source file doesn't exist");
        	return false;
        }
        finally {				// close the inputStream and the outputStream
            try {
            	if (inputStream != null) {
            		inputStream.close();
            	}
            	if (outputStream != null) {
            		outputStream.close();
            	}
            	}
            catch (IOException e) {
            	System.err.println("Unable to close outputStream/ inputStream");
            }
        }
        return success;

	}

}
