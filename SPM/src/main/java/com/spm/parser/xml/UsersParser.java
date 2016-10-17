package com.spm.parser.xml;

import android.util.Log;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.xml.sax.Attributes;
import com.google.inject.internal.Lists;
import com.spm.common.parser.xml.XMLAttr;
import com.spm.common.parser.xml.XmlParser;
import com.spm.domain.User;
import com.spm.service.APIServiceImpl;

/**
 * 
 * @author Maxi Rosson
 */
public class UsersParser extends XmlParser {
	
	private enum UsersTag {
		
		USER("user");
		
		private String name;
		
		private UsersTag(String name) {
			this.name = name;
		}
		
		public Boolean equals(String tag) {
			return name.equals(tag);
		}
	}
	
	private enum UserAttr implements XMLAttr {
		NAME("name"),
		ID("id"),
		PHONE_NUMBER("tel");
		
		private String name;
		
		private UserAttr(String name) {
			this.name = name;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
	}
	
	private List<User> users = Lists.newArrayList();
	private String name;
	private String id;
	private String phoneNumber;
	
	/**
	 * @see com.splatt.common.parser.xml.XmlParser#onStartElement(java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	protected void onStartElement(String localName, Attributes attributes) {
		// Do Nothing
		
	}
	
	/**
	 * @see com.splatt.common.parser.xml.XmlParser#onEndElement(java.lang.String, java.lang.String)
	 */
	@Override
	protected void onEndElement(String localName, String content) {
		
		if (UserAttr.ID.getName().equals(localName)) {
			id = content;
		}
		if (UserAttr.NAME.getName().equals(localName)) {
			name = content;
		}
		
		if (UserAttr.PHONE_NUMBER.getName().equals(localName)) {
			phoneNumber = content;
		}
		
		if (UsersTag.USER.equals(localName)) {
			User user = new User(new Long(id), name);
			user.setPhoneNumber(phoneNumber);
			user.setUsersUpdateDate(new Date());
			
			APIServiceImpl api = new APIServiceImpl();
			try {
				user.setOrderNumber(api.lastOrderNumber(user));
			} catch (SQLException e) {
				//throw new RuntimeException("Error con los datos del Server");
				Log.e("USERS_PARSER", e.getMessage());
			}
			// TODO: esto deberia resolverlo la misma API en el server...
			
			users.add(user);
		}
	}

	@Override
	protected Object getResponse() {
		return users;
	}
	
}
