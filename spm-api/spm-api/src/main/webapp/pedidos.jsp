<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<html>
<head>
  <title>Java-Only Example</title>
</head>

<body>

<c:import url="/contacts/names" var="xmldoc"/>
<c:import url="/test.xsl" var="xsltdoc"/>

<x:transform xml="${xmldoc}" xslt="${xsltdoc}"/>

</body>
</html>