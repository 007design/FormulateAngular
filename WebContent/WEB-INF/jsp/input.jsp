<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns:ng="http://angularjs.org/" ng-app="inputApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>${actionBean.view.name}</title>
	
	<!-- styles -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/view.input.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/views/${actionBean.account.alias}/${actionBean.view.alias}/styles.css">	
	
	<%@include file="/WEB-INF/jsp/libs.jsp" %>
	
</head>
<body ng-controller="InputViewCtrl">	
	<div class="container-fluid" id="inputFormArea">
		<ng-include src="'${pageContext.request.contextPath}/partials/view-input'"></ng-include>
	</div>    
	
	<span class="user-label">User: [${actionBean.context.user.id}] ${actionBean.context.user.name}</span>
	<span class="user-label">Access: ${actionBean.context.accessLevel}</span>
    
	<div id="loginModal" class="modal hide fade" ng-model="loginModalShown">
        <div class="modal-header">
            <h3>Your session has timed out, please log in again.</h3>
        </div>
		<div class="modal-body">			
			<form class="form-horizontal">
				<div class="control-group">
					<label class="control-label" for="input01">User</label>
					<div class="controls">
						<input type="text" ng-model="username" value="${ actionBean.context.user.name }" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="input01">Password</label>
					<div class="controls">
						<input type="password" ng-model="password" />
					</div>
				</div>
				<input class="btn btn-primary" type="button" value="login" ng-click="doLogin(username,password)" />
			</form>
		</div>
	</div>
	
	<!-- app config -->
	<script type="text/javascript">
			var accountId = '${actionBean.account.id}';
			var userId = '${actionBean.context.user.id}';
			var username = '${actionBean.context.user.name}';
			var viewId = '${actionBean.view.id}';
			var createUser = ${actionBean.view.createUser};
			var contextPath = '${pageContext.request.contextPath}';
	</script>
	
	<!-- scripts -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/app.input.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/view.input.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/views/${actionBean.account.alias}/${actionBean.view.alias}/scripts.js"></script>
</body>
</html>