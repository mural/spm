package com.spm.server;

import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;

/**
 * 
 * @author Diego Mera
 */
public class SyncForm {
	
	@FormParam(value = "seller")
	private String seller;
	
	@FormParam(value = "numero")
	private String numero;
	
	@FormParam(value = "clientId")
	private String clientId;
	
	@FormParam(value = "date")
	private String date;
	
	@FormParam(value = "stakes")
	@DefaultValue(value = "false")
	private Boolean stakes;
	
	private List<Long> parsedUserIds;
	
	/**
	 * @return the seller
	 */
	public String getSeller() {
		return seller;
	}
	
	/**
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}
	
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}
	
//	/**
//	 * @return the usersIds
//	 */
//	public List<Long> getUsersFBIds() {
//		
//		if (parsedUserIds == null) {
//			try {
//				parsedUserIds = Lists.newArrayList(Iterables.transform(StringUtils.splitToCollection(usersFBIds, ","),
//					new StringToLongFunction()));
//			} catch (NumberFormatException e) {
//				throw new BadRequestException("The parameter 'usersFBIds' must be a list of integer values");
//			}
//		}
//		
//		return parsedUserIds;
//	}
	
	/**
	 * @return the stakes
	 */
	public Boolean hasStakes() {
		return stakes;
	}
	
}
