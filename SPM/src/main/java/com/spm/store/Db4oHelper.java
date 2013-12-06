package com.spm.store;

import java.io.IOException;
import android.content.Context;
import android.util.Log;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ta.TransparentPersistenceSupport;
import com.spm.domain.Client;
import com.spm.domain.Order;
import com.spm.domain.Product;
import com.spm.domain.User;
import com.spm.domain.Visit;

/**
 * 
 * @author Agustin Sgarlata
 */
public class Db4oHelper {
	
	private static ObjectContainer oc = null;
	private Context context;
	
	/**
	 * @param ctx
	 */
	
	public Db4oHelper(Context ctx) {
		context = ctx;
	}
	
	/**
	 * Create, open and close the database
	 * 
	 * @return an object container
	 */
	public ObjectContainer db() {
		
		try {
			if ((oc == null) || oc.ext().isClosed()) {
				oc = Db4oEmbedded.openFile(dbConfig(), db4oDBFullPath(context));
				// // We first load the initial data from the database
				// new DBClientRepository(context).loadInitialData();
				// new DBUserRepository(context).loadInitialData();
				// new DBOrderRepository(context).loadInitialData();
				// new DBLineRepository(context).loadInitialData();
				// new DBProductRepository(context).loadInitialData();
			}
			
			return oc;
			
		} catch (Exception ie) {
			Log.e(Db4oHelper.class.getName(), ie.toString());
			throw new DatabaseFileLockedException("Archivo de datos corrupto");
		}
	}
	
	/**
	 * Configure the behavior of the database
	 */
	
	private EmbeddedConfiguration dbConfig() throws IOException {
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		configuration.common().add(new TransparentPersistenceSupport());
		configuration.common().objectClass(Order.class).objectField("id").indexed(true);
		configuration.common().objectClass(Order.class).cascadeOnUpdate(true);
		configuration.common().objectClass(Order.class).objectField("userId").indexed(true);
		configuration.common().objectClass(Visit.class).objectField("id").indexed(true);
		configuration.common().objectClass(Visit.class).cascadeOnUpdate(true);
		configuration.common().objectClass(Visit.class).objectField("userId").indexed(true);
		configuration.common().objectClass(User.class).objectField("firstName").indexed(true);
		configuration.common().objectClass(Product.class).objectField("id").indexed(true);
		configuration.common().objectClass(Client.class).objectField("id").indexed(true);
		configuration.common().objectClass(Client.class).objectField("userId").indexed(true);
		return configuration;
	}
	
	/**
	 * Returns the path for the database location
	 */
	
	private String db4oDBFullPath(Context ctx) {
		return ctx.getDir("data", 0) + "/" + "spm9.db4o";
	}
	
	/**
	 * Closes the database
	 */
	
	public void close() {
		if (oc != null) {
			oc.close();
		}
	}
	
}
