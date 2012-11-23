/**
 * 
 */
package org.ariadne_eu.utils.lucene.reindex;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xpath.XPathAPI;
import org.ariadne.config.PropertiesManager;
import org.ariadne.util.Stopwatch;
import org.ariadne_eu.metadata.insert.InsertMetadataFactory;
import org.ariadne_eu.metadata.insert.InsertMetadataImpl;
import org.ariadne_eu.metadata.insert.InsertMetadataLuceneImpl;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * @author gonzalo
 *
 */
public class ReIndexZipImpl extends ReIndexImpl {

	private static Logger log = Logger.getLogger(ReIndexZipImpl.class);
	private String dirString;
	private static Vector xpathQueries;
	private DocumentBuilder builder;


	public ReIndexZipImpl() {
		initialize();
	}

	void initialize() {
		super.initialize();

		try {
			dirString = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_SPIFS_DIR );
			if (dirString == null)
				log.error("initialize failed: no " + RepositoryConstants.getInstance().MD_SPIFS_DIR + " found");
			File dir = new File(dirString);
			if (!dir.isDirectory())
				log.error("initialize failed: " + RepositoryConstants.getInstance().MD_SPIFS_DIR + " invalid directory");
			dirString = dir.getParent();
			xpathQueries = new Vector();
			if (PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_XPATH_QRY_ID + ".1") == null)
				xpathQueries.add("general/identifier/entry/text()");
			else {
				int i = 1;
				while(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_XPATH_QRY_ID + "." + i) != null) {
					xpathQueries.add(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_XPATH_QRY_ID + "." + i));
					i++;
				}
			}
			//TODO: check for valid lucene index
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (Throwable t) {
			log.error("initialize: ", t);
		}

	}


	public void reIndexMetadata() {
		File mdFile;
		File dir = new File(dirString);
		File[] files = dir.listFiles();

		System.out.println("Starting reindexation");


		InsertMetadataImpl[] insertImpls = InsertMetadataFactory.getInsertImpl();
		InsertMetadataLuceneImpl luceneImpl = null;
		for (int i = 0; i < insertImpls.length; i++) {
			InsertMetadataImpl insertImpl = insertImpls[i];
			if (insertImpl instanceof InsertMetadataLuceneImpl)
				luceneImpl = (InsertMetadataLuceneImpl) insertImpl;
		}

		if (luceneImpl == null)
			return;

		luceneImpl.createLuceneIndex();

		String implementation = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_INSERT_IMPLEMENTATION);
		if (implementation != null) {

			for (int i = 0; i < files.length; i++) {

				mdFile = files[i];
				System.out.println(mdFile.getAbsolutePath());
				if (!mdFile.isDirectory() && mdFile.getName().endsWith(".zip")) {
					indexFile(mdFile, luceneImpl, new String[0]);
				}

			}

		}

	}

	private void indexFile (File mdFile, InsertMetadataLuceneImpl luceneImpl, String[] cName) {
		try {
			ZipFile zip = new ZipFile(mdFile);
			for (Enumeration e = zip.entries(); e.hasMoreElements();)
			{
				ZipEntry entry = (ZipEntry) e.nextElement();
				String[] collection = entry.getName().split("/");
				collection = Arrays.asList(collection).subList(0, collection.length -1).toArray(new String[1]);

				InputStream is = zip.getInputStream(entry);
				String xml = readInputStream(is,"UTF-8");
				try {
					Document doc = getDoc(xml);
					String identifier = getIdentifier(doc);
					StringWriter out = new StringWriter();
					XMLSerializer serializer = new XMLSerializer(out, new OutputFormat(doc));
					serializer.serialize((Element) doc.getFirstChild());
					String lom = out.toString();
					if (identifier != null)
						luceneImpl.insertMetadata(identifier, lom, collection);
				} catch (Exception ex) {
					log.error("indexFile: fileName=" + mdFile.getName(), ex);
				}
				
			}
		} catch (ZipException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static String getIdentifier (Document doc) {
		String identifier = null;
		for (int j = 0; j < xpathQueries.size() && identifier == null; j++) {
			String xpathQuery = (String) xpathQueries.elementAt(j);
			try {
				identifier = XPathAPI.selectSingleNode(doc.getFirstChild(),xpathQuery).getNodeValue();
			} catch (Exception e) {
				log.debug("getIdentifier", e);
			}
		}
		return identifier;
	}

	private Document getDoc (String xml) {
		Document doc = null;
		StringReader stringReader = new StringReader(xml);
		InputSource input = new InputSource(stringReader);
		try {
			doc = builder.parse(input);
		} catch (Exception e) {
			log.error("getDoc:",e);
		}
		return doc;
	}

	public static String readInputStream(InputStream is, String encoding){
		try {
			if (is != null) {
				Writer writer = new StringWriter();

				char[] buffer = new char[1024];
				try {
					Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					int n;
					while ((n = reader.read(buffer)) != -1) {
						writer.write(buffer, 0, n);
					}
				} finally {
					is.close();
				}
				return writer.toString();
			} else {        
				return "";
			}
		} catch (Exception e) {
			log.error("readInputStream:",e);
		}
		return "";
	}

}
