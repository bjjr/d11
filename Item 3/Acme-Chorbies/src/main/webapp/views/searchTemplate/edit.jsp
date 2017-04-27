<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="searchTemplate/chorbi/edit.do" modelAttribute="searchTemplate" >

	<spring:message code="chorbi.man" var="cMan" />
	<spring:message code="chorbi.woman" var="cWoman" />
	<spring:message code="chorbi.love" var="cLove" />
	<spring:message code="chorbi.activities" var="cActivities" />
	<spring:message code="chorbi.friendship" var="cFriendship" />
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<acme:textbox code="searchTemplate.age" path="age" />
	<div>
			<form:label path="gender">
				<spring:message code="chorbi.gender" />
			</form:label>
			<form:select path="gender">
				<form:option value="" label="----" />
				<form:option value="MAN" label="${cMan}" />
				<form:option value="WOMAN" label="${cWoman}" />
			</form:select>
			<form:errors path="gender" cssClass="error" />
		</div>
	<acme:textbox code="searchTemplate.keyword" path="keyword"/>
	<div>
		<form:label path="relationship">
			<spring:message code="chorbi.relationship" />
		</form:label>
		<form:select path="relationship">
			<form:option value="" label="----" />
			<form:option value="FRIENDSHIP" label="${cFriendship}" />
			<form:option value="LOVE" label="${cLove}" />
			<form:option value="ACTIVITIES" label="${cActivities}" />
		</form:select>
		<form:errors path="relationship" cssClass="error" />
	</div>
	
	<acme:textbox code="searchTemplate.coordinates.country" path="coordinatesTemplate.country"/>
	<acme:textbox code="searchTemplate.coordinates.state" path="coordinatesTemplate.state"/>
	<acme:textbox code="searchTemplate.coordinates.province" path="coordinatesTemplate.province"/>
	<acme:textbox code="searchTemplate.coordinates.city" path="coordinatesTemplate.city"/>
	
	<div>
		<acme:submit name="save" code="misc.save"/>
		<acme:cancel url="/" code="misc.cancel"/>
		<acme:cancel url="searchTemplate/chorbi/search.do" code="searchTemplate.search"/>
	</div>
</form:form>
