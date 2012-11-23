package org.ariadne_eu.utils.registry;

import org.jdom.Element;
import org.jdom.Namespace;

public class ProtocolIdentifier {
	String _catalog;
	String _entry;
	
	public ProtocolIdentifier(){
		
	}
	
	public ProtocolIdentifier(String entry, String catalog){
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
	
	public void parseXMLProtocolIdentifier(Element protocolIdentifier,Namespace ns){
		_catalog = protocolIdentifier.getChild("catalog", ns).getText();
		_entry = protocolIdentifier.getChild("entry", ns).getText();
	}
	
	public String getXMLProtocolIdentifier(String tab){
		String xml = "";
		xml = tab+"<protocolIdentifier>\n" +
				tab+"\t<catalog>"+_catalog+"</catalog>\n" +
				tab+"\t<entry>"+_entry+"</entry>\n" +
				tab+"</protocolIdentifier>\n";
		return xml;
	}
}
