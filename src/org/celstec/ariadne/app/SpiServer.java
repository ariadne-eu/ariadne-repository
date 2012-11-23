package org.celstec.ariadne.app;

import java.io.IOException;
import java.io.InputStream;

import org.ariadne.config.PropertiesManager;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.purl.sword.atom.ContentType;
import org.purl.sword.atom.Link;
import org.purl.sword.atom.Summary;
import org.purl.sword.atom.Title;
import org.purl.sword.base.AtomDocumentRequest;
import org.purl.sword.base.AtomDocumentResponse;
import org.purl.sword.base.Deposit;
import org.purl.sword.base.DepositResponse;
import org.purl.sword.base.SWORDAuthenticationException;
import org.purl.sword.base.SWORDEntry;
import org.purl.sword.base.SWORDErrorException;
import org.purl.sword.base.SWORDException;
import org.purl.sword.base.ServiceDocument;
import org.purl.sword.base.ServiceDocumentRequest;

public class SpiServer implements SpiSwordServer {

	private SpiGateway sg = new SpiGateway();

	public DepositResponse doDeposit(Deposit deposit) throws SWORDAuthenticationException, SWORDException {
		if (deposit.getContentType() != null && deposit.getContentType().contains("application/atom+xml"))
			return doMetadataDeposit(deposit);
		return doResourceDeposit(deposit);
	}

	public void doPut(Deposit deposit) throws SWORDAuthenticationException, SWORDErrorException, SWORDException {
		if (((SpiDeposit) deposit).getContextPath().contains("deposit/metadata")) {
			putMetadata(deposit);
		}
		if (((SpiDeposit) deposit).getContextPath().contains("deposit/resource")) {
			putResource(deposit);
		}
	}

	public void putMetadata(Deposit deposit) {
		Element atom = getXmlFromDeposit(deposit);
		Element id = atom.getChild("id", Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom"));
		String resourceId = "";
		String authorizationToken = "";
		String metadataSchemaId = ((SpiDeposit) deposit).getSchema();
		if (id != null) {
			resourceId = id.getText();
		}
		String metadataId = "";
		String contextPath = ((SpiDeposit) deposit).getContextPath();
		if (contextPath.contains("deposit/metadataForMid")) {
			resourceId = contextPath.substring(contextPath.indexOf("deposit/metadataForMid") + 23);
		}
		if (contextPath.contains("deposit/metadataForRid")) {
			metadataId = contextPath.substring(contextPath.indexOf("deposit/metadataForRid") + 23);
		}
		sg.submitMetadata(authorizationToken, metadataId, resourceId, getMetadataFromDeposit(atom), metadataSchemaId, null);

	}

	public void putResource(Deposit deposit) {
		String authorizationToken = deposit.getUsername() + deposit.getPassword();
		String resourceId = "";
		String contextPath = ((SpiDeposit) deposit).getContextPath();
		if (contextPath.contains("deposit/resource")) {
			resourceId = contextPath.substring(contextPath.indexOf("deposit/resource") + 17);
		}
		InputStream resource = deposit.getFile();
		String packageType = deposit.getPackaging();
		String contentType = deposit.getContentType();
		String collection = "";
		String filename = deposit.getFilename();
		SpiGateway sg = new SpiGateway();
		sg.submitResource(authorizationToken, resourceId, resource, packageType, contentType, collection, filename);
	}

	public ServiceDocument doServiceDocument(ServiceDocumentRequest arg0) throws SWORDAuthenticationException, SWORDException {
		// TODO Auto-generated method stub
		ServiceDocument document = new AriadneServiceDocument();
		return document;
	}

	private DepositResponse doMetadataDeposit(Deposit deposit) {
		Element atom = getXmlFromDeposit(deposit);
		Element id = atom.getChild("id", Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom"));
		Element titleElement = atom.getChild("title", Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom"));
		String resourceId = "";
		if (id != null) {
			resourceId = id.getText();
		}

		String metadataSchemaId = ((SpiDeposit) deposit).getSchema();
		String authorizationToken = "";
		String metadataIdentifier = ((SpiDeposit) deposit).getXIdentifier();
		String metadataId = sg.submitMetadata(authorizationToken, metadataIdentifier, resourceId, getMetadataFromDeposit(atom), metadataSchemaId, null);
		DepositResponse dr = new DepositResponse(Deposit.CREATED);
		String title = getTitleFromLom(atom);
		if (title == null && titleElement != null)
			title = titleElement.getText();
		String mediaEditLinkEntry = "";
		if (!resourceId.equals(""))
			mediaEditLinkEntry = PropertiesManager.getInstance().getProperty("app.baseURL") + "/deposit/resource/" + resourceId;
		String editLinkEntry = PropertiesManager.getInstance().getProperty("app.baseURL") + "/deposit/metadataForMid/" + metadataId;

		dr.setEntry(createReturnEntry(title, resourceId, mediaEditLinkEntry, editLinkEntry));
		dr.setLocation(PropertiesManager.getInstance().getProperty("app.baseURL") + "/services/oai?verb=GetRecord&metadataPrefix=oai_lom&identifier=" + metadataIdentifier);
		return dr;
	}

	private DepositResponse doResourceDeposit(Deposit deposit) {
		System.out.println("data received");
		String authorizationToken = deposit.getUsername() + deposit.getPassword();
		String resourceIdentifier = ((SpiDeposit) deposit).getXIdentifier();
		InputStream resource = deposit.getFile();
		String packageType = deposit.getPackaging();
		String contentType = deposit.getContentType();
		String collection = "";
		String filename = deposit.getFilename();
		SpiGateway sg = new SpiGateway();
		sg.submitResource(authorizationToken, resourceIdentifier, resource, packageType, contentType, collection, filename);
		DepositResponse dr = new DepositResponse(Deposit.CREATED);
		dr.setLocation("http://localhost:80/todo");
		String mediaEditLinkEntry = PropertiesManager.getInstance().getProperty("app.baseURL") + "/deposit/resource/" + resourceIdentifier;
		String editLinkEntry = PropertiesManager.getInstance().getProperty("app.baseURL") + "/deposit/metadataForRid/" + resourceIdentifier;
		dr.setEntry(createReturnEntry(deposit.getSlug(), resourceIdentifier, mediaEditLinkEntry, editLinkEntry));

		return dr;
	}

	private SWORDEntry createReturnEntry(String title, String identifier, String mediaEditLinkEntry, String editLinkEntry) {
		SWORDEntry entry = new SWORDEntry();
		Title t = new Title();
		if (title == null)
			title = "no title";
		t.setContent(title);
		t.setType(ContentType.TEXT);
		entry.setTitle(t);
		if (identifier == null)
			identifier = SpiGateway.generateIdentifier();
		entry.setId(identifier);
		Summary summary = new Summary();
		entry.setSummary(summary);

		Link em = new Link();
		em.setRel("edit-media");
		em.setHref(mediaEditLinkEntry);
		entry.addLink(em);
		Link e = new Link();
		e.setRel("edit");
		e.setHref(editLinkEntry);
		entry.addLink(e);

		return entry;
	}

	public AtomDocumentResponse doAtomDocument(AtomDocumentRequest arg0) throws SWORDAuthenticationException, SWORDErrorException, SWORDException {
		// TODO Auto-generated method stub
		return null;
	}

	private Element getXmlFromDeposit(Deposit deposit) {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(deposit.getFile());
		} catch (JDOMException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc.getRootElement();
	}

	private Element getMetadataFromDeposit(Element atom) {
		if (atom == null)
			return null;
		Element spi = atom.getChild("metadata", Namespace.getNamespace("spi", "http://www.cenorm.be/xsd/SPI"));
		if (spi == null)
			return null;
		Element lom = spi.getChild("lom", Namespace.getNamespace("lom", "http://ltsc.ieee.org/xsd/LOM"));
		if (lom == null)
			return null;
		return lom;
	}

	private String getTitleFromLom(Element atom) {
		Element lom = getMetadataFromDeposit(atom);
		Element general = lom.getChild("lom", Namespace.getNamespace("lom", "http://ltsc.ieee.org/xsd/LOM"));
		if (general == null)
			return null;
		Element identifier = lom.getChild("identifier", Namespace.getNamespace("lom", "http://ltsc.ieee.org/xsd/LOM"));
		if (identifier == null)
			return null;
		Element entry = lom.getChild("entry", Namespace.getNamespace("lom", "http://ltsc.ieee.org/xsd/LOM"));
		if (entry == null)
			return null;
		return entry.getText();
	}

}
