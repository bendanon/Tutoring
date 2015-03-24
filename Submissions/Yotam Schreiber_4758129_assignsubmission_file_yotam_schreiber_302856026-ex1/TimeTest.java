/*
 * Name: yotam schreiber
 * I.D: 302856026
 */

//package maarachot;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class TimeTest {

	//help function for checking if string is a number
	private static int checkIfInteger(String s) {
		int res = 0;
		for (int i=0; i < s.length(); i++) {
		    char c = s.charAt(i);
		    if (c < '0' || c > '9') {
		    	//if char is not a number return -1
		    	return -1;
		    }
		    res = res * 10 + (c - '0');
		}
		return res;
	}
	
	//help function for checking if string is "true" or "false"
	private static boolean checkBoolean(String s) {
		if(s.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}
	
	
	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	private static final String VERBOS = "/force";
	
	public static void main(String[] args) throws IOException {
	
	// Check arguments
	if(false/*args.length != 4 || (!(args[0].equals(VERBOS)))*/) {
		
		System.out.println(USAGE);
		
	} else {
		
		//get the arguments
		String srcFileName = args[1];
		String toFileName = args[2];
		int bufferSize = checkIfInteger(args[3]);
		boolean bOverwrite = checkBoolean(args[0]);
		
		//if we got a negative size print error
		if(bufferSize <= 0) {
			System.out.println("error: impossible buffer size");
			return;
		}
		
		// copy file
		long startTime = System.currentTimeMillis();
		if(copyFile(srcFileName, toFileName, bufferSize, bOverwrite)) {
			long endTime = System.currentTimeMillis();
			System.out.println("File " + srcFileName + " was copied to " + toFileName);
			System.out.println("Total Time: " + (endTime - startTime) + "ms");
			}
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
	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) throws IOException {
				
        FileReader inputStream = null;
        FileWriter outputStream = null;
        
        try {
        	
			inputStream = new FileReader(srcFileName);
	        outputStream = new FileWriter(toFileName);
	        
	        char[] c = new char[bufferSize];
	        int i;
	        //do the reading
	        while ((i = inputStream.read(c, 0, bufferSize)) > 0) {
                outputStream.write(c, 0, bufferSize);
            }
	        
	        return true;
	        
	      //handle Exceptions
        } catch (FileNotFoundException e) {
        	System.out.println("File or path was not found");
        	
        } catch (IOException e) {
        	System.out.println("the system got I/O problem");
		}
        
        finally {
        	
        	try {
        		
        		if(inputStream != null) {
            		inputStream.close();
            	}
            	if(outputStream != null) {
            		outputStream.close();
            	}
        	} catch (Exception e) {
        		System.out.println("something went worng with closing the streams");
        	}
        	
        }
        return false;
   	}
}
