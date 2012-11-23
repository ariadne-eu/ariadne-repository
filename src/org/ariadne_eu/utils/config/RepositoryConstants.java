	/**
 * 
 */
package org.ariadne_eu.utils.config;

import org.apache.lucene.util.Version;
import org.ariadne.config.Constants;

/**
 * @author gonzalo
 *
 */
public class RepositoryConstants extends Constants {
	
	public static RepositoryConstants getInstance() {
		return (RepositoryConstants)constants;
	}
	
	public String REPO_USERNAME = "repository.username";
	public String REPO_PASSWORD = "repository.password";
	public String REPO_STATICKEY = "repository.statickey";
	public String REPO_UPDATE_TARGET = "repository.username";
	public String REPO_UPDATE_SESSION = "repository.password";
	public String REPO_LOG4J_DIR = "repository.log4j.directory";
	public String REPO_LOG4J_FILENAME = "repository.log4j.filename";

	public String REG_CATALOG = "registry.catalog";
	public String REG_URL = "registry.url";
	public String REG_AUTH = "registry.auth";
	public String REG_DATABASE_URL = "registry.database";
	public String REG_DATABASE_USER = "registry.database.user";
	public String REG_DATABASE_PASSWORD = "registry.database.password";
	public String REG_LOGIN = "registry.login";
	
	public Version SR_LUCENE_VERSION = Version.LUCENE_29;
	public String SR_LUCENE_INDEXDIR = "search.lucene.indexdir";
	public String SR_LUCENE_INMEMORY = "search.lucene.inmemory";
	public String SR_LUCENE_HANDLER = "search.lucene.handler";
	public String SR_LUCENE_ANALYZER = "search.lucene.analyzer";
	public String SR_LUCENE_REINDEX = "search.lucene.reindex";
	public String SR_LUCENE_REINDEX_MAXQRYRESULTS = "search.lucene.reindex.maxqueryresults";
	public String SR_XPATH_QRY_ID = "search.xpath.query.identifier";
	public String SR_LUCENE_HANDLER_MACE = "search.lucene.handler.mace";
	public String SR_SOLR_DATADIR = "search.solr.dataDir";
//	public String SR_SOLR_INSTANCEDIR = "search.solr.instancedir";
	public String SR_SOLR_FACETFIELD = "search.solr.facetfield";
	
	public String MD_INSERT_IMPLEMENTATION = "mdstore.insert.implementation";
	public String MD_DELETE_IMPLEMENTATION = "mdstore.delete.implementation";
	public String MD_QUERY_IMPLEMENTATION = "mdstore.query.implementation";
	
	public String MD_SPIFS_DIR = "mdstore.spifs.dir";
	
	public String MD_SPIFWD_SM_URL = "mdstore.spiforward.sm.url";
	public String MD_SPIFWD_SPI_URL = "mdstore.spiforward.spi.url";
	public String MD_SPIFWD_SM_USERNAME = "mdstore.spiforward.sm.username";
	public String MD_SPIFWD_SM_PASSWORD = "mdstore.spiforward.sm.password";
	
	public String MD_INSERT_XMLNS_XSD = "mdstore.insert.xmlns.xsd";
	public String MD_XQUERY_WHOLEWORD = "mdstore.xquery.wholeword";
//	public String MD_RF_RLOM_URL = "mdstore.rf.rlom.url";
//	public String MD_RF_RLOM_RMETRIC = "mdstore.rf.rlom.rankingmetric";
	
	public String CNT_RETREIVE_IMPLEMENTATION = "cntstore.retrieve.implementation";
	public String CNT_INSERT_IMPLEMENTATION = "cntstore.insert.implementation";
	public String CNT_DB_URI = "cntstore.db.uri";
	public String CNT_DB_USERNAME = "cntstore.db.username";
	public String CNT_DB_PASSWORD = "cntstore.db.password";
	public String CNT_DB_XMLDB_SQL_TABLENAME = "cntstore.db.xmldb.sql.tablename";
	public String CNT_DB_XMLDB_SQL_COLUMNNAME = "cntstore.db.xmldb.sql.columnname";
	public String CNT_DB_XMLDB_SQL_IDCOLUMNNAME = "cntstore.db.xmldb.sql.idcolumnname";
	public String CNT_DR_BASEPATH = "cntstore.dr.basepath";
	public String CNT_SPIFWD_SM_URL = "cntstore.spiforward.sm.url";
	public String CNT_SPIFWD_SPI_URL = "cntstore.spiforward.spi.url";
	public String CNT_SPIFWD_SM_USERNAME = "cntstore.spiforward.sm.username";
	public String CNT_SPIFWD_SM_PASSWORD = "cntstore.spiforward.sm.password";
	public String CNT_MD_XPATHQRY_LOCATION = "cntstore.md.xpathquery.location";
	public String CNT_LUCENE_INDEXDIR = "cntstore.lucene.indexdir";

	public String OAICAT_SERVER_CATALOG_MDFIELD = "oaicat.server.catalog.field.md";
	public String OAICAT_SERVER_CATALOG_DATEFIELD = "oaicat.server.catalog.field.date";
	public String OAICAT_SERVER_CATALOG_IDFIELD = "oaicat.server.catalog.field.id";
	public String OAICAT_SERVER_CATALOG_SETFIELD = "oaicat.server.catalog.field.set";
	
	public String MACE_OAI_ALOE_TARGET = "mace.oai.aloe.target";
	public String MACE_OAI_ALOE_MDPREFIX = "mace.oai.aloe.mdprefix";
	
	public String HEADER_X_FORWARDED_FOR ="X-FORWARDED-FOR";

	
	public RepositoryConstants() {
		
		REPO_LOG4J_DIR = "repository.log4j.directory";
		REPO_LOG4J_FILENAME = "repository.log4j.filename";

		MD_DB_URI = "mdstore.db.uri";
		MD_DB_USERNAME = "mdstore.db.username";
		MD_DB_PASSWORD = "mdstore.db.password";
		MD_DB_XMLDB_LOC = "mdstore.db.xmldb.loc";
		MD_DB_XMLDB_SQL_TABLENAME = "mdstore.db.xmldb.sql.tablename";
		MD_DB_XMLDB_SQL_COLUMNNAME = "mdstore.db.xmldb.sql.columnname";
		MD_DB_XMLDB_SQL_IDCOLUMNNAME = "mdstore.db.xmldb.sql.idcolumnname";
		MD_DB_XMLDB_SQL_DATECOLUMNNAME = "mdstore.db.xmldb.sql.datecolumnname"; //TODO : check in oaitarget SQL database implementation
		
		OAICAT_SERVER_CATALOG_SEC2LIVE = "oaicat.server.catalog.seconds2live";
		OAICAT_SERVER_CATALOG_GRANULARITY = "oaicat.server.catalog.granularity";
		
		OAICAT_IDENTIFY_EMAIL = "oaicat.identify.email";
		OAICAT_IDENTIFY_REPONAME = "oaicat.identify.reponame";
		OAICAT_IDENTIFY_EARLYDATE = "oaicat.identify.earliestdatestamp";
		OAICAT_IDENTIFY_DELREC = "oaicat.identify.deletedrecord";
		OAICAT_IDENTIFY_REPOID = "oaicat.identify.repoid";
		OAICAT_IDENTIFY_REPODESC = "oaicat.identify.description"; 
		OAICAT_IDENTIFY_SAMPLEID = "oaicat.identify.sampleid";

		OAICAT_CROSSWALK = "oaicat.crosswalk";
		
		OAICAT_SERVER_CATALOG_MAXLSTSIZE = "oaicat.server.catalog.maxlistsize";
		OAICAT_SERVER_CATALOG_CLASS = "oaicat.server.catalog.class";
		OAICAT_SERVER_CATALOG_RECORD_CLASS = "oaicat.server.catalog.record.class";

		OAICAT_SERVER_CATALOG_FS_EXT = "oaicat.server.catalog.fs.ext";
		OAICAT_SERVER_CATALOG_FS_DIR = MD_SPIFS_DIR;
		
		OAICAT_SETS = "oaicat.sets";
		OAICAT_SETS_ID = "repoid";

		OAICAT_HANDLER_BASEURL = "oaicat.handler.baseuRL";
		OAICAT_HANDLER_STYLESHEET = "oaicat.handler.stylesheet";
		OAICAT_HANDLER_OLDBROWSER = "oaicat.handler.render4oldbrowsers";
		OAICAT_HANDLER_USEIDSHEME = "oaicat.handler.useoaischeme";

		OAICAT_HANDLER_EXTRAXMLNS = "oaicat.handler.extraXmlns";
		OAICAT_HANDLER_XMLENCODESETSPEC = "oaicat.handler.xmlEncodeSetSpec";
		OAICAT_HANDLER_URLENCODESETSPEC = "oaicat.handler.urlEncodeSetSpec";
		OAICAT_HANDLER_SERVICEUNAVAILABLE = "oaicat.handler.serviceUnavailable";
		OAICAT_HANDLER_FORCERENDER = "oaicat.handler.forceRender";
		OAICAT_HANDLER_MONITOR = "oaicat.handler.monitor";
		OAICAT_HANDLER_APPBASE = "oaicat.handler.appBase";
		OAICAT_HANDLER_EXTENSIONPATH = "oaicat.handler.extensionPath";
		OAICAT_HANDLER_DEBUG = "oaicat.handler.debug";
	}
	
	


}
