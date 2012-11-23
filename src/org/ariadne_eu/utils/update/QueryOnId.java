package org.ariadne_eu.utils.update;

import java.util.List;

import org.apache.log4j.Logger;
import org.ariadne_eu.metadata.query.QueryMetadataFactory;
import org.ariadne_eu.metadata.query.language.TranslateLanguage;
import org.ariadne_eu.metadata.resultsformat.TranslateResultsformat;
import org.ariadne_eu.oai.utils.OaiUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;




public class QueryOnId {

	private static Logger log = Logger.getLogger(QueryOnId.class);
	private static QueryOnId instance = null;

	
	private QueryOnId(){

	}
	
	public static QueryOnId getMACEquery() {
		if(instance == null) {
			instance = new QueryOnId();
		}
		return instance;
	}

	public String getMACEInstance(String metadataIdentifier) throws Exception {

		try {
			XMLOutputter outputter = new XMLOutputter();
			Namespace ns = Namespace.getNamespace("lom","http://ltsc.ieee.org/xsd/LOM");
			XPath oaiIds = XPath.newInstance("//lom:lom/lom:metaMetadata/lom:identifier/lom:catalog[text()=\"oai\"]/parent::*/lom:entry");
			oaiIds.addNamespace(ns);
			XPath ids = XPath.newInstance("//lom:lom/lom:metaMetadata/lom:identifier/lom:entry");
			ids.addNamespace(ns);

			String query = "lom.metaMetadata.identifier.entry = \"" + metadataIdentifier + "\"";
			log.info("Requesting : " + metadataIdentifier);

			String resultString = QueryMetadataFactory.getQueryImpl(TranslateLanguage.PLQL1).query(query, 1, 12, TranslateResultsformat.LOM);

			Document doc = OaiUtils.parseXmlString2Lom(resultString);

			List results = doc.getRootElement().getChildren();
			if(results.size() == 1) {
				
					Element el = (Element)results.get(0);
					el.detach();
					log.info("Successfully Requested : " + metadataIdentifier);
					return outputter.outputString(el);
					
			}else if (results.size() < 1) {
				String msg = "No records found, please check the identifier";
				throw new Exception(msg);
			}else if (results.size() > 1) {
				String msg = "Too many records found, please check the identifier";
				throw new Exception(msg);
			}

		} catch (Exception e) {
			log.error("getMDInstance: identifier = " + metadataIdentifier, e);
			throw e;
		}

		return null;
	}
	
	public String getMDInstance(String metadataIdentifier) throws Exception {

		try {
			XMLOutputter outputter = new XMLOutputter();

			String query = "key : \"" + metadataIdentifier + "\"";
			log.info("Requesting : " + metadataIdentifier);

			String resultString = QueryMetadataFactory.getQueryImpl(TranslateLanguage.LUCENE).query(query, 1, 12, TranslateResultsformat.LOM);

			Document doc = OaiUtils.parseXmlString2Lom(resultString);

			List results = doc.getRootElement().getChildren();
			if(results.size() == 1) {
				
					Element el = (Element)results.get(0);
					el.detach();
					log.info("Successfully Requested : " + metadataIdentifier);
					return outputter.outputString(el);
					
			}else if (results.size() < 1) {
				String msg = "No records found, please check the identifier";
				throw new Exception(msg);
			}else if (results.size() > 1) {
				String msg = "Too many records found, please check the identifier";
				throw new Exception(msg);
			}

		} catch (Exception e) {
			log.error("getMDInstance: identifier = " + metadataIdentifier, e);
			throw e;
		}

		return null;
	}
	
	public String getMetadataCollectionInstance(String metadataIdentifier) throws Exception {

		try {

			XMLOutputter outputter = new XMLOutputter();
			String query = "metadatacollection.identifier.entry = \"" + metadataIdentifier + "\"";

			log.info("Requesting : " + metadataIdentifier);
			String resultString = QueryMetadataFactory.getQueryImpl(TranslateLanguage.PLQL1).query(query, 1, 12, TranslateResultsformat.LOM);	
			Document doc = OaiUtils.parseXmlString2Lom(resultString);

			List results = doc.getRootElement().getChildren();
			if(results.size() == 1) {
				
					Element el = (Element)results.get(0);
					el.detach();
					log.info("Successfully Requested : " + metadataIdentifier);
					return outputter.outputString(el);
					
			}else {
				log.info("No MetadataCollection found, trying with protocol");
				query = "protocol.identifier.entry = \"" + metadataIdentifier + "\"";
				log.info("Requesting : " + metadataIdentifier);
				resultString = QueryMetadataFactory.getQueryImpl(TranslateLanguage.PLQL1).query(query, 1, 12, TranslateResultsformat.LOM);	
				doc = OaiUtils.parseXmlString2Lom(resultString);
				results = doc.getRootElement().getChildren();
				if(results.size() == 1) {
					Element el = (Element)results.get(0);
					el.detach();
					log.info("Successfully Requested : " + metadataIdentifier);
					return outputter.outputString(el);
				}else if (results.size() < 1) {
					String msg = "No records found, please check the identifier";
					log.error("getMetadataCollectionInstance: identifier = " + metadataIdentifier + " - msg");
					throw new Exception(msg);
				}else if (results.size() > 1) {
					String msg = "Too many records found, please check the identifier";
					log.error("getMetadataCollectionInstance: identifier = " + metadataIdentifier + " - msg");
					throw new Exception(msg);
				}				
			}

		} catch (Exception e) {
			log.error("getMetadataCollectionInstance: identifier = " + metadataIdentifier, e);
			throw e;
		}
		return null;
	}


}
