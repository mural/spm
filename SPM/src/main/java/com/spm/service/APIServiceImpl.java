package com.spm.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.spm.common.domain.Application;
import com.spm.common.domain.Application.ApplicationProvider;
import com.spm.common.http.WebServiceFactory;
import com.spm.common.http.webservice.WebService;
import com.spm.domain.Client;
import com.spm.domain.Order;
import com.spm.domain.OrderItem;
import com.spm.domain.Product;
import com.spm.domain.User;
import com.spm.domain.Visit;
import com.spm.parser.xml.ClientsParser;
import com.spm.parser.xml.PriceListDateParser;
import com.spm.parser.xml.ProductsParser;
import com.spm.parser.xml.UsersParser;

/**
 * @author Maxi Rosson
 * @author Fernando Perez
 */
public class APIServiceImpl implements APIService {

	// Common parameters
	private static final String USER_ID = "userID";

	/**
	 * @see com.spm.service.APIService#getContacts()
	 */
	@Override
	public List<User> getContacts() {
		WebService webservice = newGetService("contacts", "names", false);
		// webservice.addParameter(USERNAME, username);

		return webservice.execute(new UsersParser());
	}

	/**
	 * @see com.spm.service.APIService#getProducts()
	 */
	@Override
	public List<Product> getProducts() {
		// WebService webservice = newGetService("products", "data");
		WebService webservice = newGetService("contacts", "products", false);

		return webservice.execute(new ProductsParser());
	}

	/**
	 * @see com.spm.service.APIService#getClients(User)
	 */
	@Override
	public List<Client> getClients(User user) {
		// WebService webservice = newGetService("clients", "data");
		WebService webservice = newGetService("contacts",
				"client" + "/" + user.getId(), false);

		return webservice.execute(new ClientsParser());
	}

	/**
	 * @see com.spm.service.APIService#getPriceListDate()
	 */
	@Override
	public Date getPriceListDate() {
		// WebService webservice = newGetService("clients", "data");
		WebService webservice = newGetService("contacts", "lastUpdate", false);

		return webservice.execute(new PriceListDateParser());
	}

	private WebService newGetService(String module, String action) {
		return newGetService(module, action, true);
	}

	private WebService newGetService(String module, String action,
			Boolean mocked) {
		String url = ApplicationProvider.getServerApiURL();
		return WebServiceFactory.newGet(url, module, action, mocked);
	}

	@Override
	public Long lastOrderNumber(User user) throws SQLException {
		try {
			Class.forName(driver).newInstance();
			con = DriverManager.getConnection(url + db, userDB, passDB);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Long number = lastOrderNumberByUser(user, con);

		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return number;
	}

	/**
	 * @see com.spm.service.APIService#sync(Order)
	 */
	@Override
	public String sync(Order order, Client client) {

		return syncro(order, client);
	}

	// private WebService newFormPostService(String module, String action) {
	// return newFormPostService(module, action, false);
	// }

	// private WebService newFormPostService(String module, String action,
	// Boolean mocked) {
	// String url = ApplicationProvider.getServerApiURL();
	// return WebServiceFactory.newFormPost(url, module, action, mocked);
	// }

	// private WebService newMultipartPostService(String module, String action)
	// {
	// return newMultipartPostService(module, action, false);
	// }

	// private WebService newMultipartPostService(String module, String action,
	// Boolean mocked) {
	// String url = ApplicationProvider.getServerApiURL();
	// return WebServiceFactory.newMultipartPost(url, module, action, mocked);
	// }

	private void addUserIdParameter(WebService webservice) {
		webservice.addParameter(USER_ID, Application.APPLICATION_PROVIDER.get()
				.getUser().getId());
	}

	Connection con = null;
	String url = "jdbc:jtds:sqlserver://planetadulce.dyndns.org/";
	String db = "Planeta2";
	// String db = "Prueba"; // test table
	String driver = "net.sourceforge.jtds.jdbc.Driver";
	String userDB = "celulares";
	String passDB = "celulares";

	public String syncro(Order order, Client client) {
		int result = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		try {
			Class.forName(driver).newInstance();
			con = DriverManager.getConnection(url + db, userDB, passDB);
			con.setAutoCommit(false);
			try {
				PreparedStatement ps;
				double subtotal = 0;
				double descuento = 0;

				for (OrderItem orderItem : order.getProducts()) {
					subtotal += orderItem.getPrice() * orderItem.getQuantity(); // sin
																				// dto.
				}
				if (order.getType().equals(Order.NORMAL)) {
					descuento = subtotal * (client.getDiscount() / 100);
				} else {
					descuento = subtotal * (client.getDiscount2() / 100);
				}

				ps = con.prepareStatement(
						"INSERT INTO _INT_PED_VEN (FECHA, FEC_ENTREGA, FK_ERP_EMPRESAS, NUMERO, FK_ERP_ASESORES, "
								+ "TIPO_DE_PEDIDO, ENTREGADO, FK_ERP_CON_VEN, FK_ERP_DEPOSITOS, FK_ERP_MONEDAS, "
								+ "IMP_SUBTOTAL, IMP_DESCUENTO, IMP_TOTAL, FK_ERP_LIS_PRECIO, COORDENADAS, DESCRIPCION) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				ps.setDate(1, new java.sql.Date(System.currentTimeMillis()));
				if (order.getStatus().equals("Entregado")) {
					ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
				} else {
					ps.setDate(2, new java.sql.Date(System.currentTimeMillis()
							+ (259200 * 1000))); // 3 days
				}
				ps.setString(3, order.getClientId().toString());
				ps.setString(4, order.getNumber());
				ps.setString(5, order.getUserId().toString());

				ps.setString(6, order.getType());
				ps.setString(7, order.getStatus());
				ps.setInt(8, 3); // to define...FK_ERP_CON_VEN
				ps.setInt(9, 1); // to define...FK_ERP_DEPOSITOS
				ps.setInt(10, 1); // pesos FK_ERP_MONEDAS

				if (order.getType().equals(Order.NORMAL)) {
					subtotal = subtotal
							- (subtotal * (client.getDiscount() / 100));
					ps.setDouble(11, subtotal); // subtotal * dto
				} else {
					subtotal = subtotal
							- (subtotal * (client.getDiscount2() / 100));
					ps.setDouble(11, subtotal); // subtotal * dto
				}
				ps.setDouble(12, descuento); // dto
				if (order.getType().equals(Order.NORMAL)) {
					ps.setDouble(13, subtotal * 1.21); // total = subtotal * dto
														// + IVA
				} else {
					ps.setDouble(13, subtotal); // total = subtotal * dto
												// (especial sin IVA)
				}
				ps.setInt(14, Integer.valueOf(client.getPriceList().toString())); // lista
																					// de
																					// precios

				if (order.getStatus().equals("Entregado")) {
					ps.setByte(7, (byte) 1);
				} else {
					ps.setByte(7, (byte) 0);
				}
				ps.setString(15, order.getCoordinates());
				ps.setString(16, order.getAddress());
				ps.executeUpdate();

				ResultSet idRS = ps.getGeneratedKeys();
				idRS.next();
				int id = idRS.getInt("ID");

				for (OrderItem orderItem : order.getProducts()) {
					ps = con.prepareStatement(
							"INSERT INTO _INT_DET_PED_VEN (FK_ERP_COM_VEN, FK_ERP_ARTICULOS, CAN_COMPRA, PRE_LIS, PRECIO, "
									+ "IMP_TOT_LIS, IMP_TOTAL) "
									+ "VALUES (?, ?, ?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, id);
					ps.setString(2, orderItem.getId().toString());
					ps.setInt(3, orderItem.getQuantity());
					ps.setDouble(4, 0);
					ps.setDouble(5, orderItem.getPrice());

					ps.setDouble(6, 0);
					ps.setDouble(7,
							orderItem.getPrice() * orderItem.getQuantity());

					ps.executeUpdate();
				}

				// Obtengo el ultimo numero de orden
				User user2 = Application.APPLICATION_PROVIDER.get().getUser();
				lastOrderNumberByUser(user2, con); // asi crea los datos si no
													// existen

				ps = con.prepareStatement("SELECT TOP 10 [ID] ,[FECHA_SINC] ,[FK_ERP_ASESORES] ,[NUMERO] FROM [_INT_CONT]"
						+ "WHERE [FK_ERP_ASESORES] = ?");
				ps.setInt(1, Integer.valueOf(user2.getId().toString()));
				ResultSet res = ps.executeQuery();
				res.next();

				ps = con.prepareStatement("UPDATE _INT_CONT SET FECHA_SINC=?, NUMERO=? WHERE ID = ?");
				ps.setDate(1, new java.sql.Date(System.currentTimeMillis()));
				ps.setInt(2, Integer.valueOf(user2.getOrderNumber().toString()));
				ps.setInt(3, res.getInt("ID"));
				result = ps.executeUpdate();

				con.commit();
			} catch (SQLException se) {
				System.out.println("SQL code does not execute.");
				System.out.println(se);
				con.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException se) {
				System.out.println(se);
			}
		}
		if (result == 1) {
			return "OK";
		} else {
			return "ERROR";
		}
	}

	/**
	 * @param user2
	 * @return
	 * @throws SQLException
	 */
	private Long lastOrderNumberByUser(User user2, Connection con)
			throws SQLException {
		int result = 0;
		// Integer random = new
		// Random(System.currentTimeMillis()).nextInt(9999);
		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT TOP 10 [ID] ,[FECHA_SINC] ,[FK_ERP_ASESORES] ,[NUMERO] FROM [_INT_CONT]"
							+ "WHERE [FK_ERP_ASESORES] = ?");
			ps.setInt(1, Integer.valueOf(user2.getId().toString()));
			ResultSet res = ps.executeQuery();
			res.next();
			result = res.getInt("NUMERO");

		} catch (SQLException se) {
			System.out.println("SQL code does not execute.");
			System.out.println(se);

			if (se.getMessage().equals("No current row in the ResultSet.")) {
				PreparedStatement ps = con
						.prepareStatement("INSERT INTO _INT_CONT (FECHA_SINC, FK_ERP_ASESORES, NUMERO) VALUES (?, ?, ?)");
				ps.setDate(1, new java.sql.Date(System.currentTimeMillis()));
				ps.setInt(2, Integer.valueOf(user2.getId().toString()));
				ps.setInt(3, 1);
				result = ps.executeUpdate();
			}
		}

		return Long.valueOf(result);
	}

	/**
	 * @see com.spm.service.APIService#sync(com.spm.domain.Visit)
	 */
	@Override
	public String sync(Visit visit) {
		int result = 0;
		try {
			Class.forName(driver).newInstance();
			con = DriverManager.getConnection(url + db, userDB, passDB);
			con.setAutoCommit(false);
			try {
				PreparedStatement ps;
				ps = con.prepareStatement(
						"INSERT INTO _INT_NOT_VEN (FK_ERP_EMPRESAS, FK_ERP_ASESORES, NOTA, COORDENADAS, DESCRIPCION, FECHA, TEMA) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);

				ps.setString(1, visit.getClientId().toString());
				ps.setInt(2, visit.getUserId().intValue());
				ps.setString(3, visit.getComment());
				ps.setString(4, visit.getCoordinates());
				ps.setString(5, visit.getAddress());
				ps.setDate(6, new java.sql.Date(System.currentTimeMillis()));
				ps.setString(7, visit.getTopic());

				result = ps.executeUpdate();

				// Obtengo el ultimo numero de orden
				// User user2 =
				// Application.APPLICATION_PROVIDER.get().getUser();
				// lastOrderNumberByUser(user2, con); // asi crea los datos si
				// no existen
				//
				// ps =
				// con.prepareStatement("SELECT TOP 10 [ID] ,[FECHA_SINC] ,[FK_ERP_ASESORES] ,[NUMERO] FROM [_INT_CONT]"
				// + "WHERE [FK_ERP_ASESORES] = ?");
				// ps.setInt(1, Integer.valueOf(user2.getId().toString()));
				// ResultSet res = ps.executeQuery();
				// res.next();
				//
				// ps =
				// con.prepareStatement("UPDATE _INT_CONT SET FECHA_SINC=?, NUMERO=? WHERE ID = ?");
				// ps.setDate(1, new java.sql.Date(System.currentTimeMillis()));
				// ps.setInt(2,
				// Integer.valueOf(user2.getOrderNumber().toString()));
				// ps.setInt(3, res.getInt("ID"));
				// result = ps.executeUpdate();

				con.commit();
			} catch (SQLException se) {
				System.out.println("SQL code does not execute.");
				System.out.println(se);
				con.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException se) {
				System.out.println(se);
			}
		}
		if (result == 1) {
			return "OK";
		} else {
			return "ERROR";
		}
	}
}