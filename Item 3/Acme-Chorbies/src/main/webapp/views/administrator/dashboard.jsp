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

<h2><spring:message code="administrator.listChorbiesCountry" /></h2>
<jstl:forEach var="entry" items="${numberOfChorbiesPerCountry}">
  <spring:message code="administrator.numberOfChorbiesPerCountry.country" /><jstl:out value="${entry[0]}"/>
  <spring:message code="administrator.numberOfChorbiesPerCountry.quantity" /><jstl:out value="${entry[1]}"/><br/>
</jstl:forEach>

<h2><spring:message code="administrator.listChorbiesCity" /></h2>
<jstl:forEach var="entry" items="${numberOfChorbiesPerCity}">
  <spring:message code="administrator.numberOfChorbiesPerCity.city" /><jstl:out value="${entry[0]}"/>
  <spring:message code="administrator.numberOfChorbiesPerCountry.quantity" /><jstl:out value="${entry[1]}"/><br/>
</jstl:forEach>

<h2><spring:message code="administrator.avgAC" /></h2>
<jstl:out value="${avgAC}"></jstl:out>

<h2><spring:message code="administrator.maxAC" /></h2>
<jstl:out value="${maxAC}"></jstl:out>

<h2><spring:message code="administrator.minAC" /></h2>
<jstl:out value="${minAC}"></jstl:out>

<h2><spring:message code="administrator.ratioChorbiesNoCCInvCC" /></h2>
<jstl:out value="${ratioChorbiesNoCCInvCC}"></jstl:out>

<h2><spring:message code="administrator.ratioChorbiesSearchAct" /></h2>
<jstl:out value="${ratioChorbiesSearchAct}"></jstl:out>

<h2><spring:message code="administrator.ratioChorbiesSearchFriend" /></h2>
<jstl:out value="${ratioChorbiesSearchFriend}"></jstl:out>

<h2><spring:message code="administrator.ratioChorbiesSearchLove" /></h2>
<jstl:out value="${ratioChorbiesSearchLove}"></jstl:out>

<h2><spring:message code="administrator.chorbiesOrderByCL" /></h2>

<display:table pagesize="5" class="displaytag" name="chorbiesOrderByCL"
requestURI="dashboard/administrator/dashboard.do" id="row">

	<acme:column code="actor.name" property="${row.name}"/>
	<acme:column code="actor.surname" property="${row.surname}"/>

</display:table>

<h2><spring:message code="administrator.avgLikesPChorbi" /></h2>
<jstl:out value="${avgLikesPChorbi}"></jstl:out>

<h2><spring:message code="administrator.maxLikesPChorbi" /></h2>
<jstl:out value="${maxLikesPChorbi}"></jstl:out>

<h2><spring:message code="administrator.minLikesPChorbi" /></h2>
<jstl:out value="${minLikesPChorbi}"></jstl:out>

<h2><spring:message code="administrator.avgChirpsRecChorbi" /></h2>
<jstl:out value="${avgChirpsRecChorbi}"></jstl:out>

<h2><spring:message code="administrator.maxChirpsRecChorbi" /></h2>
<jstl:out value="${maxChirpsRecChorbi}"></jstl:out>

<h2><spring:message code="administrator.minChirpsRecChorbi" /></h2>
<jstl:out value="${minChirpsRecChorbi}"></jstl:out>

<h2><spring:message code="administrator.avgChirpsSendChorbi" /></h2>
<jstl:out value="${avgChirpsSendChorbi}"></jstl:out>

<h2><spring:message code="administrator.maxChirpsSendChorbi" /></h2>
<jstl:out value="${maxChirpsSendChorbi}"></jstl:out>

<h2><spring:message code="administrator.minChirpsSendChorbi" /></h2>
<jstl:out value="${minChirpsSendChorbi}"></jstl:out>

<h2><spring:message code="administrator.avgStarsPerChorbi" /></h2>
<jstl:out value="${avgStarsPerChorbi}"></jstl:out>

<h2><spring:message code="administrator.maxStarsPerChorbi" /></h2>
<jstl:out value="${maxStarsPerChorbi}"></jstl:out>

<h2><spring:message code="administrator.minStarsPerChorbi" /></h2>
<jstl:out value="${minStarsPerChorbi}"></jstl:out>

<h2><spring:message code="administrator.chorbiesOrderByStars" /></h2>
<display:table pagesize="5" class="displaytag" name="chorbiesOrderByStars"
requestURI="dashboard/administrator/dashboard.do" id="row">

	<acme:column code="actor.name" property="${row.name}"/>
	<acme:column code="actor.surname" property="${row.surname}"/>

</display:table>

<h2><spring:message code="administrator.chorbiesMCR" /></h2>

<display:table pagesize="5" class="displaytag" name="chorbiesMCR"
requestURI="dashboard/administrator/dashboard.do" id="row">

	<acme:column code="actor.name" property="${row.name}"/>
	<acme:column code="actor.surname" property="${row.surname}"/>

</display:table>

<h2><spring:message code="administrator.chorbiesMCS" /></h2>

<display:table pagesize="5" class="displaytag" name="chorbiesMCS"
requestURI="dashboard/administrator/dashboard.do" id="row">

	<acme:column code="actor.name" property="${row.name}"/>
	<acme:column code="actor.surname" property="${row.surname}"/>

</display:table>

<h2><spring:message code="administrator.managersSortedByNumberEvents" /></h2>

<display:table pagesize="5" class="displaytag" name="managersSortedByNumberEvents"
requestURI="dashboard/administrator/dashboard.do" id="row">

	<acme:column code="actor.name" property="${row.name}"/>
	<acme:column code="actor.surname" property="${row.surname}"/>

</display:table>

<h2><spring:message code="administrator.managersWithDebts" /></h2>

<jstl:forEach var="entry" items="${managersWithDebts}">
  <spring:message code="actor.name" /><jstl:out value="${entry[0]}"/>
  <spring:message code="administrator.managersWithDebts.debt" /><jstl:out value="${entry[1]}"/><br/>
</jstl:forEach>

<h2><spring:message code="administrator.chorbiesSortedByNumberEvents" /></h2>

<display:table pagesize="5" class="displaytag" name="chorbiesSortedByNumberEvents"
requestURI="dashboard/administrator/dashboard.do" id="row">

	<acme:column code="actor.name" property="${row.name}"/>
	<acme:column code="actor.surname" property="${row.surname}"/>

</display:table>

<h2><spring:message code="administrator.chorbiesWithDebts" /></h2>

<jstl:forEach var="entry" items="${chorbiesWithDebts}">
  <spring:message code="actor.name" /><jstl:out value="${entry[0]}"/>
  <spring:message code="administrator.chorbiesWithDebts.debt" /><jstl:out value="${entry[1]}"/><br/>
</jstl:forEach>

<h2><spring:message code="administrator.avgStarsPerChorbi" /></h2>
<jstl:out value="${avgStarsPerChorbi}"></jstl:out>

<h2><spring:message code="administrator.maxStarsPerChorbi" /></h2>
<jstl:out value="${maxStarsPerChorbi}"></jstl:out>

<h2><spring:message code="administrator.minStarsPerChorbi" /></h2>
<jstl:out value="${minStarsPerChorbi}"></jstl:out>

<h2><spring:message code="administrator.chorbiesSortedByAvgStars" /></h2>

<display:table pagesize="5" class="displaytag" name="chorbiesSortedByAvgStars"
requestURI="dashboard/administrator/dashboard.do" id="row">

	<acme:column code="actor.name" property="${row.name}"/>
	<acme:column code="actor.surname" property="${row.surname}"/>

</display:table>


