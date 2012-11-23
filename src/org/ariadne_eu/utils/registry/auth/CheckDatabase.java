package org.ariadne_eu.utils.registry.auth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;


public class CheckDatabase {

	
	static public boolean isAdmin(String email){
		Connection con = null;
	    String driver = "com.mysql.jdbc.Driver";
	    try{
		      Class.forName(driver);
		      con = DriverManager.getConnection(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_DATABASE_URL),PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_DATABASE_USER),PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_DATABASE_PASSWORD));
		      Statement st = con.createStatement();
		      try{
		        
		        ResultSet result = st.executeQuery("select * from Admin where email='"+email+"'");
		        while (result.next()){
		        	return true;
		        }
		        
		      }
		      catch (SQLException s){
		      }
		      con.close();
	    }catch (Exception e){
		      e.printStackTrace();
	    }
	    return false;
	}
	
	static public boolean isInBlackList(String email){
		Connection con = null;
	    String driver = "com.mysql.jdbc.Driver";
	    try{
		      Class.forName(driver);
		      con = DriverManager.getConnection(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_DATABASE_URL),PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_DATABASE_USER),PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_DATABASE_PASSWORD));
		      Statement st = con.createStatement();
		      try{
		        
		        ResultSet result = st.executeQuery("select * from BlackList where email='"+email+"'");
		        while (result.next()){
		        	return true;
		        }
		        
		      }
		      catch (SQLException s){
		      }
		      con.close();
	    }catch (Exception e){
		      e.printStackTrace();
	    }
	    return false;
	}
	
	static public boolean isOwner(String email, String identifier){
		Connection con = null;
	    String driver = "com.mysql.jdbc.Driver";
	    try{
		      Class.forName(driver);
		      con = DriverManager.getConnection(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_DATABASE_URL),PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_DATABASE_USER),PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_DATABASE_PASSWORD));
		      Statement st = con.createStatement();
		      try{
		        
		        ResultSet result = st.executeQuery("select * from Collections where email='"+email+"' and identifier='"+identifier+"'");
		        while (result.next()){
		        	return true;
		        }
		        
		      }
		      catch (SQLException s){
		      }
		      con.close();
	    }catch (Exception e){
		      e.printStackTrace();
	    }
	    return false;
	}
	
	static public void insertCollection(String email, String identifier){
		Connection con = null;
	    String driver = "com.mysql.jdbc.Driver";
	    try{
		      Class.forName(driver);
		      con = DriverManager.getConnection(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_DATABASE_URL),PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_DATABASE_USER),PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_DATABASE_PASSWORD));
		      Statement st = con.createStatement();
		      try{
		    	  int val = st.executeUpdate("INSERT Collections(email, identifier) VALUES('"+email+"','"+identifier+"')");
		      }
		      catch (SQLException s){
		      }
		      con.close();
	    }catch (Exception e){
		      e.printStackTrace();
	    }
	}
}
