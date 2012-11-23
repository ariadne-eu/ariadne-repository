package org.ariadne_eu.content.insert;

import javax.activation.DataHandler;

/**
 * Created by ben
 * Date: 11-sep-2007
 * Time: 21:22:52
 * To change this template use File | Settings | File Templates.
 */
public abstract class InsertContentImpl {
    private int number;

//    public abstract void insertContent(String identifier, DataHandler dataHandler);
    
    public abstract void insertContent(String identifier, DataHandler dataHandler, String fileName, String FileType);


    public int getNumber() {
        return number;
    }

    void setNumber(int number) {
        this.number = number;
    }

    void initialize() {

    }
}
