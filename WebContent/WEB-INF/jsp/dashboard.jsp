<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns:ng="http://angularjs.org/" ng-app="dashboardApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>${actionBean.view.name} Dashboard</title>

	<!-- styles -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/view.input.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/view.dashboard.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/views/${actionBean.account.alias}/${actionBean.view.alias}/styles.css">
				
	<%@include file="/WEB-INF/jsp/libs.jsp" %>
	
</head>
<body ng-controller="DashboardCtrl">	
	<ng-include src="'${pageContext.request.contextPath}/partials/view-dashboard'"></ng-include>
	
	<!-- app config -->
	<script type="text/javascript">
		var userId = '${actionBean.context.user.id}';
		var viewId = '${actionBean.view.id}';
		var accountId = '${actionBean.account.id}';
		var formId = '${actionBean.view.form.id}';
		var createUser = ${actionBean.view.createUser};
		var contextPath = '${pageContext.request.contextPath}';
	</script>	
	
	<!-- app -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/app.dashboard.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/view.dashboard.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/view.update.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/views/${actionBean.account.alias}/${actionBean.view.alias}/scripts.js"></script>
</body>
</html>