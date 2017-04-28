<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!-- Listing grid -->
<display:table pagesize="5" class="displaytag"
	name="events" requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->
	<acme:column code="event.title" property="${row.title}"/>
	
	<acme:column code="event.moment" property="${row.moment}"/>
	
	<acme:column code="event.description" property="${row.description}"/>
	
	<acme:column code="event.picture" property="${row.picture}"/>
	
	<acme:column code="event.seats" property="${row.seats}"/>
	
	<acme:column code="event.availableSeats" property="${row.availableSeats}"/>
	
	<acme:column code="actor.name" property="${row.manager.name}" />

	<security:authorize access="isAnonymous()">
	<jstl:if test="${all}">
	<jstl:if test="${row.moment < (current)}">
		
	</jstl:if>
	</jstl:if>
	</security:authorize>

</display:table>

<br />