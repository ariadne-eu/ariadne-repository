package org.ariadne_eu.utils.registry;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.Namespace;

public class OaiPmh{
	
	List<MetadataFormat> _metadataFormats;
	String _granularity;
	String _earliestDateStamp;
	String _deletedRecord;
	List<String> _sets;
	
	public OaiPmh(){
			_metadataFormats = new ArrayList<MetadataFormat>();	
			_sets = new ArrayList<String>();
	}
	
	public OaiPmh(String granularity, String earliestDateStamp, String deletedRecord){
		_granularity=granularity;
		_earliestDateStamp=earliestDateStamp;
		_deletedRecord=deletedRecord;
	}
	
	public void setGranularuty(String granularity){
		_granularity=granularity;
	}
	
	public void setEarliestDateStamp(String earliestDateStamp){
		_earliestDateStamp=earliestDateStamp;
	}
	
	public void setDeletedRecord(String deletedRecord){
		_deletedRecord=deletedRecord;
	}
	
	public String getGranularity(){
		return _granularity;
	}
	
	public String getEarliestDateStamp(){
		return _earliestDateStamp;
	}
	
	public String getDeletedRecord(){
		return _deletedRecord;
	}
	
	public List<MetadataFormat> getMetadataFormats(){
		return _metadataFormats;
	}
	
	public List<String> getSets(){
		return _sets;
	}
	
	public void addMetadataFormat(MetadataFormat metadataFormat){
		_metadataFormats.add(metadataFormat);		
	}
	
	public void addSets(String set){
		_sets.add(set);
	}
	
	public void parseXMLOaiPmh(Element oaiPmh, Namespace ns){
		Namespace oai =Namespace.getNamespace("http://www.imsglobal.org/services/lode/imslooaipmh-2p0_v1p0");
		_granularity = oaiPmh.getChild("granularity", oai).getText();
		_earliestDateStamp = oaiPmh.getChild("earliestDatestamp", oai).getText();
		_deletedRecord = oaiPmh.getChild("deletedRecord",oai).getText();
		List<Element> metadataFormats= oaiPmh.getChildren("metadataFormat",oai);
		for (int i=0;i<metadataFormats.size();i++){
			MetadataFormat metadataFormat = new MetadataFormat();
			metadataFormat.parseXMLMetadataFormat(metadataFormats.get(i), oai);
			addMetadataFormat(metadataFormat);
		}		
		List<Element> sets = oaiPmh.getChildren("set",oai);
		
		for (int i=0;i<sets.size();i++){
			addSets(sets.get(i).getText());
		}
		
	}
	
	public String getXMLOaiPmh(String tab){
		String xml = "";
		xml = tab+"<oai-pmh xmlns=\"http://www.imsglobal.org/services/lode/imslooaipmh-2p0_v1p0\" xsi:schemaLocation=\"http://www.imsglobal.org/services/lode/imslooaipmh-2p0_v1p0 http://fire.eun.org/xsd/registry/imslooaipmh-2p0_v1p0.xsd\">\n";
		for (int i=0;i<_metadataFormats.size();i++){
			xml += tab+((MetadataFormat)_metadataFormats.get(i)).getXMLMetadataFormat(tab+"\t");
		}
		for (int i=0;i<_sets.size();i++){
			xml += tab+("\t<set>"+_sets.get(i)+"</set>\n");
		}
		xml += tab+"\t<granularity>"+_granularity+"</granularity>\n" +
				tab+"\t<earliestDatestamp>"+_earliestDateStamp+"</earliestDatestamp>\n" +
				tab+"\t<deletedRecord>"+_deletedRecord+"</deletedRecord>\n" +
				tab+"</oai-pmh>\n";
		return xml;
	}
}
