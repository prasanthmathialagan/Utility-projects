package com.novelty.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom2.DocType;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.novelty.xml.intf.Document;
import com.novelty.xml.intf.impl.DomDocument;
import com.novelty.xml.intf.impl.JDomDocument;

/**
 * 
 * @author Prasanth M
 *
 */
public class XMLUtils
{
	public static void write(Document doc,String fileName) throws Exception
	{
		write(doc, fileName, null);
	}
	
	public static void write(Document doc,String fileName,String dtd) throws Exception
	{
		if (doc.getParserType().equals(XMLParserType.DOM))
		{
			org.w3c.dom.Document d = ((DomDocument)doc).getDoc();
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");
			if (dtd != null)
				transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtd);
			DOMSource source = new DOMSource(d);
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			StreamResult result = new StreamResult(fos);
			transformer.transform(source, result);
		}
		else if(doc.getParserType().equals(XMLParserType.JDOM))
		{
			org.jdom2.Document d =((JDomDocument)doc).getDoc();
			if(dtd != null)
			{
				String root = null;
				try
				{
					root = d.getRootElement().getName();
				} 
				catch (Exception e){}
				if(root != null)
				{
					d.setDocType(new DocType(root, dtd));
				}
			}
			XMLOutputter xmlOutput = new XMLOutputter();

			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(d, new FileWriter(fileName));
		}
	}
}
