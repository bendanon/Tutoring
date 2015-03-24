// ID: 306420787 Avi Zeldin
import java.io.*;

/*
Arguments:
Force flag: will be ‘/verbose’, and its purpose is for telling us to overwrite the existing target file. 
Source file path: the original file we want to copy. (Example: c:\os\ex1\book.txt)
Target file path: the location where we want the copy to be created, should be a full path name. (Example: c:\os\ex1\book_copy.txt)
Buffer size: a number, the size (in bytes) of the buffer to use when reading the file. (Example: 1024)

Some examples of running it with and without the verbose flag:
java TimeTest c:\file.txt c:\copy.txt 1024
java TimeTest /force c:\file.txt c:\copy.txt 2048
Parameter order matters!
Usage: java TimeTest [/force] source_file target_file buffer_size
*/
public class TimeTest {
    // Messages Constants
    private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";

    public static void main(String[] args) {
        String sourceFile, targetFile;
        int bufferSize;
        boolean shouldOverwrite = false;

        try {
            //checks 2 possible args lengths and fill local variables
            if (args.length == 4) {
                //checks if we should to overwrite out file
                shouldOverwrite = args[0].equals("/force");
                sourceFile = args[1];
                targetFile = args[2];
                bufferSize = Integer.parseInt(args[3], 10);
            } else if (args.length == 3) {
                sourceFile = args[0];
                targetFile = args[1];
                bufferSize = Integer.parseInt(args[2], 10);
            } else {
                //in case that we got strange args.length we will terminate the program
                System.out.println(USAGE);
                System.err.println("Got wrong number of args: " + args.length);
                System.exit(1);
                return;
            }
        } catch (NumberFormatException nfe) {
            System.out.println(USAGE);
            System.err.println("Got NumberFormatException when tried to parse bufferSize");
            nfe.printStackTrace();
            System.exit(1);
            return;
        }

        long startTime = System.currentTimeMillis();
        // copy file
        boolean res = copyFile(sourceFile,targetFile,bufferSize,shouldOverwrite);
        long endTime = System.currentTimeMillis();

        // in case of valid copying file we will write the time difference
        if (res){
            System.out.println("File " + sourceFile + " was copied to " + targetFile);
            System.out.println("Total time: " + (endTime - startTime) + "ms");
        }

    }

    /**
     * Copies a file to a specific path, using the specified buffer size. *
     * @param srcFileName File to copy
     * @param toFileName Destination file name
     * @param bufferSize Buffer size in bytes
     * @param bOverwrite If file already exists, overwrite it
     * @return true when copy succeeds, false otherwise */
    public static boolean copyFile(String srcFileName, String toFileName,
                                   int bufferSize, boolean bOverwrite) {
        FileReader inputStream = null;
        FileWriter outputStream = null;
        boolean res = false;
        try {
            File outFile = new File(toFileName);
            if (outFile.exists()) {
                if (bOverwrite){
                    outputStream = new FileWriter(toFileName, false);
                } else {
                    System.out.println("Error: out file already exists and /force flag wasn't mention");
                    return res;
                }
            } else{
                outputStream = new FileWriter(toFileName);
            }

            inputStream = new FileReader(srcFileName);

            char[] buffer = new char[bufferSize];
            int offset = 0;
            int charactersLength;

            while ((charactersLength = inputStream.read(buffer, offset, bufferSize))  != -1) {
              outputStream.write(buffer, offset, charactersLength);
            }
            res = true;
        } catch (FileNotFoundException ex) {
            System.err.println("copyFile got FileNotFoundException");
            ex.printStackTrace();
        } catch (IOException e) {
            System.err.println("copyFile got IOException");
            e.printStackTrace();
        } catch (Exception exc){
            System.err.println("copyFile got Exception");
            exc.printStackTrace();
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                System.err.println("got IOException when closed " + srcFileName +
                        " StackTrace: " + e.getStackTrace());
                res = false;
            }

        }
        return res;
    }
}