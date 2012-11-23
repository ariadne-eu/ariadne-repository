package org.ariadne_eu.utils.registry;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class Results {
	
	List<MetadataCollection> _results;
	
	public Results(){
		_results = new ArrayList<MetadataCollection>();
	}
	
	public void addMetadataCollection(MetadataCollection metadataCollection){
		_results.add(metadataCollection);		
	}
	
	public List<MetadataCollection> getResults(){
		return _results;
	}
	
	public int getSizeMetadataCollection(){
		return _results.size();
	}
	
	public void parseXMLResults(String result){
		SAXBuilder builder = new SAXBuilder();
		Reader in = new StringReader(result);
		org.jdom.Document doc;
		try {
			doc = builder.build(in);
			Namespace ns = Namespace.getNamespace("http://www.imsglobal.org/services/lode/imsloreg_v1p0");
			List<Element> mdInstances = doc.getRootElement().getChildren("metadataCollection",ns);
			
			
			for(int i=0; i<mdInstances.size();i++){
				MetadataCollection metadataCollection = new MetadataCollection();
				metadataCollection.parseXMLMetadataCollection((org.jdom.Element) mdInstances.get(i),ns);
				addMetadataCollection(metadataCollection);				
			}
			

			
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
