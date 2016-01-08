package com.novelty.xml.intf.impl;

import com.novelty.xml.TypeMismatchException;
import com.novelty.xml.XMLParserType;
import com.novelty.xml.intf.Document;
import com.novelty.xml.intf.Element;

public class JDomDocument implements Document
{
	protected org.jdom2.Document doc;
	
	public JDomDocument(org.jdom2.Document doc)
	{
		this.doc = doc;
	}

	@Override
	public Element getDocumentElement()
	{
		try
		{
			return new JDomElement(doc.getRootElement());
		}
		catch (Exception e){}
		
		return null;
	}

	@Override
	public Element createElement(String name)
	{
		return new JDomElement(new org.jdom2.Element(name));
	}

	@Override
	public void appendChild(Element e) throws TypeMismatchException
	{
		if (e instanceof JDomElement)
			doc.setRootElement(((JDomElement)e).element);
		else 
			throw new TypeMismatchException(e.getClass().getSimpleName() +" element cannot be appended to JDomDocument" );
	}

	@Override
	public XMLParserType getParserType()
	{
		return XMLParserType.JDOM;
	}

	public org.jdom2.Document getDoc()
	{
		return doc;
	}
}
