import java.io.File;


/**
 * Main application class.
 * This application searches for all files under some given path that contain
 * a given extension. All files found are copied to some specific folder.
 * 
 * @author Yotam Harchol
 */
public class DiskSearcher {
	
	private static final String USAGE = "Usage: java DiskSearcher <extension> <root> <destination folder> <searchers> <copiers>";
	private static final int NUM_ARGS = 5;
	private static final int ARG_IDX_PATTERN = 0;
	private static final int ARG_IDX_ROOT = 1;
	private static final int ARG_IDX_DEST = 2;
	private static final int ARG_IDX_SEARCHERS = 3;
	private static final int ARG_IDX_COPIERS = 4;
	
	private static final String ERR_DEST = "Error: Cannot open/create destination folder";
	private static final String ERR_SEARCHERS = "Error: Invalid value for number of searchers";
	private static final String ERR_COPIERS = "Error: Invalid value for number of copiers";
	
	private static final String MSG_COMPLETED = "Process Completed";
	
	/**
	 * Capacity of the queue that holds the directories to be searched
	 */
	public static final int DIRECTORY_QUEUE_CAPACITY = 50;
	
	/**
	 * Capacity of the queue that holds the files found
	 */
	public static final int RESULTS_QUEUE_CAPACITY = 50;
	
	/**
	 * Main method. Reads arguments from command line and starts the search.
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != NUM_ARGS) {
			System.err.println(USAGE);
			return;
		}
		
		String pattern = args[ARG_IDX_PATTERN];
		String root = args[ARG_IDX_ROOT];
		String dest = args[ARG_IDX_DEST];
		String sSearchers = args[ARG_IDX_SEARCHERS];
		String sCopiers = args[ARG_IDX_COPIERS];
		int searchers, copiers;
		
		try {
			searchers = Integer.parseInt(sSearchers);
			if (searchers < 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			System.err.println(ERR_SEARCHERS);
			return;
		}
		try {
			copiers = Integer.parseInt(sCopiers);
			if (copiers < 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			System.err.println(ERR_COPIERS);
			return;
		}
		
		startSearch(pattern, root, dest, searchers, copiers);
	}
	
	private static void startSearch(String pattern, String root, String destination, int nSearchers, int nCopiers) {
		Searcher[] searchers = new Searcher[nSearchers];
		Copier[] copiers = new Copier[nCopiers];
		Thread[] searchThreads = new Thread[nSearchers];
		Thread[] copyThreads = new Thread[nCopiers];
		
		SynchronizedQueue<File> directoryQueue = new SynchronizedQueue<File>(DIRECTORY_QUEUE_CAPACITY);
		SynchronizedQueue<File> resultsQueue = new SynchronizedQueue<File>(RESULTS_QUEUE_CAPACITY);
	
		Scouter scouter = new Scouter(directoryQueue, new File(root));
		Thread scouterThread = new Thread(scouter);
		
		File dest = null;
		try {
			dest = new File(destination);
			if (!dest.isDirectory()){
				dest.mkdir();
			}
		} catch (Exception e) {
			System.err.println(ERR_DEST);
			System.exit(1);
		}
		
		scouterThread.start();

		for (int i = 0; i < nSearchers; i++) {
			searchers[i] = new Searcher(pattern, directoryQueue, resultsQueue);
			searchThreads[i] = new Thread(searchers[i]);
			searchThreads[i].start();
		}
		for (int i = 0; i < nCopiers; i++) {
			copiers[i] = new Copier(dest, resultsQueue);
			copyThreads[i] = new Thread(copiers[i]);
			copyThreads[i].start();
		}
	
		try {
			scouterThread.join();
		} catch (InterruptedException e) { }
		
		for (int i = 0; i < nSearchers; i++) {
			try {
				searchThreads[i].join();
			} catch (InterruptedException e) { }
		}
		for (int i = 0; i < nCopiers; i++) {
			try {
				copyThreads[i].join();
			} catch (InterruptedException e) { }
		}
		
		System.out.println(MSG_COMPLETED);
	}
}
