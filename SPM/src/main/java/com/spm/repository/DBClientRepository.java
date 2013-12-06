package com.spm.repository;

import java.util.Collection;
import java.util.List;
import android.content.Context;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import com.google.inject.Inject;
import com.spm.common.repository.ObjectNotFoundException;
import com.spm.domain.Client;
import com.spm.store.DbProvider;

/**
 * 
 * @author Agustin Sgarlata
 */
public class DBClientRepository extends DbProvider<Client> implements ClientRepository {
	
	@Inject
	public DBClientRepository(Context ctx) {
		super(Client.class, ctx);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(java.lang.Long)
	 */
	@Override
	public Client get(Long id) throws ObjectNotFoundException {
		Client client = new Client(id);
		if (super.get(client).size() == 0) {
			// throw new ObjectNotFoundException(persistentClass);
			return null;
		}
		return super.get(client).get(0);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(java.lang.Long)
	 */
	@Override
	public Collection<Client> getByUserId(Long userId) throws ObjectNotFoundException {
		Query query = db().query();
		query.constrain(Client.class);
		query.descend("userId").constrain(userId);
		ObjectSet<Client> result = query.execute();
		return result;
	}
	
	/**
	 * @see com.spm.common.repository.Repository#add(com.spm.common.domain.Entity)
	 */
	@Override
	public void add(Client entity) {
		try {
			Client entity2 = get(entity.getId());
			if (entity2 != null) {
				entity2.modify(entity.getFirstName(), entity.getDiscount(), entity.getDiscount2(), entity.getUserId(),
					entity.getPriceList());
				entity = entity2;
			}
		} catch (ObjectNotFoundException e) {
		}
		super.store(entity);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#addAll(java.util.Collection)
	 */
	@Override
	public void addAll(Collection<Client> entities) {
		for (Client client : entities) {
			add(client);
		}
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(com.spm.common.domain.Entity)
	 */
	@Override
	public void remove(Client entity) {
		super.delete(entity);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#removeAll()
	 */
	@Override
	public void removeAll() {
		List<Client> clients = getAll();
		for (Client client : clients) {
			delete(client);
		}
	}
	
	/**
	 * @see com.spm.common.repository.Repository#getAll()
	 */
	@Override
	public List<Client> getAll() {
		return super.findAll();
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(java.lang.Long)
	 */
	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	public void loadInitialData() {
		removeAll();
		db().commit();
		// if (super.findAll().isEmpty()) {
		// Client client1 = new Client(1L, "LOS BOHEMIOS SRL");
		// Client client2 = new Client(2L, "JEREZ DANIEL A.");
		// Client client100035 = new Client(100035L, "SANTILLAN DANIEL");
		// Client client100036 = new Client(100036L, "FRETES FRANCO MARCELINO D");
		//
		// add(client100035);
		// add(client100036);
		// add(client1);
		// add(client2);
		// }
	}
}
