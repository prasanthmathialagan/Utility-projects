package com.novelty.xml;

public class TypeMismatchException extends Exception
{
	private static final long serialVersionUID = 1L;

	public TypeMismatchException()
	{
		
	}
	
	public TypeMismatchException(String message)
	{
		super(message);
	}
	
	public TypeMismatchException(String message,Throwable e)
	{
		super(message,e);
	}
}
