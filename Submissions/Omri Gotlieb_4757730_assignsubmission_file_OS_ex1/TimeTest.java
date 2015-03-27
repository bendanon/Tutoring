// Id: 302671136

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class TimeTest {
// Messages Constants
private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";

public static void main(String[] args) throws IOException {
	//if the args is not as the USAGE description throws exception. 
	if ((args.length > 6) /*|| (args.length < 5)*/ ){
		System.err.println(USAGE);
		System.exit(1);
	}
	// Initiates variables 
	String source_file = null;
	String target_file = null;
	int buffer_size = 0;
	long total_time = 0;
	boolean bOverwrite = false;
	
	// if args.length equals to 5 there is no force flag.
	if (args.length == 5){
	 source_file = args[2];
	 target_file = args[3];
     buffer_size = Integer.parseInt(args[4]);
	}
	// there is a force flag
	if (args.length == 6){
		// Checks if the flag is /force
		if(!args[2].equals("/force")){
			System.err.println("Arguments are not valid");
			System.exit(1);
		}
		// flag force is true.
		 bOverwrite = true;
		 // set the args into variables
		 source_file = args[3];
		 target_file = args[4];
	     buffer_size = Integer.parseInt(args[5]);
	}
	// checks if the size is nagative.
	if (buffer_size < 0) {
		System.err.println("Size can`t be negative");
		System.exit(1);
	}
	
 // copy file:
long startTime = System.currentTimeMillis();
 // copy logic
boolean isCopied = copyFile(source_file,target_file, buffer_size,bOverwrite);
// file did not copy.
if(!isCopied){
	System.err.println("Could not copy");
	System.exit(1);
}

 long endTime = System.currentTimeMillis();
// output that the file copied to the target
System.out.println("File "  + source_file +  " was copied to " +  target_file);
//calculate the total time
total_time = (endTime - startTime);
System.out.println("Total time: " + total_time + "ms");
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
public static boolean copyFile(String srcFileName,
 String toFileName,
 int bufferSize,
 boolean bOverwrite) throws IOException {
	
	File target = new File(toFileName);
	if (target.exists() && !bOverwrite && !target.isDirectory()) {
		return false;
	}

    BufferedReader inputStream = null;
    PrintWriter outputStream = null;

    try {
    	inputStream = new BufferedReader(new FileReader(srcFileName), bufferSize);
		outputStream = new PrintWriter(new FileWriter(toFileName));

        String l;
        while ((l = inputStream.readLine()) != null) {
            outputStream.println(l);
        }
    } catch (Exception e) {
    	if (inputStream != null) {
			inputStream.close();
		}
		if (outputStream != null) {
			outputStream.close();
		}
		return false;
    	
    } finally {
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }
    return true;
}
}
