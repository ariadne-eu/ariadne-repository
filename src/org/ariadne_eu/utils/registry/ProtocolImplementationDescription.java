package org.ariadne_eu.utils.registry;

import org.jdom.Element;
import org.jdom.Namespace;

public class ProtocolImplementationDescription {
	
		OaiPmh _oaiPmh;
		Sqi _sqi;
		
	public ProtocolImplementationDescription(){
		_oaiPmh = null;
		_sqi = null;
	}
	
	public ProtocolImplementationDescription(OaiPmh oaiPmh){
		_oaiPmh=oaiPmh;		
	}
	
	public ProtocolImplementationDescription(Sqi sqi){
		_sqi=sqi;
	}
	
	public void setOaiPmh(OaiPmh oaiPmh){
		_oaiPmh=oaiPmh;		
	}
	
	public void setSqi(Sqi sqi){
		_sqi=sqi;		
	}
	
	public Sqi getSqi(){
		return _sqi;		
	}
	
	public OaiPmh getOaiPmh(){
		return _oaiPmh;		
	}
	
	public void parseXMLOaiPmh(Element protocolDescription, Namespace ns){
		_oaiPmh = new OaiPmh();
		Namespace oai =Namespace.getNamespace("http://www.imsglobal.org/services/lode/imslooaipmh-2p0_v1p0");
		_oaiPmh.parseXMLOaiPmh((Element)protocolDescription.getChildren().get(0),ns);
	}
	
	public String getXMLDescription(String tab){
		if (_oaiPmh != null){
			return tab+"<protocolImplementationDescription>\n"+
						_oaiPmh.getXMLOaiPmh(tab+"\t")+
						tab+"</protocolImplementationDescription>\n";
		}
		if (_sqi != null){
			return tab+"<protocolImplementationDescription>\n"+
						_sqi.getXMLSqi(tab + "\t")+
						tab+"</protocolImplementationDescription>\n";
		}
		return tab+"<protocolImplementationDescription/>\n";
	}

}
