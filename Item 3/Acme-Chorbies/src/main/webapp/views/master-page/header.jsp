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
					<li><acme:link href="acmemanager/register.do" code="master.page.manager.register"/></li>
					<li><acme:link href="dashboard/administrator/dashboard.do" code="master.page.dashboard"/></li>
					<li><acme:link href="banner/list.do" code="master.page.banner.list"/></li>
					<li><acme:link href="cache/administrator/display.do" code="master.page.cache"/></li>
					<li><acme:link href="fee/administrator/list.do" code="master.page.fee.list"/></li>
					<li><acme:link href="charge/administrator/generateCharges.do" code="master.page.charge.generateCharges"/></li>
					<li><acme:link href="charge/administrator/economicStatistics.do" code="master.page.charge.economicStatistics"/></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('CHORBI')">
			<li><a class="fNiv"><spring:message	code="master.page.chorbi" /></a>
				<ul>
					<li class="arrow"></li>
					<li><acme:link href="chorbi/edit.do" code="master.page.chorbi.edit" /></li>
					<li><acme:link href="creditCard/display.do" code="master.page.chorbi.editCreditCard" /></li>
					<li><acme:link href="searchTemplate/chorbi/search.do" code="master.page.chorbi.search" /></li>
					<li><acme:link href="searchTemplate/chorbi/edit.do" code="master.page.chorbi.editSearchTemplate" /></li>
					<li><acme:link href="userAccount/edit.do" code="master.page.ua.edit" /></li>
					<li><acme:link href="chirp/listSent.do" code="master.page.chorbi.chirp.listSent" /></li>
					<li><acme:link href="chirp/listReceived.do" code="master.page.chorbi.chirp.listReceived" /></li>
					<li><acme:link href="charge/user/list.do" code="master.page.user.charge.list" /></li>					
					<li><acme:link href="event/chorbi/list.do" code="master.page.chorbi.event.list" /></li>
				</ul>	
			</li>
			<li><a class="fNiv" href="chorbi/listChorbiesLikedMe.do"><spring:message code="master.page.chorbi.list.likers" /></a></li>
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
		
		<security:authorize access="hasRole('MANAGER')">
			<li><a class="fNiv"><spring:message	code="master.page.manager" /></a>
				<ul>
					<li class="arrow"></li>
					<li><acme:link href="acmemanager/edit.do" code="master.page.chorbi.edit"/></li>
					<li><acme:link href="event/manager/list.do" code="master.page.manager.event.list"/></li>
					<li><acme:link href="event/manager/create.do" code="master.page.manager.event.create"/></li>
					<li><acme:link href="broadcast/manager/create.do" code="master.page.manager.broadast.create"/></li>
					<li><acme:link href="creditCard/display.do" code="master.page.chorbi.editCreditCard" /></li>
					<li><acme:link href="chirp/listSent.do" code="master.page.chorbi.chirp.listSent" /></li>
					<li><acme:link href="chirp/listReceived.do" code="master.page.chorbi.chirp.listReceived" /></li>
					<li><acme:link href="charge/user/list.do" code="master.page.user.charge.list" /></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv" href="chorbi/register.do"><spring:message code="master.page.chorbi.register" /></a></li>
			<li><a class="fNiv" href="event/list.do"><spring:message code="master.page.event.list.all" /></a></li>
			<li><a class="fNiv" href="event/listAvSts.do"><spring:message code="master.page.event.list.recent" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasAnyRole('CHORBI', 'ADMIN', 'MANAGER')">
			<li><a class="fNiv" href="chorbi/list.do"><spring:message code="master.page.chorbi.list" /></a></li>
			<li><a class="fNiv" href="event/list.do"><spring:message code="master.page.event.list.all" /></a></li>
			<li><a class="fNiv" href="event/listAvSts.do"><spring:message code="master.page.event.list.recent" /></a></li>
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

