<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Server Exception</title>

	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/lib/bootstrap.css">
	
</head>
<body>

	<div id="pageArea" class="container" style="width:340px; margin-top:20px;">
		<div id="contentArea">
			<div id="errorArea" class="alert alert-error">
				<h4 class="alert-heading">Error</h4>
				Something has gone wrong, sorry.
				<br>
				<s:errors></s:errors>
				<s:messages></s:messages>
			</div>
		</div>
	</div>
	
</body>
</html>
