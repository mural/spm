package com.spm.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.google.inject.Inject;
import com.spm.common.repository.ObjectNotFoundException;
import com.spm.domain.User;
import com.spm.store.MySQLiteHelper;

/**
 * 
 * @author agustin.sgarlata
 */
public class SQLUserRepository implements UserRepository {
	
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_TEL };
	
	@Inject
	public SQLUserRepository(Context context) {
		dbHelper = new MySQLiteHelper(context);
		close();
		open();
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(java.lang.Long)
	 */
	@Override
	public User get(Long id) throws ObjectNotFoundException {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumns, MySQLiteHelper.COLUMN_ID + " = " + id,
			null, null, null, null);
		cursor.moveToFirst();
		User newUser = cursorToUser(cursor);
		cursor.close();
		return newUser;
	}
	
	/**
	 * @see com.spm.repository.UserRepository#getByName(java.lang.String)
	 */
	@Override
	public User getByName(String name) throws ObjectNotFoundException {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumns, MySQLiteHelper.COLUMN_NAME + " = '"
				+ name + "'", null, null, null, null);
		cursor.moveToFirst();
		User newUser = cursorToUser(cursor);
		cursor.close();
		return newUser;
	}
	
	/**
	 * @see com.spm.common.repository.Repository#add(com.spm.common.domain.Entity)
	 */
	@Override
	public void add(User user) { // El add esta haciendo de update, por ahora puedo dejarlo asi o cambiar todos (que es
									// mejor ...)
		ContentValues values = new ContentValues();
		try {
			values.put(MySQLiteHelper.COLUMN_ID, user.getId());
			values.put(MySQLiteHelper.COLUMN_NAME, user.getFirstName());
			values.put(MySQLiteHelper.COLUMN_TEL, user.getPhoneNumber());
			long insertId = database.insert(MySQLiteHelper.TABLE_USERS, null, values);
		} catch (Exception e) {
			values.put(MySQLiteHelper.COLUMN_TEL, user.getPhoneNumber());
			values.put(MySQLiteHelper.COLUMN_USERS_UPDATE, user.getUsersUpdateDate().toString());
			database.update(MySQLiteHelper.TABLE_USERS, values, MySQLiteHelper.COLUMN_ID + " = " + user.getId(), null);
		}
	}
	
	/**
	 * @see com.spm.common.repository.Repository#addAll(java.util.Collection)
	 */
	@Override
	public void addAll(Collection<User> entities) {
		for (User user : entities) {
			add(user);
		}
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(com.spm.common.domain.Entity)
	 */
	@Override
	public void remove(User user) {
		long id = user.getId();
		System.out.println("User deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_USERS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(java.lang.Long)
	 */
	@Override
	public void remove(Long id) {
		// TODO no hace falta
	}
	
	/**
	 * @see com.spm.common.repository.Repository#removeAll()
	 */
	@Override
	public void removeAll() {
		List<User> users = getAll();
		for (User user : users) {
			remove(user);
		}
	}
	
	/**
	 * @see com.spm.common.repository.Repository#getAll()
	 */
	@Override
	public List<User> getAll() {
		List<User> users = new ArrayList<User>();
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			User user = cursorToUser(cursor);
			users.add(user);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return users;
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(com.spm.common.domain.Entity)
	 */
	@Override
	public List<User> get(User entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private User cursorToUser(Cursor cursor) {
		User user = new User(cursor.getLong(0), cursor.getString(1));
		// TODO meter el phone.
		return user;
	}
	
}
