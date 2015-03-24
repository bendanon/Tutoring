import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTest {

	// Messages Constants 
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size"; 
	
	public static void main(String[] args) { 
		long startTime = System.currentTimeMillis(); 
		//Checks the correctness of the input arguments.
		if (args.length == 4) { 
			if (!(args[0].equals("/force"))) {
					System.out.println(USAGE);
					return;
			}
			else
				//Private method - checks the bufferSize validity.
				if (bufferSizeChecker(args[3]))
					copyFile(args[1], args[2], Integer.parseInt(args[3]), false);
				else
					return;
		}
		//Checks the correctness of the input arguments.
		else if (args.length == 3)
			//Private method - checks the bufferSize validity.
			if (bufferSizeChecker(args[2]))
				copyFile(args[0], args[1], Integer.parseInt(args[2]), true);
			else
				return;
		else {
			System.out.println(USAGE);
			return;
		}
		long endTime = System.currentTimeMillis(); 
		System.out.println("Total Time: " + (endTime - startTime)+ " ms");
	}

	public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {
		
		try {
			File toWrite = new File(toFileName);
			FileReader read = new FileReader(srcFileName);
			FileWriter write = new FileWriter(toWrite, bOverwrite);
			char[] letters = new char[bufferSize];
			//Reads from the source file into "letters".
			int numOfLetters = read.read(letters, 0, bufferSize);
			//Continue the program until the end of file.
			while (numOfLetters != -1) {
				write.write(letters, 0, numOfLetters);
				write.flush();
				numOfLetters = read.read(letters, 0, bufferSize);
			}
			read.close();
			write.flush();
			write.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}
	
	//Checks if the argument of the buffer size is valid and is a number.
	private static boolean bufferSizeChecker(String size) {
		int bufferSize;
		//Checks if can be casted to a number.
		try {
            bufferSize = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            return false;
        }
        //Checks if it is a positive number.
        if (bufferSize <= 0) {
            System.out.println("Invalid bufferSize, suppose to be positive.");
            return false;
        }
        return true;
	}
}
