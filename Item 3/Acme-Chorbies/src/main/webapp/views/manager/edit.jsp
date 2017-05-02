<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="${action}" modelAttribute="${modelAttribute}" >
		
		<acme:textbox code="actor.name" path="name"/>
		<br/>
		<acme:textbox code="actor.surname" path="surname"/>
		<br/>
		<acme:textbox code="actor.email" path="email"/>
		<br/>
		<acme:textbox code="actor.phone" path="phone"/>
		<br/>
		<acme:textbox code="manager.company" path="company"/>
		<br/>		
		<acme:textbox code="manager.vat" path="vat"/>
		<br/>
		<acme:textbox code="chorbi.username" path="userAccount.username"/>
		<br/>
		<acme:password code="chorbi.password" path="userAccount.password"/>
		<br/>
		<acme:password code="chorbi.passwdConf" path="passwdConfirmation"/>
		<br/>
		

	<acme:submit name="save" code="misc.save"/>		
	<acme:cancel url="/" code="misc.cancel"/>
		
</form:form>
<spring:message code="misc.eula" />