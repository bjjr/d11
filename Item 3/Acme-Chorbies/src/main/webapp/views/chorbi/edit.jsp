<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="${action}" modelAttribute="${modelAttribute}" >
	
	<spring:message code="chorbi.man" var="cMan" />
	<spring:message code="chorbi.woman" var="cWoman" />
	<spring:message code="chorbi.love" var="cLove" />
	<spring:message code="chorbi.activities" var="cActivities" />
	<spring:message code="chorbi.friendship" var="cFriendship" />
	
	<jstl:if test="${isEdit}">
		<acme:textbox code="actor.email" path="email" />
		<br/>
		<acme:textbox code="actor.phone" path="phone" />
		<br/>
		<acme:textbox code="chorbi.picture" path="picture" />
		<br/>
		<acme:textarea code="chorbi.description" path="description"/>
		<br/>
	</jstl:if>
	
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
	<br/>
	
	<jstl:if test="${!isEdit}">
		<acme:textbox code="actor.name" path="name"/>
		<br/>
		<acme:textbox code="actor.surname" path="surname"/>
		<br/>
		<acme:textbox code="actor.email" path="email"/>
		<br/>
		<acme:textbox code="actor.phone" path="phone"/>
		<br/>
		<acme:textbox code="chorbi.picture" path="picture"/>
		<br/>
		<acme:textarea code="chorbi.description" path="description"/>
		<br/>
		<acme:datebox code="chorbi.birthdate" path="birthdate"/>
		<br/>
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
		<br/>
		<acme:textbox code="coordinates.country" path="coordinates.country"/>
		<br/>
		<acme:textbox code="coordinates.state" path="coordinates.state"/>
		<br/>
		<acme:textbox code="coordinates.province" path="coordinates.province"/>
		<br/>
		<acme:textbox code="coordinates.city" path="coordinates.city"/>
		<br />
		<acme:textbox code="chorbi.username" path="userAccount.username"/>
		<br/>
		<acme:password code="chorbi.password" path="userAccount.password"/>
		<br/>
		<acme:password code="chorbi.passwdConf" path="passwdConfirmation"/>
	</jstl:if>

	<acme:submit name="save" code="misc.save"/>		
	<acme:cancel url="/" code="misc.cancel"/>
		
</form:form>
<spring:message code="misc.eula" />