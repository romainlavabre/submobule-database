package org.romainlavabre.database;

import org.romainlavabre.database.exception.NotInitializedException;

public class DataStorageConfigurer {
    private static DataStorageConfigurer INSTANCE;
    private Enum onTransactionSuccess;

    public DataStorageConfigurer(){
        INSTANCE = this;
    }


    protected Enum getOnTransactionSuccess() {
        return onTransactionSuccess;
    }

    protected static DataStorageConfigurer get(){
        if ( INSTANCE == null ){
            throw new NotInitializedException();
        }

        return INSTANCE;
    }

    public static DataStorageConfigurer init(){
        return new DataStorageConfigurer();
    }


    public DataStorageConfigurer setTransactionSuccessEvent( Enum event) {
        get().onTransactionSuccess = event;

        return get();
    }

    public void build(){}
}
