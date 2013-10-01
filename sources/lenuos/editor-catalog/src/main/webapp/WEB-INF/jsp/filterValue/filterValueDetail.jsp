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
	<table>
	<tr>
		<td>
			<label>Filter Value</label>
		</td>
	</tr>
	<tr>
		<td>
			<label>Id</label>
			<input type="text" name="id" value="${filterValue.id}"/>
		</td>
	</tr>
	<tr>
		<td>
			<label>Name</label>
			<input type="text" name="name" value="${filterValue.name}"/>
		</td>
	</tr>
	<tr>
		<td>
			<label>Type</label>
			<input type="text" name="type" value="${filterValue.type}"/>
		</td>
	</tr>
	<tr>
		<td>
			<label>Int value</label>
			<input type="text" name="intValue" value="${filterValue.intValue}"/>
		</td>
	</tr>
	<tr>
		<td>
			<label>String value</label>
			<input type="text" name="stringValue" value="${filterValue.stringValue}"/>
		</td>
	</tr>
	<tr>
		<td>
			<label>Boolean value</label>
			<input type="text" name="booleanValue" value="${filterValue.booleanValue}"/>
		</td>
	</tr>
	<tr>
		<td>
			<label>Date value</label>
			<input type="text" name="dateValue" value="${filterValue.dateValue}" onclick=""/>
			
		</td>
	</tr>
	</table>
</body>
</html>