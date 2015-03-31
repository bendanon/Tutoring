import java.io.File;
import java.io.FilenameFilter;

/**
 * A searcher thread. Searches for files with a given extension in all directories
 * listed in a directory queue.
 * 
 * @author Yotam Harchol
 */
public class Searcher implements Runnable {

	private String extension;
	private SynchronizedQueue<File> directoryQueue;
	private SynchronizedQueue<File> resultsQueue;
	
	/**
	 * Constructor. Initializes the searcher thread and registers it as a producer on the results queue.
	 * 
	 * @param extension Extension to look for
	 * @param directoryQueue A queue with directories to search in (as listed by the scouter)
	 * @param resultsQueue A queue for files found (to be zipped by a zipper)
	 */
	public Searcher(String extension, SynchronizedQueue<File> directoryQueue, SynchronizedQueue<File> resultsQueue) {
		this.extension = extension;
		this.directoryQueue = directoryQueue;
		this.resultsQueue = resultsQueue;
		this.resultsQueue.registerProducer();
	}
	
	/**
	 * Runs the searcher thread. Thread will fetch a directory to search in from the
	 * directory queue, then filter all files inside it (but will not recursively
	 * search subdirectories!). Files that are found to have the given extension
	 * are enqueued to the results queue.
	 * When finishes, this method unregisters from the results queue.
	 */
	@Override
	public void run() {
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith("." + extension);
			}
		};
		
		while (true) {
			File dir = directoryQueue.dequeue();
			if (dir == null) {
				// No more dirs to search
				resultsQueue.unregisterProducer();
				return;
			}
			
			File[] files = dir.listFiles(filter);
			
			if (files == null)
				continue;
			
			for (File result : files) {
				resultsQueue.enqueue(result);
			}
		}
	}
}
