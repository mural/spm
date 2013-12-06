package com.spm.store;

import java.util.Collection;
import java.util.List;
import android.content.Context;
import com.spm.common.repository.ObjectNotFoundException;
import com.spm.common.repository.Repository;
import com.spm.domain.User;

/**
 * 
 * @author Agustin Sgarlata
 */
public class UserProvider extends DbProvider<User> implements Repository<User> {
	
	public UserProvider(Context ctx) {
		super(User.class, ctx);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(java.lang.Long)
	 */
	@Override
	public User get(Long id) throws ObjectNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @see com.spm.common.repository.Repository#add(com.spm.common.domain.Entity)
	 */
	@Override
	public void add(User entity) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @see com.spm.common.repository.Repository#addAll(java.util.Collection)
	 */
	@Override
	public void addAll(Collection<User> entities) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(com.spm.common.domain.Entity)
	 */
	@Override
	public void remove(User entity) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @see com.spm.common.repository.Repository#removeAll()
	 */
	@Override
	public void removeAll() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @see com.spm.common.repository.Repository#getAll()
	 */
	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(java.lang.Long)
	 */
	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		
	}
	
}