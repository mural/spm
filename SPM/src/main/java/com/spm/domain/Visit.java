package com.spm.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 
 * @author Agustin Sgatlata
 */
public class Visit extends Work implements Serializable {
	
	public static final String PRIMER_CONTACTO = "1er Contacto";
	public static final String NEGOCIACION = "Negociacion";
	public static final String SUPERVISION = "Supervision";
	public static final String COBRANZA = "Cobranza";
	public static final String OTROS = "Otros";
	
	private String number;
	private String comment;
	private String type;
	private String topic;
	private Long clientId;
	private Date syncDate;
	private Long userId;
	
	public Visit(Long id) {
		super(id);
	}
	
	public Visit(Long id, Long clientId, Boolean sync, Long userId) {
		super(id);
		this.clientId = clientId;
		setSync(sync);
		this.userId = userId;
	}
	
	public Visit(Long id, String number, String date, String topic, Long clientId, Boolean sync, Long userId) {
		super(id);
		this.number = number;
		this.clientId = clientId;
		setDate(date);
		this.topic = topic;
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
	public Visit(Long id, String name, String date, String type, Collection<OrderItem> products, String status,
			String price, String discount) {
		super(id);
		setName(name);
		setDate(date);
		this.type = type;
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
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}
	
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
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
	 * @param topic
	 */
	public void modifyTopic(String topic) {
		this.topic = topic;
	}
	
	/**
	 * @param comment
	 */
	public void modifyComment(String comment) {
		this.comment = comment;
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
