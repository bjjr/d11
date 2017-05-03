<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="${action}" modelAttribute="event" >
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<acme:textbox code="event.title" path="title" />
	<acme:datebox code="event.moment" path="moment" />
	<acme:textarea code="event.description" path="description" />
	<acme:textbox code="event.picture" path="picture" />
	<acme:textbox code="event.seats" path="seats" />
	
	<acme:submit name="save" code="misc.save"/>
	<acme:delete confirmationCode="misc.confirm.delete" buttonCode="misc.delete" id="${event.id}"/>
	<acme:cancel url="event/manager/list.do" code="misc.cancel"/>
		
</form:form>