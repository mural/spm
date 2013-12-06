package com.spm.parser.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.xml.sax.Attributes;
import com.spm.common.parser.xml.XMLAttr;
import com.spm.common.parser.xml.XmlParser;

/**
 * 
 * @author Agustin Sgarlata
 */
public class PriceListDateParser extends XmlParser {
	
	private enum DateTag {
		
		PRICE_LIST_DATE("pricelistupdate");
		
		private String name;
		
		private DateTag(String name) {
			this.name = name;
		}
		
		public Boolean equals(String tag) {
			return name.equals(tag);
		}
	}
	
	private enum DateAttr implements XMLAttr {
		DATE_UPDATE("date");
		
		private String name;
		
		private DateAttr(String name) {
			this.name = name;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
	}
	
	private String date;
	
	/**
	 * @see com.spm.common.parser.xml.XmlParser#onStartElement(java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	protected void onStartElement(String localName, Attributes attributes) {
		// Do Nothing
		
	}
	
	/**
	 * @see com.spm.common.parser.xml.XmlParser#onEndElement(java.lang.String, java.lang.String)
	 */
	@Override
	protected void onEndElement(String localName, String content) {
		
		if (DateAttr.DATE_UPDATE.getName().equals(localName)) {
			date = content;
		}
	}
	
	/**
	 * @see com.spm.common.parser.xml.XmlParser#getResponse()
	 */
	@Override
	protected Object getResponse() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		Date updateDate;
		try {
			updateDate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			updateDate = null;
		}
		return updateDate;
	}
	
}
