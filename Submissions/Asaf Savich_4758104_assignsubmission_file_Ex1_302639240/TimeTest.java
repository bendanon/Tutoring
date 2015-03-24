import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTest {
    private static final String ERROR = "Error: ";
    private static final String USAGE =
            "Usage: java TimeTest [/force] source_file target_file buffer_size\n";
    private static final String VERBOSE = "/force";
    private static final String TIME_MESSAGE = "Total Time: ";
    private static final String BUFFER_ERROR = "Buffer Size Must Be Positive";
    private static final String FILE_ERROR = "Oops! Please Check The Files";
    public static void main(String[] args) {

        String from;
        String to;
        String stringToBufferSize;
        int bufferSize;
        boolean force;

        if (args.length == 4) {
            if (!(args[0].equals(VERBOSE))) {
                //If there are 4 arguments and the first one is not /force.
                System.out.println(ERROR + USAGE);
                return;
            }
            // If indeed the first one is force. Store the files path and the buffer size.
            from = args[1];
            to = args[2];
            stringToBufferSize = args[3];
            force = false;
        } else if (args.length == 3) {
            // If there are 3 arguments store the files path and the buffer size.
            from = args[0];
            to = args[1];
            stringToBufferSize = args[2];
            force = true;
        } else {
            System.out.println(ERROR + USAGE);
            return;
        }
        try {
            bufferSize = Integer.parseInt(stringToBufferSize);
        } catch (NumberFormatException e) {
            System.out.println(ERROR + USAGE);
            return;
        }
        // If the bufferSize is not positive.
        if (bufferSize <= 0) {
            System.out.println(ERROR + BUFFER_ERROR);
            return;
        }
        // Check the time before and after executing the copyFile method.
        long startTime = System.currentTimeMillis();
        copyFile(from, to, bufferSize, force);
        long endTime = System.currentTimeMillis();
        System.out.println(TIME_MESSAGE + (endTime - startTime) + "ms");


    }

    public static boolean copyFile(String srcFileName, String toFileName,
                                   int bufferSize, boolean bOverwrite) {
        int endOfFile;
        File copyFrom = new File(srcFileName);
        File copyTo = new File(toFileName);
        // Checking if the files indeed are valid files.
        if (!copyTo.isFile() || !copyFrom.isFile()) {
            System.out.println(FILE_ERROR);
        }
        try {
            char[] toWrite = new char[bufferSize];
            FileReader fr = new FileReader(copyFrom);
            FileWriter fw = new FileWriter(copyTo , bOverwrite);
            // Reading the first bufferSize chars from the copyFrom.
            endOfFile = fr.read(toWrite, 0, bufferSize);

            // Keep reading from file and writing until there are no more chars to read.
            while (endOfFile != -1) {
                fw.write(toWrite, 0, endOfFile);
                fw.flush();
                endOfFile = fr.read(toWrite, 0 , bufferSize);
            }

            // Writing the remaining chars to copyTo.
            fw.flush();
            // Closing the buffers.
            fw.close();
            fr.close();

        } catch (IOException e) {
            e.getStackTrace();
            return false;
        }

        return true;
    }
}
