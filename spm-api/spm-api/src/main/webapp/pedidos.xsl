<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html" indent="yes" version="4.0"/>
<xsl:template match="/">
  <html>
  <head>
    <title>SPM</title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <script type="text/javascript" src="../tablefilter.js"></script> 
    <link rel="stylesheet" type="text/css" href="../style.css"/>
    <link rel="stylesheet" type="text/css" href="../filtergrid.css"/>
    <style type="text/css">
		body
		{
		margin:10px;
		background-color:#eeeeee;
		font-family:verdana,helvetica,sans-serif;
		}
    </style>
  </head>
  <body>
	  <h2>SPM <img src="../green-circle.png" alt="estado OK" height="22" width="22" /></h2>
	  <h3>Pedidos</h3>
	  <table id="hor-zebra">
	    <tr bgcolor="#9acd32">
	      <th align="center">ID</th>
	      <th bgcolor="#9acd32" align="center">Fecha</th>
	      <th align="center">Usuario</th>
	      <th align="center">Cliente</th>
	      <th align="center">Ubicacion</th>
	    </tr>
	    <xsl:for-each select="orders/order">
	    <xsl:choose>
	        <xsl:when test="(position() mod 2)">
	            <tr>
	                <td align="center">&#160;<xsl:value-of select="id" />&#160;</td>
	                <td align="center">&#160;<xsl:value-of select="fecha" />&#160;</td>
	                <td align="center">&#160;<xsl:value-of select="user" />&#160;</td>
	                <td align="center">&#160;<xsl:value-of select="client" disable-output-escaping="yes" />&#160;</td>
	                <td align="center">&#160;<xsl:value-of select="location" disable-output-escaping="yes" />&#160;</td>
	            </tr>
	        </xsl:when>
	        <xsl:otherwise>
	            <tr bgcolor="#aaaaaa">
	                <td align="center">&#160;<xsl:value-of select="id" />&#160;</td>
	                <td align="center">&#160;<xsl:value-of select="fecha" />&#160;</td>
	                <td align="center">&#160;<xsl:value-of select="user" />&#160;</td>
	                <td align="center">&#160;<xsl:value-of select="client" disable-output-escaping="yes" />&#160;</td>
                    <td align="center">&#160;<xsl:value-of select="location" disable-output-escaping="yes" />&#160;</td>
	            </tr>
	        </xsl:otherwise>
	    </xsl:choose>
	    </xsl:for-each>
	  </table>
<script language="javascript" type="text/javascript">
//<![CDATA[ 
    var tProps =  {                   
                    col_2: "select",
                    display_all_text: "[ Todos ]",
                    sort_select: true,
                    rows_counter: true,
                    rows_counter_text: "Cantidad: ",
                    btn_reset: true,
                    bnt_reset_text: "Volver"
                };
    setFilterGrid("hor-zebra",tProps);
//]]>
</script>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>