<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns:ng="http://angularjs.org/" ng-app="app">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>${actionBean.view.name}</title>
	
	<!-- styles -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/view.input.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/views/${actionBean.account.alias}/${actionBean.view.alias}/styles.css">
	
	<!-- app config -->
	<script type="text/javascript">
			var accountId = '${actionBean.account.id}';
			var username = '${actionBean.username}';
			var viewId = '${actionBean.view.id}';
			var contextPath = '${pageContext.request.contextPath}';
			var hasErrors = ${actionBean.context.validationErrors.size()>0};
			var hasMessages = ${actionBean.context.messages.size()>0};
	</script>
	
	<%@include file="/WEB-INF/jsp/libs.jsp" %>
	
	<!-- scripts -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/view.register.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/views/${actionBean.account.alias}/${actionBean.view.alias}/scripts.js"></script>
	
</head>
<body ng-controller="MainCtrl">

	<div id="pageArea" class="container" style="width:340px; margin-top:20px;">
		<div id="contentArea">
			<div id="errorArea" class="alert" ng-class="messageClass()" ng-show>
				<s:errors></s:errors>
				<s:messages></s:messages>
			</div>
		
			<div id="formArea" class="well">
				<s:form name="theForm" id="theForm" beanclass="com.lcp.formulate.stripes.action.operations.RegisterAction" method="post" class="form-horizontal">
					<s:hidden name="account" />
					<s:hidden name="view" />
					<s:hidden name="nextPage" />

					<div class="control-group">
						<label class="control-label" style="width:50px;" for="userInput">Username:</label>
						<div class="controls" style="margin-left:70px;">
							<input type="text" name="username" id="userInput" ng-model="username" required />
						</div>					
					</div>
					
					<div class="control-group">
						<label class="control-label" style="width:50px;" for="userInput">Email:</label>
						<div class="controls" style="margin-left:70px;">
							<input type="email" name="email" id="emailInput" ng-model="email" required />
						</div>					
					</div>
					
					<div class="control-group">
						<label class="control-label" style="width:50px;" for="passwordInput">Password:</label>
						<div class="controls" style="margin-left:70px;">
							<input type="password" name="password" id="passwordInput" ng-model="password" required />
						</div>					
					</div>					
					
					<div id="spacer"></div>
					
					<input type="submit" name="register" value="Register" id="loginButton" class="btn btn-primary" ng-disabled="theForm.$invalid"/>
				</s:form>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		document.getElementById('userInput').focus();
	</script>

</body>
</html>