import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Tests {
	
   @Before
    public void setUp() {
	   	
	   	File d = new File(System.getProperty("user.dir") + "/TestData/");
   		d.mkdir();   		
   		
        for(int i = 0; i < 10; i++)
        {
        	File sd = new File(System.getProperty("user.dir") + "/TestData/" + i + "/");
        	sd.mkdir();
        	
        	if(i % 2 == 0)
        	{
        		File sd2 = new File(System.getProperty("user.dir") + "/TestData/" + i + "/" + i + "/");
            	sd2.mkdir();
            	File blafile = new File(System.getProperty("user.dir") + "/TestData/" + i + "/" + i + "/stam.pdf");
            	try {
					blafile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	else
        	{
        		Model.SynchronizedQueue<File> q = new Model.SynchronizedQueue<File>(1);
        		File f = new File(System.getProperty("user.dir") + "/books.txt");
        		q.enqueue(f);
        		Thread t = new Thread(new Model.Copier(sd, q));
        		t.start();
        		try {
					t.join();
					File created = new File(System.getProperty("user.dir") + "/TestData/" + i + "/books.txt");
					created.renameTo(new File(System.getProperty("user.dir") + 
										"/TestData/" + i + "/books_" + i + ".txt"));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}        		
        	}
        }
    }
   
   @After
   public void tearDown() {
	   try {
		Runtime.getRuntime().exec("rm -rf " +  System.getProperty("user.dir") + "/TestData/");
		Thread.sleep(200);
	} catch (IOException | InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }

	private static void fill(float[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				m[i][j] = (float)(Math.random() * 100);
			}
		}
	}
	
	@Test
	public void MatrixMult_Single() {
		float[][] a = new float[3][3];
		float[][] b = new float[3][3];		
		
		fill(a);
		fill(b);		
		
		assertArrayEquals(MatrixMultThread.mult(a, b, 1), 
						  Model.MatrixMultThread.mult(a, b, 1));
	}
	
	@Test
	public void MatrixMult_Multi() {
		float[][] a = new float[1024][1024];
		float[][] b = new float[1024][1024];		
		
		fill(a);
		fill(b);		
		
		assertArrayEquals(MatrixMultThread.mult(a, b, 8), 
						  Model.MatrixMultThread.mult(a, b, 8));
	}
	
	final int OK_FACTOR = 2;
	
	@Test
	public void MatrixMult_Timing() {
		float[][] a = new float[512][512];
		float[][] b = new float[512][512];		
		
		fill(a);
		fill(b);	
		
		
		for(int i = 4; i <= 7 ;i++)
		{
			long start_student = System.currentTimeMillis();
			float[][] res_student = MatrixMultThread.mult(a, b, i);
			long end_student = System.currentTimeMillis();
			
			long start_model = System.currentTimeMillis();
			float[][] res_model= Model.MatrixMultThread.mult(a, b, i);
			long end_model = System.currentTimeMillis();		
			
			assertArrayEquals(res_student, res_model);
			
			assertTrue(end_student - start_student <= (end_model - start_model)*OK_FACTOR);
		}
	}
	
	@Test
	public void SynchronizedQueue_Fill() 
	{
		SynchronizedQueue<Float> student_queue = new SynchronizedQueue<Float>(10);
		Model.SynchronizedQueue<Float> model_queue = new Model.SynchronizedQueue<Float>(10);
		for(int i = 0; i < 10; i++)
		{
			float f = (float)(Math.random() * 100);
			student_queue.enqueue(f);
			model_queue.enqueue(f);
		}
		
		for(int i = 0; i < 10; i++)
		{
			assertEquals(model_queue.dequeue(), student_queue.dequeue());			
		}
	}
	
	@Test
	public void SynchronizedQueue_Size() 
	{
		SynchronizedQueue<Float> student_queue = new SynchronizedQueue<Float>(10);
		Model.SynchronizedQueue<Float> model_queue = new Model.SynchronizedQueue<Float>(10);		
		
		assertEquals("Capacity is not equal!", model_queue.getCapacity(), student_queue.getCapacity());
		
		for(int i = 0; i < 10; i++)
		{
			student_queue.enqueue((float)i);
			model_queue.enqueue((float)i);
			assertEquals("Size is not equal!", model_queue.getSize(), student_queue.getSize());	
			
		}		
	}
	
	public class EnqDeqThread implements Runnable {

		private SynchronizedQueue<Integer> m_q;
		public EnqDeqThread(SynchronizedQueue<Integer> q)
		{
			m_q = q;
		}
		@Override
		public void run() {
			Integer integer = new Integer(3);
			for(int i = 0; i < 1000; i++)
			{
				m_q.enqueue(integer);				
				m_q.dequeue();				
			}			
		}
		
	}
	
	@Test
	public void SynchronizedQueue_ThreadSafe() 
	{		
		for(int i = 0; i < 5; i++)
		{
			SynchronizedQueue<Integer> student_queue = new SynchronizedQueue<Integer>(10);
			EnqDeqThread tstt1 = new EnqDeqThread(student_queue);			
			EnqDeqThread tstt2 = new EnqDeqThread(student_queue);
			EnqDeqThread tstt3 = new EnqDeqThread(student_queue);
			EnqDeqThread tstt4 = new EnqDeqThread(student_queue);
			student_queue.registerProducer();
			student_queue.registerProducer();
			student_queue.registerProducer();
			student_queue.registerProducer();
			Thread t1 = new Thread(tstt1);
			Thread t2 = new Thread(tstt2);
			Thread t3 = new Thread(tstt3);
			Thread t4 = new Thread(tstt4);
			
			t1.start();
			t2.start();
			t3.start();
			t4.start();
			
			try {
				t1.join(100);
				student_queue.unregisterProducer();
				t2.join(100);
				student_queue.unregisterProducer();
				t3.join(100);
				student_queue.unregisterProducer();
				t4.join(100);
				student_queue.unregisterProducer();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			assertTrue(String.format("Queue size is %d", student_queue.getSize()), student_queue.getSize() == 0);	
			
		}		
	}
	
	@Test
	public void Scouter_FindDirsSimple() {
		File root = new File(System.getProperty("user.dir") + "/TestData/2");
				
		Model.SynchronizedQueue<File> model_directoryQueue = new Model.SynchronizedQueue<File>(2);
		Model.Scouter model_scouter = new Model.Scouter(model_directoryQueue, root);
		Thread model_scouterThread = new Thread(model_scouter);
		
		SynchronizedQueue<File> student_directoryQueue = new SynchronizedQueue<File>(2);		
		Scouter student_scouter = new Scouter(student_directoryQueue, root);	
		Thread student_scouterThread = new Thread(student_scouter);
		
		model_scouterThread.start();
		student_scouterThread.start();
		try {
			model_scouterThread.join(1000);
			student_scouterThread.join(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		assertEquals(model_directoryQueue.getSize(), student_directoryQueue.getSize());
		
		File model_f, student_f;		
		int[] model_arr = new int[2];
		int model_arr_index = 0;
		int[] student_arr = new int[2];
		int student_arr_index = 0;
		
		
		while((student_f = student_directoryQueue.dequeue()) != null)
		{
			student_arr[student_arr_index++] = student_f.getAbsolutePath().hashCode();
		}
		while((model_f = model_directoryQueue.dequeue()) != null)
		{	
			model_arr[model_arr_index++] = model_f.getAbsolutePath().hashCode();
		}
		
		Arrays.sort(model_arr);
		Arrays.sort(student_arr);
		
		assertArrayEquals(model_arr, student_arr);		
	}
	
	@Test
	public void Scouter_FindDirsRecursive() {
		File root = new File(System.getProperty("user.dir") + "/TestData");
				
		Model.SynchronizedQueue<File> model_directoryQueue = new Model.SynchronizedQueue<File>(29);
		Model.Scouter model_scouter = new Model.Scouter(model_directoryQueue, root);
		Thread model_scouterThread = new Thread(model_scouter);
		
		SynchronizedQueue<File> student_directoryQueue = new SynchronizedQueue<File>(29);		
		Scouter student_scouter = new Scouter(student_directoryQueue, root);	
		Thread student_scouterThread = new Thread(student_scouter);
		
		model_scouterThread.start();
		student_scouterThread.start();
		try {
			model_scouterThread.join(1000);
			student_scouterThread.join(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		assertEquals(model_directoryQueue.getSize(), student_directoryQueue.getSize());
		
		File model_f, student_f;		
		int[] model_arr = new int[29];
		int model_arr_index = 0;
		int[] student_arr = new int[29];
		int student_arr_index = 0;
		
		
		while((student_f = student_directoryQueue.dequeue()) != null)
		{
			student_arr[student_arr_index++] = student_f.getAbsolutePath().hashCode();
		}
		while((model_f = model_directoryQueue.dequeue()) != null)
		{	
			model_arr[model_arr_index++] = model_f.getAbsolutePath().hashCode();
		}
		
		Arrays.sort(model_arr);
		Arrays.sort(student_arr);
		
		assertArrayEquals(model_arr, student_arr);		
	}
	
	
	
	
	public boolean isFileBinaryEqual(
		      File first,
		      File second
		   ) throws IOException
		   {
		      // TODO: Test: Missing test
		      boolean retval = false;
		      
		      if ((first.exists()) && (second.exists()) 
		         && (first.isFile()) && (second.isFile()))
		      {
		         if (first.getCanonicalPath().equals(second.getCanonicalPath()))
		         {
		            retval = true;
		         }
		         else
		         {
		            FileInputStream firstInput = null;
		            FileInputStream secondInput = null;
		            BufferedInputStream bufFirstInput = null;
		            BufferedInputStream bufSecondInput = null;

		            try
		            {            
		               firstInput = new FileInputStream(first); 
		               secondInput = new FileInputStream(second);
		               bufFirstInput = new BufferedInputStream(firstInput, 1024); 
		               bufSecondInput = new BufferedInputStream(secondInput, 1024);
		   
		               int firstByte;
		               int secondByte;
		               
		               while (true)
		               {
		                  firstByte = bufFirstInput.read();
		                  secondByte = bufSecondInput.read();
		                  if (firstByte != secondByte)
		                  {
		                     break;
		                  }
		                  if ((firstByte < 0) && (secondByte < 0))
		                  {
		                     retval = true;
		                     break;
		                  }
		               }
		            }
		            finally
		            {
		               try
		               {
		                  if (bufFirstInput != null)
		                  {
		                     bufFirstInput.close();
		                  }
		               }
		               finally
		               {
		                  if (bufSecondInput != null)
		                  {
		                     bufSecondInput.close();
		                  }
		               }
		            }
		         }
		      }
		      
		      return retval;
		   }
	
	@Test
	public void Copier_SimpleCopy() 
	{
		File destPath = new File(System.getProperty("user.dir") + "/TestData/0/");
		File srcFile = new File(System.getProperty("user.dir") + "/TestData/7/books_7.txt");
		
		
		SynchronizedQueue<File> student_directoryQueue = new SynchronizedQueue<File>(1);		
		Copier copier = new Copier(destPath, student_directoryQueue);		
		
		student_directoryQueue.enqueue(srcFile);
		
		Thread t = new Thread(copier);
		t.start();		
		
		try {
			t.join(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		try {
			File destFile = new File(System.getProperty("user.dir") + "/TestData/0/books_7.txt");
			assertTrue(isFileBinaryEqual(srcFile, destFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Test
	public void Copier_MoreThreadsThanFiles() 
	{		
		File destPath = new File(System.getProperty("user.dir") + "/TestData/0/");
		File srcFile = new File(System.getProperty("user.dir") + "/TestData/7/books_7.txt");
		
		
		SynchronizedQueue<File> student_directoryQueue = new SynchronizedQueue<File>(1);		
		
		Thread[] threadArray = new Thread[5];
		for(int i = 0; i < 1; i++)
		{											
			threadArray[i] = new Thread(new Copier(destPath, student_directoryQueue));
		}
		
		student_directoryQueue.enqueue(srcFile);		
		
		for(int i = 0; i < 1; i++)
		{											
			threadArray[i].start();
		}		
		
		try {
			for(int i = 0; i < 1; i++)
			{											
				threadArray[i].join(2000);
			}	
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		try {
			File destFile = new File(System.getProperty("user.dir") + "/TestData/0/books_7.txt");
			assertTrue(isFileBinaryEqual(srcFile, destFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void Copier_MultipleCopy() 
	{				
		SynchronizedQueue<File> fileQueue = new SynchronizedQueue<File>(5);
		File dest = new File(System.getProperty("user.dir") + "/TestData/0/");
		
		Thread[] threadArray = new Thread[5];
		for(int i = 0; i < 5; i++)
		{											
			threadArray[i] = new Thread(new Copier(dest, fileQueue));
		}
		
		for(int i = 0; i < 10; i++)
		{			
			if(i % 2 == 1)
			{
				fileQueue.enqueue(new File(System.getProperty("user.dir") + 
														"/TestData/" + i + "/books_" + i + ".txt"));
			}
		}
		
		for(int i = 0; i < 5; i++)
		{					
			threadArray[i].start();
		}				
		
		try {
			for(int i = 0; i < 5; i++)
			{					
				threadArray[i].join(200);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		try {
			File books = new File(System.getProperty("user.dir") + "/books.txt");
			for(int i = 0; i < 10; i++)
			{
				if(i % 2 == 1)
				{
					File destFile = new File(System.getProperty("user.dir") + "/TestData/0/books_" + i + ".txt");
					assertTrue(isFileBinaryEqual(books, destFile));
				}
			}		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Test
	public void Searcher_Simple()
	{
		SynchronizedQueue<File> student_directoryQueue = new SynchronizedQueue<File>(1);
		SynchronizedQueue<File> student_resultsQueue = new SynchronizedQueue<File>(1);
		student_directoryQueue.enqueue(new File(System.getProperty("user.dir") + "/TestData/2/2/"));
		Thread student_t = new Thread(new Searcher("bla", student_directoryQueue, student_resultsQueue));
		
		Model.SynchronizedQueue<File> model_directoryQueue = new Model.SynchronizedQueue<File>(1);
		Model.SynchronizedQueue<File> model_resultsQueue = new Model.SynchronizedQueue<File>(1);
		model_directoryQueue.enqueue(new File(System.getProperty("user.dir") + "/TestData/2/2/"));
		Thread model_t = new Thread(new Model.Searcher("bla", model_directoryQueue, model_resultsQueue));
		
		student_t.start();
		model_t.start();		
		
		try {
			student_t.join(100);
			model_t.join(100);			
			
			assertEquals(model_resultsQueue.getSize(), student_resultsQueue.getSize());
			
			while(model_resultsQueue.getSize() > 0)
			{
				
				assertEquals("Pathnames different!",
							model_resultsQueue.dequeue().getAbsolutePath().hashCode(),
							student_resultsQueue.dequeue().getAbsolutePath().hashCode());
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Test
	public void Searcher_Multiple()
	{
		SynchronizedQueue<File> student_directoryQueue = new SynchronizedQueue<File>(10);
		SynchronizedQueue<File> student_resultsQueue = new SynchronizedQueue<File>(10);		
		
		Model.SynchronizedQueue<File> model_directoryQueue = new Model.SynchronizedQueue<File>(10);
		Model.SynchronizedQueue<File> model_resultsQueue = new Model.SynchronizedQueue<File>(10);		
		Thread model_t = new Thread(new Model.Searcher("pdf", model_directoryQueue, model_resultsQueue));
		
		
		for(int i = 0; i < 10; i++)
		{
			File f;
			if(i % 2 == 0)
			{
				 f = new File(System.getProperty("user.dir") + "/TestData/" + i + "/" + i + "/");
			}
			else
			{
				 f = new File(System.getProperty("user.dir") + "/TestData/" + i);
			}
			student_directoryQueue.enqueue(f);
			model_directoryQueue.enqueue(f);
		}
		
		Thread []studentThreadArr = new Thread[15];
		for(int i = 0; i < 15; i++)
		{
			studentThreadArr[i] = new Thread(new Searcher("pdf", student_directoryQueue, student_resultsQueue));
		}
		
		for(int i = 0; i < 15; i++)
		{
			studentThreadArr[i].start();
		}	
		
		model_t.start();		
		
		try {
			for(int i = 0; i < 15; i++)
			{
				studentThreadArr[i].join(100);
			}
			model_t.join(100);			
			
			assertEquals(model_resultsQueue.getSize(), student_resultsQueue.getSize());
			
			int []student_arr = new int[model_resultsQueue.getSize()];
			int []model_arr = new int[model_resultsQueue.getSize()];
			int index = 0;
			
			while(model_resultsQueue.getSize() > 0)
			{
				student_arr[index] = student_resultsQueue.dequeue().getAbsolutePath().hashCode();
				model_arr[index++] = model_resultsQueue.dequeue().getAbsolutePath().hashCode();				
			}
			
			Arrays.sort(student_arr);
			Arrays.sort(model_arr);
			
			assertArrayEquals(model_arr, student_arr);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Test
	public void DiskSearcher_Full()
	{
		for(int j = 1; j <= 5; j++)
		{
			 try {
			
				 	File destDir = new File(System.getProperty("user.dir") + "/Result/");
				 	destDir.mkdir();			 	
				 	
				 	
				 	String cmd = String.format("java DiskSearcher txt TestData/ Result/ %d %d", j ,j*2);
					Runtime.getRuntime().exec(cmd);
					
					Thread.sleep(2000);
					
					File books = new File(System.getProperty("user.dir") + "/books.txt");
					
					for(int i = 0; i < 10; i++)
					{
						if(i % 2 == 1)
						{
							File destFile = new File(System.getProperty("user.dir") + "/Result/books_" + i + ".txt");
							assertTrue(isFileBinaryEqual(books, destFile));
						}
					}
					
					destDir.delete();
					
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
