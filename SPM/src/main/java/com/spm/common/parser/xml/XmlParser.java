package com.spm.common.parser.xml;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.util.Log;
import android.util.Xml;
import com.spm.common.domain.Application.ApplicationProvider;
import com.spm.common.exception.CommonErrorCode;
import com.spm.common.parser.WebServiceParser;
import com.spm.common.utils.FileUtils;
import com.spm.common.utils.NumberUtils;

/**
 * XML input streams parser
 * 
 * @author Maxi Rosson
 */
public abstract class XmlParser extends DefaultHandler implements WebServiceParser {
	
	private static final String TAG = XmlParser.class.getSimpleName();
	
	private StringBuilder builder = new StringBuilder();
	
	/**
	 * @see com.spm.common.parser.WebServiceParser#parse(java.io.InputStream)
	 */
	@Override
	public Object parse(InputStream inputStream) {
		try {
			if (ApplicationProvider.LOGGING_SERVER_RESPONSE) {
				Log.d(TAG, "Parsing started.");
				inputStream = FileUtils.copy(inputStream);
				Log.d(TAG, FileUtils.toString(inputStream));
				inputStream.reset();
			}
			
			// Parse the xml
			// Xml.parse(inputStream, Xml.Encoding.UTF_8, this);
			Xml.parse(inputStream, Xml.Encoding.ISO_8859_1, this);
			
			return getResponse();
		} catch (IOException e) {
			throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
		} catch (SAXException e) {
			throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
		} finally {
			if (ApplicationProvider.LOGGING_SERVER_RESPONSE) {
				Log.d(TAG, "Parsing finished.");
			}
		}
	}
	
	/**
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String,
	 *      org.xml.sax.Attributes)
	 */
	@Override
	public final void startElement(String uri, String localName, String qName, Attributes attributes) {
		try {
			super.startElement(uri, localName, qName, attributes);
			this.onStartElement(localName, attributes);
		} catch (SAXException e) {
			throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
		}
	}
	
	/**
	 * Method called at the start of every element of the XML.
	 * 
	 * @param localName The name of the tag.
	 * @param attributes The attributes of the tag.
	 */
	protected abstract void onStartElement(String localName, Attributes attributes);
	
	/**
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public final void endElement(String uri, String localName, String qName) {
		try {
			super.endElement(uri, localName, qName);
			this.onEndElement(localName, builder.toString().trim());
			builder.setLength(0);
		} catch (SAXException e) {
			throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
		}
	}
	
	/**
	 * Method called at the end of every element of the XML.
	 * 
	 * @param localName The name of the tag.
	 * @param content The content of the tag.
	 */
	protected abstract void onEndElement(String localName, String content);
	
	/**
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		builder.append(ch, start, length);
	}
	
	/**
	 * @return The parser response
	 */
	protected abstract Object getResponse();
	
	/**
	 * @param attributes The {@link Attributes}
	 * @param xmlAttr The attribute used to retrieve the value
	 * @param value The value to compare
	 * @return Whether the attribute has the received value or not
	 */
	protected Boolean hasAttribute(Attributes attributes, XMLAttr xmlAttr, String value) {
		return value.equals(getStringValue(attributes, xmlAttr));
	}
	
	/**
	 * @param attributes The {@link Attributes}
	 * @param xmlAttr The attribute used to retrieve the value
	 * @return The {@link Integer} value of the attribute
	 */
	protected Integer getIntegerValue(Attributes attributes, XMLAttr xmlAttr) {
		String value = attributes.getValue(xmlAttr.getName());
		return NumberUtils.getInteger(value);
	}
	
	/**
	 * @param attributes The {@link Attributes}
	 * @param xmlAttr The attribute used to retrieve the value
	 * @return The {@link Double} value of the attribute
	 */
	protected Double getDoubleValue(Attributes attributes, XMLAttr xmlAttr) {
		String value = attributes.getValue(xmlAttr.getName());
		return NumberUtils.getDouble(value);
	}
	
	/**
	 * @param attributes The {@link Attributes}
	 * @param xmlAttr The attribute used to retrieve the value
	 * @return The {@link String} value of the attribute
	 */
	protected String getStringValue(Attributes attributes, XMLAttr xmlAttr) {
		return attributes.getValue(xmlAttr.getName());
	}
}
