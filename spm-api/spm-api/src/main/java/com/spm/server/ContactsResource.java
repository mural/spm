package com.spm.server;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.annotations.Form;
import org.jboss.resteasy.annotations.providers.jaxb.Stylesheet;
import org.jboss.resteasy.annotations.providers.jaxb.XmlHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Path(ContactsResource.CONTACTS_URL)
public class ContactsResource
{
	   Connection con = null;
	   String url = "jdbc:jtds:sqlserver://planetadulce.dyndns.org/";
	   String db = "Planeta2";
	   String driver = "net.sourceforge.jtds.jdbc.Driver";
	   String user = "celulares";
	   String pass = "celulares";
	   
   public static final String CONTACTS_URL = "/contacts";
   @Autowired
   ContactService service;

   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("lastUpdate")
   public String getLastUpdate()
   {
      return lastUpdate();
   }
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("names")
   public String getNames()
   {
      return names();
   }
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("products")
   public String getProducts()
   {
      return products();
   }
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("client/{id}")
   public String getClients(@PathParam("id") String id)
   {
      return clients(new Long(id));
   }
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("clients")
   public String getClients()
   {
      return clients(null);
   }
   
   @GET
   @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
   @Path("data")
   public Contacts getAll()
   {
      return service.getAll();
   }

   @PUT
   @POST
   @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
   @Path("data")
   public Response saveContact(@Context UriInfo uri, Contact contact)
         throws URISyntaxException
   {
      service.save(contact);
      URI newURI = UriBuilder.fromUri(uri.getPath()).path(contact.getLastName()).build();
      return Response.created(newURI).build();
   }

   @GET
   @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
   @Path("data/{lastName}")
   public Contact get(@PathParam("lastName") String lastName)
   {
      return service.getContact(lastName);
   }

   @POST
   @PUT
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   @Produces(MediaType.TEXT_HTML)
   public ModelAndView saveContactForm(@Form Contact contact)
         throws URISyntaxException
   {
      service.save(contact);
      return viewAll();
   }
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("orders")
   @XmlHeader("<?xml-stylesheet type='text/xsl' href='${baseuri}foo.xsl' ?>")
   @Stylesheet(type="text/css", href="test.xsl")
   public String getOrders()
   {
      return orders();
   }
   
	@POST
	@Path("/sync")
	@Produces(MediaType.TEXT_PLAIN)
	public String sync(@Form SyncForm form) {
		
		String response;
		if (form.hasStakes()) {
//			response = "true";
			response = syncro(form);
		} else {
			response = "false";
		}
		
		return response;
	}
	
	   public String syncro(SyncForm form) {
		   int result = 0;
		   try {
			   Class.forName(driver).newInstance();
			   con = DriverManager.getConnection(url+db, user, pass);
			   try{
				    //Statement st = con.createStatement();
				    Integer random = new Random(System.currentTimeMillis()).nextInt(9999);
				    System.out.println(random.toString());
				    PreparedStatement ps = con.prepareStatement("INSERT INTO _INT_PED_VEN (ID, FECHA, FEC_ENTREGA, FK_ERP_EMPRESAS, NUMERO, FK_ERP_ASESORES) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
				    ps.setInt(1, random);
				    ps.setDate(2, java.sql.Date.valueOf("2012-05-15"));//(form.getDate().replaceAll("/", "-"))));
				    ps.setDate(3, java.sql.Date.valueOf("2012-05-18"));
				    ps.setString(4, form.getClientId());
				    ps.setString(5, form.getNumero());
				    ps.setString(6, form.getSeller());
				    result = ps.executeUpdate(); 
				    
				    ps = con.prepareStatement("INSERT INTO _INT_DET_PED_VEN (IDD, FK_ERP_COM_VEN) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
				    ps.setInt(1, random);
				    ps.setString(2, random.toString());
				    ps.executeUpdate(); 
				    
				    con.close();
			   } catch (SQLException se) {
				   System.out.println("SQL code does not execute.");
				   System.out.println(se);
			   }  
		   	} catch (Exception e) {
		   		e.printStackTrace();
		    }
		   	if (result == 1) {
		   		return "OK";
		   	} else return "ERROR";
	   }
	   
	   public String orders() {
		   StringBuffer s = new StringBuffer();
		   int count = 0;
		   s.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
		   s.append("<?xml-stylesheet type='text/xsl' href='../pedidos.xsl' ?>");
		   
		   s.append("<orders type='array'>");
		   try {
			   Class.forName(driver).newInstance();
			   con = DriverManager.getConnection(url+db, user, pass);
			   try{
				    Statement st = con.createStatement();
				    ResultSet res = st.executeQuery("SELECT TOP 1000 ped.[ID], [FECHA], vend.FK_ITRIS_USERS, empr.DESCRIPCION as CLIENTE, ped.DESCRIPCION as UBICACION" +
				    		"							FROM _INT_PED_VEN ped" +
				    		"							LEFT JOIN _INT_INF_VENDEDORES vend ON ped.FK_ERP_ASESORES = vend.ID" +
				    		"							LEFT JOIN _INT_INF_EMPRESAS empr ON ped.FK_ERP_EMPRESAS = empr.ID" + 
				    		"							ORDER BY [FECHA] DESC");
				    System.out.println("Pedidos: ");
				    while (res.next()) {
				    	s.append("<order>");
				    	
				    	s.append("<id>");
					    s.append(res.getString("ID"));
					    s.append("</id>");
					    
				    	s.append("<fecha>");
					    s.append(res.getString("FECHA"));
					    s.append("</fecha>");
					    
				    	s.append("<user>");
					    s.append(res.getString("FK_ITRIS_USERS"));
					    s.append("</user>");
					    
				    	s.append("<client>");
				    	s.append("<![CDATA[");
					    s.append(res.getString("CLIENTE"));
					    s.append("]]>");
					    s.append("</client>");

				    	s.append("<location>");
				    	s.append("<![CDATA[");
					    s.append(res.getString("UBICACION"));
					    s.append("]]>");
					    s.append("</location>");
					    
					    s.append("</order>");
					    count++;
					    
				    }
				    System.out.println(s.toString());
				    con.close();
			   } catch (SQLException se) {
				   System.out.println("SQL code does not execute.");
				   System.out.println(se);
			   }  
		   	} catch (Exception e) {
		   		e.printStackTrace();
		    }
		   	s.append("<count>");
		   	s.append(count);
		   	s.append("</count>");

		   	s.append("</orders>");
		   	return s.toString();
	   }
   
   @GET
   @Produces(MediaType.TEXT_HTML)
   public ModelAndView viewAll()
   {
      // forward to the "contacts" view, with a request attribute named
      // "contacts" that has all of the existing contacts
      return new ModelAndView("contacts", "contacts", service.getAll());
   }
   
   public String names() {
	   StringBuffer s = new StringBuffer();
	   s.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
	   s.append("<users type='array'>");
	   try {
		   Class.forName(driver).newInstance();
		   con = DriverManager.getConnection(url+db, user, pass);
		   try{
			    Statement st = con.createStatement();
			    ResultSet res = st.executeQuery("SELECT TOP 1000 [ID] ,[DESCRIPCION] ,[FK_ITRIS_USERS] ,[HABILITADO] ,[CARGO], [_TELEFONO] FROM _INT_INF_VENDEDORES");
			    System.out.println("Usuarios: ");
			    while (res.next()) {
			    	s.append("<user>");
			    	
			    	s.append("<id>");
				    s.append(res.getString("ID"));
				    s.append("</id>");
			    	
				    s.append("<fullName>");
				    s.append(res.getString("DESCRIPCION"));
				    s.append("</fullName>");
	
				    s.append("<name>");
					s.append(res.getString("FK_ITRIS_USERS"));
					s.append("</name>");
					    
					s.append("<enabled>");
					s.append(res.getString("HABILITADO"));
					s.append("</enabled>");
					    
					s.append("<charge>");
					s.append(res.getString("CARGO"));
					s.append("</charge>");
					    
					s.append("<tel>");
					s.append(res.getString("_TELEFONO"));
					s.append("</tel>");
				          
				    s.append("</user>");
				    
				    System.out.println(s.toString());
			    }
			    con.close();
		   } catch (SQLException se) {
			   System.out.println("SQL code does not execute.");
		   }  
	   	} catch (Exception e) {
	   		e.printStackTrace();
	    }
	   	s.append("</users>");
	   	return s.toString();
   }
   
   public String products() {
	   StringBuffer s = new StringBuffer();
	   s.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
	   s.append("<products type='array'>");
	   try {
		   Class.forName(driver).newInstance();
		   con = DriverManager.getConnection(url+db, user, pass);
		   try{
			    Statement st = con.createStatement();
			    ResultSet res = st.executeQuery("SELECT TOP 1000 [_INT_INF_ARTICULOS].[ID] ,[DESCRIPCION] ,[HABILITADO] ,[TIPO], [PRECIO], [FK_ERP_LIS_PRECIO] FROM [_INT_INF_ARTICULOS]" + 
			    		" LEFT JOIN [_INT_INF_PRE_VEN] ON [_INT_INF_ARTICULOS].[ID] = [_INT_INF_PRE_VEN].[FK_ERP_ARTICULOS]" +
			    		"WHERE [FK_ERP_LIS_PRECIO] <> 0");
			    System.out.println("Productos: ");
			    while (res.next()) {
			    	s.append("<product>");
			    	
			    	s.append("<id>");
				    s.append(res.getString("ID"));
				    s.append("</id>");
			    	
				    s.append("<name>");
				    s.append(res.getString("DESCRIPCION"));
				    s.append("</name>");
				    
				    s.append("<price>");
				    s.append(res.getString("PRECIO"));
				    s.append("</price>");
				    
				    s.append("<priceList>");
				    s.append(res.getString("FK_ERP_LIS_PRECIO"));
				    s.append("</priceList>");
				    
				    s.append("</product>");
				    
			    }
			    System.out.println(s.toString());
			    con.close();
		   } catch (SQLException se) {
			   System.out.println("SQL code does not execute.");
		   }  
	   	} catch (Exception e) {
	   		e.printStackTrace();
	    }
	   	s.append("</products>");
	   	return s.toString();
   }
   
   public String clients(Long seller) {
	   StringBuffer s = new StringBuffer();
	   s.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
	   s.append("<clients type='array'>");
	   try {
		   Class.forName(driver).newInstance();
		   con = DriverManager.getConnection(url+db, user, pass);
		   try{
			    Statement st = con.createStatement();
			    StringBuffer query = new StringBuffer("SELECT TOP 1000 [ID] ,[DESCRIPCION] ,[_BON_ACU] ,[_BON_ACU2], [FK_ERP_ASESORES], [FK_ERP_LIS_PRECIO] FROM [_INT_INF_EMPRESAS]");
			    if (seller != null) {
			    	query.append(" WHERE [FK_ERP_LIS_PRECIO] IS NOT NULL AND [FK_ERP_ASESORES] = " + seller.toString());
			    }
			    ResultSet res = st.executeQuery(query.toString());
			    
			    System.out.println("Productos: ");
			    while (res.next()) {
			    	s.append("<client>");
			    	
			    	s.append("<id>");
				    s.append(res.getString("ID"));
				    s.append("</id>");
			    	
				    s.append("<name>");
				    s.append(res.getString("DESCRIPCION"));
				    s.append("</name>");
				    
				    s.append("<discount>");
				    String dto = res.getString("_BON_ACU");
				    Double nDesc = new Double(100);
				    Double discount = new Double(0);
				    if (dto != null && dto.length() > 0) {
				    	String[] dis = dto.split("\\+");
				    	for (int i = 0; i < dis.length; i++) {
				    		nDesc = nDesc * (1 - (new Double(dis[i]) / 100));
				    	}
				    	discount = 100 - nDesc;
				    }
				    s.append(roundTwoDecimals(discount));
				    s.append("</discount>");
				    
				    s.append("<discount2>");
				    String dto2 = res.getString("_BON_ACU2");
				    Double nDesc2 = new Double(100);
				    Double discount2 = new Double(0);
				    if (dto2 != null && dto2.length() > 0) {
				    	String[] dis2 = dto2.split("\\+");
				    	for (int i = 0; i < dis2.length; i++) {
				    		nDesc2 = nDesc2 * (1 - (new Double(dis2[i]) / 100));
				    	}
				    	discount2 = 100 - nDesc2;
				    }
				    s.append(roundTwoDecimals(discount2));
				    s.append("</discount2>");
				    
				    s.append("<user_id>");
				    s.append(res.getString("FK_ERP_ASESORES"));
				    s.append("</user_id>");
				    
				    s.append("<priceList>");
				    s.append(res.getString("FK_ERP_LIS_PRECIO"));
				    s.append("</priceList>");
				    
				    s.append("</client>");
				    
			    }
			    System.out.println(s.toString());
			    con.close();
		   } catch (SQLException se) {
			   System.out.println("SQL code does not execute.");
		   }  
	   	} catch (Exception e) {
	   		e.printStackTrace();
	    }
	   	s.append("</clients>");
	   	return s.toString().replaceAll("&", "&amp;");
   }
   
   String roundTwoDecimals(Double d) {
           DecimalFormat twoDForm = new DecimalFormat("#.##");
       return twoDForm.format(d).replaceAll(",", ".");
   }
   
   public String lastUpdate() {
	   StringBuffer s = new StringBuffer();
	   s.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
	   s.append("<pricelistupdate>");
	   try {
		   Class.forName(driver).newInstance();
		   con = DriverManager.getConnection(url+db, user, pass);
		   try{
			    Statement st = con.createStatement();
			    StringBuffer query = new StringBuffer("SELECT TOP 1 [ID] ,[FK_ERP_ARTICULOS] ,[FK_ERP_LIS_PRECIO] ,[FK_ERP_MONEDAS] ,[PRECIO] ,[FEC_ULT_ACT] FROM [Planeta2].[dbo].[_INT_INF_PRE_VEN] ORDER BY [FEC_ULT_ACT] DESC");
			    ResultSet res = st.executeQuery(query.toString());
			    
			    System.out.println("Fecha: ");
			    while (res.next()) {
			    	s.append("<date>");
			   
				    s.append(res.getString("FEC_ULT_ACT"));

				    s.append("</date>");
				    
			    }
			    System.out.println(s.toString());
			    con.close();
		   } catch (SQLException se) {
			   System.out.println("SQL code does not execute.");
		   }  
	   	} catch (Exception e) {
	   		e.printStackTrace();
	    }
	   	s.append("</pricelistupdate>");
	   	return s.toString().replaceAll("&", "&amp;");
   }
   
   public static void main(String[] args) {
	   System.out.println("Getting All Rows from a table!");
	   Connection con = null;
	   String url = "jdbc:jtds:sqlserver://190.99.115.46/";
	   String db = "Planeta2";
	   String driver = "net.sourceforge.jtds.jdbc.Driver";
	   String user = "celulares";
	   String pass = "celulares";
	   try{
	   Class.forName(driver).newInstance();
	   con = DriverManager.getConnection(url+db, user, pass);
	   try{
	   Statement st = con.createStatement();
	   ResultSet res = st.executeQuery("SELECT TOP 1000 [ID] ,[DESCRIPCION] ,[FK_ITRIS_USERS] ,[HABILITADO] ,[CARGO] FROM _INT_INF_VENDEDORES");
	   System.out.println("Nombre: ");
	   while (res.next()) {
	   String s = res.getString("DESCRIPCION");
	   System.out.println(s);
	   }
	   con.close();
	   }
	   catch (SQLException s){
	   System.out.println("SQL code does not execute.");
	   }  
	   }
	   catch (Exception e){
	   e.printStackTrace();
	   }
	   }
}
