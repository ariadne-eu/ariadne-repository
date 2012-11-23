package org.ariadne_eu.utils.registry;

import org.jdom.Element;
import org.jdom.Namespace;

public class ResponsibleType {
	Responsibility _responsibility;
	String _vCard;
	
	public ResponsibleType(){
		_responsibility = new Responsibility();
		_vCard = "";
	}
	
	public void setResponsibility(Responsibility responsibility){
		_responsibility = responsibility;
	}
	
	public void setVCard(String vCard){
		_vCard=vCard;
	}
	
	public Responsibility getResponsibility(){
		return _responsibility;
	}
	
	public String getVCard(){
		return _vCard;
	}
	
	public void parseXMLResponsibleType(Element responsibility,Namespace ns){
		if (responsibility!=null){
			_responsibility.parseXMLResponsibility(responsibility.getChild("responsibility",ns), ns);
			_vCard = responsibility.getChild("vcard",ns).getText();
		}
	}
	
	public String getXMLResponsibleType(String tab){
		String xml = "";
		if (_vCard.compareTo("")!=0){
			xml = tab+ "<responsible>\n" +
					_responsibility.getXMLResponsibility(tab+"\t")+
					tab+ "\t<vcard>"+_vCard+"</vcard>\n" +
					tab+ "</responsible>\n";
		}
		return xml;
	}
}
