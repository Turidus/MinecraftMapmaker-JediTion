package de.turidus.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class FileHandler {

    private static final String name   = "FH";
    private static final Logger logger = LoggerFactory.getLogger(name);

    private static void logFileCreation(File file) {
        logger.warn("Created " + file.getName() + " from default resource.");
    }

    private static void logFileRead(File file) {
        logger.debug("Read " + file.getName() + ".");
    }

    public static FileReader getFileReaderFromConfigFolderOrDefault(String filepath, String defaultResource) {
        try {
            return new FileReader(getFileFromConfigFolderOrDefault(filepath, defaultResource));
        } catch(FileNotFoundException e) {
            throw new URE("A FileReader could not be created from filepath: " + filepath, e);
        }
    }

    public static File getFileFromConfigFolder(String filepath) {
        return getFileFromConfigFolderOrDefault(filepath, filepath);
    }

    public static File getFileFromConfigFolderOrDefault(String filepath, String defaultResource) {
        // does the path exist: configuration/scenario/
        File file = new File(FaF.CONFIG_FOLDER + filepath);

        // search for filename in configuration/scenario/
        if(file.length() == 0) {
            // create directory
            file.getParentFile().mkdirs();

            // search in resource stream for filename
            try(InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(defaultResource)) {
                if(inputStream == null)
                    throw new IOException("The stream " + defaultResource + " could not be found");
                // write file in directory configuration/scenario
                try(FileOutputStream outputStream = new FileOutputStream(file)) {
                    int    length;
                    byte[] buffer = new byte[1024];
                    while((length = inputStream.read(buffer)) != -1)
                        outputStream.write(buffer, 0, length);
                } catch(IOException ioe) {
                    throw new IOException(filepath + " could not be created. " + ioe.getMessage());
                }
                logFileCreation(file);
            } catch(IOException e) {
                throw new URE("There was a problem handling the file: " + filepath, e);
            }
        }
        logFileRead(file);
        return file;
    }

}
