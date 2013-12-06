package com.spm.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import com.spm.domain.Client;
import com.spm.domain.Order;
import com.spm.domain.Product;
import com.spm.domain.User;
import com.spm.domain.Visit;

/**
 * 
 * @author Maxi Rosson
 * @author Fernando Perez
 */
public interface APIService {
	
	/**
	 * @return A list of users in the app.
	 */
	public List<User> getContacts();
	
	/**
	 * @return The list of products
	 */
	public List<Product> getProducts();
	
	/**
	 * @param user
	 * @return The list of clients of an user
	 */
	public List<Client> getClients(User user);
	
	/**
	 * @param order The order
	 * @param client
	 * @return The result of the sync
	 */
	public String sync(Order order, Client client);
	
	/**
	 * @param visit The visit
	 * @return The result of the sync
	 */
	public String sync(Visit visit);
	
	/**
	 * @return The last update of the price list
	 */
	public Date getPriceListDate();
	
	/**
	 * @param user
	 * @return The last update number of order
	 * @throws SQLException
	 */
	public Long lastOrderNumber(User user) throws SQLException;
	
}