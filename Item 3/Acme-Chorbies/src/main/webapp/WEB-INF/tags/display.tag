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

<%-- Attributes --%> 

<%@ attribute name="code" required="true" %>
<%@ attribute name="property" required="false" %>
<%@ attribute name="date" required="false" type="java.util.Date" %>
<%@ attribute name="isDate" required="false" %>
<%@ attribute name="isTimestamp" required="false" %>

<jstl:if test="${isDate == null}">
	<jstl:set var="isDate" value="false" />
</jstl:if>

<jstl:if test="${isTimestamp == null}">
	<jstl:set var="isTimestamp" value="false" />
</jstl:if>

<%-- Definition --%>

<div>
	<spring:message code="${code}" var="var" />
	<h4>
		<jstl:choose>
			<jstl:when test="${isDate}">
				<jstl:out value="${var}" />: <fmt:formatDate value="${date}" type="date" />
			</jstl:when>
			
			<jstl:when test="${isTimestamp}">
				<jstl:out value="${var}" />: <fmt:formatDate value="${date}" type="both" timeStyle="short" />
			</jstl:when>
			
			<jstl:otherwise>
				<jstl:out value="${var}" />: <jstl:out value="${property}" />
			</jstl:otherwise>
		</jstl:choose>
		
	</h4>
</div>