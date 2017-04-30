<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Listing grid -->

<fmt:formatDate value="${current}" pattern="yyyy" var="currentYear" />
<fmt:formatDate value="${current}" pattern="MM" var="currentMonth" />
<fmt:formatDate value="${current}" pattern="dd" var="currentDay" />

<display:table pagesize="5" class="displaytag" name="events"
	requestURI="${requestURI}" id="row">

	<fmt:formatDate value="${row.moment}" pattern="yyyy" var="momentYear" />
	<fmt:formatDate value="${row.moment}" pattern="MM" var="momentMonth" />
	<fmt:formatDate value="${row.moment}" pattern="dd" var="momentDay" />

	<jstl:choose>

		<jstl:when
			test="${currentYear lt momentYear or (currentYear eq momentYear and currentMonth lt momentMonth) or (currentYear eq momentYear and currentMonth eq momentMonth and currentDay le momentDay)}">

			<!-- Attributes -->
			<acme:column code="event.title" property="${row.title}" />

			<acme:column code="event.moment" property="${row.moment}" />

			<acme:column code="event.description" property="${row.description}" />

			<acme:column code="event.picture" property="${row.picture}" />

			<acme:column code="event.seats" property="${row.seats}" />

			<acme:column code="event.availableSeats"
				property="${row.availableSeats}" />

			<acme:column code="actor.name" property="${row.manager.name}" />

		</jstl:when>
		<jstl:when
			test="${currentYear gt momentYear or (currentYear eq momentYear and currentMonth gt momentMonth) or (currentYear eq momentYear and currentMonth eq momentMonth and currentDay gt momentDay)}">

			<!-- Attributes -->
			<acme:column code="event.title" property="${row.title}"
				style="background-color:grey;" />

			<acme:column code="event.moment" property="${row.moment}"
				style="background-color:grey;" />

			<acme:column code="event.description" property="${row.description}"
				style="background-color:grey;" />

			<acme:column code="event.picture" property="${row.picture}"
				style="background-color:grey;" />

			<acme:column code="event.seats" property="${row.seats}"
				style="background-color:grey;" />

			<acme:column code="event.availableSeats"
				property="${row.availableSeats}" style="background-color:grey;" />

			<acme:column code="actor.name" property="${row.manager.name}"
				style="background-color:grey;" />

		</jstl:when>

	</jstl:choose>


	<security:authorize access="isAnonymous()">
		<jstl:if test="${all}">
			<jstl:if test="${row.moment < (current)}">
				<display:column></display:column>

			</jstl:if>
		</jstl:if>
	</security:authorize>

</display:table>

<br />