package com.novelty.xml.intf.impl;

import com.novelty.xml.TypeMismatchException;
import com.novelty.xml.XMLParserType;
import com.novelty.xml.intf.Document;
import com.novelty.xml.intf.Element;

public class DomDocument implements Document
{
	private org.w3c.dom.Document doc;
	
	public DomDocument(org.w3c.dom.Document doc)
	{
		this.doc = doc;
	}
	
	@Override
	public Element getDocumentElement()
	{
		return new DomElement(doc.getDocumentElement());
	}

	@Override
	public Element createElement(String name)
	{
		return new DomElement(doc.createElement(name));
	}

	@Override
	public void appendChild(Element e) throws TypeMismatchException
	{
		if (e instanceof DomElement)
			doc.appendChild(((DomElement)e).element);
		else 
			throw new TypeMismatchException(e.getClass().getSimpleName() +" element cannot be appended to DomDocument" );
	}

	@Override
	public XMLParserType getParserType()
	{
		return XMLParserType.DOM;
	}

	public org.w3c.dom.Document getDoc()
	{
		return doc;
	}
}
