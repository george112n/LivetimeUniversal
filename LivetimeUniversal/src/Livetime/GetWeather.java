package Livetime;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import OpenWeatherMap.Current;

public class GetWeather extends DefaultHandler
{
	private static Current current;

	Location pLocation;
	long[] coord;
	
	String[] DataReturned;
	
	boolean testValueVaryWeather = true;
	
	public static Current entry(String URL)
	{
		current = new Current();
		try
		{
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			SAXParser saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setErrorHandler(new MyErrorHandler(System.err));

			xmlReader.setContentHandler((ContentHandler) new GetWeather());
			xmlReader.parse(URL);
		}
		catch (SAXException e)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LivetimeUniversal.GetWeather - SAXException: Error reading in api xml weather file");			
			e.printStackTrace();
		}
		catch (IOException e)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LivetimeUniversal.GetWeather - IOException: Error reading in api xml weather file");			
			e.printStackTrace();
		}
		catch (Exception e)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LivetimeUniversal.GetWeather - Exception: Error reading in api xml weather file");
			e.printStackTrace();
		}
		return current;
	}
	
	public void startDocument() throws SAXException
	{
		current = new Current();
	}

	public void startElement(String namespaceURI, String localName, String qName,  Attributes atts) throws SAXException
	{
		int i;
		int iAttributes = atts.getLength();
		
		//Checks the element name for sun
		if (localName.equals("sun"))
		{
			//Goes through the attributes of sun until it finds rise and set
			for (i = 0 ; i < iAttributes ; i++)
			{
				if (atts.getLocalName(i).equals("rise"))
				{
					current.setSunrise((LocalDateTime.parse(atts.getValue(i))));
				}
				if (atts.getLocalName(i).equals("set"))
				{
					current.setSunset((LocalDateTime.parse(atts.getValue(i))));
				}
			}
		}
	}
	
	public void endDocument() throws SAXException
	{
		
	}
}

class MyErrorHandler implements ErrorHandler
{
	private PrintStream out;

	MyErrorHandler(PrintStream out)
	{
		this.out = out;
	}

	private String getParseExceptionInfo(SAXParseException spe)
	{
		String systemId = spe.getSystemId();

		if (systemId == null) {
			systemId = "null";
		}

		String info = "URI=" + systemId + " Line=" 
				+ spe.getLineNumber() + ": " + spe.getMessage();

		return info;
	}

	public void warning(SAXParseException spe) throws SAXException {
		out.println("Warning: " + getParseExceptionInfo(spe));
	}

	public void error(SAXParseException spe) throws SAXException {
		String message = "Error: " + getParseExceptionInfo(spe);
		throw new SAXException(message);
	}

	public void fatalError(SAXParseException spe) throws SAXException {
		String message = "Fatal Error: " + getParseExceptionInfo(spe);
		throw new SAXException(message);
	}
}
