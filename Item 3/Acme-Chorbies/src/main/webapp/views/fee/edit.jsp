<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="fee/administrator/edit.do" modelAttribute="fee" >
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<jstl:if test="${feeType == 1}">
		<h3><spring:message code="fee.editFeeOf"/> <spring:message code="fee.feeType.managers" /></h3>
	</jstl:if>
	
	<jstl:if test="${feeType == 2}">
		<h3><spring:message code="fee.editFeeOf"/> <spring:message code="fee.feeType.chorbies" /></h3>
	</jstl:if>
	
	<acme:textbox code="fee.value" path="value"/>
	
	<acme:submit name="save" code="misc.save"/>
	
	<input type="button" name="cancel"
		value="<spring:message code="misc.cancel" />"
		onclick="window.location='fee/administrator/list.do'" />
		
</form:form>