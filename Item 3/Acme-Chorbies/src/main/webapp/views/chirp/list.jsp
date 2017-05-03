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

<jstl:if test="${isSent}">
	<h2><spring:message code="chirp.listSent"/></h2>
</jstl:if>

<jstl:if test="${!isSent}">
	<h2><spring:message code="chirp.listReceived"/></h2>
</jstl:if>

<!-- Listing grid -->
<display:table pagesize="5" class="displaytag"
	name="chirps" requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->
	<acme:column code="chirp.moment" property="moment" isTimestamp="true"/>
	
	<acme:column code="chirp.subject" property="${row.subject}"/>
	
	<acme:column code="chirp.text" property="${row.text}"/>
	
	<spring:message code="chirp.attachments" var="att" />
	<display:column title="${att}">
		<jstl:forEach items="${row.attachments}" var="attachment">
			<a href="${attachment}"><jstl:out value="${attachment}" /></a>
			<br/>
		</jstl:forEach>
	</display:column>

	<jstl:if test="${isSent == true}">
		<acme:column code="chirp.recipient" property="${row.recipient.userAccount.username}"/>
		
		<display:column>
			<a href="chirp/resend.do?chirpId=${row.id}"><spring:message code="chirp.resend" /></a>
		</display:column>
	</jstl:if>
	
	<jstl:if test="${isSent == false}">
		<acme:column code="chirp.sender" property="${row.sender.userAccount.username}"/>
		
		<display:column>
			<a href="chirp/reply.do?chirpId=${row.id}"><spring:message code="chirp.reply" /></a>
		</display:column>
	</jstl:if>
	
	<display:column>
		<a href="chirp/delete.do?chirpId=${row.id}" onclick="return confirm('<spring:message code="misc.confirm.delete" />')"><spring:message code="misc.delete" /></a>
	</display:column>

</display:table>

<br />