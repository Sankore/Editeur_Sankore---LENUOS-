 <html 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:spring="http://www.springframework.org/tags" 
	xmlns:c="http://java.sun.com/jsp/jstl/core" 
	xmlns:util="urn:jsptagdir:/WEB-INF/tags/util" >  
	
	<jsp:directive.page contentType="text/html; charset=UTF-8"/>
	
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=8" />	
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css" />
			
		<title><spring:message code="app.title" /></title>
	</head>
	
  	<body>
  		<div class="unlarge">
  		<div id="global">
		<form method="POST" action="${pageContext.request.contextPath}/j_spring_security_check" >
			<div class="window">
				<div class="dial_conn">
					<div class="haut_dialConn"><!-- NULL --></div>
					<div class="mil_dialConn">
						<div>
							<div class="intitule_dialConn flotL">
								<div class="logo">
									<img src="${pageContext.request.contextPath}/images/icn_conect_dial.png" />
								</div>
								<div>
									<p class="titre"><spring:message code="login.title" /></p>
									<p class="sous_titre"><spring:message code="login.description" /></p>
								</div>
								<div class="clear"><!-- NULL --></div>
							</div>
							<div class="clear"><!-- NULL --></div>
						</div>
						<div class="mil_mil_dialConn">
							<div>
								<div>
									<p class="loginMessage"><spring:message code="login.message" /></p>
									<div class="loginfields form">
										<c:if test="${not empty error}">
											<div>
												<span class="error"><spring:message code="${error}" /></span>
											</div>
										</c:if>
										<div class="row">
											<div class="labelContainer">
												<div class="labelLeft flotL"><!-- NULL --></div>
												<div class="labelRight flotL">
													<label class="flotR"><spring:message code="login.ident" /></label>
												</div>
												<div class="clear"><!-- NULL --></div>
											</div>
											<div class="field flotL">
												<div class="fieldLeft flotL">
													<input type="text" name="j_username" />
												</div>
												<div class="fieldRight flotR"><!-- NULL --></div>
												<div class="clear"><!-- NULL --></div>
											</div>
											<div class="clear"><!-- NULL --></div>
										</div>
										
										<div class="row">
											<div class="labelContainer">
												<div class="labelLeft flotL"><!-- NULL --></div>
												<div class="labelRight flotL">
													<label class="flotR"><spring:message code="login.password" /></label>
												</div>
												<div class="clear"><!-- NULL --></div>
											</div>
											<div class="field flotL">
												<div class="fieldLeft flotL">
													<input type="password" name="j_password" />
												</div>
												<div class="fieldRight flotR"><!-- NULL --></div>
												<div class="clear"><!-- NULL --></div>
											</div>
											<div class="clear"><!-- NULL --></div>
										</div>
									</div>
								</div>
							</div>
							<spring:message code="button.send" var="buttonSend" />
							<button class="btn_dial flotR">
								<spring:message code="button.send" />
							</button>
							<div class="clear"><!-- NULL --></div>
						</div>
					</div>
					<div class="bas_dialConn"><!-- NULL --></div>
				</div>
			</div>
		</form>
		</div>
		</div>
	</body>
</html>