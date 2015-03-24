import java.io.*;
import java.nio.file.*;

/**
 * Created by Sagi Pinchas, ID: 021647821, on 5/3/2015.
 */
public class TimeTest {
    private static final String INPUT_NOT_VALID = "Bad input! This program expects to get: Force Flag (optional), " +
            "source file path, target file path and buffer size. \nThis is mandatory. ";
    private static final String BUFFER_NOT_VALID = "The buffer size parameter was not valid.";
    private static final String FORCE_NOT_VALID = "The 'force' parameter was not correct.";
    private static final String WITH_OVERWRITE = "The program will copy from %s to %s with buffer size of %d " +
            "with overwriting existing file if exists.\n";
    private static final String WITHOUT_OVERWRITE = "The program will copy from %s to %s with buffer size of %d " +
            "without overwriting existing file if exists.\n";
    private static final String COPYING_TIME = "The copying done successfully. time was: ";
    private static final String SOURCE_FILE_NOT_FOUND = "The source file was not found in given source.";
    private static final String TARGET_FILE_ERROR = "The target file exists and cannot be overwritten.";
    private static final String IO_EXCEPTION = "I/O exception has happened.";
    private static final String GENERAL_EXCEPTION = "Unknown error.";
    private static final String COPYING_FAILED = "The copying process was failed.";
    private static final String PATH_NOT_FOUND = "Source or target path error.";
    private static final String CREATE_DIR_ERROR = "Destination path does not " +
            "exist and system couldn't create it.";
    private static final String DIR_CREATED = "The Destination path was created on disk.";

    /**
    * This program can get either 3 or 4 parameters from the command line.
    * in case of 3, they suppose to be source file path, destination file path and buffer size
    * in case of 4, the first parameter stands for overwriting the destination file and has to be "/force".
    * the other 3 are the same as in the first case. This is mandatory, as in the guidelines.
    * This program makes validations on all inputs.
    */
    public static void main(String[] args) {
        // Validating and initializing the input parameters
        boolean overwriteFlag = false;
        String sourceFilePath = null;
        String targetFilePath = null;
        int bufferSize = 0;

        //parsing scenario with 3 parameters and no 'force' flag
        if (args.length == 3) {
            sourceFilePath = args[0];
            targetFilePath = args[1];
            if (tryParseInt(args[2])) {
                bufferSize = Integer.parseInt(args[2]);
            }
        }

        //parsing scenario with 4 parameters where the first one is the 'force' flag
        else if (args.length == 4) {
                if (args[0].toLowerCase().equals("/force")) {
                overwriteFlag = true;
                }
                else {
                    System.out.println( FORCE_NOT_VALID );
                    System.exit(1);
                }
                sourceFilePath = args[1];
                targetFilePath = args[2];
                if (tryParseInt(args[3])){
                    bufferSize = Integer.parseInt(args[3]);
                    if (bufferSize < 1) {
                        System.out.println( BUFFER_NOT_VALID );
                        System.exit(1);
                    }
                }
            }
        else {
            System.out.println( INPUT_NOT_VALID);
            System.exit(1);
        }

        // printing to screen what is going to be done.
        if (!overwriteFlag) {
            System.out.printf(WITHOUT_OVERWRITE, sourceFilePath, targetFilePath, bufferSize);
        } else {
            System.out.printf(WITH_OVERWRITE, sourceFilePath, targetFilePath, bufferSize);
        }

        // copy file and measure time
        long startTime = System.currentTimeMillis();
        if (copyFile(sourceFilePath, targetFilePath, bufferSize, overwriteFlag)){
            long endTime = System.currentTimeMillis();
            System.out.println( COPYING_TIME + Long.toString(endTime - startTime));
            }
        else {
            System.out.println( COPYING_FAILED );
        }
    }
    /**
     * checks if a string value (buffer size) can be parsed to integer.
     * @param value buffer size.
     * @return true when parsing to integer can be done, false otherwise.
     */
    public static boolean tryParseInt(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch(NumberFormatException e)
        {
            System.out.println( BUFFER_NOT_VALID + e );
            System.exit(1);
        }

        return false;
    }
    /**
     * Copies a file to a specific path, using the specified buffer size. If the path doesn't exist - the program
     * will create it.
     * @param srcFileName File to copy
     * @param toFileName Destination file name
     * @param bufferSize Buffer size in bytes
     * @param bOverwrite If file already exists, overwrite it
     * @return true when copy succeeds, false otherwise
     */
    public static boolean copyFile(String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {

        // check buffer size
        if (bufferSize < 1) {
            System.out.println( BUFFER_NOT_VALID );
            return false;
        }

        // check source file exists
        if (!new File(srcFileName).exists()){
            System.out.println( SOURCE_FILE_NOT_FOUND );
            return false;
        }

        File destPath = new File(toFileName);
        // check permission to overwrite in case the destination file exists.
        if (destPath.exists() && !bOverwrite){
            System.out.println( TARGET_FILE_ERROR );
            return false;
        }

        //create the destination folder in case it doesn't exist.
        /*if (!destPath.exists()) {
            try {
                if (destPath.getParentFile().mkdirs()) {
                    System.out.println(DIR_CREATED);
                }
            } catch (Exception e){
                System.out.println( CREATE_DIR_ERROR );
                System.exit(1);
            }
        }*/

        //creating the reader and writer.
        // using FileReader since we deal with text, and according to the guidelines.
        FileReader inputStream = null;
        FileWriter outputStream = null;

        try {
            inputStream = new FileReader(srcFileName);
            outputStream = new FileWriter(toFileName);
            char[] cbuf = new char[bufferSize];

            //reading the whole file using chunks of buffer size.
            int charsToCopy;
            while ((charsToCopy = inputStream.read(cbuf, 0, bufferSize)) != -1) {
                outputStream.write(cbuf, 0, charsToCopy);
            }
        } catch ( FileNotFoundException | NoSuchFileException e){
            System.out.println( PATH_NOT_FOUND );
            return false;
        } catch ( IOException e){
            System.out.println( IO_EXCEPTION );
            return false;
        } catch ( Exception e){
            System.out.println( GENERAL_EXCEPTION );
            return false;

        //closing the input/output streams
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    return true;
    }
}
