package com.spm.domain;

import java.io.Serializable;
import com.spm.common.domain.Entity;
import com.spm.common.domain.EntityInterface;
import com.spm.common.domain.FileContent;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * 
 * @author Agustin Sgatlata
 */
@RealmClass
public class Client implements Serializable, Comparable<Client>, RealmModel, EntityInterface {

    @PrimaryKey
    private Long id;

	private String userName;
	private String firstName;
	private String lastName;
	private String fullName;
	private String email;
	private Integer age;
	private Double discount;
	private Double discount2;
	private Boolean privacy;
	private Long userId;
	private Long priceList;

    public Client() {
    }

    public Client(Long id) {
        this.id = id;
    }

	public Client(Long id, String name, Double discount, Long userId) {
		this(id);
		firstName = name;
		fullName = name;
		this.discount = discount;
		this.userId = userId;
	}
	
	public Client(Long id, String name, Double discount, Double discount2, Long userId, Long priceList) {
		this(id, name, discount, userId);
		this.discount2 = discount2;
		this.priceList = priceList;
	}
	
	public void modify(String name, Double discount, Double discount2, Long userId, Long priceList) {
		firstName = name;
		fullName = name;
		this.discount = discount;
		this.userId = userId;
		this.discount2 = discount2;
		this.priceList = priceList;
	}
	
	/**
	 * Constructor
	 * 
	 * @param id The id
	 * @param userName The user's username
	 * @param firstName The user's firstName name
	 * @param lastName The user's lastName name
	 * @param fullName The user's fullName name
	 * @param email The user's email
	 * @param age The user's age
	 * @param privacy The user's privacy setting
	 */
	public Client(Long id, String userName, String firstName, String lastName, String fullName, String email,
			Integer age, Boolean privacy) {
		this(id);
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = fullName;
		this.email = email;
		this.age = age;
		this.privacy = privacy;
	}

    public Long getId() {
        return id;
    }

	public Integer getAge() {
		return age;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getFullname() {
		return (firstName != null) & (lastName != null) ? firstName + " " + lastName : fullName;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public Double getDiscount() {
		return discount;
	}
	
	public Double getDiscount2() {
		return discount2;
	}
	
	public Boolean getPrivacy() {
		return privacy;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public Long getPriceList() {
		return priceList;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getFullname();
	}
	
	@Override
	public int compareTo(Client aClient) {
		if (this.equals(aClient)) {
			return 0;
		}
		return this.getFirstName().compareTo(aClient.getFirstName());
		
	}
	
}
