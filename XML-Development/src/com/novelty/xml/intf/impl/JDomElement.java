package com.novelty.xml.intf.impl;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Comment;

import com.novelty.xml.TypeMismatchException;
import com.novelty.xml.intf.Element;

public class JDomElement implements Element
{
	protected org.jdom2.Element element;
	
	protected JDomElement(org.jdom2.Element element)
	{
		this.element = element;
	}
	
	@Override
	public String getAttribute(String attribute)
	{
		return element.getAttributeValue(attribute);
	}

	@Override
	public void setAttribute(String attribute, String value)
	{
		element.setAttribute(attribute, value);
	}

	@Override
	public void appendChild(Element e) throws TypeMismatchException
	{
		if (e instanceof JDomElement)
			element.addContent(((JDomElement)e).element);
		else 
			throw new TypeMismatchException(e.getClass().getSimpleName() +" element cannot be appended to JDomElement" );
	}

	@Override
	public void addComment(String comment)
	{
		element.addContent(new Comment(comment));
	}

	@Override
	public boolean isNull()
	{
		return element == null ? true:false;
	}

	public org.jdom2.Element getElement()
	{
		return element;
	}

	@Override
	public List<Element> getElementByTagName(String name)
	{
		List<Element> elements = new ArrayList<Element>();
		List<org.jdom2.Element> list = element.getChildren(name);
		for (org.jdom2.Element el : list)
		{
			elements.add(new JDomElement(el));
		}
		return elements;
	}
}
