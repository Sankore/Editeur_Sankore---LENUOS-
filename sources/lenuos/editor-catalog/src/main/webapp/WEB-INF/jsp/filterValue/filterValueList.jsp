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
	<table id="filterValueGrid">
		<thead>
			<tr>
				<th>Id</th>
				<th>Name</th>
				<th>Type</th>
				<th>Int Value</th>
				<th>String Value</th>
				<th>Boolean Value</th>
				<th>Date Value</th>			
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${filterValues}" var="filterValue">
				<tr>
					<td><a href="${filterValue.id}">${filterValue.id}</a></td>
					<td>${filterValue.name}</td>
					<td>${filterValue.type}</td>
					<td>${filterValue.intValue}</td>
					<td>${filterValue.stringValue}</td>
					<td>${filterValue.booleanValue}</td>
					<td>${filterValue.dateValue}</td>					
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>