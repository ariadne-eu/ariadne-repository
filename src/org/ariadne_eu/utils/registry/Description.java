package org.ariadne_eu.utils.registry;

import org.jdom.Element;
import org.jdom.Namespace;

public class Description {
	String _language;
	String _string;
	
	public Description(){
				
	}
	
	public Description(String language, String string){
		_language=language;
		_string=string;
	}
	
	public void setLanguage(String language){
		_language=language;
	}
	
	public void setString(String string){
		_string=string;		
	}
	
	public String getLanguage(){
		return _language;		
	}
	
	public String getString(){
		return _string;		
	}
	
	public void parseXMLDescription(Element description,Namespace ns){
		_string = description.getChild("string", ns).getText();
		_language = description.getChild("language", ns).getText();
	}
	
	public String getXMLDescription(String tab){
		String xml = "";
		xml = tab+ "<description>\n" +
				tab+ "\t<language>"+_language+"</language>\n" +
				tab+ "\t<string>"+_string+"</string>\n" +
				tab+ "</description>\n";
		return xml;
	}
	
	
}
