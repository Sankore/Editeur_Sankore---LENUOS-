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
	
	<template:page code="familyFilterDetail">
		<div class="pageDetail">
			<form:form method="${empty familyFilter.id?'POST':'PUT'}" modelAttribute="family" action="${pageContext.request.contextPath}/familyFilters/">
				<table cellpadding="0" cellspacing="0" border="0">
					<tbody>
						<tr>
							<td class="labelLeft" />
							<td class="labelRight"><label><spring:message code="familyFilter.title" /></label></td>
							<td class="fieldLeft read">
								<span class="error"><form:errors/></span>
							</td>
							<td class="fieldRight read" />
						</tr>
						<tr>
							<td class="labelLeft" />
							<td class="labelRight"><label><spring:message code="familyFilter.id" /></label></td>
							<td class="fieldLeft read">
								<label>${family.id}</label>
								<form:hidden path="id"/>
							</td>			
							<td class="fieldRight read" />
						</tr>
						<tr>
							<td class="labelLeft" />
							<td class="labelRight"><label><spring:message code="familyFilter.version" /></label></td>
							<td class="fieldLeft read">
								<label>${family.version}</label>
								<form:hidden path="version"/>
							</td>	
							<td class="fieldRight read" />
						</tr>
						<tr>
							<td class="labelLeft" />
							<td class="labelRight"><label><spring:message code="familyFilter.name" /></label></td>
							<td class="fieldLeft">	
								<p>
									<form:input path="name"/><span class="error"><form:errors path="name"/></span>
								</p>
									
							</td>		
							<td class="fieldRight" />
						</tr>										
					</tbody>				
				</table>

				<template:buttonBar back="/familyFilters/">
					<spring:message code="button.save" var="submitSave"/>
					<input type="submit" value="${submitSave}" class="button valider right" />
		 		</template:buttonBar>
			</form:form>
		</div>
	</template:page>
</jsp:root>