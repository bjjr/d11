<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="userAccount/edit.do" modelAttribute="userAccountForm" >

	<acme:password code="chorbi.password.old" path="previousPassword"/>
	<br/>
	<acme:password code="chorbi.password.new" path="password"/>
	<br/>
	<acme:password code="chorbi.passwdConf" path="passwdConf"/>
	<br/>

	<acme:submit name="save" code="misc.save"/>		
	<acme:cancel url="/" code="misc.cancel"/>
		
</form:form>