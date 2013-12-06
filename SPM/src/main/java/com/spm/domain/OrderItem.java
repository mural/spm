package com.spm.domain;

import java.io.Serializable;
import com.spm.common.domain.Entity;

/**
 * 
 * @author Agustin Sgatlata
 */
public class OrderItem extends Entity implements Serializable, Comparable<OrderItem> {
	
	private String product;
	private Integer quantity;
	private Integer discount;
	private Double price;
	private Integer orderId; // TODO link con la order !
	
	public OrderItem(Long id, String product, Integer quantity, Integer discount, Double price) {
		super(id);
		this.product = product;
		this.quantity = quantity;
		this.discount = discount;
		this.price = price;
	}
	
	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}
	
	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}
	
	/**
	 * @return the discount
	 */
	public Integer getDiscount() {
		return discount;
	}
	
	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}
	
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(OrderItem another) {
		return product.compareToIgnoreCase(another.getProduct());
	}
	
}
