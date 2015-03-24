//Ben Levy 029977386

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class TimeTest {

	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	public static void main(String[] args) {
	   if (args.length < 3 || args.length > 4  ){
		  System.out.print(USAGE);
		  return;
	   }
	   
	   //Save the parameters in  local variables
	   boolean bOverwrite = (args[0].equals("/force")) ? true:false;
	   String srcFileName = (bOverwrite) ? args[1]: args[0];
	   String toFileName = (bOverwrite) ? args[2]: args[1];
	   Integer bufferSize = (bOverwrite) ? Integer.parseInt(args[3]): Integer.parseInt(args[2]);
	   
	//Start measuring   
	long startTime = System.currentTimeMillis();
	try {
		if (copyFile(srcFileName, toFileName, bufferSize.intValue(),bOverwrite)){
			
			//The end of the measure
			long endTime = System.currentTimeMillis();
			System.out.print( "File " +  srcFileName + " was copied to " + toFileName  + "\nTotal time: "
			                          + (endTime - startTime) + "ms");
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
			
	
	}
	
	/**
	* Copies a file to a specific path, using the specified buffer size. *
	* @param srcFileName File to copy
	* @param toFileName Destination file name
	* @param bufferSize Buffer size in bytes
	* @param bOverwrite If file already exists, overwrite it
	* @return true when copy succeeds, false otherwise 
	 * @throws IOException */
	public static boolean copyFile(String srcFileName, String toFileName,int bufferSize, boolean bOverwrite) throws IOException {
		
		File src = new File (srcFileName);
		File dest =  new File (toFileName);
		char [] buffer = new char [bufferSize] ;
		
		//If the there is a destination file and  the word /force not part of the parameters
		if(bOverwrite == false && dest.exists()){
			System.out.print("The file " + toFileName +" can't be overwritten, unless specified");
			return false;
		}
		
		//If the source file doesn't exists
		if(!src.exists()){
			System.out.print("The file " + srcFileName +" doesn't exists");
			return false;
		}
		
		//Creates the pipes 
		FileReader reader = new FileReader(src);
		FileWriter writer = new FileWriter(dest);
		try{
			
		//	The copying process
	    while (reader.read(buffer,0, bufferSize) != -1){
	    	writer.write(buffer, 0, bufferSize);
	    	buffer = new char [bufferSize];
	    	
	    }
		}catch(IOException e){
			System.err.println("Error: " + e.getMessage());
		}
	    finally {
	    	
		try {
		   if (reader != null) {
		       reader.close();
		   }if (writer != null) {
			  writer.close();
		} 
		}catch (IOException e) {
		     System.err.println("Error closing stream");
		}
		
	    }
		return true;

	}
}
