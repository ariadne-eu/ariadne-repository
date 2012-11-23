package org.ariadne_eu.oai.server.lucene.crosswalk;

import java.util.List;
import java.util.Properties;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.ariadne.oai.utils.OaiUtils;
//import org.ariadne.util.JDomUtils;
//import org.ariadne_eu.oai.utils.ESEOaiUtils;
//import org.ariadne_eu.utils.config.RepositoryConstants;
import org.ariadne_eu.oai.utils.ESEOaiUtils;
import org.oclc.oai.server.crosswalk.Crosswalk;
import org.oclc.oai.server.verb.CannotDisseminateFormatException;
import org.jdom.Element;
import org.jdom.Namespace;

public class Lucene2oai_ese extends Crosswalk {

        protected static Namespace esens = Namespace.getNamespace("http://www.europeana.eu/schemas/ese/");
        protected static Namespace europeanans  = Namespace.getNamespace("europeana","http://www.europeana.eu/schemas/ese/");
	protected static Namespace dcns = Namespace.getNamespace("dc","http://purl.org/dc/elements/1.1/");
        protected static Namespace dctermsns= Namespace.getNamespace("dcterms", "http://purl.org/dc/terms/");

        protected static Namespace xsi = Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");


	public Lucene2oai_ese(Properties properties) {
		super("http://www.europeana.eu/schemas/ese/ http://www.europeana.eu/schemas/ese/ESE-V3.4.xsd");
		String classname = "Lucene2oai_ese";
/*		fullLomField = properties.getProperty(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_MDFIELD);
		if (fullLomField == null) {
		    throw new IllegalArgumentException(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_MDFIELD + " is missing from the properties file");
		}*/
	}

	public String createMetadata(Object nativeItem) throws CannotDisseminateFormatException {                                              
            //Cast the nativeItem to your object
		Document doc = (Document)nativeItem; 
                List fieldsList = doc.getFields();

            
                List<org.apache.lucene.document.Field > fieldsNodes = doc.getFields();
                for (org.apache.lucene.document.Field element : fieldsNodes)
                {
                    System.out.println("The fields are:"+element.toString() );
                }

		Element record = new Element("record", esens);
                record.addNamespaceDeclaration(europeanans);
                record.addNamespaceDeclaration(dcns);

		record.setAttribute("schemaLocation","http://www.europeana.eu/schemas/ese/ http://www.europeana.eu/schemas/ese/ESE-V3.4.xsd",xsi);
              //  record.setAttribute("dc","http://purl.org/dc/elements/1.1/",dcns);


                //dc:identifier
                Element identifier = new Element("identifier",dcns);
                record.addContent(identifier);

                if (doc.getField("lom.general.identifier.entry")!= null)
                {
                String idString = doc.getField("lom.general.identifier.entry").stringValue();
                if (idString != null)
                    identifier.setText(idString);
                }

                //dc:title
                Element title = new Element("title",dcns);
                record.addContent(title);

                if (doc.getField("lom.general.title.string") != null)
                {
                String titleString = doc.getField("lom.general.title.string").stringValue();
                 if (titleString != null)
                    title.setText(titleString);
                }

                
                //dc:language
               
                if (doc.getField("lom.general.language") != null)
                {

                Element language = new Element("language",dcns);
                record.addContent(language);

                String languageSrt = doc.getField("lom.general.language").stringValue();
                 if (languageSrt != null)
                    language.setText(languageSrt);
                }

                //dc:description
                Element descr = new Element("description", dcns);
                record.addContent(descr);
                
               if (doc.getField("lom.general.description.string")!=null)
               {
                String descrString = doc.getField("lom.general.description.string").stringValue();
                if (descrString != null)
                 descr.setText(descrString);
               }
                
                
                //dc:subject
                Field[] keyNodes = doc.getFields("lom.general.keyword.string");
                for (org.apache.lucene.document.Field element : keyNodes)
                {
                    String theString = element.stringValue();
                    if(theString != null && !theString.trim().isEmpty()) {
					Element subject = new Element("subject", dcns);
                                        record.addContent(subject);
                                        subject.setText(theString);
				}
                }

                //dc:format
                Element format = new Element("format",dcns);
                record.addContent(format);

                if (doc.getField("lom.technical.format") != null)
                {
                String formatString = doc.getField("lom.technical.format").stringValue();
                 if (formatString != null)
                    format.setText(formatString);
                }


                //dcterms:spatial
                if (doc.getFields("lom.general.coverage.string")!=null)
                {
                    Field[] spatNodes = doc.getFields("lom.general.coverage.string");
                    for (org.apache.lucene.document.Field element : spatNodes)
                    {
                        String theString = element.stringValue();
                        if(theString != null && !theString.trim().isEmpty()) {
                                            Element coverage = new Element("spatial", dctermsns);
                                            record.addContent(coverage);
                                            coverage.setText(theString);
                                    }
                    }   
                }

                //dcterms:temploral
                if (doc.getFields("lom.educational.typicalAgeRange")!=null)
                {
                    Field[] tempNodes = doc.getFields("lom.educational.typicalAgeRange");
                    for (org.apache.lucene.document.Field element : tempNodes)
                    {
                        String theString = element.stringValue();
                        if(theString != null && !theString.trim().isEmpty()) {
                                            Element temporal = new Element("temporal", dctermsns);
                                            record.addContent(temporal);
                                            temporal.setText(theString);
                                    }
                    }
                }

               //europeana:object
               
               if (doc.getField("lom.technical.duration") != null)
                {
                Element object = new Element("object",europeanans);
                record.addContent(object);
                String thumbString = doc.getField("lom.technical.duration").stringValue();
                if (thumbString != null)
                 object.setText(thumbString);
               }

                //europeana:provider
                Element provider = new Element("provider",europeanans);
                record.addContent(provider);
                provider.setText("The Natural Europe Project");

                // europeana:type
                // When europeana:type is "TEXT" then a value for "dc:language must be provided".
                Element type_eu = new Element ("type",europeanans);
                record.addContent(type_eu);

                if (doc.getField("lom.technical.format") != null)
                {
                    String typeString = doc.getField("lom.technical.format").stringValue();
                    type_eu.setText(typeString);

                    if (typeString != null)
                    {
                        if (typeString.contains("image")|| typeString.contains("jpeg") || typeString.contains("jpg"))
                             type_eu.setText("IMAGE");
                        else if (typeString.contains("video"))
                            type_eu.setText("VIDEO");
                        else if (typeString.contains("pdf") || typeString.contains("text") )
                            type_eu.setText("TEXT");
                    }
                }
    

               //europeana:rights
                Element rights = new Element ("rights",europeanans);
                record.addContent(rights);

             if (doc.getField("lom.rights.copyrightandotherrestrictions.string") != null)
                {
                String rightsString = doc.getField("lom.rights.copyrightandotherrestrictions.string").stringValue();
                rightsString = "http://creativecommons.org/licenses/"+rightsString+"/3.0/";
                rights.setText(rightsString);
            }
                

                //europeana:dataProvider
                Element dataProvider = new Element ("dataProvider",europeanans);
                record.addContent(dataProvider);
                
                if (doc.getField("lom.metametadata.identifier.catalog")!=null)
                 {
                String dataProviderString = doc.getField("lom.metametadata.identifier.catalog").stringValue();
                if (dataProviderString != null)
                 dataProvider.setText(dataProviderString);   }


                //europeana:isShownBy
                Element isShownBy = new Element ("isShownBy",europeanans);
                record.addContent(isShownBy);
                if (doc.getField("lom.technical.location") != null)
                {
                String locationString = doc.getField("lom.technical.location").stringValue();
                if (locationString != null)
                 isShownBy.setText(locationString);
               }
                //europeana:isShownAt

           
                
                
		String result = "";
		result = OaiUtils.parseLom2Xmlstring(record);
		return result;

	}

	public boolean isAvailableFor(Object arg0) {
		return true;
	}

}