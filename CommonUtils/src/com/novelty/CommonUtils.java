package com.novelty;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Prasanth M
 *
 */
public class CommonUtils
{
	public static String getCommaSeparatedValues(Collection<String> coll)
	{
		return getDelimitedStringOfValues(coll, ',', false);
	}
	
	public static String getDelimitedStringOfValues(Collection<String> coll, char delimiter, boolean isQuotesRequired)
	{
		if (coll == null || coll.isEmpty())
			return "";

		int size = coll.size();
		int i = 0;
		
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> iterator = coll.iterator(); iterator.hasNext();)
		{
			String s = iterator.next();
			
			if(isQuotesRequired)
				sb.append("'");
			
			sb.append(s);
			
			if(isQuotesRequired)
				sb.append("'");
			
			if (i != size - 1)
				sb.append(delimiter);
			
			i++;
		}
		return sb.toString();
	}
	
	public static String[] splitOnLastSlash(String s)
	{
		if(s == null)
			throw new NullPointerException("String cannot be null!!");
		
		int index = s.lastIndexOf("/");
		if(index != -1)
		{
			String[] split = new String[2];
			split[0] = s.substring(0,index);
			split[1] = s.substring(index+1);
			return split;
		}
		// if the string does not have any slash('/'), return the input string in an array. 
		return new String[]{s};
	}
	
	public static List<String> getValuesFromFile(String fileName) throws FileNotFoundException
	{
		File file = new File(fileName);
		if(!file.exists())
			throw new FileNotFoundException(fileName);
		List<String> list = new ArrayList<String>();
		try(BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String s = null;
			while ((s = br.readLine()) != null)
			{
				list.add(s);
			}
			return list;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeToFile(String fileName,boolean append,List<String> values) throws Exception
	{
		if(values == null)
			return ;
		File file = new File(fileName);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, append));)
		{
			for (String value : values)
			{
				writer.write(value);
				writer.newLine();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();	
		}
	}
	
	public static String getCommaSeparatedValues(String fileName, boolean allowDuplicates, boolean isQuotesRequired)
	{
		File file = new File(fileName);
		Set<String> hash = new HashSet<String>();
		try(BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			StringBuilder sb = new StringBuilder();
			String s = null;
			while ((s = br.readLine()) != null)
			{
				if(allowDuplicates)
					appendToSB(sb, s,isQuotesRequired);
				else
				{
					if (!hash.contains(s))
					{
						appendToSB(sb, s,isQuotesRequired);
						hash.add(s);
					}
				}
			}
			
			String str = sb.toString();
			int index = str.lastIndexOf(',');
			if(index != -1)
				str = str.substring(0, index);
			return str;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private static void appendToSB(StringBuilder sb, String s,boolean isQuotesRequired)
	{
		if(isQuotesRequired)
			sb.append("'");
		sb.append(s);
		if(isQuotesRequired)
			sb.append("'");
		sb.append(",");
	}
}
