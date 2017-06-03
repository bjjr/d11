<%--
 * textarea.tag
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Attributes --%> 

<%@ attribute name="code" required="true" %>
<%@ attribute name="property" required="true" %>
<%@ attribute name="sortable" required="false" %>
<%@ attribute name="style" required="false" %>
<%@ attribute name="isDate" required="false" %>
<%@ attribute name="isTimestamp" required="false" %>

<jstl:if test="${sortable == null}">
	<jstl:set var="sortable" value="false" />
</jstl:if>

<jstl:if test="${isDate == null}">
	<jstl:set var="isDate" value="false" />
</jstl:if>

<jstl:if test="${isTimestamp == null}">
	<jstl:set var="isTimestamp" value="false" />
</jstl:if>

<%-- Definition --%>

<spring:message code="${code}" var="var" />

<jstl:choose>
	<jstl:when test="${isDate}">
		<display:column property="${property}" title="${var}" sortable="${sortable}" style="${style}" format="{0,date,dd/MM/yyyy}" />
	</jstl:when>
	
	<jstl:when test="${isTimestamp}">
		<display:column property="${property}" title="${var}" sortable="${sortable}" style="${style}" format="{0,date,dd/MM/yyyy HH:mm}" />
	</jstl:when>
	
	<jstl:otherwise>
		<display:column  title="${var}" sortable="${sortable}" style="${style}">
			<jstl:out value="${property}" />
		</display:column>
	</jstl:otherwise>
</jstl:choose>