package org.romainlavabre.database.exception;

public class NotInitializedException extends RuntimeException{
    public NotInitializedException(){
        super("DataStorage not initialized, use DataStorageConfigurer to fix it");
    }
}
