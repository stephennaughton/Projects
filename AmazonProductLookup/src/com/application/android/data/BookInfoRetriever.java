package com.application.android.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.application.android.data.vo.BookInfo;

/**
 * Utility class to retrieve and package the CarPark Data into a usable format
 * @author snaughton
 *
 */
public class BookInfoRetriever {
	
	public List<BookInfo> getBookData(String url) {
		
		List<BookInfo> books = new ArrayList<BookInfo>();
		
		// Get the http data from the server
		String strSource = retrieveData(url);
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		String expression = "/";
		try {
			// Get the car park names
			NodeList nodes = (NodeList) xpath.evaluate(expression, new InputSource(new StringReader(strSource)), XPathConstants.NODESET);
			
			for (int i = 0; i < nodes.getLength(); i++) {
				
				String value = getXMLValue(strSource, "//Title");
				// Create our car park data object
				BookInfo thisBook = new BookInfo();
				thisBook.setTitle(value);
				books.add(thisBook);
			}			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return books;
	}   
	
	/**
	 * Using XPath get the individual car park information
	 * @param xmlSource
	 * @param xpathQuery
	 * @return
	 */
	private String getXMLValue(String response, String xpathQuery) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		// TODO optimize
		InputSource xmlSource = new InputSource(new StringReader(response));
		
		try {
			NodeList nodes = (NodeList) xpath.evaluate(xpathQuery, xmlSource, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				return nodes.item(i).getNodeValue(); 
			}
		} catch (XPathExpressionException e) {
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
		return out.toString();
	}

}
