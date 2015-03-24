//EX-1 038780037
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class TimeTest {

	// Messages Constants
	private static final String USAGE = "Usage: jave TimeTest [/force] source_file target_file buffer_size";
	public static void main(String[] args) throws IOException {

		long startTime = 0;
		long endTime = 0;
		boolean force = false;


		// Check arguments
		if((args.length != 3) && (args.length != 4)) {
			System.out.println(USAGE);
		} else {
			if (args[0].equals("/force")) {
				force = true;
			}

			if (force) {
				int buffSize = Integer.parseInt(args[3]);
				startTime = System.currentTimeMillis();
				boolean res = copyFile(args[1], args[2], buffSize, true);
				endTime = System.currentTimeMillis();
				if (res) {
					System.out.println("File " + args[1] +" was copied to " + args[2]);
					System.out.println("Total Time " + (endTime -startTime) + "ms");
				}
			} else {
				int buffSize = Integer.parseInt(args[2]);
				startTime = System.currentTimeMillis();
				boolean res = copyFile(args[0], args[1], buffSize, false);
				endTime = System.currentTimeMillis();
				if (res) {
					System.out.println("File " + args[0] +" was copied to " + args[1]);
					System.out.println("Total Time " + (endTime -startTime) + "ms");
				}
			}
		}
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
	public static boolean copyFile(String srcFileName ,String toFileName ,int bufferSize ,boolean bOverwrite) throws IOException {

		FileReader inputStream = null;
		FileWriter outputStream = null;


		File out = new File(toFileName);
		File in	= new File(srcFileName);

		if(out.exists() == true && bOverwrite == false) {
			System.out.println("Target file exists, can not overwrite");
			return false;
		}

		if(in.canRead() != true) {
			System.out.println("Can not read file");
			return false;
		}

		try {
			inputStream = new FileReader(srcFileName);
			outputStream = new FileWriter(toFileName);


			char [] cbuf = new char[bufferSize];
			int ofset= 0;
			while ((inputStream.read(cbuf ,ofset ,bufferSize)) != -1) {
				outputStream.write(cbuf);
			}	                
			outputStream.write(cbuf);
		} catch (Exception e) {
			e.printStackTrace();
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

