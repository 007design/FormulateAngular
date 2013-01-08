<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>

<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.0.4/css/bootstrap-combined.min.css">

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.1/angular.min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.1/angular-resource.min.js"></script>
<script type="text/javascript" src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.0.4/js/bootstrap.min.js"></script>
	
</head>
<body>

	<div id="pageArea" class="container" style="width:340px; margin-top:20px;">
		<div id="contentArea">
			<div id="messageArea" class="alert alert-info">
				<s:messages></s:messages>
			</div>
		
			<div id="formArea" class="well">
				<s:form beanclass="com.lcp.formulate.stripes.action.operations.LoginAction"
					method="post" class="form-horizontal">
					<s:hidden name="account" />
					<s:hidden name="view" />
					<s:hidden name="nextPage" />
					
					<div class="control-group">
						<label class="control-label" style="width:50px;" for="userInput">User:</label>
						<div class="controls" style="margin-left:70px;">
							<s:text name="username" id="userInput" />
						</div>					
					</div>
										
					<c:if test="${actionBean.requirePassword == true}">
						<div class="control-group">
							<label class="control-label" style="width:50px;" for="passwordInput">Password:</label>
							<div class="controls" style="margin-left:70px;">
								<s:password name="password" id="passwordInput" />
							</div>					
						</div>
					</c:if>
					
					<div id="spacer"></div>
					
					<s:submit name="login" value="Login" id="loginButton" class="btn btn-primary" />
				</s:form>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		document.getElementById('userInput').focus();
	</script>

</body>
</html>