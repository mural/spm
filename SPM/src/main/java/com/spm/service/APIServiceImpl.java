package com.spm.service;

import android.content.SyncStatusObserver;
import android.util.Log;

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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hugo.weaving.DebugLog;

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
    @DebugLog
    @Override
    public List<User> getContacts() {
        List<User> users = new ArrayList<>();
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url + db, userDB, passDB);
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT TOP 1000 [_INT_INF_VENDEDORES].[ID] AS ID_USER ,[DESCRIPCION] ,[FK_ITRIS_USERS] ,[HABILITADO] ,[CARGO], [_TELEFONO], [NUMERO] " +
                        "FROM _INT_INF_VENDEDORES " +
                        "LEFT OUTER JOIN [_INT_CONT] ON [_INT_INF_VENDEDORES].[ID] = [_INT_CONT].[FK_ERP_ASESORES]");
                while (res.next()) {
                    User user = new User(Long.valueOf(res.getString("ID_USER")), res.getString("FK_ITRIS_USERS"));
                    user.setUserName(res.getString("DESCRIPCION"));
                    user.setPhoneNumber(res.getString("_TELEFONO"));
                    user.setUsersUpdateDate(new Date());
                    user.setEnabled(Long.valueOf(res.getString("HABILITADO")) == 1);
                    Long orderNumber;
                    int numeroDeOrden = res.getInt("NUMERO"); // puede ser NULL
                    orderNumber = Long.valueOf(numeroDeOrden);
                    if (res.wasNull()) {
                        orderNumber = lastOrderNumber(user);
                    }
                    if (orderNumber > 0) {
                        user.setOrderNumber(orderNumber);
                        users.add(user);
                        System.out.println(user.getUserName() + " ok");
                    } else {
                        System.out.println(user.getUserName() + " error");
                    }
                }
                con.close();
            } catch (SQLException se) {
                System.out.println("SQL code does not execute.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * @see com.spm.service.APIService#getProducts()
     */
    @DebugLog
    @Override
    public List<Product> getProducts() {
//		// WebService webservice = newGetService("products", "data");
//		WebService webservice = newGetService("contacts", "products", false);
//		return webservice.execute(new ProductsParser());
        List<Product> productsList = new ArrayList<>();
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url + db, userDB, passDB);
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT TOP 1000 [_INT_INF_ARTICULOS].[ID] ,[DESCRIPCION] ,[HABILITADO] ,[TIPO], [PRECIO], [FK_ERP_LIS_PRECIO] FROM [_INT_INF_ARTICULOS]" +
                        " LEFT JOIN [_INT_INF_PRE_VEN] ON [_INT_INF_ARTICULOS].[ID] = [_INT_INF_PRE_VEN].[FK_ERP_ARTICULOS]" +
                        "WHERE [FK_ERP_LIS_PRECIO] <> 0");
                System.out.println("Productos: ");
                while (res.next()) {
                    Product product = null;

                    String price;
                    if (res.getString("PRECIO").equals("null")) {
                        price = "0";
                    } else {
                        price = res.getString("PRECIO");
                    }
                    String priceList;
                    if (res.getString("FK_ERP_LIS_PRECIO").equals("null")) {
                        priceList = "0";
                    } else {
                        priceList = res.getString("FK_ERP_LIS_PRECIO");
                    }

                    for (Product productToFind : productsList) {
                        if (productToFind.getId().toString().equals(res.getString("ID"))) {
                            product = productToFind;
                        }
                    }
                    if (product == null) {
                        product = new Product(Long.valueOf(res.getString("ID")), res.getString("DESCRIPCION"), 1L, Integer.valueOf(priceList));
                    }
                    if (priceList.equals("1")) {
                        product.setPrice1ant(product.getPrice1());
                        product.setPrice1(Double.valueOf(price));
                    }
                    if (priceList.equals("2")) {
                        product.setPrice2ant(product.getPrice2());
                        product.setPrice2(Double.valueOf(price));
                    }
                    if (priceList.equals("3")) {
                        product.setPrice3ant(product.getPrice3());
                        product.setPrice3(Double.valueOf(price));
                    }
                    if (priceList.equals("4")) {
                        product.setPrice4ant(product.getPrice4());
                        product.setPrice4(Double.valueOf(price));
                    }
                    if (priceList.equals("5")) {
                        product.setPrice5ant(product.getPrice5());
                        product.setPrice5(Double.valueOf(price));
                    }
                    productsList.add(product);
                }
                //System.out.println(s.toString());
                con.close();
            } catch (SQLException se) {
                System.out.println("SQL code does not execute.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productsList;
    }

    /**
     * @see com.spm.service.APIService#getClients(User)
     */
    @DebugLog
    @Override
    public List<Client> getClients(User user) {
//		// WebService webservice = newGetService("clients", "data");
//		WebService webservice = newGetService("contacts",
//				"client" + "/" + user.getId(), false);
//		return webservice.execute(new ClientsParser());
        List<Client> clientsList = new ArrayList<>();
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url + db, userDB, passDB);
            try {
                Statement st = con.createStatement();
                StringBuffer query = new StringBuffer("SELECT TOP 1000 [ID] ,[DESCRIPCION] ,[_BON_ACU] ,[_BON_ACU2], [FK_ERP_ASESORES], [FK_ERP_LIS_PRECIO] FROM [_INT_INF_EMPRESAS]");
                if (user.getId() != null) {
                    query.append(" WHERE [FK_ERP_LIS_PRECIO] IS NOT NULL AND [FK_ERP_ASESORES] = " + user.getId().toString());
                }
                ResultSet res = st.executeQuery(query.toString());

                System.out.println("Clientes: ");
                while (res.next()) {
                    String dto = res.getString("_BON_ACU");
                    Double nDesc = 100d;
                    Double discount = 0d;
                    if (dto != null && dto.length() > 0) {
                        String[] dis = dto.split("\\+");
                        for (int i = 0; i < dis.length; i++) {
                            nDesc = nDesc * (1 - (Double.valueOf(dis[i]) / 100));
                        }
                        discount = 100 - nDesc;
                    }
                    String dto2 = res.getString("_BON_ACU2");
                    Double nDesc2 = 100d;
                    Double discount2 = 0d;
                    if (dto2 != null && dto2.length() > 0) {
                        String[] dis2 = dto2.split("\\+");
                        for (int i = 0; i < dis2.length; i++) {
                            nDesc2 = nDesc2 * (1 - (Double.valueOf(dis2[i]) / 100));
                        }
                        discount2 = 100 - nDesc2;
                    }

                    clientsList.add(new Client(Long.valueOf(res.getString("ID")), res.getString("DESCRIPCION"), Double.valueOf(roundTwoDecimals(discount)), Double.valueOf(roundTwoDecimals(discount2)),
                            Long.valueOf(res.getString("FK_ERP_ASESORES")), Long.valueOf(res.getString("FK_ERP_LIS_PRECIO"))));

                }
                //System.out.println(s.toString());
                con.close();
            } catch (SQLException se) {
                System.out.println("SQL code does not execute.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clientsList;
    }


    private String roundTwoDecimals(Double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return twoDForm.format(d).replaceAll(",", ".");
    }

    /**
     * @see com.spm.service.APIService#getPriceListDate()
     */
    @DebugLog
    @Override
    public Date getPriceListDate() {
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url + db, userDB, passDB);
            try {
                Statement st = con.createStatement();
                StringBuffer query = new StringBuffer("SELECT TOP 1 [ID] ,[FK_ERP_ARTICULOS] ,[FK_ERP_LIS_PRECIO] ,[FK_ERP_MONEDAS] ,[PRECIO] ,[FEC_ULT_ACT] " +
                        "FROM [Planeta2].[dbo].[_INT_INF_PRE_VEN] ORDER BY [FEC_ULT_ACT] DESC");
                ResultSet res = st.executeQuery(query.toString());

                System.out.println("Fecha: ");
                while (res.next()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                    Date updateDate;
                    try {
                        updateDate = sdf.parse(res.getString("FEC_ULT_ACT"));
                        System.out.println(updateDate);
                        return updateDate;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                con.close();
            } catch (SQLException se) {
                System.out.println("SQL code does not execute.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        Log.d(APIServiceImpl.class.toString(), "lastOrderNumber user: " + user.getUserName());
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

    @DebugLog
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

                int id = getLastIdfromTable("_INT_PED_VEN", con) + 1;
                ps = con.prepareStatement(
                        "INSERT INTO _INT_PED_VEN (ID, FECHA, FEC_ENTREGA, FK_ERP_EMPRESAS, NUMERO, FK_ERP_ASESORES, "
                                + "TIPO_DE_PEDIDO, ENTREGADO, FK_ERP_CON_VEN, FK_ERP_DEPOSITOS, FK_ERP_MONEDAS, "
                                + "IMP_SUBTOTAL, IMP_DESCUENTO, IMP_TOTAL, FK_ERP_LIS_PRECIO, COORDENADAS, DESCRIPCION) "
                                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, id);
                ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
                if (order.getStatus().equals("Entregado")) {
                    ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                } else {
                    ps.setDate(3, new java.sql.Date(System.currentTimeMillis()
                            + (259200 * 1000))); // 3 days
                }
                ps.setString(4, order.getClientId().toString());
                ps.setString(5, order.getNumber());
                ps.setString(6, order.getUserId().toString());

                ps.setString(7, order.getType());
                ps.setString(8, order.getStatus());
                ps.setInt(9, 3); // to define...FK_ERP_CON_VEN
                ps.setInt(10, 1); // to define...FK_ERP_DEPOSITOS
                ps.setInt(11, 1); // pesos FK_ERP_MONEDAS

                if (order.getType().equals(Order.NORMAL)) {
                    subtotal = subtotal
                            - (subtotal * (client.getDiscount() / 100));
                    ps.setDouble(12, subtotal); // subtotal * dto
                } else {
                    subtotal = subtotal
                            - (subtotal * (client.getDiscount2() / 100));
                    ps.setDouble(12, subtotal); // subtotal * dto
                }
                ps.setDouble(13, descuento); // dto
                if (order.getType().equals(Order.NORMAL)) {
                    ps.setDouble(14, subtotal * 1.21); // total = subtotal * dto
                    // + IVA
                } else {
                    ps.setDouble(14, subtotal); // total = subtotal * dto
                    // (especial sin IVA)
                }
                ps.setInt(15, Integer.valueOf(client.getPriceList().toString())); // lista
                // de
                // precios

                if (order.getStatus().equals("Entregado")) {
                    ps.setByte(8, (byte) 1);
                } else {
                    ps.setByte(8, (byte) 0);
                }
                ps.setString(16, order.getCoordinates());
                ps.setString(17, order.getAddress());
                ps.executeUpdate();

//                ResultSet idRS = ps.getGeneratedKeys();
//                idRS.next();
//                int id = idRS.getInt("ID");

                for (OrderItem orderItem : order.getProducts()) {
                    int idd = getLastIdfromTable("_INT_DET_PED_VEN", "IDD", con) + 1;
                    ps = con.prepareStatement(
                            "INSERT INTO _INT_DET_PED_VEN (IDD, FK_ERP_COM_VEN, FK_ERP_ARTICULOS, CAN_COMPRA, PRE_LIS, PRECIO, "
                                    + "IMP_TOT_LIS, IMP_TOTAL) "
                                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, idd);
                    ps.setInt(2, id);
                    ps.setString(3, orderItem.getId().toString());
                    ps.setInt(4, orderItem.getQuantity());
                    ps.setDouble(5, 0);
                    ps.setDouble(6, orderItem.getPrice());

                    ps.setDouble(7, 0);
                    ps.setDouble(8,
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
    @DebugLog
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
                int id = getLastIdfromTable("_INT_CONT", con) + 1;
                PreparedStatement ps = con
                        .prepareStatement("INSERT INTO _INT_CONT (ID, FECHA_SINC, FK_ERP_ASESORES, NUMERO) VALUES (?, ?, ?, ?)");
                ps.setInt(1, id);
                ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
                ps.setInt(3, Integer.valueOf(user2.getId().toString()));
                ps.setInt(4, 1);
                result = ps.executeUpdate();
            }
        }

        return Long.valueOf(result);
    }

    private int getLastIdfromTable(String tableName, String idName, Connection con) {
        int id = -1;
        try {
            try {
                PreparedStatement ps;
                ps = con.prepareStatement(
                        "SELECT TOP 1 * FROM " + tableName + " ORDER BY " + idName + " DESC");

                ResultSet res = ps.executeQuery();
                res.next();
                id = res.getInt(idName);
            } catch (SQLException se) {
                System.out.println("SQL code does not execute.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            try {
//                con.close();
//            } catch (SQLException se) {
//                System.out.println(se);
//            }
        }
        return id;
    }

    //ID column must call ID !
    private int getLastIdfromTable(String tableName, Connection con) {
        return getLastIdfromTable(tableName, "ID", con);
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

                //TODO deberia actualizar eso ?
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