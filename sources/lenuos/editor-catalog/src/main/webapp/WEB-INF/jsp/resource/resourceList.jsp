<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:fn="http://java.sun.com/jsp/jstl/functions" 
	xmlns:spring="http://www.springframework.org/tags" 
	xmlns:c="http://java.sun.com/jsp/jstl/core" 
	xmlns:page="urn:jsptagdir:/WEB-INF/tags/form" 
	xmlns:table="urn:jsptagdir:/WEB-INF/tags/form/fields" 
	xmlns:template="urn:jsptagdir:/WEB-INF/tags/template" 
	xmlns:form="http://www.springframework.org/tags/form"
	version="2.0">
	
    <jsp:output omit-xml-declaration="yes"/>
	<jsp:directive.page contentType="text/html;charset=UTF-8" />  
	
	<template:page code="resourceList">
		<div class="pageList">
			<c:if test="${not empty error}">
				<span class="error"><spring:message code="${error}" /></span>
			</c:if>
			<form:form method="GET" modelAttribute="search">
			<table>
				<tbody>
					<tr>
						<td>
							<form:input path="text"/>
						</td>
						<td>
							<spring:message code="button.search" var="btnSearch"/>
							<input type="Submit" value="" title="${btnSearch}" class="button search" />
						</td>
					</tr>
				</tbody>
			</table>
			</form:form>
			<table cellpadding="0" cellspacing="0" border="0">
				<thead>
					<tr>
						<th class="cellLeft cellId"><spring:message code="resource.id" /></th>
						<c:forEach items="${filters}" var="filter">
							<th class="cellMiddle cellFilter cellFilter${filter.type}">${filter.name}</th>
						</c:forEach>
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
					<c:forEach items="${resources}" var="resource">
						<tr>
							<td class="cellLeft cellId">${resource.id}</td>	
							<c:forEach items="${resource.values}" var="item">
								<c:if test="${empty item.value}">
									<td class="cellMiddle cellFilter cellFilter${item.type}"><c:out value="&#160;" /></td>
								</c:if>
								<c:if test="${not empty item.value}">
									<td class="cellMiddle cellFilter cellFilter${item.type}">${item.value}</td>
								</c:if>
							</c:forEach>
							<td class="cellMiddle cellEdit">
								<spring:message code="button.update" var="update" />
								<form:form method="GET" action="${resource.id}">
									<input type="Submit" value="" title="${update}" class="button edit" />
								</form:form>
							</td>
							<td class="cellRight cellDel">
								<spring:message code="button.confirmDelete" var="confirmDelete" />
								<spring:message code="button.delete" var="delete" />
								<form:form method="DELETE" action="${resource.id}">
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