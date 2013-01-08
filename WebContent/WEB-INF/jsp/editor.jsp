<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns:ng="http://angularjs.org/" ng-app="viewEditorApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>View Editors</title>
	
	<!-- styles -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/editor.view.css">
	<%@include file="/WEB-INF/jsp/libs.jsp" %>
			
</head>
<body ng-controller="ViewEditorCtrl">

	<form id="viewEditor_form" name="viewEditor_form">

		<div class="navbar navbar-fixed-top">
			<div class="navbar-inner">		
				<div class="container-fluid">
				
					<!-- Action Buttons -->
					<ul class="nav pull-right">
						<li class="dropdown">
							<a href="" class="dropdown-toggle" data-toggle="dropdown">Actions<b class="caret"></b></a>
							<ul class="dropdown-menu">
								<li><a href="#" ng-click="showForm()" ng-show="editorSvc.master.view.enableInput">Show Input</a></li>
								<li><a href="#" ng-click="showDashboard()" ng-show="editorSvc.master.view.enableDashboard">Show Dashboard</a></li>
								<li><a href="#" ng-click="showDashboard()" ng-show="editorSvc.master.view.enableUpdate">Show Update</a></li>
								<li class="divider"></li>
								<li><a href="#" ng-click="showManager()">Account Manager</a></li>
								<li><a href="#" ng-click="logout()">Logout</a></li>
							</ul>
						</li>
					</ul>
					
					<!-- Other Views Buttons -->
<!-- 					<ul class="nav pull-right" ng-show="otherViews.length"> -->
<!-- 						<li class="dropdown"> -->
<!-- 							<a href="" class="dropdown-toggle" data-toggle="dropdown">Views<b class="caret"></b></a> -->
<!-- 							<ul class="dropdown-menu"> -->
<!-- 								<li ng-repeat="v in otherViews"><a href="#" ng-click="showView(v)">{{v.name}}</a></li> -->
<!-- 							</ul> -->
<!-- 						</li> -->
<!-- 					</ul> -->
										
					<a class="brand">${actionBean.account.alias}/${actionBean.view.alias}</a>
					
					<!-- Form config links -->
					<ul class="nav pull-left">
						<li class="dropdown">
							<a href="" class="dropdown-toggle" data-toggle="dropdown">View Options<b class="caret"></b></a>
							<ul class="dropdown-menu" style="color:#000">
               					<li style="margin-left:10px">
               						<label class="checkbox inline">
               							<input type="checkbox" id="viewProperties_enableInputCheckbox" ng-model="editorSvc.view.enableInput"/> 
               							Enable Input
               						</label>
               					</li>
               					<li style="margin-left:10px">
               						<label class="checkbox inline">
               							<input type="checkbox" id="viewProperties_enableDashboardCheckbox" ng-model="editorSvc.view.enableDashboard"/> 
               							Enable Dashboard
               						</label>
               					</li>
               					<li style="margin-left:10px">
               						<label class="checkbox inline">
               							<input type="checkbox" id="viewProperties_enableUpdateCheckbox" ng-model="editorSvc.view.enableUpdate"/> 
               							Enable Update
               						</label>
               					</li>
               					<li class="divider"></li>
								<li style="margin-left:10px">
									<label class="checkbox inline">
										<input type="checkbox" id="viewProperties_requireUserCheckbox" ng-model="editorSvc.view.requireUser"/> 
               							Require User
               						</label>
               					</li>
               					<li style="margin-left:10px">
               						<label class="checkbox inline">
               							<input type="checkbox" id="viewProperties_requirePasswordCheckbox" ng-model="editorSvc.view.requirePassword"/> 
               							Require Password
               						</label>
               					</li>
								<li style="margin-left:10px">
									<label class="checkbox inline">
										<input type="checkbox" id="viewProperties_createUserCheckbox" ng-model="editorSvc.view.createUser"/> 
               							Enable Registration
               						</label>
               					</li>
							</ul>
						</li>
					</ul>
					
					<!-- Section links -->
					<ul class="nav pull-left">
						<li class="dropdown">
							<a href="" class="dropdown-toggle" data-toggle="dropdown">Configure<b class="caret"></b></a>
							<ul class="dropdown-menu">
								<li><a href="#/fields">Fields</a></li>
								<li ng-show="master.view.enableDashboard"><a href="#/filters">Filters <span style="color:#999;">[Dashboard]</span></a></li>
								<li class="divider"></li>
								<li><a href="#/notifications">Notifications</a></li>
								<li><a href="#/events">Events</a></li>
								<li><a href="#/users">Users</a></li>
								
								<c:if test="${actionBean.context.user.admin}">
									<li class="divider"></li>
									<li><a href="#/form">Edit Form</a></li>
								</c:if>
								
							</ul>
						</li>
					</ul>

					<div class="navbar-form form-inline pull-left">
						<div class='input-prepend'>
							<span class='add-on'>Title</span>
							<input type="text" class="span2 pull-right" tb-tooltip data-original-title="Toggle options for this view"
								ng-model="editorSvc.view.name" required />
						</div> 
						<input type="submit" 
					   		value="Save" 
					   		class="btn" 
					   		ng-click="save()"
					   		ng-disabled="isSaveDisabled()"/>
					   	<input type="button" 
						    value="Cancel" 
						    class="btn"  
						    ng-disabled="isCancelDisabled()"
						    ng-click="cancel()"/> 
					</div>	
                </span>
				</div>		
			</div>
		</div>		
		
		<!-- Editor Area -->
		<div id="editorArea" style="padding-top:45px" ng-view></div>

	</form>
	

	<bootstrap-popover id="pop-userId">
		<b>${actionBean.context.user.id}</b> 
	</bootstrap-popover>
	<span class="label user-label" pop-target="#pop-userId" pop-event="hover" pop-placement="right">Logged in as ${actionBean.context.user.name}</span>
    
	<div ui-modal id="loginModal" class="modal hide" ng-model="loginModalShown">
        <div class="modal-header">
            <h3>Your session has timed out, please log in again.</h3>
        </div>
		<div class="modal-body">
			<form class="form-horizontal">
				<div class="control-group">
					<label class="control-label" for="input01">User</label>
					<div class="controls">
						<input type="text" ng-model="username"
							value="${ actionBean.context.user.name }" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="input01">Password</label>
					<div class="controls">
						<input type="password" ng-model="password" />
					</div>
				</div>
				<input class="btn btn-primary" type="button" value="login" ng-click="$parent.doLogin(username,password)" />
			</form>
		</div>
	</div>
	
	<div id="thinkingArea" ng-show="thinking">
		<div id="thinkingOverlay"></div>
		<div id="thinkingThrobber"></div>
	</div>
	
	
	
	<!-- app config -->
	<script type="text/javascript">
		var accountId = '${actionBean.account.id}';
		var accountAlias = '${actionBean.account.alias}';
		var viewId = '${actionBean.view.id}';
		var userId = '${actionBean.context.user.id}';
		var contextPath = '${pageContext.request.contextPath}';
	</script>	
	
	<!-- scripts -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/app.editor.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/editor.view.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/editor.fields.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/editor.notifications.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/editor.events.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/editor.filters.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/editor.viewUsers.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/editor.form.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/editor.user.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/view.input.js"></script>
	
</body>
</html>