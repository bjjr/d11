<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="chorbiLike/chorbi/create.do" modelAttribute="chorbiLike">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="liker"/>
	<form:hidden path="liked"/>
	
	<acme:textarea code="chorbiLike.comment" path="comment" />
	<br />
	
	<acme:textbox code="chorbiLike.stars" path="stars" size="1"/>
	<br />
	
	<acme:textbox code="chorbiLike.moment" path="moment" readonly="true"/>
	<br />
		
	<!-- Action buttons -->
	
	<div>
		<acme:submit name="save" code="chorbiLike.save"/>
		<acme:cancel url="chorbi/list.do" code="chorbiLike.cancel"/>
	</div>
		
</form:form>