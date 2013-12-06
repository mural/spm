package com.spm.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 
 * @author Agustin Sgatlata
 */
public class Order extends Work implements Serializable {
	
	public static final String TO_DELIVER = "A entregar";
	public static final String DELIVERED = "Entregado";
	
	public static final String NORMAL = "Normal";
	public static final String SPECIAL = "Especial";
	
	private String number;
	// private String deliverDate; // +3 days
	private String type;
	private Collection<OrderItem> products;
	private String status;
	private String price;
	private String discount;
	private Long clientId;
	private Date syncDate;
	private Long userId;
	
	public Order(Long id) {
		super(id);
	}
	
	public Order(Long id, Long clientId, Boolean sync, Long userId) {
		super(id);
		this.clientId = clientId;
		setSync(sync);
		this.userId = userId;
	}
	
	// public Order(Long id, String name, Long clientId, Collection<OrderItem> products) {
	// super(id);
	// this.name = name;
	// this.clientId = clientId;
	// this.products = products;
	// }
	
	public Order(Long id, String number, String date, String type, String status, Long clientId,
			Collection<OrderItem> products, Boolean sync, Long userId) {
		// this(id, name, clientId, products);
		super(id);
		this.number = number;
		this.clientId = clientId;
		this.products = products;
		setDate(date);
		this.type = type;
		this.status = status;
		setSync(sync);
		this.userId = userId;
	}
	
	/**
	 * @param id
	 * @param name
	 * @param date
	 * @param type
	 * @param products
	 * @param status
	 * @param price
	 * @param discount
	 */
	public Order(Long id, String name, String date, String type, Collection<OrderItem> products, String status,
			String price, String discount) {
		super(id);
		setName(name);
		setDate(date);
		this.type = type;
		this.products = products;
		this.status = status;
		this.price = price;
		this.discount = discount;
	}
	
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @return the products
	 */
	public Collection<OrderItem> getProducts() {
		return products;
	}
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	
	/**
	 * @return the discount
	 */
	public String getDiscount() {
		return discount;
	}
	
	/**
	 * @return the clientId
	 */
	public Long getClientId() {
		return clientId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	/**
	 * @param number
	 */
	public void modifyNumber(String number) {
		this.number = number;
	}
	
	/**
	 * @param type
	 */
	public void modifyType(String type) {
		this.type = type;
	}
	
	/**
	 * @param status
	 */
	public void modifyStatus(String status) {
		this.status = status;
	}
	
	/**
	 * @return the syncDate
	 */
	public Date getSyncDate() {
		return syncDate;
	}
	
	/**
	 * @param syncDate the syncDate to set
	 */
	public void setSyncDate(Date syncDate) {
		this.syncDate = syncDate;
	}
	
}
