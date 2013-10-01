<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:fn="http://java.sun.com/jsp/jstl/functions" 
	xmlns:spring="http://www.springframework.org/tags" 
	xmlns:c="http://java.sun.com/jsp/jstl/core" 
	xmlns:template="urn:jsptagdir:/WEB-INF/tags/template" 
	xmlns:form="http://www.springframework.org/tags/form"
	version="2.0">
	
    <jsp:output omit-xml-declaration="yes"/>
	<jsp:directive.page contentType="text/html;charset=UTF-8" />  
	
	<template:page code="filterList">
		<div class="pageList">
			<c:if test="${not empty error}">
				<span class="error"><spring:message code="${error}" /></span>
			</c:if>
			<table cellpadding="0" cellspacing="0" border="0">
				<thead>
					<tr>
						<th class="cellLeft cellId"><spring:message code="filter.id" /></th>
						<th class="cellMiddle cellName"><spring:message code="filter.name" /></th>
						<th class="cellMiddle cellType"><spring:message code="filter.type" /></th>	
						<th class="cellMiddle cellFamily"><spring:message code="filter.family" /></th>
						<th class="cellMiddle cellEdit">
							<spring:message code="button.create" var="create"/>
							<form:form method="GET" action="create.html">
								<input type="Submit" value="" title="${create}" class="button create" />
							</form:form>
						</th>
						<th class="cellRight cellDel"><c:out value="&#160;" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${filters}" var="filtre">
						<tr>
							<td class="cellLeft cellId">${filtre.id}</td>						
							<td class="cellMiddle cellName">${filtre.name}</td>
							<td class="cellMiddle cellType">${filtre.type}</td>
							<td class="cellMiddle cellFamily">
								<c:if test="${empty filtre.family}">
									<c:out value="&#160;" />
								</c:if>
								<c:if test="${not empty filtre.family}">
									<c:out value="${filtre.family.name}" />
								</c:if>
							</td>
							<td class="cellMiddle cellEdit">
								<spring:message code="button.update" var="update" />
								<form:form method="GET" action="${filtre.id}">
									<input type="Submit" value="" title="${update}" class="button edit" />
								</form:form>
							</td>
							<td class="cellRight cellDel">
								<spring:message code="button.confirmDelete" var="confirmDelete" />
								<spring:message code="button.delete" var="delete" />
								<form:form method="DELETE" action="${filtre.id}">
									<input type="Submit" value="" title="${delete}" onclick="return confirm('${confirmDelete}');" class="button delete" />
								</form:form>
							</td>	
						</tr>
					</c:forEach>				
				</tbody>
			</table>
		</div>
	</template:page>
</jsp:root>