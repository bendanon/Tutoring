// ID: 305583569
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class TimeTest {
	// int Constatnts
	private static final int PARAM_COUNT_VERBOSE = 3;
	private static final int PARAM_COUNT_FORCE = 4;
	private static final int OVERWRITE_INDEX = 0;
	private static final int OVERWRITE_ERROR_CODE = 10;
	private static final int SRC_INDEX = OVERWRITE_INDEX + 1;
	private static final int SRC_ERROR_CODE = 20;
	private static final int TARGET_INDEX = SRC_INDEX + 1;
	private static final int TARGET_ERROR_CODE = 30;
	private static final int BUFFER_INDEX = TARGET_INDEX + 1;
	private static final int BUFFER_ERROR_CODE = 40;
	// regex string constatnt	
	private static final String VALID_PATH_REGEX = "^(?:[\\w]\\:|\\\\)(\\\\[^\"<>\\|\\.]+)+\\.(\\w+)$";
	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [<optional> /force] [src ath] [target path] [buffer]";
	private static final String OVERWRITE = "Copy failed because file already exists - try using /force"; 
	private static final String	ACCESS = "failed to access src/target files";
	private static final String	IO = "failed to read src file / write target file";
	
	
	public static void main(String[] args) {
		boolean ind_overwrite = false;
		// ind_overwrite_index_change used as offset on access to args index to handle both 3 params case and 4 params case. 
		byte ind_overwrite_index_change = 0;
		int buffer = 0;
		// Check arguments		
		// check overwrite arg
		if(args.length == PARAM_COUNT_VERBOSE) {
			ind_overwrite = false;
			ind_overwrite_index_change = 1;
		} else if(args.length == PARAM_COUNT_FORCE && args[OVERWRITE_INDEX].equalsIgnoreCase("/force")) {
			ind_overwrite = true;			
		} else {
			System.out.println(USAGE);
            System.out.println("1");
			System.exit(OVERWRITE_ERROR_CODE);
		}
		// check buffer arg
		try { 
			buffer = Integer.parseInt(args[BUFFER_INDEX-ind_overwrite_index_change]);
		} catch(NumberFormatException e) {
			System.out.println(USAGE);
            System.out.println("2");			
			System.exit(BUFFER_ERROR_CODE);
		}		
		
		// check src path
		/*if(!args[SRC_INDEX-ind_overwrite_index_change].matches(VALID_PATH_REGEX)) {
			System.out.println(USAGE);			
            System.out.println("3");
			System.exit(SRC_ERROR_CODE);
		}*/
		// check target path
		/*if(!args[TARGET_INDEX-ind_overwrite_index_change].matches(VALID_PATH_REGEX)) {
			System.out.println(USAGE);
            System.out.println("4");
			System.exit(TARGET_ERROR_CODE);
		}*/
		
		// copy file
		long startTime = System.currentTimeMillis();
		// copy logic
		boolean ind_copy_result = copyFile(args[SRC_INDEX-ind_overwrite_index_change], args[TARGET_INDEX-ind_overwrite_index_change], buffer, ind_overwrite);
		long endTime = System.currentTimeMillis();
		if(ind_copy_result) {
			System.out.println("Total time: " + (endTime-startTime) + "ms");
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
		InputStream srcInputStream = null;
	    OutputStream targetOutputStream = null;
	    File targetFile = null;	    
	    
	    // create target file variable
	    try {
	    	targetFile = new File(toFileName);
	    } catch (NullPointerException e) {
	    	System.out.println(USAGE);
			System.exit(TARGET_ERROR_CODE);
	    }	    	    	 
	    
	    if(targetFile.exists()) {
	    	if(bOverwrite) {
	    		// perform overwrite by deleting the file before re-writing to it.
		    	targetFile.delete();
	    	} else {
	    		// need to overwrite but not allowed.
	    		System.out.println(OVERWRITE);
		    	return false;
	    	}	
	    }
	    
	    // try to create the target file
	    try {
	    	targetFile.createNewFile();
	    } catch (IOException e) {
	    	System.out.println(ACCESS);
	    	return false;
	    }
	    
	    // perform the copy
	    try {
	    	// open the io streams
	    	srcInputStream = new FileInputStream(srcFileName);
	    	targetOutputStream = new FileOutputStream(toFileName);
	        byte[] buffer = new byte[bufferSize];
	        int length;
	        // copy the data
	        while ((length = srcInputStream.read(buffer)) > 0) {
	        	targetOutputStream.write(buffer, 0, length);
	        }	             
	    } catch (FileNotFoundException e_FileNotFoundException) {
	    	// src or target file doesnt exist (in theory, should only get to here when src file doesnt exist since target file is created by the program)
	    	System.out.println(ACCESS);	    	
	    	return false;
	    } catch (IOException e_IOException) {
	    	// error in reading the src file or writing to the target file
	    	System.out.println(IO);
	    	return false;
	    } finally {
	    	try {
	    		// finaly close the io streams
	    		srcInputStream.close();
		        targetOutputStream.close();
	    	} catch (NullPointerException e_NullPointerException) {	    			    		
		    } catch (IOException e_IOException) {	    			    		
		    }	    	
	    }
	    return true;
	}
	
}


