<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
	<table>
		<tbody>
			<tr>
				<td>
				<label>Technical Data</label>
				</td>
			</tr>
			<tr>
				<td>
					<label>Id</label>
					<input type="text" name="id" value="${technicalData.id}"/>
				</td>
			</tr>
			<tr>
				<td>
					<label>Type</label>
					<input type="text" name="type" value="${technicalData.type}"/>	
				</td>
			</tr>
			<tr>
				<td>
					<label>Value</label>
					<input type="text" name="value" value="${technicalData.value}"/>	
				</td>		
			</tr>	
			</tbody>
	</table>
</body>
</html>