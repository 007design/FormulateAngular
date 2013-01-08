<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>    
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!-- styles -->
<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.0.4/css/bootstrap-combined.min.css">

<!-- scripts -->
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.1/angular.min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.1/angular-resource.min.js"></script>
<script type="text/javascript" src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.0.4/js/bootstrap.min.js"></script>
	
<!-- angular-ui -->
<link href="${contextPath}/css/lib/angular-ui.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${contextPath}/js/lib/angular-ui.js"></script>
	
<!-- datepicker -->
<link href="${contextPath}/css/lib/bootstrap-datepicker.css" rel="stylesheet" type="text/css">	
<script type="text/javascript" src="${contextPath}/js/lib/bootstrap-datepicker.js"></script>	
	
<!-- fileuploader -->	
<link href="${contextPath}/css/lib/fileuploader.css" rel="stylesheet" type="text/css">	
<script type="text/javascript" src="${contextPath}/js/lib/fileuploader.js"></script>
	
<!-- modules -->
<script type="text/javascript" src="${contextPath}/js/app.services.js"></script>
<script type="text/javascript" src="${contextPath}/js/app.directives.js"></script>

<script type="text/javascript" src="${contextPath}/js/lib/bootstrap-popover.js"></script>	

<!--[if lte IE 8]>
   	<script>
       	document.createElement('ng-include');
       	document.createElement('ng-form');
       	document.createElement('formulate-textfield');
       	document.createElement('formulate-dropdown');
       	document.createElement('formulate-checkboxes');
       	document.createElement('formulate-radios');
       	document.createElement('formulate-textarea');
       	document.createElement('formulate-uploader');
   	</script>
<![endif]-->	