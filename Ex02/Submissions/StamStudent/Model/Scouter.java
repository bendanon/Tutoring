package Model;
import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A scouter thread
 * This thread lists all sub-directories from a given root path. Each sub-directory is
 * enqueued to be searched for files by Searcher threads.
 *  
 * @author Yotam Harchol
 */
public class Scouter implements Runnable {

	private SynchronizedQueue<File> directoryQueue;
	private File root;

	/**
	 * Construnctor. Initializes the scouter with a queue for the directories to
	 * be searched and a root directory to start from.
	 * This method also registers the Scouter as a producer on the directory queue.
	 * 
	 * @param directoryQueue A queue for directories to be searched
	 * @param root Root directory to start from
	 */
	public Scouter(SynchronizedQueue<File> directoryQueue, File root) {
		this.directoryQueue = directoryQueue;
		this.root = root;
		this.directoryQueue.registerProducer();
	}
	
	/**
	 * Starts the scouter thread. Lists directories under root directory and adds
	 * them to queue, then lists directories in the next level and enqueues them
	 * and so on.
	 * When finishes, this method unregisters from the directory queue.
	 */
	@Override
	public void run() {
		Queue<File> dirs = new LinkedList<File>();
		dirs.add(root);
		
		while (!dirs.isEmpty()) {
			File dir = dirs.poll();
			
			directoryQueue.enqueue(dir);
			File[] subdirs = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File arg0) {
					return arg0.isDirectory();
				}
			});
			
			if (subdirs == null)
				continue;
			
			for (File subdir : subdirs) {
				dirs.add(subdir);
			}
		}
		directoryQueue.unregisterProducer();
	}

}
