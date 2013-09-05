package com.appdear.client.utility;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * XML处理类
 * Xml config = new Xml("config.xml","config");
  System.out.println("title: "+config.child("title").content());
  Xml version = config.child("version");
  System.out.println("version: "+version.integer("major")+"."+version.integer("minor"));
  for(Xml role:config.child("roles").children("role"))
     System.out.println("role: name: "+role.string("name"));
 for(Xml user:config.child("users").children("user"))
 {
     String email = user.optString("email");
     System.out.println(
         "user: name: "+user.string("name")+
         ", password: "+user.string("password")+
         ", role: "+user.string("role")+
         ", email: "+(email==null ? "-" : email));
 }
System.out.println("test: "+config.option("test"));
 */

public class Xml
{
	private static Element rootElement(InputStream inputStream, String rootName)
	{
		try
		{
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
		    Document document = builder.parse(inputStream);
		    Element rootElement = document.getDocumentElement();
		    if(!rootElement.getNodeName().equals(rootName))
		    	throw new RuntimeException("Could not find root node: "+rootName);
		    return rootElement;
		}
		catch(IOException exception)
		{
			throw new RuntimeException(exception);
		}
		catch(ParserConfigurationException exception)
		{
			throw new RuntimeException(exception);
		}
		catch(SAXException exception)
		{
			throw new RuntimeException(exception);
		}
		finally
		{
			if(inputStream!=null)
			{
				try
				{
					inputStream.close();
				}
				catch(Exception exception)
				{
					throw new RuntimeException(exception);
				}
			}
		}
	}

	private static FileInputStream fileInputStream(String filename)
	{
		try
		{
			return new FileInputStream(filename);
		}
		catch(IOException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	public Xml(InputStream inputStream, String rootName)
	{
		this(rootElement(inputStream,rootName));
	}

	public Xml(String filename, String rootName)
	{
		this(fileInputStream(filename),rootName);
	}

    /**
     * 在2.1的sdk上element没有getTextContent方法，所以要建立这个方法，这个方法有bug，比如<p><b>aa</b>cc</p>这样的里面，cc是取不到的。
     * @param node
     * @return
     */
private static String getTextContent(Node node) {
    int cl = node.getChildNodes().getLength();
 
    if (cl == 1)return node.getFirstChild().getNodeValue();
    return "";
}


	private Xml(Element element)
	{
		this.name = element.getNodeName();
		this.content = getTextContent(element);
		NamedNodeMap namedNodeMap = element.getAttributes();
		int n = namedNodeMap.getLength();
		for(int i=0;i<n;i++)
		{
			Node node = namedNodeMap.item(i);
			String name = node.getNodeName();
    		addAttribute(name,node.getNodeValue());
		}
		NodeList nodes = element.getChildNodes();
		n = nodes.getLength();
	    for(int i=0;i<n;i++)
	    {
	    	Node node = nodes.item(i);
	    	int type = node.getNodeType();
	    	if(type==Node.ELEMENT_NODE) addChild(node.getNodeName(),new Xml((Element)node));
	    }
	}

	private void addAttribute(String name, String value)
	{
		nameAttributes.put(name,value);
	}

	private void addChild(String name, Xml child)
	{
		ArrayList<Xml> children = nameChildren.get(name);
		if(children==null)
		{
			children = new ArrayList<Xml>();
			nameChildren.put(name,children);
		}
		children.add(child);
	}

	public String name()
	{
		return name;
	}

	public String content()
	{
		return content;
	}

	public Xml child(String name)
	{
		Xml child = optChild(name);
		if(child==null) throw new RuntimeException("Could not find child node: "+name);
		return child;
	}
	
	public Xml childExt(String name)
	{
		Xml child = optChild(name);
		if(child==null) return null;
		return child;
	}	

	public static String getXmlContent(Xml node)
	{
		if (node == null)return "";
		return node.content();
	}
	
	public Xml optChild(String name)
	{
		ArrayList<Xml> children = children(name);
		int n = children.size();
		if(n>1) throw new RuntimeException("Could not find individual child node: "+name);
		return n==0 ? null : children.get(0);
	}

	public boolean option(String name)
	{
		return optChild(name)!=null;
	}

	public ArrayList<Xml> children(String name)
	{
		ArrayList<Xml> children = nameChildren.get(name);
		return children==null ? new ArrayList<Xml>() : children;
	}

	public String string(String name)
	{
		String value = optString(name);
		if(value==null)
		{
			throw new RuntimeException(
				"Could not find attribute: "+name+", in node: "+this.name);
		}
		return value;
	}

	public String optString(String name)
	{
		return nameAttributes.get(name);
	}

	public int integer(String name)
	{
		return Integer.parseInt(string(name));
	}

	public int optInteger(String name)
	{
		return Integer.parseInt(optString(name));
	}

	private String name;
	private String content;
	private Map<String,String> nameAttributes = new HashMap<String,String>();
	private Map<String,ArrayList<Xml>> nameChildren = new HashMap<String,ArrayList<Xml>>();
}