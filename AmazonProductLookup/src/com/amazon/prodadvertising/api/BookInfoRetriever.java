package com.amazon.prodadvertising.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

import com.application.android.data.vo.BookInfo;

/**
 * Utility class to retrieve and package the Book Data into a usable format
 * @author snaughton
 *
 */

public class BookInfoRetriever {
	
	/*
	 * Your AWS Access Key ID, as taken from the AWS Your Account page.
	 */
	private static final String AWS_ACCESS_KEY_ID = "AKIAJ4TRBPEMPHH4R65Q";

	/*
	 * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
	 * Your Account page.
	 */
	private static final String AWS_SECRET_KEY = "XD5NtnRomszG4or6386fgMt111szxqbxGOPRQgZq";

	/*
	 * Use one of the following end-points, according to the region you are
	 * interested in:
	 * 
	 *      US: ecs.amazonaws.com 
	 *      CA: ecs.amazonaws.ca 
	 *      UK: ecs.amazonaws.co.uk 
	 *      DE: ecs.amazonaws.de 
	 *      FR: ecs.amazonaws.fr 
	 *      JP: ecs.amazonaws.jp
	 * 
	 */
	private static final String ENDPOINT = "ecs.amazonaws.com";
	
	private static DocumentBuilderFactory domFactory;
	private static XPathFactory factory;
	
	static {
		domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);		
		factory = XPathFactory.newInstance();		
	}	
	
	/**
	 * Lookup the book information based on the supplied ISBN
	 * @param isbn
	 * @return
	 */
	public BookInfo lookupAmazonProductInfo(String isbn) {
		/*
		 * Set up the signed requests helper 
		 */
		SignedRequestsHelper helper = null;
		try {
			helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();            
		}

		String requestUrl = null;

		/* The helper can sign requests in two forms - map form and string form */

		/*
		 * Here is an example in map form, where the request parameters are stored in a map.
		 */
		System.out.println("Map form example:");
		Map<String, String> params = new HashMap<String, String>();
		params.put("Service", "AWSECommerceService");
		params.put("Version", "2011-08-01");
		params.put("Operation", "ItemLookup");
		params.put("ItemId", isbn);
		params.put("SearchIndex", "Books");
		params.put("SubscriptionId", "Your_AWS_ID");
		params.put("AssociateTag", "assoc-id-20");
		params.put("IdType", "EAN");
		params.put("ResponseGroup", "ItemAttributes");

		requestUrl = helper.sign(params);	
	
		System.out.println("Signed Request is \"" + requestUrl + "\"");

		BookInfo data = getBookData(requestUrl);

		if (data != null) {
			Log.d("BOOKINFO", data.toString());
		} else {
			Log.d("BOOKINFO", "No data retrieved");
		}

		return data;
	}
	
  /**
   * Do the lookup and then parse the data to the BookInfo data structure
   * @param url
   * @return
   */
	private BookInfo getBookData(String url) {
		
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
			book.setDescription(getXMLValue(createDocument(strSource), "//aws:Content"));
		}		    
		return book;
	}   
	
	/**
	 * Build the document
	 * @param strResponse
	 * @return
	 */
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