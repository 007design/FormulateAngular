function ViewUsersListCtrl($scope, $http, EditorSvc, AccountUsersSvc, ViewUsersSvc, UserSvc) {	
	$scope.editorSvc = EditorSvc;
	$scope.userSvc = UserSvc;
	
	$scope.master = [];
	$scope.viewUsers = [];
	
	/**
	 * Watch for view to be loaded and load the account users
	 */
	$scope.$watch('editorSvc.view', function(){
		if ($scope.editorSvc.view)
			AccountUsersSvc.query({id: accountId}, function(data){
				
				$scope.editorSvc.users = data;
				
				/**
				 * When the users list is loaded, get the view users
				 */
				ViewUsersSvc.query({id: $scope.editorSvc.view.id}, function(data){
					
					angular.forEach(data, function(v,k){
						for (var a=0; a<$scope.editorSvc.users.length; a++) {
							$scope.editorSvc.users[a].hasView = false;
							if ($scope.editorSvc.users[a].id == v.id) {
								$scope.editorSvc.users[a].hasView = true;
								break;
							}
						}
					});

					$scope.editorSvc.master.users = angular.copy($scope.editorSvc.users);
				});
			});
	});

	$scope.editUser = function(u) {
		$scope.userSvc.editUser = angular.copy(u);
	};
	
	$scope.isActive = function(b) {
		return b ? "active" : '';
	};
	
	$scope.hasView = function(user){
		return user.hasView;
	};
	
	$scope.noView = function(user){
		return !user.hasView;
	};
}