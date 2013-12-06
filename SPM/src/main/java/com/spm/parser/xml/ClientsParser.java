package com.spm.parser.xml;

import java.util.List;
import org.xml.sax.Attributes;
import com.google.inject.internal.Lists;
import com.spm.common.parser.xml.XMLAttr;
import com.spm.common.parser.xml.XmlParser;
import com.spm.domain.Client;

/**
 * 
 * @author Agustin Sgarlata
 */
public class ClientsParser extends XmlParser {
	
	private enum ClientsTag {
		
		CLIENT("client");
		
		private String name;
		
		private ClientsTag(String name) {
			this.name = name;
		}
		
		public Boolean equals(String tag) {
			return name.equals(tag);
		}
	}
	
	private enum ClientsAttr implements XMLAttr {
		ID("id"),
		NAME("name"),
		DISCOUNT("discount"),
		DISCOUNT2("discount2"),
		USER_ID("user_id"),
		PRICE_LIST("priceList");
		
		private String name;
		
		private ClientsAttr(String name) {
			this.name = name;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
	}
	
	private List<Client> clients = Lists.newArrayList();
	private String id;
	private String name;
	private String discount;
	private String discount2;
	private String userId;
	private String priceList;
	
	/**
	 * @see com.splatt.common.parser.xml.XmlParser#onStartElement(java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	protected void onStartElement(String localName, Attributes attributes) {
		// Do Nothing
		
	}
	
	/**
	 * @see com.splatt.common.parser.xml.XmlParser#onEndElement(java.lang.String, java.lang.String)
	 */
	@Override
	protected void onEndElement(String localName, String content) {
		
		if (ClientsAttr.ID.getName().equals(localName)) {
			id = content;
		}
		if (ClientsAttr.NAME.getName().equals(localName)) {
			name = content;
		}
		if (ClientsAttr.DISCOUNT.getName().equals(localName)) {
			discount = content;
		}
		if (ClientsAttr.DISCOUNT2.getName().equals(localName)) {
			if (content.length() > 0) {
				discount2 = content;
			} else {
				discount2 = "0";
			}
		}
		if (ClientsAttr.USER_ID.getName().equals(localName)) {
			userId = content;
		}
		
		if (ClientsAttr.PRICE_LIST.getName().equals(localName)) {
			priceList = content;
		}
		
		try {
			if (ClientsTag.CLIENT.equals(localName)) {
				clients.add(new Client(new Long(id), name, new Double(discount), new Double(discount2),
						new Long(userId), new Long(priceList)));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * @see com.splatt.common.parser.xml.XmlParser#getResponse()
	 */
	@Override
	protected Object getResponse() {
		return clients;
	}
	
}
