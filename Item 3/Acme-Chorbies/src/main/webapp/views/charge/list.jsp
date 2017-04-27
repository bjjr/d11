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
	name="charges" requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->
	
	<acme:column code="charge.amount" property="${row.amount}"/>
	
	<display:column>
		<jstl:if test="${row.paid == false}">
			<a href="charge/user/pay.do?chargeId=${row.id}"><spring:message code="charge.pay" /></a>
		</jstl:if>
		<jstl:if test="${row.paid == true}">
			<spring:message code="charge.paid"/>
		</jstl:if>
	</display:column>
	
</display:table>