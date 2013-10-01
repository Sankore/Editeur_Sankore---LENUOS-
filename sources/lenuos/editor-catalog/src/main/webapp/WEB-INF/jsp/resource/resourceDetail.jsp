<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:template="urn:jsptagdir:/WEB-INF/tags/template"
	xmlns:form="http://www.springframework.org/tags/form"
	version="2.0">

	<jsp:output omit-xml-declaration="yes" />
	<jsp:directive.page contentType="text/html;charset=UTF-8" />

	<template:page code="resourceDetail">
		<div class="pageDetail">
			<form:form method="${empty resource.id?'POST':'PUT'}" modelAttribute="resource" enctype="multipart/form-data" action="${pageContext.request.contextPath}/resources/">
				<table cellpadding="0" cellspacing="0" border="0">
					<tbody>
						<tr>
							<td class="labelLeft" />
							<td class="labelRight"><label><spring:message code="resource.title" /></label></td>
							<td class="fieldLeft read">
								<span class="error"><form:errors/></span>
							</td>
							<td class="fieldRight read" />
						</tr>
						<tr>
							<td class="labelLeft" />
							<td class="labelRight"><label><spring:message code="resource.id" /></label></td>
							<td class="fieldLeft read">
								<label>${resource.id}</label>
								<form:hidden path="id"/>
							</td>
							<td class="fieldRight read" />
						</tr>
						<tr>
							<td class="labelLeft" />
							<td class="labelRight"><label><spring:message code="resource.version" /></label></td>
							<td class="fieldLeft read">
								<label>${resource.version}</label>
								<form:hidden path="version"/>
							</td>
							<td class="fieldRight read" />
						</tr>
						<tr>
							<td class="labelLeft" />
							<td class="labelRight"><label><spring:message code="resource.file" /></label></td>
							<td class="fieldLeft">
								<div class="fileInput">
									<form:input path="fileData" type="file" onchange="document.getElementById('fileText').value=this.value;;return true;"/>
									<div class="fileSimulator">
										<spring:message code="button.browse" var="submitBrowse"/>
										<input id="fileText" class="fileText" type="text" value="" readonly="readonly" onfocus="document.getElementById('fileData').focus();" />
										<input type="submit" value="${submitBrowse}" class="fileButton button right" onclick="return false;" />
									</div>
								</div>
							</td>
							<td class="fieldRight" />
						</tr>
						<c:forEach items="${resource.filterKeys}" var="key">
							<tr>
								<td class="labelLeft" />
								<td class="labelRight"><label>${resource.filterValues[key].label}</label></td>
								<td class="fieldLeft">
       								<form:hidden path="filterValues['${key}'].key" />
       								<form:hidden path="filterValues['${key}'].label" />
       								<form:hidden path="filterValues['${key}'].type" />
									<c:if test="${resource.filterValues[key].boolean}">
										<form:radiobutton path="filterValues['${key}'].value" value="true"/>true
        								<form:radiobutton path="filterValues['${key}'].value" value="false"/>false
									</c:if>
									<c:if test="${resource.filterValues[key].integer}">
										<form:input path="filterValues['${key}'].value" />
									</c:if>
									<c:if test="${resource.filterValues[key].string}">
										<form:input path="filterValues['${key}'].value" />
									</c:if>
									<c:if test="${resource.filterValues[key].date}">
										<form:input path="filterValues['${key}'].value" />
									</c:if>
									<span class="error"><form:errors path="filterValues['${key}'].value" /></span>
								</td>
								<td class="fieldRight" />
							</tr>
						</c:forEach>
						<tr>
							<td class="labelLeft" />
							<td class="labelRight"><label><spring:message code="resource.thumbnail" /></label></td>
							<td class="fieldLeft read"><label class="url">${resource.thumbnailUrl}</label></td>
							<td class="fieldRight read" />
						</tr>
						<tr>
							<td class="labelLeft" />
							<td class="labelRight"><label><spring:message code="resource.url" /></label></td>
							<td class="fieldLeft read"><label class="url">${resource.fileUrl}</label></td>
							<td class="fieldRight read" />
						</tr>
						<c:forEach items="${resource.technicalDatas}" var="item">
							<tr>
								<td class="labelLeft" />
								<td class="labelRight"><label>${item.type}</label></td>
								<td class="fieldLeft read"><label>${item.value}</label></td>
								<td class="fieldRight read" />
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<template:buttonBar back="/resources/">
					<spring:message code="button.save" var="submitSave"/>
					<input type="submit" value="${submitSave}" class="button valider right" />
		 		</template:buttonBar>
	      	</form:form>
      	</div>
	</template:page>
</jsp:root>