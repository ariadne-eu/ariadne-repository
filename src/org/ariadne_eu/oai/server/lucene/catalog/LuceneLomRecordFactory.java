package org.ariadne_eu.oai.server.lucene.catalog;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.apache.lucene.document.Document;
import org.ariadne.oai.server.catalog.AbstractRecordFactory;
import org.ariadne_eu.utils.config.RepositoryConstants;

public class LuceneLomRecordFactory extends AbstractRecordFactory{
	private String dateField;
	private String identifierField;
	
	
	public LuceneLomRecordFactory(Properties properties)	throws IllegalArgumentException {
		super(properties);;
		dateField = properties.getProperty(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_DATEFIELD);
		if (dateField == null) {
		    throw new IllegalArgumentException(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_DATEFIELD + " is missing from the properties file");
		}
		identifierField = properties.getProperty(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_IDFIELD);
		if (identifierField == null) {
		    throw new IllegalArgumentException(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_IDFIELD + " is missing from the properties file");
		}
	}

    /**
     * Extract the local identifier from the native item
     *
     * @param nativeItem native Item object
     * @return local identifier
     */
    public String getLocalIdentifier(Object nativeItem) {
    	Document doc = (Document)nativeItem;

    	return doc.getField(identifierField).stringValue();
    }

    /**
     * get the datestamp from the item
     *
     * @param nativeItem a native item presumably containing a datestamp somewhere
     * @return a String containing the datestamp for the item
     * @exception IllegalArgumentException Something is wrong with the argument.
     */
    public String getDatestamp(Object nativeItem)
	throws IllegalArgumentException  {
    	String dateString = "";
		try {
			Document doc = (Document)nativeItem;
			dateString = doc.getField(dateField).stringValue();
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmssS");
			Date date = df.parse(dateString);
			String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
			DateFormat df2 = new SimpleDateFormat(pattern);
			dateString = df2.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return dateString;
    }

    
    
    /**
     * get the setspec from the item
     *
     * @param nativeItem a native item presumably containing a setspec somewhere
     * @return a String containing the setspec for the item
     * @exception IllegalArgumentException Something is wrong with the argument.
     */
    public Iterator getSetSpecs(Object nativeItem)
	throws IllegalArgumentException  {
	return null;
    }

    /**
     * Get the about elements from the item
     *
     * @param nativeItem a native item presumably containing about information somewhere
     * @return a Iterator of Strings containing &lt;about&gt;s for the item
     * @exception IllegalArgumentException Something is wrong with the argument.
     */
    public Iterator getAbouts(Object nativeItem) throws IllegalArgumentException {
	return null;
    }

    /**
     * Is the record deleted?
     *
     * @param nativeItem a native item presumably containing a possible delete indicator
     * @return true if record is deleted, false if not
     * @exception IllegalArgumentException Something is wrong with the argument.
     */
    public boolean isDeleted(Object nativeItem)
	throws IllegalArgumentException {
	return false;
    }

    /**
     * Allows classes that implement RecordFactory to override the default create() method.
     * This is useful, for example, if the entire &lt;record&gt; is already packaged as the native
     * record. Return null if you want the default handler to create it by calling the methods
     * above individually.
     * 
     * @param nativeItem the native record
     * @param schemaURL the schemaURL desired for the response
     * @param the metadataPrefix from the request
     * @return a String containing the OAI &lt;record&gt; or null if the default method should be
     * used.
     */
    public String quickCreate(Object nativeItem, String schemaLocation, String metadataPrefix) {
	// Don't perform quick creates
	return null;
    }
}

