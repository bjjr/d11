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
	name="fees" requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->
	
	<acme:column code="fee.feeType" property="${row.feeType}"/>
	
	<display:column>
		<jstl:if test="${row.feeType == 1}">
			<spring:message code="fee.feeType.managers"/>
		</jstl:if>
		<jstl:if test="${row.feeType == 2}">
			<spring:message code="fee.feeType.chorbies"/>
		</jstl:if>
	</display:column>
	
	<acme:column code="fee.value" property="${row.value}"/>
		
	<display:column>
		<a href="fee/administrator/edit.do?feeId=${row.id}"><spring:message code="misc.edit" /></a>
	</display:column>
	
</display:table>

<br />