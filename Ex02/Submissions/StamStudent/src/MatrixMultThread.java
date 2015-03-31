

public class MatrixMultThread extends Thread {

	public static final int THREAD_COUNT = 4;
	public static final int MATRIX_SIZE = 875;
	
	private float[][] a,b,c;
	private int startRow, endRow;
	
	public MatrixMultThread(float[][] a, float[][] b, float[][] c, int startRow, int endRow) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.startRow = startRow;
		this.endRow = endRow;
	}
	
	public void run() {
		for (int i = startRow; i <= endRow; i++) {
			for (int j = 0; j < a[0].length; j++) {
				float sum = 0;
				for (int x = 0; x < a[i].length; x++) {
					sum += a[i][x] * b[x][j];
				}
				c[i][j] = sum;
			}
		}
	}
	
	public static float[][] mult(float[][] a, float[][] b, int threadCount) {
		MatrixMultThread[] threads = new MatrixMultThread[threadCount];
		int matrixSize = a.length;
		float[][] c = new float[matrixSize][matrixSize];
		
		int rowsPerThread = (int)Math.ceil((float)matrixSize / threadCount);
		
		for (int i = 0; i < threadCount; i++) {
			int startRow = i * rowsPerThread;
			int endRow = startRow + rowsPerThread - 1;
			if (endRow >= matrixSize)
				endRow = matrixSize - 1;
			threads[i] = new MatrixMultThread(a, b, c, startRow, endRow);
			threads[i].start();
		}
		
		for (int i = 0; i < threadCount; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) { }
		}
		return c;
	}
	
	public static void main(String[] args) {
		float[][] a = new float[MATRIX_SIZE][MATRIX_SIZE];
		float[][] b = new float[MATRIX_SIZE][MATRIX_SIZE];

		fill(a);
		fill(b);
		
		long start = System.currentTimeMillis();
		mult(a, b, THREAD_COUNT);
		System.out.println("Total time: " + (System.currentTimeMillis() - start) + "ms");
	}
	
	/**
	 * Fills a matrix with random values
	 * @param m a float matrix
	 */
	private static void fill(float[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				m[i][j] = (float)(Math.random() * 100);
			}
		}
	}
}
