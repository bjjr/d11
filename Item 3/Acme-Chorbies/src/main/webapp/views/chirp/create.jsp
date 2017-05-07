<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="chirp/send.do" modelAttribute="chirp" >
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<jstl:if test="${isResend}">
		<h3><spring:message code="chirp.resendChirp"/></h3>
		<acme:textbox code="chirp.subject" path="subject" readonly="true"/>
		<acme:textarea code="chirp.text" path="text" readonly="true"/>
		<acme:textarea code="chirp.attachments" path="attachments" readonly="true"/>
		<acme:select items="${chorbies}" itemLabel="userAccount.username" code="chirp.recipient" path="recipient"/>
		
		<acme:submit name="resend" code="chirp.resend"/>
		<acme:cancel url="/chirp/listSent.do" code="misc.cancel"/>
	</jstl:if>
	
	<jstl:if test="${!isResend}">
		<jstl:if test="${!isReply}">
			<h3><spring:message code="chirp.sendTo"/> ${infoRecipient}</h3>
			<acme:textbox code="chirp.subject" path="subject"/>
		</jstl:if>
		<jstl:if test="${isReply}">
			<h3><spring:message code="chirp.replyTo"/> ${infoRecipient}</h3>
			<acme:textbox code="chirp.subject" path="subject" readonly="true"/>
		</jstl:if>
		
		<acme:textarea code="chirp.text" path="text"/>
		<acme:textarea code="chirp.attachments" path="attachments"/>
		
		<jstl:if test="${isReply}">
			<acme:submit name="reply" code="chirp.reply"/>
			<acme:cancel url="/chirp/listReceived.do" code="misc.cancel"/>
		</jstl:if>
		
		<jstl:if test="${!isReply}">
			<acme:submit name="send" code="chirp.send"/>
			<acme:cancel url="/chorbi/list.do" code="misc.cancel"/>
		</jstl:if>
	</jstl:if>
	
</form:form>