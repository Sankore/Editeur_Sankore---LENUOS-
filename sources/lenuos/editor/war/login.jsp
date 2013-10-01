<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Login</title>
</head>
<body>
	<form action="<c:url value="/authenticates"/>" method="post">
		<p>${requestScope['shiroLoginFailure']}</p>
		<p>${pageContext.request.contextPath}</p>
		<p>
			<label for="username">Username</label><input id="username" name="username" type="text"/>
		</p>
		<p>
			<label for="password">Password</label><input id="password" name="password" type="password"/>
		</p>
		<button type="submit">Submit</button>
	</form>
</body>
</html>