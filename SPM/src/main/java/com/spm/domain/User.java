package com.spm.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.spm.common.domain.Entity;
import com.spm.common.domain.FileContent;

/**
 * 
 * @author Agustin Sgatlata
 */
public class User extends Entity implements Serializable {
	
	private String userName;
	private String firstName;
	private String lastName;
	private String fullName;
	private String email;
	private Integer age;
	private Boolean privacy;
	private FileContent avatar;
	private List<Client> clients;
	private Date updateDate;
	private String phoneNumber;
	private Long orderNumber;
	private Date usersUpdateDate;
	
	public User(Long id, String name) {
		super(id);
		firstName = name;
	}
	
	public User(Long id, String name, List<Client> clients, Date updateDate, Long orderNumber, Date usersUpdateDate) {
		super(id);
		firstName = name;
		this.clients = clients;
		this.updateDate = updateDate;
		this.orderNumber = orderNumber;
		this.usersUpdateDate = usersUpdateDate;
	}
	
	public void modify(String name, List<Client> clients, String phoneNumber) {
		firstName = name;
		this.clients = clients;
		this.phoneNumber = phoneNumber;
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
	 * @param clients
	 */
	public User(Long id, String userName, String firstName, String lastName, String fullName, String email,
			Integer age, Boolean privacy, List<Client> clients) {
		super(id);
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = fullName;
		this.email = email;
		this.age = age;
		this.privacy = privacy;
		this.clients = clients;
	}
	
	/**
	 * @param id
	 */
	public User(Long id) {
		super(id);
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
	
	public FileContent getAvatar() {
		return avatar;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public Boolean getPrivacy() {
		return privacy;
	}
	
	/**
	 * @return the clients
	 */
	public List<Client> getClients() {
		return clients;
	}
	
	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	
	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * @return the orderNumber
	 */
	public Long getOrderNumber() {
		return orderNumber;
	}
	
	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getFullname();
	}
	
	/**
	 * @return the usersUpdateDate
	 */
	public Date getUsersUpdateDate() {
		return usersUpdateDate;
	}
	
	/**
	 * @param usersUpdateDate the usersUpdateDate to set
	 */
	public void setUsersUpdateDate(Date usersUpdateDate) {
		this.usersUpdateDate = usersUpdateDate;
	}
	
	public boolean checkValidDate() {
		if (usersUpdateDate == null) {
			return false;
		}
		return usersUpdateDate.getDay() == (new Date()).getDay();
		// return usersUpdateDate.getMinutes() == (new Date()).getMinutes();
	}
}
