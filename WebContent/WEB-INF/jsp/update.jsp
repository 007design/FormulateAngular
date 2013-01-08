<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns:ng="http://angularjs.org/" ng-app="dashboardApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>${actionBean.view.name} Update</title>
	
	<!-- styles -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/view.update.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/views/${actionBean.account.alias}/${actionBean.view.alias}/styles.css">
	
	<%@include file="/WEB-INF/jsp/libs.jsp" %>
    
</head>
<body ng-controller="UpdateViewCtrl">

	<div class="container-fluid" id="inputFormArea">
		<ng-include src="'${pageContext.request.contextPath}/partials/view-update'"></ng-include>
	</div>
	
	<!-- app config -->
	<script type="text/javascript">
		var accountId = '${actionBean.context.user.account.id}';
		var userId = '${actionBean.context.user.id}';
		var viewId = '${actionBean.view.id}';
		var formId = '${actionBean.view.form.id}';
		var submissionId = '${actionBean.submission.id}';
		var contextPath = '${pageContext.request.contextPath}';
	</script>	
	
	<!-- scripts -->	
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/app.dashboard.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/view.update.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/views/${actionBean.account.alias}/${actionBean.view.alias}/scripts.js"></script>
</body>
</html>