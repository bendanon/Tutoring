/* my ID is 301266979 */
//package ex1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TimeTest {
	// static error msg
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";

	// input validation
	public static boolean isValid(String[] args) {
		int len = args.length;
		if (len != 3 && len != 4) {
			System.out.println(USAGE);
			return false;
		}
		return true;
	}

	public static boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, boolean bOverwrite) {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(srcFileName);
			byte[] buffer = new byte[bufferSize];

			out = new FileOutputStream(toFileName);
			Path path = Paths.get(toFileName);
			// write only if file isn't exists or if overrite is forced
			if (bOverwrite || (!(Files.exists(path)))) {
				// reading and writting the file
				while (in.read(buffer) != -1)
					out.write(buffer);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error with input file");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			System.out.println("Error while reading");
			e.printStackTrace();
			return false;
		}

		if (in != null)
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		if (out != null)
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return true;
	}

	public static void main(String[] args) throws IOException {
		// isValid
		int len = args.length;
		boolean force = false;

		if (isValid(args)) {
			if (args[0].equals("/force"))
				force = true;
			int size = Integer.parseInt(args[len - 1]);
			// start time measure
			long startTime = System.currentTimeMillis();
			if (!(copyFile(args[len - 3], args[len - 2], size, force)))
				return;
			else {
				long endTime = System.currentTimeMillis();
				System.out.println("Total time: " + (endTime - startTime)
						+ "ms");
			}
		}
		else
			return;
	}
}
