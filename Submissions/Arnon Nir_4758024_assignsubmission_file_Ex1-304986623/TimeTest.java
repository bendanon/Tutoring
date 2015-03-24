// I.D - 304986623, Name - Arnon Nir
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class TimeTest {

	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	
	public static void main(String[] args) {
		if (args.length != 3 && args.length != 4) {
			System.out.println(USAGE);
			return;
		}
		int i = 0;
		boolean hasForceFlag = false;
		if (args.length == 4) {
			if(args[0].compareTo("/force") == 0) {
				hasForceFlag = true;
				i++;
			}else {
				System.out.println(USAGE);
				return;
			}
		}
		int buffer_size = 0;
		try {
			buffer_size = Integer.parseInt(args[i+2]);
			if (buffer_size <= 0) {
				System.out.println("Error: The buffer_size must be positive number!");
				return;
			}
		} catch (NumberFormatException n) {
			System.err.println("Error: " + n.getMessage());
		}
		
		String source = args[i] ;
		String target = args[i+1];
		long startTime = System.currentTimeMillis();
		boolean success = copyFile(source, target, buffer_size, hasForceFlag);
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		if (success) {
			System.out.println("File " + source +" was copied to " + target + "\n" + "Total time: " + time + "ms");
		}
	}

	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {
		FileReader reader = null;
		FileWriter writer = null;
		File fileDest = new File(toFileName);
		
		if(fileDest.exists() && !bOverwrite) {
			System.out.println("Error: The File is already exists");
			return false;
		}
		try {
			reader = new FileReader(srcFileName);
			writer = new FileWriter(toFileName);
			char[] buffer = new char[bufferSize];
			int howManyBitesRead;
			while((howManyBitesRead = reader.read(buffer, 0, bufferSize)) != -1) {
				writer.write(buffer, 0, howManyBitesRead);
				writer.flush();
			}
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		finally {
			try {
				if(reader != null) {
					reader.close();
				}
				if(writer != null) {
					writer.close();
				}
			} catch (IOException e){
				System.err.println("Error: " + e.getMessage());
			}
		}
		return true;		
	}
}