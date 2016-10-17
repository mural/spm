package com.spm.repository;

import java.util.Collection;
import java.util.List;
import android.content.Context;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import com.google.inject.Inject;
import com.spm.common.repository.ObjectNotFoundException;
import com.spm.domain.User;
import com.spm.store.DbProvider;

/**
 * 
 * @author Agustin Sgarlata
 */
public class DBUserRepository extends DbProvider<User> implements UserRepository {
	
	private Context ctx;
	
	@Inject
	public DBUserRepository(Context ctx) {
		super(User.class, ctx);
		this.ctx = ctx;
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(java.lang.Long)
	 */
	@Override
	public User get(Long id) throws ObjectNotFoundException {
		User user = new User(id);
		if (super.get(user).size() == 0) {
			throw new ObjectNotFoundException(persistentClass);
		}
		return super.get(user).get(0);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(java.lang.Long)
	 */
	@Override
	public User getByName(String name) throws ObjectNotFoundException {
		Query query = db().query();
		query.constrain(User.class);
		query.descend("firstName").constrain(name);
		ObjectSet<User> result = query.execute();
		return result.get(0);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#add(com.spm.common.domain.Entity)
	 */
	@Override
	public void add(User entityToAdd) {
		User entity;
		try {
			entity = get(entityToAdd.getId());
			entity.modify(entityToAdd.getFirstName(), entityToAdd.getClients(), entityToAdd.getPhoneNumber());
			if (entityToAdd.getUpdateDate() != null) {
				entity.setUpdateDate(entityToAdd.getUpdateDate());
			}
			if (entityToAdd.getUsersUpdateDate() != null) {
				entity.setUsersUpdateDate(entityToAdd.getUsersUpdateDate());
			}
			if (entity.getOrderNumber() == null || (entity.getOrderNumber() < entityToAdd.getOrderNumber())) {
				entity.setOrderNumber(entityToAdd.getOrderNumber());
			}
		} catch (ObjectNotFoundException e) {
			entity = entityToAdd;
		}
		super.store(entity);
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
	public void remove(User entity) {
		super.delete(entity);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#removeAll()
	 */
	@Override
	public void removeAll() {
		List<User> users = getAll();
		for (User user : users) {
			delete(user);
		}
	}
	
	/**
	 * @see com.spm.common.repository.Repository#getAll()
	 */
	@Override
	public List<User> getAll() {
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
		new DBClientRepository(ctx).removeAll();
		db().commit();
		if (findAll().isEmpty()) {
			
			// List<Client> clients3 = Lists.newArrayList();
			// clients3.add(new Client(100057L, "LAMARA MARIA NELIDA", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100063L, "FELINO RICARDO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100075L, "INTERDIS S.A", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100076L, "RAIROLO ROBERTO Y AMERICO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100083L, "DISTRIBUIDORA PACO S.H", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100088L, "RISTO JUAN CARLOS", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100092L, "CLAN S.R.L", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100104L, "CANDY'S SHOP S.R.L", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100118L, "CASA TONY", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100128L, "MARZUK GUSTAVO FABIAN", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100129L, "VENTURINI MARIA ROSARIO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100731L, "TERESIN ADRIAN ENRIQUE", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100783L, "KREMASTI S.A.", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100784L, "FLANINI GABRIEL", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100798L, "CASA CHICHIN S.R.L.", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100821L, "DISTRIBUIDORA LA PRIMERA S.R.L.", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(100873L, "DIST.NUTRESUR SRL", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101002L, "DISTB FIGON SH DE FIGON CLAUDIO A Y FIGO", new Double(20),
			// new Double(10), 3L));
			// clients3.add(new Client(101003L, "ARANCIBIA EDMUNDO ANTONIO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101041L, "TAIBO HNOS.", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101055L, "LAURO OSCAR Y ARRIMESTO DELIA S.H.", new Double(20), new Double(10),
			// 3L));
			// clients3.add(new Client(101077L, "YÑIGUEZ MANUEL DE JESUS", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101170L, "VIEGAS ANALIA CILIA", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101175L, "BARRETO ALEJANDRO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101181L, "OLSZANIECKI ESTEBAN", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101189L, "PEÑALOZA ONTIVEROS IRENE", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101237L, "IZZETA  GOMES OSVALDO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101257L, "JIMENEZ SUPERMERCADOS", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101444L, "ALVARIÑO LORENZO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101445L, "OJEDA OSCAR DANIEL", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101468L, "RAFAEL PRIETO E HIJOS S.A.", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101474L, "RIVERO DOMINGA", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101494L, "GALLEMAR SRL", new Double(28), new Double(10), 3L));
			// clients3.add(new Client(101510L, "ROSOLINO GUSTAVO GABRIEL Y ROSOLINO SONI", new Double(20),
			// new Double(10), 3L));
			// clients3.add(new Client(101522L, "CANTEROS MIGUEL ANGEL", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101638L, "FRETES MARTIN ALBERTO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101664L, "FUCILI PABLO ESTEBAN", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101708L, "URELLI JUAN JOSE", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101720L, "VILARIÑO JUAN MANUEL", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101727L, "PEREZ JORGE", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101745L, "LOMAS DULCE S.R.L.", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101771L, "LA FONTAINE MIRTA", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101795L, "WE SRL", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101810L, "BARCO OSVALDO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101814L, "SOTO OLGA CRISTINA", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101838L, "DIAZ ROSA CIPRIANA", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101855L, "COLACE ADRIAN EDUARDO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101907L, "CORTI LORENA KARINA", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101908L, "FERNANDEZ LUCIA RAQUEL", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101915L, "CASIN LEONARDO MARCELO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101916L, "ORMA DIEGO LEONARDO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101966L, "MELANG SRL", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101972L, "PAOLINI ROMINA", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101974L, "JESICA S.ROMERO Y GASTON A.ROMERO S.H.", new Double(20), new
			// Double(10),
			// 3L));
			// clients3.add(new Client(101982L, "LISI VIRGINIA VERONICA", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(101986L, "LANDONI RUBEN RUIZ", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(102081L, "COOPERATIVA DE TRABAJO LA BOMBA LIMITADA", new Double(20),
			// new Double(10), 3L));
			// clients3.add(new Client(102104L, "ATANES JORGE MARCELO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(102161L, "DISTRIBUIDORA ALBERDI 3274 SRL", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(102177L, "IORIZZO MARCELO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(102187L, "DISMAR S.A.", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(102193L, "PASTOR ANALIA VERONICA", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(102229L, "SUCESION DE GIGLIA NATALIA", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(102234L, "CEBALLOS ADRIAN MARIO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(102253L, "ANSELMO LEOPOLDO ALEJANDRO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(102291L, "KOVACEVIC ROMUALDO PABLO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(102292L, "JANACOR S.A.", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(102299L, "CLERICI ALEJANDRO", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(102341L, "DISTRIBUIDORA LAMADRID", new Double(20), new Double(10), 3L));
			// clients3.add(new Client(102401L, "HELAMONT S.A.", new Double(20), new Double(10), 3L));
			// User user3 = new User(3L, "CANTERO MIGUEL ANGEL", clients3);
			//
			// List<Client> clients51 = Lists.newArrayList();
			// clients51.add(new Client(100143L, "PANIZO JOSE", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100144L, "CARRIZO NORMA SUSANA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100147L, "ZARZA JORGE RICARDO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100150L, "DISTRIBUIDORA REDONDO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100154L, "CASA CROVARA S.R.L", new Double(24), new Double(14.5), 51L));
			// clients51.add(new Client(100155L, "CASA PRIETO S.H", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100164L, "OLSZANIECKI JUAN", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100170L, "BLOISI JUAN MARIO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100177L, "MINARDI LINO Y MINARDI MARIO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100198L, "DISTRIBUIDORA GEMA S.R.L", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100207L, "MASSA JOSE SEVERINO-CAMBIO R/SOCIAL 1244", new Double(20),
			// new Double(10), 51L));
			// clients51.add(new Client(100214L, "DOS ANJOS DE BRITO CARLOS", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100217L, "ARGENZIANO ESTEBAN", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100221L, "OJEDA E HIJOS S.A", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100223L, "HINDELBRANT", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100742L, "ALMACEN DE GOLOSINAS S.A.", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100758L, "COMACCHIO DANIEL Y COMACCHIO CLAUDIO S.H", new Double(20),
			// new Double(10), 51L));
			// clients51.add(new Client(100792L, "LUCERO MIRIAM BEATRIZ", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100807L, "GONZALEZ ADRIANA GABRIELA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100845L, "LOS ENTRERRIANOS S.H.DE OJEDA RAMON Y LI", new Double(20),
			// new Double(10), 51L));
			// clients51.add(new Client(100971L, "VEDDA IRIS", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(100972L, "PANTANO OSCAR", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101208L, "CORIGLIANO FERNANDO JORGE D Y JORGE O SH", new Double(20),
			// new Double(10), 51L));
			// clients51.add(new Client(101209L, "CYCLE S.R.L.", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101322L, "CASTILLA DISTRIBUIDORA S.A.", new Double(24), new Double(14.5), 51L));
			// clients51.add(new Client(101337L, "GONZALEZ FRETES CRISTIAN", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101438L, "GONZALEZ CATAN S.R.L.", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101441L, "D.I.S.A.", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101442L, "RODRIGUES QUEIROS SUSANA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101590L, "BRAVO GUILLERMO GABRIEL", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101593L, "MACCHI FABIAN ERNESTO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101672L, "ANTUNES PAOLA KARINA", new Double(24), new Double(10), 51L));
			// clients51.add(new Client(101707L, "SAN MARTIN VICTORIA ALBINA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101715L, "ROBLEDO ANDRES ARIEL", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101791L, "ALTAMIRANDA JORGE DANIEL", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101832L, "BELLO FABIANA ANDREA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101873L, "DISTRIBUIDORA MIXI SRL", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101881L, "ZARO CRISTINA BEATRIZ", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101883L, "JULIO CESAR ALBARRACIN ¨EL CORDOBEZ¨", new Double(20), new Double(10),
			// 51L));
			// clients51.add(new Client(101893L, "RANDAZZO MARCELO GABRIEL Y ALEJANDRO", new Double(20), new Double(10),
			// 51L));
			// clients51.add(new Client(101912L, "PITELLA BRUNO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101914L, "RODRIGUES MAGDALENA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101958L, "KALARUTO CARLOS ROBERTO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101976L, "CULZONI ANTONIO ARNOLFO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(101980L, "MANZUR ADRIANA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102030L, "MONTMEL SRL", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102046L, "DISTRIBUIDORA MARCOS", new Double(0), new Double(10), 51L));
			// clients51.add(new Client(102049L, "AVILA NELSON ALEJANDRO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102058L, "MUN SUN NAM", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102061L, "FERNANDEZ LUIS JORGE", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102062L, "TIME CUP SRL", new Double(24), new Double(14.5), 51L));
			// clients51.add(new Client(102066L, "DERMERDJIAN MARIA CRISTINA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102069L, "BARROS RUBEN ROBERTO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102070L, "LLANERAS MARIA CONCEPCION", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102074L, "RIDANO RICARDO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102075L, "FERNANDEZ BORGES MA.ISABEL", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102086L, "ALVAREZ JUAREZ JOSE RAUL", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102088L, "DELGADO JOSE ALBERTO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102101L, "OTEGUI LEONARDO LUJAN", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102106L, "CASTILLO ESTEBAN SEBASTIAN", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102107L, "OLIVERA HERNAN IGNACIO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102116L, "GURFINKEL ALEJANDRA LEONOR", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102120L, "MARCIAL JORGE GUSTAVO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102121L, "ESCALANTE MARIANA PATRICIA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102144L, "ACOSTA DANIEL ALEJANDRO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102146L, "VEGA CARLOS ALCIDES", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102159L, "GARCIA ALICIA FILOMENA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102160L, "PETTINARI MARTA NOEMI", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102176L, "CANO DIEGO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102208L, "DAI WENBIN", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102220L, "HECTOR VISCARDI SRL", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102212L, "TIMUKA SACICI Y F", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102216L, "LICENCIAS S.A.", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102217L, "PUCHETA PATRICIA ELINA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102225L, "PEREZ SILVANA E.", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102233L, "NAYA JUAN HUGO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102256L, "TULIETI MARIANA ANDREA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102287L, "MA LI YUN", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102298L, "GODOY SERGIO RAMON", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102319L, "RODRIGUEZ MIGUEL ANGEL", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102333L, "GIARDULLO MARIELA MABEL", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102346L, "MERELES RAFAELA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102369L, "RODRIGUEZ JOSE OSCAR", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102371L, "GUTIERREZ OLGA ROSA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102374L, "LA CALENDA S.A.", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102377L, "MONTANIA ANTONIA LUISA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102378L, "NATIVITY S.A. EN FORMACION", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102380L, "ENRIQUEZ NESTOR OSCAR", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102384L, "CASAFUZ JESUS VICTOR EMILIO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102392L, "NOVOA ALEJANDRO MANUEL", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102402L, "OJEDA M.A. Y OJEDA R.E", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102417L, "GOLOSINAS AVELLANEDA S.A.", new Double(24), new Double(10), 51L));
			// clients51.add(new Client(102419L, "MERCADO DE DULCES SRL", new Double(24), new Double(10), 51L));
			// clients51.add(new Client(102442L, "ATACADAO S.A.", new Double(24), new Double(10), 51L));
			// clients51.add(new Client(102443L, "ANTONIO MIRIAM LILIANA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102465L, "MORALES HECTOR ALEJANDRO", new Double(24), new Double(10), 51L));
			// clients51.add(new Client(102479L, "VELAZQUEZ OMAR", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102495L, "TOMMASIELLO MIGUEL EMILIO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102509L, "MESIANO JOSE", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102512L, "BUCHACRA OSCAR ANIBAL", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102517L, "BIRGNERT CARLOS ALBERTO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102519L, "SANABRIA CARLOS ALBERTO", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(102521L, "LUJAMBIO SANDRA VIVIANA", new Double(20), new Double(10), 51L));
			// clients51.add(new Client(105029L, "DANIEL PIÑEYRO", new Double(0), new Double(10), 51L));
			// User user51 = new User(51L, "ESPINDOLA GUSTAVO", clients51);
			//
			// List<Client> clients52 = Lists.newArrayList();
			// clients52.add(new Client(100011L, "ENFACO S.A", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100035L, "SANTILLAN DANIEL", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100036L, "FRETES FRANCO MARCEL", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100037L, "GLIKANA S.A", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100138L, "MELE Y CIA S.A.C.F.", new Double(24), new Double(10), 52L));
			// clients52.add(new Client(100151L, "ECHAVARRIA PATRICIA ", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100153L, "CASA OSLE S.A.C.I.F.", new Double(28), new Double(10), 52L));
			// clients52.add(new Client(100179L, "BOYADJIAN ANA MARIA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100182L, "AUTOSERVICIO MAYORIS", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100183L, "VIGANO MARIA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100201L, "GENARO DI LENA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100208L, "RAIGOSO PABLO FERNAN", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100211L, "GOLOSINAS MITRE S.A", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100212L, "WASINGER IGNACIO ERN", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100219L, "SPINELLI CARLOS ADRI", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100224L, "VALENTINETTI FRANCIS", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100225L, "MARIO DE MORON S.R.L", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100232L, "S.TORRES & CIA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100237L, "JESUS OSCAR BRITO", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100240L, "ANTONIO POMPONIO", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100248L, "TEMELADRI MARIA ROSA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100253L, "GENCARELLI ROBERTO A", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100827L, "DISTRIBUCION DERQUI ", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(100882L, "GENTILE SERGIO ANIBA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101015L, "CAPIZZANO NESTOR FAB", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101166L, "FLORENTIN ANIBAL AND", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101167L, "ARDUINI MARIA SEBAST", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101200L, "FARIÑA PASTOR LUCIAN", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101225L, "FERNANDEZ Y REY S.A.", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101236L, "JOANIDIS CHRISTIAN", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101251L, "ARVAHNS S.A.", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101339L, "FERNANDEZ NANCY LILI", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101352L, "FLORES HILDA DEL VAL", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101372L, "ECHAVARRIA MARIA ALE", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101377L, "JUAREZ CARLOS ARTURO", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101440L, "BREGMAN MAURICIO GUS", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101492L, "DIDONAZ S.A", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101517L, "GOMEZ IGNACIO Y GOME", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101527L, "BANGALUPI SANDRA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101531L, "DIAZ FRIDA ESTHER", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101536L, "GOROSITO OMAR HECTOR", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101592L, "CATTANEO GABRIEL MAR", new Double(10), new Double(10), 52L));
			// clients52.add(new Client(101606L, "PASSARO AIDA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101639L, "REPOSTERIA ARTESANAL", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101641L, "GIANSANTI MARIO DIEG", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101648L, "COTILLON INTEGRAL SR", new Double(28), new Double(10), 52L));
			// clients52.add(new Client(101666L, "AUTOSERVICIO CAVALIE", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101670L, "BERGESIO LIONEL IVAN", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101705L, "VERA BERTHA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101722L, "SANCHEZ JORGE FELIX", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101806L, "ABUD JULIO CESAR", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101807L, "ORIENTAL PARTY SRL", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101819L, "PSAROPOULOS CRISTINA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101833L, "HUGO ASIS", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101878L, "MARTINEZ LUCIA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101886L, "COSTA SEBASTIAN JUAN", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101887L, "ZAPDOS S.A.", new Double(19), new Double(10), 52L));
			// clients52.add(new Client(101888L, "DIAZ MARIANA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101894L, "LILIANA DEZAN (DIST.", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101901L, "GIUNTA LAURA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101903L, "EL VIRREY INTERCONTI", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101921L, "TORNESE SERGIO", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101944L, "SKARBUN CLAUDIO LUCI", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101968L, "ARNAUD OSCAR", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(101998L, "POMPONIO VICENTE ANT", new Double(0), new Double(10), 52L));
			// clients52.add(new Client(102017L, "GERBER NORA BEATRIZ", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102045L, "RUIZ DANIEL OSCAR", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102076L, "BRITEZ ROSALIA IDA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102079L, "BAO BINGLING", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102090L, "OCHOTORINO SILVINA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102091L, "RUSSO VERONICA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102111L, "CONTRERAS ALEJANDRO ", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102118L, "BRESSAN JORGE LUIS", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102140L, "ESPARZA FABRICIO JAV", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102151L, "FRANCICA SILVIA LILI", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102182L, "CHIRIMIA S.A.", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102200L, "MOLOCHNIK CLAUDIO NE", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102207L, "FLORES ALBINA SOLEDA", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102250L, "SKYCEO S.A.", new Double(24), new Double(10), 52L));
			// clients52.add(new Client(102251L, "BANGA NESTOR MARIANO", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102252L, "QUIO MART SRL", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102258L, "CARUSO JULIO CESAR", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102305L, "GOLOSAN S.A.", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102323L, "MEDINA PERDOMO LUZ A", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102334L, "PRUNELLO DIEGO SEBAS", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102360L, "IL CAPO DE N.PRIETO ", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102394L, "AGUIRRE MARIANO", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102398L, "LOPEZ JUAN CARLOS", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102400L, "AGNOLETTI SANDRA AZU", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102433L, "GOLOSINAS COSQUIN SR", new Double(20), new Double(10), 52L));
			// clients52.add(new Client(102508L, "FUSSARO GUILLERMO FA", new Double(20), new Double(10), 52L));
			// User user52 = new User(52L, "MARIANA AREVALO", clients52);
			//
			// List<Client> clients141 = Lists.newArrayList();
			// clients141.add(new Client(100045L, "DISTRIBUIDORA BERISSO SRL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(100091L, "VOZZA HECTOR JOSE", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(100093L, "BIELANOVICH ANITA EMILIA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(100124L, "FERNANDEZ HNOS.", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(100136L, "CABRAL MARTA ISABEL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(100194L, "IANNINO FRANCO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(100781L, "COTI EXPRESS S.R.L.", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(100993L, "FRETES GRACIELA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101020L, "PRIETTO GLADYS MAXIMILIANO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101043L, "VILLALBA EDITH", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101065L, "ZAPATA CARLOS ANDRES", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101072L, "CASA IGLESIAS", new Double(0), new Double(10), 141L));
			// clients141.add(new Client(101201L, "VILARDO CARMELO Y VILARDO NATALIO", new Double(20), new Double(10),
			// 141L));
			// clients141.add(new Client(101218L, "SANCHEZ HECTOR A.", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101247L, "FIGUEREDO MARCELO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101254L, "ANDERSON CARLOS", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101255L, "AMAEDO VIDELA-SANDRA MARIEL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101279L, "VASQUEZ ADOLFO", new Double(0), new Double(10), 141L));
			// clients141.add(new Client(101283L, "OVEJERO SEGUNDO Y CARIBONI MARTA NOEMI S", new Double(20), new
			// Double(
			// 10), 141L));
			// clients141.add(new Client(101406L, "SURTIKIOSCO S.R.L.", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101412L, "SIMONETTI FRANCISCO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101422L, "GIACOMANTONE HERRERA FACUNDO NAHUEL", new Double(20), new Double(10),
			// 141L));
			// clients141.add(new Client(101507L, "ALANIS JOSE ANTONIO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101521L, "CALVET PEDRO ALBERTO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101552L, "LEAS ALEJANDRO Y FERNANDEZ RODOLFO", new Double(20), new Double(10),
			// 141L));
			// clients141.add(new Client(101556L, "FLECKENSTEIN HORACIO OSCAR", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101746L, "LA ZULEMITA S.A.", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101773L, "TASTIL S.A.", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101775L, "JOSÉ CIPRIANO S.A.", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101820L, "ESPOSITO JORGE ALBERTO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101871L, "DERUVO JOSE NESTOR", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101889L, "VILLALBA EDITH", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101890L, "IRIARTE BRITO SERGIO ANTONIO", new Double(20), new Double(10),
			// 141L));
			// clients141.add(new Client(101906L, "RAGGIO VERONICA ANABEL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(101975L, "ALFONSO JOSE Y ALFONSO M. S.H.", new Double(20), new Double(10),
			// 141L));
			// clients141.add(new Client(102005L, "DE POLITO GRACIELA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102036L, "SANCHEZ MIGUEL JOSE", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102040L, "PERALTA RONALD NOEL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102082L, "CARLOS FERNANDEZ GOLOSINAS S.A.", new Double(20), new Double(10),
			// 141L));
			// clients141.add(new Client(102094L, "BAZAN CLAUDIO ROBERTO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102150L, "PATA PABLO DANIEL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102163L, "RISOLI LAURA ESTHER", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102165L, "CASTILLO MOSTACERO YOLANDA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102166L, "IZURRIETA DIEGO ISRAEL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102167L, "AVALLE JOSE MANUEL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102168L, "FIGUEROA GUTIERREZ JOSE VALERICIO", new Double(20), new Double(10),
			// 141L));
			// clients141.add(new Client(102169L, "DIST SANTA ANA COOP DE TRABAJO LTDA", new Double(20), new Double(10),
			// 141L));
			// clients141.add(new Client(102174L, "ALONSO DANIEL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102175L, "RONGA MAXIMO GERMAN", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102181L, "GOLOCOM SRL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102185L, "ROSSO PATRICIA VIVIANA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102189L, "DISTRIBUIDORA SANTA MARIA S.A.", new Double(24), new Double(14.5),
			// 141L));
			// clients141.add(new Client(102190L, "MAFERTOM S.A.", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102192L, "MAYTIN S.A.", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102195L, "SALME MARCELO CLAUDIO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102196L, "FERNANDEZ CARLOS LUIS", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102197L, "RIAFRECHA JUAN CARLOS", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102201L, "TEMES AISPURO GLORIA JEANNETTE", new Double(20), new Double(10),
			// 141L));
			// clients141.add(new Client(102204L, "RIVERA MENDEZ MARIA BEATRIZ", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102205L, "BASUALDO GABRIELA PAULA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102206L, "BARTHE GUSTAVO Y BARTHE MARIANA S.H.", new Double(20), new
			// Double(10),
			// 141L));
			// clients141.add(new Client(102209L, "LOISI MARIANO ADRIAN", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102211L, "MALDONADO LAURA LORENA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102213L, "CALVO MARIANA ALEJANDRA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102215L, "DICUNDO ROBERTO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102218L, "AVENA CARLOS ALBERTO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102219L, "PERAZZO JUAN", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102220L, "GRANDMONTAGNE SILVIA VIVIANA", new Double(20), new Double(10),
			// 141L));
			// clients141.add(new Client(102221L, "MAYORISTA PACO S.H. DE NUÑEZ R. Y TEDESC", new Double(24), new
			// Double(
			// 10), 141L));
			// clients141.add(new Client(102227L, "EGIDI OSCAR ALBERTO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102228L, "SALAS ALICIA ADRIANA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102230L, "MAVI S.H. DE MORENO I. Y ROMERO S.", new Double(20), new Double(10),
			// 141L));
			// clients141.add(new Client(102232L, "DISTRIBUIDORA AMADO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102238L, "RODPETROL SRL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102239L, "IRUSTA RAMON ROBERTO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102240L, "BENITEZ YESICA ROSANA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102243L, "AUTOS. ESECE S.H. DE ZENI D.E. Y ZENI N.", new Double(20), new
			// Double(
			// 10), 141L));
			// clients141.add(new Client(102244L, "SOFRAS CAROLINA CINTIA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102247L, "MARZARI VIVIANA KARINA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102248L, "VAZQUEZ DANIEL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102254L, "RAGO ANTONIO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102255L, "CABRERA ARAUJO IRMA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102260L, "SANTOS CORRIPIO EDMUNDO HORACIO", new Double(20), new Double(10),
			// 141L));
			// clients141.add(new Client(102261L, "BERRUEZO ALEJANDRA SUSANA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102262L, "ROMERO SERGIO ADRIAN", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102265L, "RICCIO LUIS GASTON", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102267L, "CARRERA RAMOS CESAR AUGUSTO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102268L, "GUZMAN SERGIO LEONARDO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102269L, "CANTERO LAURA GABRIELA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102270L, "OLIVA DIEGO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102271L, "CANZANI JORGE RODOLFO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102273L, "BATISTA NIGUEL ANGEL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102275L, "RODRIGUEZ MARIA DOLORES", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102277L, "ROJAS ROBERTO FABIAN", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102278L, "NAVARRO RICARDO DANIEL", new Double(0), new Double(10), 141L));
			// clients141.add(new Client(102282L, "RODRIGUEZ MARIA ALICIA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102284L, "VILLANUEVA NORMA NOEMI", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102285L, "MAZZRELLO ROBERTO JORGE", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102286L, "GODOY ANDRES OSVALDO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102294L, "VIRGILIO LUCAS LEONEL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102296L, "LOPEZ CARLOS", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102297L, "TOMASELLI MARIANA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102303L, "PRIETO DORA ELISA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102304L, "DAVENTINI EDUARDO HECTOR", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102306L, "ARPOTA SRL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102313L, "QUINTANA MIRIAM GRACIELA", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102381L, "CASA NATY SRL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102383L, "DISTRIBUIDORA DISTRO-MANE SRL", new Double(20), new Double(10),
			// 141L));
			// clients141.add(new Client(102385L, "BARSELLINI MARCELO ADRIAN", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102386L, "ZARATE MARIA ISABEL", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102391L, "GARNICA MARCELINO", new Double(0), new Double(10), 141L));
			// clients141.add(new Client(102415L, "LO MAS DULCE S.A.", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102422L, "DE LA FUENTE JULIAN", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102427L, "BARRIENTOS JORGE ALFREDO", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102436L, "BARRAGAN ERICA ELIZABETH", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102447L, "TEDESCO DIEGO ALEJANDRO", new Double(24), new Double(14.5), 141L));
			// clients141.add(new Client(102448L, "GALLESUR S.A.", new Double(20), new Double(10), 141L));
			// clients141.add(new Client(102449L, "COOPERATIVA DE TRABAJO GOLOCOOP LTDA", new Double(24),
			// new Double(14.5), 141L));
			// clients141.add(new Client(102450L, "GONZALEZ FERNANDO JAVIER", new Double(20), new Double(10), 141L));
			// User user141 = new User(141L, "BELINDA OLMEDO", clients141);
			//
			// add(user3);
			// add(user51);
			// add(user52);
			// add(user141);
		}
	}
}
