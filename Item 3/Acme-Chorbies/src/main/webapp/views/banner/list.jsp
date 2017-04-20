<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<display:table pagesize="5" class="displaytag" 
		name="banners" requestURI="${requestURI}" id="row">
	
		<!-- Attributes -->
		
		<acme:column code="banner.service" property="${row.service}" sortable="true"/>
	
		<acme:column code="banner.path" property="${row.path}"/>
<display:column>
		<acme:link href="banner/edit.do?BannerId=${row.id }" code="misc.edit"/>
	</display:column>
	
</display:table>
