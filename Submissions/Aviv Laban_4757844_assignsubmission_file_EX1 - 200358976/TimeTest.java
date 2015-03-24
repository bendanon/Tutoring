//ID: 200358976
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;


public class TimeTest {

	// Messages Constants
	private static final String USAGE = "Usage: ...";
	public static void main(String[] args) { // Check arguments
		
		String source = "";
		String destenation = "";
		Boolean toOverwrite = false;
		int length = 0;
		
		if(args.length < 3){
			System.out.print(USAGE);
			System.exit(0);
		}else if ((args[0] == "//force") || args.length == 4){
			toOverwrite = true;
			source = args[1];
			destenation = args[2];
			length = Integer.parseInt(args[3]);
		}else if (args.length == 3){
			source = args[0];
			destenation = args[1];
			length = Integer.parseInt(args[2]);
		}else {
			System.out.print(USAGE);
			System.exit(0);
		}
		if(length < 0){
			System.err.println("Error: wrong length!");
		}
		
		
		
	
	// copy file
	long startTime = System.currentTimeMillis();
	// copy logic
	 

	boolean check = copyFile(source, destenation, length, toOverwrite);
	if(check){
		long endTime = System.currentTimeMillis();
		System.out.println("The amount of Time: " + (endTime - startTime) + "ms");
	}
}
	/**
	* Copies a file to a specific path, using the specified buffer size. *
	* @param srcFileName File to copy
	* @param toFileName Destination file name
	* @param bufferSize Buffer size in bytes
	* @param bOverwrite If file already exists, overwrite it
	* @return true when copy succeeds, false otherwise */
	public static boolean copyFile(String srcFileName, String toFileName,
	int bufferSize, boolean bOverwrite) {
		if((srcFileName.isEmpty()) || (toFileName.isEmpty())){
			System.out.println("Error : Invalid Path(1)");
		}
		
		File sourceF = new File(srcFileName);
		File destenationF = new File (toFileName);
		if (bOverwrite){
			if(destenationF.isDirectory() || sourceF.isDirectory() || !sourceF.exists()){
				System.out.println("Error : Invalid Path(2)");
				return false;
			}
		}else{
			if(!destenationF.isDirectory() || !sourceF.isDirectory() || sourceF.exists()){
				if(destenationF.exists()){
					System.out.println("Error : file already exists");
					return false;
				}
			}else{
				System.out.println("Error : Invalid Path(3)");
				return false;
			}
		}
		InputStream in = null;
		OutputStream out = null;
		
		try{
			byte [] len = new byte[bufferSize];
			in = new FileInputStream(sourceF);
			out = new FileOutputStream(destenationF);
			int c;
			while ((c = in.read(len, 0, bufferSize)) != -1){
				out.write(len, 0, c);
				out.flush();
				
			}
		}catch(IOException e){
			return false;
		}finally{
			try{
				if(in != null){
					in.close();
				}
				if(out != null){
					out.close();
				}
			}catch(IOException e){
				e.printStackTrace(System.out);
			}
		}
		return true;
	}
	
}
