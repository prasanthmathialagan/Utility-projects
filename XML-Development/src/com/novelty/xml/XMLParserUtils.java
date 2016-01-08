package com.novelty.xml;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLParserUtils
{
	private static Logger logger = Logger.getLogger(XMLParserUtils.class);

	private XMLParserUtils()
	{}

	public static Document getDocument(File file) throws Exception
	{
		DocumentBuilder builder = getDocumentBuilder();
		FileInputStream fstream = new FileInputStream(file);
		InputSource inputSource = new InputSource(fstream);
		inputSource.setSystemId(file.toURI().toASCIIString());
		Document doc = builder.parse(inputSource);

		if(fstream != null)
			fstream.close();

		return doc;
	}

	private static DocumentBuilder getDocumentBuilder() throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setXIncludeAware(true);
		return getDocumentBuilder(factory);
	}

	private static DocumentBuilder getDocumentBuilder(DocumentBuilderFactory factory) throws Exception
	{
		if(factory == null)
			 factory = DocumentBuilderFactory.newInstance();

		return  factory.newDocumentBuilder();
	}

	public static Document getDocument(String fileName, String xsdPath) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		SchemaFactory sfactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		File schemaFile = new File(xsdPath);
		ParseErrorHandler errorHandler = new ParseErrorHandler();
		Schema schema = sfactory.newSchema(schemaFile);
		factory.setSchema(schema);

		DocumentBuilder db = getDocumentBuilder(factory);
		db.setErrorHandler(errorHandler);
		
		File file = new File(fileName);
		FileInputStream fstream = new FileInputStream(file);
		InputSource inputSource = new InputSource(fstream);
		inputSource.setSystemId(file.toURI().toASCIIString());
		Document doc = db.parse(inputSource);

		if(fstream != null)
			fstream.close();

		if (!errorHandler.isDocumentValid())
			throw errorHandler.getParseException();

		return doc;
	}

	public static Document getDocument(String fileName) throws Exception
	{
		return getDocument(new File(fileName));
	}

	public static Element getDocumentRoot(String fileName) throws Exception
	{
		return getDocumentRoot(new File(fileName));
	}

	public static Element getDocumentRoot(File file) throws Exception
	{
		return getDocument(file).getDocumentElement();
	}

	public static Element getDocumentElement(Document doc)
	{
		return doc.getDocumentElement();
	}

	public static Element[] getElementsByTagName(Document doc, String tagName)
	{
		return getElementsByTagName(doc.getElementsByTagName(tagName), tagName);
	}

	public static Element[] getElementsByTagName(NodeList nl, String tagName)
	{
		int l = nl.getLength();

		if (l == 0)
			return new Element[0];

		List<Element> list = new ArrayList<Element>();
		int c = nl.getLength();
		for (int i = 0; i < c; i++)
		{
			Node n = nl.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE)
				continue;

			Element e = (Element) n;
			if (e.getTagName().equals(tagName))
				list.add(e);
		}

		return list.toArray(new Element[0]);
	}

	public static Element[] getElementsByTagName(Element parent, String tagName)
	{
		if (parent == null)
			return new Element[0];

		NodeList nl = parent.getChildNodes(); //parent.getElementsByTagName(tagName);
		return getElementsByTagName(nl, tagName);
	}

	public static Element getElement(Element parent, String tagName)
	{
		Element[] es = getElementsByTagName(parent, tagName);
		return (es == null || es.length == 0) ? null : es[0];
	}

	public static boolean getAttributeValueAsBoolean(Element element, String attributeName)
	{
		String v = element.getAttribute(attributeName);
		return Boolean.valueOf(v);
	}

	public static int getAttributeValueAsInt(Element element, String attributeName)
	{
		String v = element.getAttribute(attributeName);
		return Integer.parseInt(v);
	}

	public static double getAttributeValueAsDouble(Element element, String attributeName)
	{
		String v = element.getAttribute(attributeName);
		return Double.parseDouble(v);
	}

	public static long getAttributeValueAsLong(Element element, String attributeName)
	{
		String v = element.getAttribute(attributeName);
		return Long.parseLong(v);
	}

	private static class ParseErrorHandler implements ErrorHandler
	{

		private Exception parseException = null;
		private boolean isValid = true;

		@Override
		public void warning(SAXParseException exception) throws SAXException
		{
			logger.info("exception = " + exception);
			parseException = exception;
			isValid = false;
		}

		@Override
		public void fatalError(SAXParseException exception) throws SAXException
		{
			logger.info("exception = " + exception);
			parseException = exception;
			isValid = false;
		}

		@Override
		public void error(SAXParseException exception) throws SAXException
		{
			logger.info("exception = " + exception);
			parseException = exception;
			isValid = false;
		}

		public boolean isDocumentValid()
		{
			return isValid;
		}

		public Exception getParseException()
		{
			return parseException;
		}
	}
}
/**
 *  Source : OTNMS Module XMLParserUtils
 *  @author  K Adithyan
*/
