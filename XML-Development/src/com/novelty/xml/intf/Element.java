package com.novelty.xml.intf;

import java.util.List;

import com.novelty.xml.TypeMismatchException;

public interface Element
{
	public String getAttribute(String attribute);
	public void setAttribute(String attribute,String value);
	public void appendChild(Element e) throws TypeMismatchException;
	public void addComment(String comment);
	public boolean isNull();
	public List<Element> getElementByTagName(String name);
}
