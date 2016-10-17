package com.spm.repository;

import java.util.Collection;
import java.util.List;
import android.content.Context;
import com.google.inject.Inject;
import com.spm.common.repository.ObjectNotFoundException;
import com.spm.domain.Product;
import com.spm.store.DbProvider;

import hugo.weaving.DebugLog;

/**
 * 
 * @author Agustin Sgarlata
 */
public class DBProductRepository extends DbProvider<Product> implements ProductRepository {
	
	@Inject
	public DBProductRepository(Context ctx) {
		super(Product.class, ctx);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(java.lang.Long)
	 */
	@Override
	public Product get(Long id) throws ObjectNotFoundException {
		Product product = new Product(id, null, null, null);
		if (super.get(product).size() == 0) {
			// throw new ObjectNotFoundException(persistentClass);
			return null;
		}
		return super.get(product).get(0);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#add(com.spm.common.domain.Entity)
	 */
	@Override
	public void add(Product entity) {
		try {
			Product entity2 = get(entity.getId());
			if (entity2 != null) {
				entity2.modify(entity.getName(), entity.getPriceList(), entity.getPrice1(), entity.getPrice1ant(),
					entity.getPrice2(), entity.getPrice2ant(), entity.getPrice3(), entity.getPrice3ant(),
					entity.getPrice4(), entity.getPrice4ant(), entity.getPrice5(), entity.getPrice5ant());
				entity = entity2;
			}
		} catch (ObjectNotFoundException e) {
		}
		super.store(entity);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#addAll(java.util.Collection)
	 */
	@DebugLog
	@Override
	public void addAll(Collection<Product> entities) {
		for (Product product : entities) {
			add(product);
		}
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(com.spm.common.domain.Entity)
	 */
	@Override
	public void remove(Product entity) {
		super.delete(entity);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#removeAll()
	 */
	@Override
	public void removeAll() {
		// super.
	}
	
	/**
	 * @see com.spm.common.repository.Repository#getAll()
	 */
	@Override
	public List<Product> getAll() {
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
		// TODO: removeAll();
		// db().commit();
		if (super.findAll().isEmpty()) {
			
			// add(new Product(1002L, "1002 TEMBLEKE 'SERIE SURTIDO' 48X4", 1L, new Double(177)));
			// add(new Product(1003L, "1003 TEMBLEKE 'SERIE CORAZON' 48X4", 1L, new Double(177)));
			// add(new Product(1004L, "1004 TEMBLEKE SERIE DISNEY 32X12", 1L, new Double(354)));
			// add(new Product(1005L, "1005 TEMBLEKE SERIE DISNEY 48X4", 1L, new Double(177)));
			// add(new Product(1006L, "1006 TEMBLEKE STICK 24X50", 1L, new Double(336)));
			// add(new Product(2001L, "2001 TREMBLY 'SERIE SURTIDO' 48X8", 1L, new Double(143)));
			// add(new Product(2004L, "2004 TREMBLY 'SERIE FRUTAS' 48X8", 1L, new Double(143)));
			// add(new Product(2005L, "2005 TREMBLY 'BUNNY SLURPS' 48 X 8", 1L, new Double(143)));
			// add(new Product(2006L, "2006 TREMBLY GALACTICO 48X8", 1L, new Double(143)));
			// add(new Product(2008L, "2008 TREMBLITO DELUXE 48X10", 1L, new Double(104)));
			// add(new Product(2009L, "2009 TREMBLY BICHOS 48X8", 1L, new Double(143)));
			// add(new Product(2010L, "2010 TREMBLITO DOMINO 48 X 10", 1L, new Double(104)));
			// add(new Product(2011L, "2011 TEMBLEKE SIMPSONS C/STICKERS 48X8", 1L, new Double(178)));
			// add(new Product(2012L, "2012 TEMBLEKE SIMPSONS C/STICKERS 16X40", 1L, new Double(297)));
			// add(new Product(2014L, "2014 TREMBLY GOL  48X8", 1L, new Double(143)));
			// add(new Product(2015L, "2015 TREMBLY SELVA 48X8", 1L, new Double(143)));
			// add(new Product(2017L, "2017 TEMBLEKE PRINCESS C/STICKERS 48X8", 1L, new Double(178)));
			// add(new Product(2019L, "2017 TEMBLEKE Mix", 1L, new Double(183)));
			// add(new Product(2020L, "2010 TEMBLEKE Princes School", 1L, new Double(150)));
			// add(new Product(3001L, "3001 FIGURAS LARGAS 'COBRA JELLY' 14X12", 1L, new Double(288)));
			// add(new Product(3002L, "3002 TEMBLEKE 'CARITAS DISNEY JELLY'14X12", 1L, new Double(288)));
			// add(new Product(3004L, "3004 TEMBLEKE 'RELOJ JELLY' 14 x12", 1L, new Double(288)));
			// add(new Product(3005L, "3005 TEMBLEKE FIGURAS JELLY LARGAS X60", 1L, new Double(100)));
			// add(new Product(4004L, "4004 DRACULENGUA JELLY En ESTUCHE 12x12", 1L, new Double(333)));
			// add(new Product(4008L, "4008 DRACULENGUA JELLY  x60", 1L, new Double(134)));
			// add(new Product(4009L, "4009 TEMBLEKE SIMPSON NAIPES 12x14", 1L, new Double(250)));
			// add(new Product(4011L, "4011 TEMBLEKE PRINCESS SORPRESA 18X18", 1L, new Double(256)));
			// add(new Product(4014L, "4014 TEMBLEKE DISNEY NAIPES 12x14", 1L, new Double(250)));
			// add(new Product(4019L, "4019 TEMBLEKE SIMPSON DOMINO 12x12", 1L, new Double(246)));
			// add(new Product(4020L, "4020 TEMBLEKE SIMPSON NAIPES TORRE 6X33", 1L, new Double(295)));
			// add(new Product(4023L, "4023 TEMBLEKE 'CARS' NAIPES Y STICKERS 12X14", 1L, new Double(274)));
			// add(new Product(4024L, "4024 TEMBLEKE Spiderman con tazos", 1L, new Double(380)));
			// add(new Product(9001L, "9001 EXHIBIDOR x20 TIRAS SURTIDAS", 1L, new Double(50)));
			// add(new Product(5115L, "5115 FLUTI NARANJA X 12", 1L, new Double(134)));
			// add(new Product(9010L, "9010 VASOS PROMO X12", 1L, new Double(30)));
			// add(new Product(5116L, "5116 FLUTI MANZANA X12", 1L, new Double(134)));
			// add(new Product(5117L, "5117 FLUTI MULTIFRUTA X 12", 1L, new Double(134)));
			// add(new Product(1001L, "1001 TEMBLEKE 'SERIE CLASICO' 32X12", 1L, new Double(354)));
			// add(new Product(2021L, "2021 TEMBLEKE PRINCESS C/ STICKERS", 1L, new Double(297)));
			// add(new Product(6001L, "6001 TEMBLEKE GOMIS OSITOS 12x14", 1L, new Double(190)));
			
		}
	}
}
