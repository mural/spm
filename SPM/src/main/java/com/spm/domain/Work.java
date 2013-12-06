package com.spm.domain;

import java.io.Serializable;
import com.spm.common.domain.Entity;

/**
 * 
 * @author Agustin Sgarlata
 */
public class Work extends Entity implements Serializable, Comparable<Work> {
	
	private String name;
	private String date;
	private Boolean sync;
	private String coordinates;
	private String address;
	
	protected Work(Long id) {
		super(id);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * @return the sync
	 */
	public Boolean getSync() {
		return sync;
	}
	
	/**
	 * @return the coordinates
	 */
	public String getCoordinates() {
		return coordinates;
	}
	
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	protected void setDate(String date) {
		this.date = date;
	}
	
	protected void setSync(Boolean sync) {
		this.sync = sync;
	}
	
	/**
	 * @param name
	 */
	public void modify(String name) {
		this.name = name;
	}
	
	/**
	 * @param date
	 */
	public void modifyDate(String date) {
		this.date = date;
	}
	
	/**
	 * @param sync
	 */
	public void modifySync(Boolean sync) {
		this.sync = sync;
	}
	
	/**
	 * @param coordinates
	 * @param address
	 */
	public void modifyPosition(String coordinates, String address) {
		this.coordinates = coordinates;
		this.address = address;
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Work another) {
		return name.compareTo(another.getName());
	}
	
}
