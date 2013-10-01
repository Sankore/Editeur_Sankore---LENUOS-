<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
	<table id="technicalDataGrid">
		<thead>
			<tr>
				<th>Id</th>								
				<th>Type</th>
				<th>Value</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${tachnicalDatas}" var="technicalData">
				<tr>
					<td><a href="${technicalData.id}">${technicalData.id}</a></td>
					<td>${technicalData.type}</td>
					<td>${technicalData.value}</td>					
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>