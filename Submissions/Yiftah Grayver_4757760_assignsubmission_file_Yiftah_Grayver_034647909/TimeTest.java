import java.io.*;
import java.nio.file.*;

/**
 * Yiftah Grayver 034647909
 */
public class TimeTest {
    public static void main(String[] args) {
        String srcFile = null;
        String tgtFile = null;
        boolean bOverwrite = false;
        int bufferSize = -1;
        long startTime, endTime;

        // Checking arguments
        switch (args.length) {

            case 3:
                if (args[0].equals("/force"))
                    throw new IllegalArgumentException("Illegal argument");
                srcFile = args[0];
                tgtFile = args[1];
                try {
                    bufferSize = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid integer as buffer size");
                    return;
                }
                break;

            case 4:
                if (!args[0].equals("/force"))
                    throw new IllegalArgumentException("Illegal argument");
                srcFile = args[1];
                tgtFile = args[2];
                try {
                    bufferSize = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid integer as buffer size");
                    return;
                }
                bOverwrite = true;
                break;

            default:
                throw new IllegalArgumentException("Illegal argument");
        }

        // Copy file, measure time and print output
        startTime = System.currentTimeMillis();
        if (copyFile(srcFile, tgtFile, bufferSize, bOverwrite)) {
            endTime = System.currentTimeMillis();
            System.out.println("File " + srcFile + " was copied to " + tgtFile +
                    "\nTotal time: " + (endTime - startTime) + "ms" );
        }
    }

    /**
     *
     * @param srcFileName File to copy
     * @param toFileName Destination file name
     * @param bufferSize Buffer size in bytes
     * @param bOverwrite If file already exists, overwrite it
     * @return true when copy succeeds, false otherwise
     */
    public static boolean copyFile (String srcFileName, String toFileName, int bufferSize, boolean bOverwrite) {
        FileReader fr = null;
        FileWriter fw = null;
        int b = 0;

        // Checking if file already exist
        if (!bOverwrite) {
            Path tgtFilePath = Paths.get(toFileName);
            if (Files.exists(tgtFilePath))
                throw new IllegalArgumentException("File name already exists");
        }

        try {
            char[] buffer = new char[bufferSize];
            fr = new FileReader(srcFileName);
            fw = new FileWriter(toFileName);
            while ((b = fr.read(buffer, 0, bufferSize)) != -1)
                fw.write(buffer, 0, b);
            fr.close();
            fw.close();
        } catch (Exception e) {
            System.out.println("File failed to copy: " + e);
            return false;
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
                System.out.println("Could not close FileReader/FileWriter");
            }
        }
        return true;
    }
}
