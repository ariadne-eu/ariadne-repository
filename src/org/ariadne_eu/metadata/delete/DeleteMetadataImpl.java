package org.ariadne_eu.metadata.delete;

public abstract class DeleteMetadataImpl {
    private int implementation;

    public abstract void deleteMetadata(String identifier);


    public int getImplementation() {
        return implementation;
    }

    void setImplementation(int implementation) {
        this.implementation = implementation;
    }

    void initialize() {

    }
}
