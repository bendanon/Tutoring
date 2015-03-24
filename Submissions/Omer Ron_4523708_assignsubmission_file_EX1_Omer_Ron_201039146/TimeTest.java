/*
 * ex1 
 * Omer Ron
 * 201039146
 */

package TimeTest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.FileAlreadyExistsException;


public class TimeTest {

	//Messages Constants
	private static final String USAGE = "Usage: [/force] source_file target_file buffer_size";
	
	public static void main(String[] args) {
		String srcFileName;
		String toFileName;
		int bufferSize;
		int count = 0;
		boolean bOverwrite =false;
		boolean sucess;
		
		try {
			//Check arguments
			if (args.length >4 || args.length < 3){
				throw new Exception();
			}
			
			if (args[count].equals("/force")){
				bOverwrite = true;
				count ++;
			}
			
			srcFileName = args[count];
			toFileName = args[count+1];
			bufferSize = Integer.parseInt(args[count+2]);
			
		} catch (Exception e) {
			System.out.println(USAGE);
			return;
		}
		
		//copy file
		long startTime = System.currentTimeMillis();
		//copy logic
		sucess = copyFile(srcFileName,toFileName,bufferSize,bOverwrite);
		long endTime = System.currentTimeMillis();
		if (sucess){
			System.out.println("Total time: "+(endTime-startTime)+" ms");
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
	public static boolean copyFile(String srcFileName,String toFileName,int bufferSize,	boolean bOverwrite) {
	
		File f = null;
		FileReader fileReader = null;
		FileWriter fileWriter = null;
		BufferedReader inputStream = null;
		BufferedWriter outputStream = null;
	     
	    try{
 	
	    	f = new File(toFileName);
	    	if(f.exists() && !f.isDirectory() && bOverwrite == false) {
	    		throw new FileAlreadyExistsException("file exist and force flag is off");
	    	}
	    	f.createNewFile();
	    	
	         //open streams for reading and writing.
	    	 fileReader = new FileReader(srcFileName);
	    	 fileWriter = new FileWriter(toFileName);

	         // change to buffered  stream
	    	 inputStream = new BufferedReader(fileReader);			
	    	 outputStream = new BufferedWriter(fileWriter);
	    	 
	    	 char[] buffer=new char[bufferSize];
	    	 // read if available
	         while((inputStream.read(buffer, 0, bufferSize)) != -1)
	         {
	             //read and write the buffer
	        	 outputStream.write(buffer, 0, buffer.length);
	        	 //clean buffer
	        	 buffer=new char[bufferSize];
	         }
	         
	     }catch(Exception e){
	    	  System.out.print("Error copy file, Error: "+e.getMessage());
	    	  return false;
	     }finally{
	    	 //close streams
	         if(fileReader!=null)
	        	 try {fileReader.close();} catch (Exception e2) {}
	        	 
	         if(fileWriter!=null)
	        	 try {fileWriter.close();} catch (Exception e2) {}
	        	
	         if(inputStream!=null)
	        	 try {inputStream.close();} catch (Exception e2) {}
	        	 
	         if(outputStream!=null)
	        	 try {outputStream.close();} catch (Exception e2) {}
	      }

		return true;
	}
}
