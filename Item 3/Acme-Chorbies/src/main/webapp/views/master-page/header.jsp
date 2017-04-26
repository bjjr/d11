<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<div>
	<img src="images/logo.png" alt="Acme-Chorbies, Inc." />
</div>

<div>

	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><acme:link href="dashboard/administrator/dashboard.do" code="master.page.dashboard"/></li>
					<li><acme:link href="banner/list.do" code="master.page.banner.list"/></li>
					<li><acme:link href="cache/administrator/display.do" code="master.page.cache"/></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('CHORBI')">
			<li><a class="fNiv"><spring:message	code="master.page.chorbi" /></a>
				<ul>
					<li class="arrow"></li>
					<li><acme:link href="chorbi/edit.do" code="master.page.chorbi.edit" /></li>
					<li><a href="creditCard/chorbi/display.do"><spring:message code="master.page.chorbi.editCreditCard" /></a></li>
					<li><acme:link href="searchTemplate/chorbi/search.do" code="master.page.chorbi.search" /></li>
					<li><acme:link href="searchTemplate/chorbi/edit.do" code="master.page.chorbi.editSearchTemplate" /></li>
					<li><acme:link href="userAccount/edit.do" code="master.page.ua.edit" /></li>
					<li><acme:link href="chirp/chorbi/listSent.do" code="master.page.chorbi.chirp.listSent" /></li>
					<li><acme:link href="chirp/chorbi/listReceived.do" code="master.page.chorbi.chirp.listReceived" /></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('BANNED')">
			<li>
			<a class="fNiv"><spring:message code="master.page.banned" /></a>
				<ul>
					<li class="arrow"></li>
					<li><acme:link href="j_spring_security_logout" code="master.page.logout" /></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv" href="chorbi/register.do"><spring:message code="master.page.chorbi.register" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasAnyRole('CHORBI', 'ADMIN')">
			<li><a class="fNiv" href="chorbi/list.do"><spring:message code="master.page.chorbi.list" /></a></li>
			<li><a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>
					<li><acme:link href="j_spring_security_logout" code="master.page.logout" /></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

