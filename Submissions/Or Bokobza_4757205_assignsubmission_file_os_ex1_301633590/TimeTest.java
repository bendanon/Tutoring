//Or Bokobza. ID 301633590


import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;


public class TimeTest {
	
	private static final String usage = "usage: java TimeTest [/force] source_file target_file buffer_size";
	private static final String totalTime = "The total time is:";
	private static final String badValue = "bad value to the buffer size";
	private static final String force = "/force";
	private static final String cantOverWrite = "Can't over write on the file because it already exists.";
	private static final String hasDirectory = "There is a directory with the current destination";
	private static final String IOException = "There is an IO exeption";
	private static final String notFound = "The file could not found";
	private static final String closeException = "We have a problem to close the file";
	
	
	public static void main(String[] args) {
		
		int place = 0;
		boolean flag = args[0].equals(force);
		int bufferSize;	
		if (args.length < 3 || args.length > 4 /*|| (flag && (args.length == 4))*/) {
			System.out.println(usage);
			return;
		}
		if (flag){
			place++;
		}
		String src = args[place++];
		String dest = args[place++];
		String size = args[place];
		try {
				bufferSize = Integer.parseInt(size);
				}
				catch (NumberFormatException e) {
					 bufferSize = -1;
				}		
				
		if (bufferSize <= 0) {
					System.out.println(badValue);
					return;
		}
		// In this part we are making the copy and print a message if there in any problem in the process.
		long startTime = System.currentTimeMillis();
		if (copyFile(src, dest, bufferSize, flag)) {
			long endTime = System.currentTimeMillis();
			System.out.println(totalTime);
			System.out.println(endTime - startTime);
		}
	}
	
	

	public static boolean copyFile(String src, String dest, int bufferSize, boolean flag) {
		
		if (bufferSize < 0) {
			System.out.println(badValue);
			return false;
		}
		// If the destination directory is exists - we failed
		File destFile = new File(dest);
		if (destFile.isDirectory()) {
			System.out.println(hasDirectory);					
			return false;
		}
		if (!flag && destFile.exists()) {
		System.out.println(cantOverWrite);					
		return false;
		}
		
		RandomAccessFile fileToRead = null, fileToWrite = null;
		try {
			fileToWrite = new RandomAccessFile(dest,"rw");
			fileToRead = new RandomAccessFile(src,"r");
			int length;
			byte[] buffer = new byte[bufferSize];
			while ((length = fileToRead.read(buffer, 0, bufferSize)) > 0) {
				fileToWrite.write(buffer, 0, length);
			}
			return true;
		}
		
		catch (FileNotFoundException e) {
			System.out.println(notFound);
		}
		catch (IOException e) {
			System.out.println(IOException);
		}
		finally {
			try {
				if (fileToRead != null)
					fileToRead.close();
				if (fileToWrite != null)
					fileToWrite.close();
				}
			catch (Exception e) {
				System.out.println(closeException);
			}
		}
		return false;	
	}
}
