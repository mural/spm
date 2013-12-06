package com.spm.common.domain;


/**
 * 
 * @author Maxi Rosson
 */
public class Entity extends BusinessObject {
	
	private Long id;
	
	/**
	 * @param id
	 */
	public Entity(Long id) {
		this.id = id;
	}
	
	public Entity() {
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	// /**
	// * @see java.lang.Object#hashCode()
	// */
	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = 1;
	// result = (prime * result) + ((id == null) ? 0 : id.hashCode());
	// return result;
	// }
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Entity other = (Entity)obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
