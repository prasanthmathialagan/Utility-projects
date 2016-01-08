package com.novelty.xml.intf;

import com.novelty.xml.TypeMismatchException;
import com.novelty.xml.XMLParserType;

/**
 * 
 * @author Prasanth M
 *
 */
public interface Document
{
	public Element getDocumentElement();
	public Element createElement(String name);
	public void appendChild(Element e) throws TypeMismatchException;
	public XMLParserType getParserType();
}
