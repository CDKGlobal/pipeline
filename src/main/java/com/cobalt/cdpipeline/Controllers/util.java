package com.cobalt.cdpipeline.Controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class util {
	
	/**
	 * Returns a parsed xml Document from the given url
	 * @param urlString url to read xml data
	 * @return parsed xml Document
	 */
	public static Document getDomFromUrl(String urlString) {
		try{
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(conn.getInputStream());
			return doc;
		}catch(MalformedURLException e){
			e.printStackTrace();
			return null;
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}catch(ParserConfigurationException e){
			e.printStackTrace();
			return null;
		}catch(SAXException e){
			e.printStackTrace();
			return null;
		}
	}
}
