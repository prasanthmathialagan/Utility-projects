package com.novelty.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.SAXException;

import com.novelty.xml.intf.Document;
import com.novelty.xml.intf.impl.DomDocument;
import com.novelty.xml.intf.impl.JDomDocument;

public class DocumentBuilderFactory
{
	private static Logger logger = Logger.getLogger(DocumentBuilderFactory.class);
	public static Document defaultDocument(String fileName) throws SAXException, IOException, ParserConfigurationException, JDOMException
	{
		return getDocument(fileName, XMLParserType.DOM);
	}
	
	public static Document getDocument(String fileName,XMLParserType parserType) throws SAXException, IOException, ParserConfigurationException, JDOMException
	{
		switch (parserType)
		{
		case JDOM:
			return getJDomDocument(fileName);
		default:
			return getDomDocument(fileName);
		}
	}

	private static Document getDomDocument(String fileName) throws SAXException, IOException, ParserConfigurationException
	{
		javax.xml.parsers.DocumentBuilderFactory documentBuilderFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		javax.xml.parsers.DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
		org.w3c.dom.Document doc = null;
		File file = new File(fileName);
		
		if(!file.exists())
			doc = db.newDocument();
		else
		{
			try
			{
				doc = db.parse(new File(fileName));
			} 
			catch (SAXException e)
			{
				logger.error("", e);
				doc = db.newDocument();
			}
		}

		return new DomDocument(doc);
	}
	
	private static Document getJDomDocument(String fileName) throws JDOMException, IOException
	{
		File file = new File(fileName);
		org.jdom2.Document doc = null;
		if(!file.exists())
			doc = new org.jdom2.Document();
		else
		{
			try
			{
				SAXBuilder builder = new SAXBuilder();
				doc = builder.build(new File(fileName));
			} 
			catch (JDOMException e)
			{
				logger.error("", e);
				doc = new org.jdom2.Document();
			} 
		}
		return new JDomDocument(doc);
	}
	
}
