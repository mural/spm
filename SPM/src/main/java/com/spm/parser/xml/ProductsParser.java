package com.spm.parser.xml;

import java.util.List;
import org.xml.sax.Attributes;
import com.google.inject.internal.Lists;
import com.spm.common.parser.xml.XMLAttr;
import com.spm.common.parser.xml.XmlParser;
import com.spm.domain.Product;

/**
 * 
 * @author Agustin Sgarlata
 */
public class ProductsParser extends XmlParser {
	
	private enum ProductsTag {
		
		PRODUCT("product");
		
		private String name;
		
		private ProductsTag(String name) {
			this.name = name;
		}
		
		public Boolean equals(String tag) {
			return name.equals(tag);
		}
	}
	
	private enum ProductsAttr implements XMLAttr {
		ID("id"),
		NAME("name"),
		PRICE("price"),
		PRICE_LIST("priceList");
		
		private String name;
		
		private ProductsAttr(String name) {
			this.name = name;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
	}
	
	private List<Product> products = Lists.newArrayList();
	private String id;
	private String name;
	private String price;
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
		
		if (ProductsAttr.ID.getName().equals(localName)) {
			id = content;
		}
		if (ProductsAttr.NAME.getName().equals(localName)) {
			name = content;
		}
		if (ProductsAttr.PRICE.getName().equals(localName)) {
			if (content.equals("null")) {
				price = "0";
			} else {
				price = content;
			}
		}
		
		if (ProductsAttr.PRICE_LIST.getName().equals(localName)) {
			if (content.equals("null")) {
				priceList = "0";
			} else {
				priceList = content;
			}
		}
		
		if (ProductsTag.PRODUCT.equals(localName)) {
			Product product = null;
			for (Product productToFind : products) {
				if (productToFind.getId().toString().equals(id)) {
					product = productToFind;
				}
			}
			if (product == null) {
				product = new Product(new Long(id), name, 1L, new Integer(priceList));
			}
			if (priceList.equals("1")) {
				product.setPrice1ant(product.getPrice1());
				product.setPrice1(new Double(price));
			}
			if (priceList.equals("2")) {
				product.setPrice2ant(product.getPrice2());
				product.setPrice2(new Double(price));
			}
			if (priceList.equals("3")) {
				product.setPrice3ant(product.getPrice3());
				product.setPrice3(new Double(price));
			}
			if (priceList.equals("4")) {
				product.setPrice4ant(product.getPrice4());
				product.setPrice4(new Double(price));
			}
			if (priceList.equals("5")) {
				product.setPrice5ant(product.getPrice5());
				product.setPrice5(new Double(price));
			}
			
			products.add(product);
		}
	}
	
	/**
	 * @see com.splatt.common.parser.xml.XmlParser#getResponse()
	 */
	@Override
	protected Object getResponse() {
		return products;
	}
	
}
