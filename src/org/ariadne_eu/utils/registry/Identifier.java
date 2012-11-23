package org.ariadne_eu.utils.registry;

import org.jdom.Element;
import org.jdom.Namespace;

public class Identifier {
	String _catalog;
	String _entry;
	
	public Identifier(){
		
	}
	
	public Identifier(String entry, String catalog){
		_entry = entry;
		_catalog = catalog;
	}
	
	public void setEntry(String entry){
		_entry=entry;		
	}
	
	public void setCatalog(String catalog){
		_catalog=catalog;
	}
	
	public String getEntry(){
		return _entry;		
	}
	
	public String getCatalog(){
		return _catalog;
	}
	
	public void parseXMLIdentifier(Element identifier,Namespace ns){
		_catalog = identifier.getChild("catalog", ns).getText();
		_entry = identifier.getChild("entry", ns).getText();
	}
	
	public String getXMLIdentifier(String tab){
		String xml = "";
		xml = tab+ "<identifier>\n" +
				tab+ "\t<catalog>"+_catalog+"</catalog>\n" +
				tab+ "\t<entry>"+_entry+"</entry>\n" +
				tab+ "</identifier>\n";
		return xml;
	}
}
