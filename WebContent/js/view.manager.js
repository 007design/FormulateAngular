function ManagerCtrl($scope, $http, UserSvc, AccountUsersSvc, FormsSvc, UserViewsSvc, EditorSvc, FormSvc, ViewSvc, AliasSvc) {
	$scope.userSvc = UserSvc;
	$scope.editorSvc = EditorSvc;
	
	$scope.contextPath = contextPath;
	$scope.accountAlias = accountAlias;
	
	$scope.forms = [];
	$scope.views = [];
	$scope.submissions = [];
	
	$scope.newView = {};
	$scope.newForm = {};
	
	$scope.$watch('newView.name', function(){
		if ($scope.newView.name) { 
			var bits = $scope.newView.name.replace(/[^A-z| ]/g, "").toLowerCase().split(" ");
			for (var a=0; a<bits.length; a++) {
				if (a==0)
					$scope.newView.alias = bits[a];
				else
					$scope.newView.alias += bits[a].substr(0,1).toUpperCase() + bits[a].substr(1);
			}
		}
	});
		
	/**
	 * Load the user
	 */
	UserSvc.get({id: userId}, function(){		
		/**
		 * When the user is loaded, if it is admin, try loading the forms and users
		 */
		if ($scope.userSvc.user.admin) {
			$scope.newUser = {account: {id: accountId}};
			$scope.forms = FormsSvc.query({id: accountId});
			$scope.accountUsers = AccountUsersSvc.query({id: accountId});
		}
		
		/**
		 * If they're not an admin, get their userviews
		 */
		else {
			$scope.userViews = UserViewsSvc.query({id:$scope.userSvc.user.id}, function(data){
				console.log(data);
			});
		}
		
	});
	
	$scope.editForm = function(f) {
		FormSvc.get({id: f.id}, function(data){
			$scope.editorSvc.form = data;
		});
	};
	
	$scope.editUser = function(u) {
		$scope.userSvc.editUser = angular.copy(u);
	};
	
	$scope.logout = function(){
		window.open(contextPath+'/do/logout', "_self");
	};
	
	$scope.createView = function(f, viewName){
		var viewAlias = AliasSvc.get(viewName);
		
		ViewSvc.save({form:{id:f.id}, account:{id:accountId}, name:viewName, alias:viewAlias}, function(data){
			window.open(contextPath+'/edit/'+accountAlias+'/'+viewAlias, "_self");
		});
	};
	
	$scope.createForm = function(newFormName){
		var formAlias = AliasSvc.get(newFormName);
		
		FormSvc.save({name:newFormName, alias:formAlias}, function(data){
			$scope.forms.push(data);
			$scope.editForm(data);
		});
	};
	
	/**
	 * Get the users submissions
	 */
//	$scope.submissions = SubmissionsSvc.query({id: userId});
	
	
	$scope.getFormViews = function(f){
		var views = [];
		angular.forEach($scope.views, function(v){
			if (v.view.form.id == f.id)
				views.push(v);
		});
		return views;
	};
	
	$scope.logout = function(){
		window.open(contextPath+'/do/logout', "_self");
	};
	
//	$scope.createForm = function(){
//		$http.post(contextPath+'/create/form', $scope.newForm).success(function(data){
//			if (data.success)
//				window.open(contextPath+'/edit/form/'+data.form, '_self');
//			else
//				showDialog(data);
//		}).error(function(data, status, headers, config){
//			alert(data);
//		});
//	};
	
//	$scope.createView = function(f){
//		$scope.newView.form = f;
//		
//		$http.post(contextPath+'/create/view', $scope.newView).success(function(data){
//			if (data.success)
//				window.open(contextPath+'/edit/view/'+data.view, '_self');
//			else
//				showDialog(data);
//		}).error(function(data, status, headers, config){
//			alert(data);
//		});
//	};
	
//	$scope.createAccount = function(){		
//		$http.get(contextPath+'/create/account?accountName='+$scope.newAccount.name).success(function(data){
//			if (data.success)
//				showDialog("Account created");
//			else
//				showDialog(data);
//		}).error(function(data, status, headers, config){
//			alert(data);
//		});
//	};
	
//	$scope.createUser = function(){
//		$http.post(contextPath+'/create/user', $scope.newUser).success(function(data){
//			if (data.success) {
//				showDialog("User Created");
//				$scope.users = UsersSvc.query({id: $scope.user.account.id});
//			} else {
//				showDialog(data);
//			}
//		}).error(function(data, status, headers, config){
//			alert(data);
//		});
//	};
	
	$scope.saveUser = function(u){
		$http.post(contextPath+'/save/user', u).success(function(data){
			if (data.success)
				showDialog("User Saved");
			else
				showDialog(data);
		}).error(function(data, status, headers, config){
			alert(data);
		});
	};
}