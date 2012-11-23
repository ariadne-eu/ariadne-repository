package org.ariadne_eu.utils.registry;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.Namespace;

public class MetadataCollection{
	Identifier _identifier;
	Description _description;
	List<TargetDescription> _target;
	ResponsibleType _responsible;
	
	public MetadataCollection(){
		_identifier = new Identifier();
		_description = new Description();
		_target=new ArrayList<TargetDescription>();
		_responsible = new ResponsibleType();
	}
	
	public MetadataCollection(Identifier identifier, Description description){
		_description=description;
		_identifier=identifier;
	}
	
	public void setIdentifier(Identifier identifier){
		_identifier=identifier;		
	}
	
	public void setDescription(Description description){
		_description=description;
	}
	
	public void setResponsible(ResponsibleType responsible){
		_responsible=responsible;
	}
	
	public ResponsibleType getResponsible(){
		return _responsible;
	}
	
	public Identifier getIdentifier(){
		return _identifier;
	}
	
	public Description getDescription(){
		return _description;
	}
	
	public void addTarget(TargetDescription targetDescription){
		_target.add(targetDescription);
	}
	
	public List<TargetDescription> getTarget(){
		return _target;
	}
	
	public void parseXMLMetadataCollection(Element metadataCollection,Namespace ns){
		_identifier.parseXMLIdentifier(metadataCollection.getChild("identifier",ns), ns);
		_description.parseXMLDescription(metadataCollection.getChild("description",ns), ns);
		_responsible.parseXMLResponsibleType(metadataCollection.getChild("responsible",ns), ns);
		List<Element> targets = metadataCollection.getChildren("target",ns);
		for (int i=0;i<targets.size();i++){
			TargetDescription targetDescription = new TargetDescription();
			targetDescription.parseXMLTargetDescription(((org.jdom.Element) targets.get(i)).getChild("targetDescription",ns), ns);
			_target.add(targetDescription);
		}
		
	}
	
	public String getXMLMetadataCollection(){
		String xml = "<metadataCollection xmlns=\"http://www.imsglobal.org/services/lode/imsloreg_v1p0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.imsglobal.org/services/lode/imsloreg_v1p0 http://fire.eun.org/xsd/registry/imsloreg_v1p0.xsd\">\n"
						+ _identifier.getXMLIdentifier("\t") 
						+ _description.getXMLDescription("\t")
						+ _responsible.getXMLResponsibleType("\t");
		for (int i=0;i<_target.size();i++){
			xml += _target.get(i).getXMLTargetDescription("\t");
		}		
		xml+="</metadataCollection>";		
		return xml;		
	}
}
