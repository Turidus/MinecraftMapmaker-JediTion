package de.turidus.minecraft_mapmaker.utils;

/**
 * Unspecific Runtime Exception
 * A wrapper exception for checked exceptions that are supposed to crash the program if they occur.
 */
public class URE extends RuntimeException {

    public URE(Exception e) {
        super(String.format("A %s occurred with message: %s", e.getClass().getSimpleName(), e.getMessage()), e);
    }

    public URE(String message) {
        super(message);
    }

    public URE(String message, Exception e) {
        super(message, e);
    }

}
