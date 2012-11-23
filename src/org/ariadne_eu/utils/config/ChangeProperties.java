package org.ariadne_eu.utils.config;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.ariadne.config.PropertiesManager;


public class ChangeProperties {
   public static void main(String[] args) {
       try {
           PropertiesManager.getInstance().init("jsp/install/ariadne.properties");
           PropertiesManager.getInstance().saveProperty(args[0],args[1]);//"generate.xmlLocation", "/localhost/projects/melt/ja-to-50-base/webapps/melt-zipped-logs/MELTstats.xml");
       } catch (FileNotFoundException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
   }
}
