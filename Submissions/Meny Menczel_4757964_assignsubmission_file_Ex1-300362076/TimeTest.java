//Nir Barzilay 302650072

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class TimeTest {
	
	public static final String USAGE = "Usage: TimeTest [/force] src dest buff_size";
	
	public static void main(String[] args) {
	
		boolean force = false; //Default is false
		String src = "";
		String dst = "";
		int bufSize = 0;
		
		//Parsing args
		if (args.length == 3) {
			src = args[0];
			dst = args[1];
			bufSize = Integer.valueOf(args[2]).intValue();
		} else if (args.length == 4) {
			force = true;
			src = args[1];
			dst = args[2];
			bufSize = Integer.valueOf(args[3]).intValue();
		} else {
			System.err.print(USAGE);
			return;
		}
		
		boolean dstExists = new File(dst).exists();
    	
    	if (!dstExists || dstExists && force) {
    		long startTime = System.currentTimeMillis();
    		copyFile(src, dst, bufSize, force);
    		long endTime = System.currentTimeMillis();
    		
    		//File <SRC> was copied to <DST>
    		//Total time: <X>ms
			StringBuilder sb = new StringBuilder().append("File").append(src)
					.append(" was copied to ").append(dst)
					.append(System.lineSeparator()).append("Total time: ")
					.append((endTime - startTime)).append("ms");
					
    		System.out.println(sb.toString());
    	} else {
    		System.out.println("NO COPY: Destination file exists and /force wasn't used");
    	}
	}
	
	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {
	
		InputStream input = null;
    	OutputStream output = null;

    	try {
    		
    		//Open streams
	        input = new FileInputStream(srcFileName);
        	output = new FileOutputStream(toFileName);
        	
        	byte[] buf = new byte[bufferSize];
        	
        	//read
        	int bytesRead;
        	while ((bytesRead = input.read(buf, 0, buf.length)) > 0) {
	            output.write(buf, 0, bytesRead);
	        }
        } catch (IOException e1) {
        	e1.printStackTrace();
        	return false;
    	} finally { 
        	try {
        		input.close();
				output.close();
			} catch (IOException e2) {
				e2.printStackTrace();
				return false;
			}
    	}
    	
    	return true;
	}
}
