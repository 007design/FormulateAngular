<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns:ng="http://angularjs.org/" ng-app="managerApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>User Account Manager</title>
	
	<!-- styles -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/view.manager.css">
	
	<%@include file="/WEB-INF/jsp/libs.jsp" %>
		
</head>
<body>

	<div class="container" ng-controller="ManagerCtrl">		
		<div class="row">		
			
			<div class="span6">
				<c:choose>
					<c:when test="${actionBean.context.user.admin == true}">				
						<ng-include src="'${pageContext.request.contextPath}/partials/view-admin'"></ng-include>
					</c:when>
					<c:otherwise>
						<ng-include src="'${pageContext.request.contextPath}/partials/view-user'"></ng-include>
					</c:otherwise>
				</c:choose>
			</div>
			
			<div class="span6" ng-controller="UserEditorCtrl">
				<div class="row">
					<button class="btn pull-right" ng-click="logout()">Logout</button>
					<a class="btn btn-primary pull-right" href="${pageContext.request.contextPath}/docs.html">Documentation</a>
					
					<h2>User Details</h2>
					
					<div class="well">
						<ng-form class="form-horizontal" name="userEditor_form">
			
							<div class="control-group">
								<label class="control-label" for="usernameInput">Username</label>
								<div class="controls">
									<input type="text" class="input-large" id="usernameInput" ng-model="userSvc.user.name" required>
									<p class="help-block">This is your login name.</p>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label" for="emailInput">Email</label>
								<div class="controls">
									<input type="email" class="input-large" id="emailInput" ng-model="userSvc.user.email" required>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label" for="passwordInput1">Change Password</label>
								<div class="controls">
									<input type="password" class="input-large" id="passwordInput1" ng-model="userSvc.user.password" placeholder="Type new password to change">
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="passwordInput2">Retype New Password</label>
								<div class="controls">
									<input type="password" class="input-large" id="passwordInput2" ng-model="password2" placeholder="Retype new password to change">
								</div>
							</div>
						
							<div class="form-actions">
								<button type="submit" class="btn btn-primary" ng-click="saveUser(userSvc.user)" ng-disabled="userEditor_form.$invalid || userSvc.user.password != password2">Save changes</button>
							</div>
								
						</ng-form>
					</div>
				</div>

				<c:if test="${actionBean.context.user.admin == true}">				
					<ng-include src="'${pageContext.request.contextPath}/partials/editor-accountUsers'"></ng-include>
				</c:if>
				
			</div>
						
		</div>
	</div>
	
	<!-- app config -->
	<script type="text/javascript">
		var accountId = '${actionBean.account.id}';
		var accountAlias = '${actionBean.account.alias}';
		var userId = '${actionBean.context.user.id}';
		var contextPath = '${pageContext.request.contextPath}';
	</script>
	
	<!-- scripts -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/app.manager.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/view.manager.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/editor.user.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/editor.form.js"></script>
	
</body>
</html>