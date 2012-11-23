package org.ariadne_eu.utils.registry;

import org.jdom.Element;
import org.jdom.Namespace;

public class Responsibility {
	String _vocabularyID;
	String _value;
	
	public Responsibility(){
		_vocabularyID="";
		_value="";
	}
	
	public void setVocabularyId(String vocabularyID){
		_vocabularyID=vocabularyID;
	}
	
	public void setValue(String value){
		_value=value;
	}
	
	public String getVocabularyID(){
		return _vocabularyID;
	}
	
	public String getValue(){
		return _value;
	}
	
	public void parseXMLResponsibility(Element responsibility,Namespace ns){
		_vocabularyID = responsibility.getChild("vocabularyID", ns).getText();
		_value = responsibility.getChild("value", ns).getText();
	}
	
	public String getXMLResponsibility(String tab){
		String xml = "";
		xml = tab+ "<responsibility>\n" +
				tab+ "\t<vocabularyID>"+_vocabularyID+"</vocabularyID>\n" +
				tab+ "\t<value>"+_value+"</value>\n" +
				tab+ "</responsibility>\n";
		return xml;
	}
}
