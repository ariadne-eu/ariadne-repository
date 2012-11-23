package org.ariadne_eu.metadata.insert;

/**
 * Created by ben
 * Date: 5-mei-2007
 * Time: 19:00:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class InsertMetadataImpl {
    private int language;

    public abstract void insertMetadata(String identifier, String metadata, String collection) throws InsertMetadataException;


    public int getLanguage() {
        return language;
    }

    void setLanguage(int language) {
        this.language = language;
    }

    void initialize() {

    }
}
