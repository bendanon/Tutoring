package Model;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


/*A copier thread. 
 * Reads files to copy from a queue 
 * and copies them to the given destination.
 * 
 */

public class Copier implements Runnable {
	/*
	 * Size of buffer used for a single file copy process

	 */
	public static final int	COPY_BUFFER_SIZE = 4096;
	private java.io.File destination;
	private SynchronizedQueue<java.io.File> resultsQueue;
	
	/*
	 * Constructor. 
	 * Initializes the worker with a destination directory and a queue of files to copy.
	 * Parameters:
	 * destination - Destination directory
	 * resultsQueue - Queue of files found, to be copied.
	 */
	public Copier(java.io.File destination,
            SynchronizedQueue<java.io.File> resultsQueue){
		this.destination = destination;
		this.resultsQueue = resultsQueue;
		
	}
	
	
	/*
	 * Runs the copier thread. Thread will fetch files from queue and copy them, one after 
	 * each other, to the destination directory. When the queue has no more files, the thread
	 * finishes.
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		byte[] buffer = new byte[COPY_BUFFER_SIZE];
		String path = destination.getAbsolutePath();
		File file;
		while ((file = resultsQueue.dequeue()) != null){
			File fileOutput = new File(path + File.separator + file.getName());
			FileInputStream inputStream = null;
			FileOutputStream outputStream = null;
			int len;
        
     
			try {
				inputStream = new FileInputStream(file);
				outputStream = new FileOutputStream(fileOutput);
            
				//Read from src file and write to destination file.
				while ((len = inputStream.read(buffer, 0, COPY_BUFFER_SIZE)) != -1) {
					outputStream.write(buffer,0,len);
				}
			}
			catch (Exception e) {
				System.err.println("File not created, " + e.getMessage());
			}
        
			//Close files.
			try{
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
 
			} catch (Exception e){
       
			}		           
         } 
	}

		
		
}


