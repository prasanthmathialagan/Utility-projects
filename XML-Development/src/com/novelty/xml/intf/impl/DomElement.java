package com.novelty.xml.intf.impl;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NodeList;

import com.novelty.xml.TypeMismatchException;
import com.novelty.xml.intf.Element;

public class DomElement implements Element 
{
	protected org.w3c.dom.Element element;
	
	protected DomElement(org.w3c.dom.Element element)
	{
		this.element = element;
	}
	
	@Override
	public String getAttribute(String attribute)
	{
		return element.getAttribute(attribute);
	}

	@Override
	public void setAttribute(String attribute, String value)
	{
		element.setAttribute(attribute, value);
	}

	@Override
	public void appendChild(Element e) throws TypeMismatchException
	{
		if (e instanceof DomElement)
			element.appendChild(((DomElement)e).element);
		else 
			throw new TypeMismatchException(e.getClass().getSimpleName() +" element cannot be appended to DomElement" );
	}

	@Override
	public void addComment(String comment)
	{
		element.appendChild(element.getOwnerDocument().createComment(comment));
	}

	@Override
	public boolean isNull()
	{
		return element == null ? true:false;
	}

	public org.w3c.dom.Element getElement()
	{
		return element;
	}

	@Override
	public List<Element> getElementByTagName(String name)
	{
		List<Element> elements = new ArrayList<Element>();
		NodeList nd = element.getElementsByTagName(name);
		if(nd == null)
			return elements;
		for (int i = 0; i < nd.getLength(); i++)
		{
			org.w3c.dom.Element el = (org.w3c.dom.Element) nd.item(i);
			elements.add(new DomElement(el));
		}
		return elements;
	}
}
