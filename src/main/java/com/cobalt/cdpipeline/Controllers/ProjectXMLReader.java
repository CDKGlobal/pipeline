package com.cobalt.cdpipeline.Controllers;

import java.io.IOException;
import java.security.DomainCombiner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ProjectXMLReader {
	public static final String COMMON_URL = "/rest/api/latest/";
	/*
	public static String host = "localhost";
	public static String port = "6990";
	public static String host = "dc2pvpdc00155.vcac.dc2.dsghost.net";
	public static String port  = "8085";
	*/
	
	// public static String host = "localhost:6990/bamboo";
	public static String host = "dc2pvpdc00155.vcac.dc2.dsghost.net:8085";
			
	public static void main (String[] args) {

		
		/*
		Document doc = util.getDomFromUrl("http://" + host + ":" + port + COMMON_URL + "project");
		parseDocument(doc);
		*/
		
		parseDocument();
		
	}
	
	
	//public static void parseDocument(Document dom) {
	public static void parseDocument() {
		Document dom = util.getDomFromUrl("http://" + host + COMMON_URL + "project");

		// get the root element
		Element docEle = dom.getDocumentElement();
		
		
		// get a list of elements
		NodeList nl = docEle.getElementsByTagName("project");
		if (nl != null) {
			// get the project element
			for (int i = 0; i < nl.getLength(); i++) {
				Element e = (Element)nl.item(i);
				// get the project object
				String projectName = e.getAttribute("name");
				String projectKey = e.getAttribute("key");				
				System.out.println(projectName + "\t" + projectKey);
				getPlans(projectKey);
				
			}
		}
		
	}
	
	public static void getPlans(String projectKey) {
		Document planDoc = util.getDomFromUrl("http://" + host + COMMON_URL + "project/" + projectKey +
				"?expand=plans.plan");
		Element docEle = planDoc.getDocumentElement();
		
		// get a list of elements
		NodeList nl = docEle.getElementsByTagName("plan");
		if (nl != null) {
			// get the project element
			for (int i = 0; i < nl.getLength(); i++) {
				Element e = (Element)nl.item(i);
				// get the project object
				String planName = e.getAttribute("shortName");
				String planKey = e.getAttribute("key");				
				
				System.out.println("\t\t\t\t" + planName + "\t" + planKey);
							
			}
		}
	}
}
