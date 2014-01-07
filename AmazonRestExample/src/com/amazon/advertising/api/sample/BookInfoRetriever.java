package com.amazon.advertising.api.sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Utility class to retrieve and package the CarPark Data into a usable format
 * @author snaughton
 *
 */
public class BookInfoRetriever {
	
	private static DocumentBuilderFactory domFactory;
	private static XPathFactory factory;
	
	static {
		domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		
		factory = XPathFactory.newInstance();
		
	}
	
	public static void main(String[] args) {
		
		BookInfoRetriever info = new BookInfoRetriever();
		BookInfo book = info.getBookData("http://ecs.amazonaws.com/onca/xml?AWSAccessKeyId=&AssociateTag=assoc-id-20&IdType=EAN&ItemId=9780131479418&Operation=ItemLookup&ResponseGroup=ItemAttributes%2CEditorialReview&SearchIndex=Books&Service=AWSECommerceService&Timestamp=2011-08-26T10%3A50%3A33.000Z&Version=2011-08-01&Signature=");
		
		System.out.println(book.toString());
		
	}
	
	public BookInfo getBookData(String url) {
		
		BookInfo book = null;
		
		// Get the http data from the server
		String strSource = retrieveData(url);

		String isValidResponse = getXMLValue(createDocument(strSource), "//aws:IsValid");
		if (isValidResponse != null
					&& isValidResponse.equalsIgnoreCase("true")) {
			
			book = new BookInfo();
			book.setTitle(getXMLValue(createDocument(strSource), "//aws:Title"));
			book.setAuthor(getXMLValue(createDocument(strSource), "//aws:Author"));			
			book.setEdition(getXMLValue(createDocument(strSource), "//aws:Binding"));			
			book.setIsbn(getXMLValue(createDocument(strSource), "//aws:EAN"));			
			book.setPages(new Integer(getXMLValue(createDocument(strSource), "//aws:NumberOfPages")));
			book.setPublisher(getXMLValue(createDocument(strSource), "//aws:Label"));
			book.setReleaseDate(getXMLValue(createDocument(strSource), "//aws:PublicationDate"));
			//book.setDescription(getXMLValue(createDocument(strSource), "//aws:Content"));
		}		    
		return book;
	}   
	
	private Document createDocument(String strResponse) {
	  DocumentBuilder builder;
	  Document document = null;	  
    try {
      builder = domFactory.newDocumentBuilder();
	    document = builder.parse(new InputSource(new StringReader(strResponse)));	    
    } catch (ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }	    
    return document;		
	}
	
	/**
	 * Using XPath get the individual node information 
	 * @param xmlSource
	 * @param xpathQuery
	 * @return
	 */
	private String getXMLValue(Document response, String xpathQuery) {
	  
    try {	    
	    XPath xpath = factory.newXPath();
	    xpath.setNamespaceContext( new NamespaceContext() {
	      public String getNamespaceURI( String prefix) {	      	
	        if ( prefix.equals( "aws")) {
	          return "http://webservices.amazon.com/AWSECommerceService/2011-08-01";
	        } else {
	        	return null;
	        }        
	      }
	    
	      public String getPrefix( String namespaceURI) {
	        if ( namespaceURI.equals( "http://webservices.amazon.com/AWSECommerceService/2011-08-01")) {
	          return "aws";
	        } else { 
	          return null;
	        }	      
	      }
	    
	      public Iterator<String> getPrefixes( String namespaceURI) {
	        ArrayList<String> list = new ArrayList<String>();	      
	        if ( namespaceURI.equals( "http://webservices.amazon.com/AWSECommerceService/2011-08-01")) {
	          list.add( "aws");
	        } else { 
	          // nothing
	        }	      
	        return list.iterator();
	      }
	    });

	    return xpath.evaluate(xpathQuery, response.getDocumentElement());
	    
    } catch (XPathExpressionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();	    
    }
    
    return null;
	}

	/**
	 * Get the xml data returned from the car park url
	 * @return
	 */
	private String retrieveData(String url) {
		URL feedUrl = null;
		try {
			feedUrl = new URL(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}		

		try {			
			return slurp(feedUrl.openConnection().getInputStream());			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}		
	}

	/**
	 * Pull in all the information contained in the feed.
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private String slurp (InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		
		System.out.println("Resp:" + out.toString());
		
		return out.toString();
	}

}
