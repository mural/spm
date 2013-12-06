package com.spm.domain;

import java.io.Serializable;
import com.spm.common.domain.Entity;

/**
 * 
 * @author Agustin Sgatlata
 */
public class Line extends Entity implements Serializable {
	
	private String name;
	
	/**
	 * Constructor
	 * 
	 * @param id The id
	 * @param name
	 */
	public Line(Long id, String name) {
		super(id);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
