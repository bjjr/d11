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

<display:table pagesize="5" class="displaytag" name="events"
	requestURI="${requestURI}" id="row">

	<jstl:set value="${row.moment.time - current.time}" var="diffms" />
	
	<jstl:set value="${(diffms <= 2592000000) and (diffms >= 0)}" var="isNear" />
	<jstl:set value="${diffms < 0}" var="isPast" />

	<spring:message code="event.picture" var="pic" />
	<display:column  title="${pic}">
		<img src="${row.picture}" style="max-width: 150px; max-height: 150px;" />
	</display:column>

	<jstl:choose>
		<jstl:when test="${!isNear and !isPast}">
			<!-- Attributes -->
			<acme:column code="event.title" property="${row.title}" />
			<acme:column code="event.moment" property="moment" isTimestamp="true" />
			<acme:column code="event.description" property="${row.description}" />
			<acme:column code="event.seats" property="${row.seats}" />
			<acme:column code="event.availableSeats" property="${row.availableSeats}" sortable="true" />
			<acme:column code="manager.company" property="${row.manager.company}" />
		</jstl:when>

		<jstl:when test="${isNear}">
			<!-- Attributes -->
			<acme:column code="event.title" property="${row.title}" style="background-color:yellow;" />
			<acme:column code="event.moment" property="moment" style="background-color:yellow;" isTimestamp="true" />
			<acme:column code="event.description" property="${row.description}" style="background-color:yellow;" />
			<acme:column code="event.seats" property="${row.seats}" style="background-color:yellow;" />
			<acme:column code="event.availableSeats" property="${row.availableSeats}" style="background-color:yellow;" sortable="true" />
			<acme:column code="manager.company" property="${row.manager.company}" style="background-color:yellow;" />
		</jstl:when>

		<jstl:when test="${isPast}">
			<!-- Attributes -->
			<acme:column code="event.title" property="${row.title}" style="background-color:grey;" />
			<acme:column code="event.moment" property="moment" style="background-color:grey;" isTimestamp="true" />
			<acme:column code="event.description" property="${row.description}" style="background-color:grey;" />
			<acme:column code="event.seats" property="${row.seats}" style="background-color:grey;" />
			<acme:column code="event.availableSeats" property="${row.availableSeats}" style="background-color:grey;" sortable="true" />
			<acme:column code="manager.company" property="${row.manager.name}" style="background-color:grey;" />
		</jstl:when>
	</jstl:choose>

	<security:authorize access="hasRole('CHORBI')">
		<jstl:if test="${chorbiEvents != null}">
			<jstl:if test="${row.moment > current && !chorbiEvents.contains(row) && row.availableSeats > 0}">
				<display:column>
					<acme:link href="event/chorbi/register.do?eventId=${row.id}" code="event.register"/>
				</display:column>
			</jstl:if>
			
			<jstl:if test="${row.moment > current && chorbiEvents.contains(row)}">
				<display:column>
					 <acme:link href="event/chorbi/unregister.do?eventId=${row.id}" code="event.unregister"/>
				</display:column>
			</jstl:if>
		</jstl:if>
	</security:authorize>
	
	<jstl:if test="${isManagerView and row.moment > current}">
		<display:column>
			<acme:link href="event/manager/edit.do?eventId=${row.id}" code="event.edit"/>
		</display:column>
	</jstl:if>

</display:table>

<br />

<security:authorize access="hasRole('MANAGER')">
	<acme:link href="event/manager/create.do" code="master.page.manager.event.create"/>
</security:authorize>
