function UserEditorCtrl($scope, EditorSvc, UserSvc) {
	$scope.editorSvc = EditorSvc;
	
	$scope.isSaveUserDisabled = function() {
		return $scope.userEditor_form.$invalid || !angular.equals($scope.userSvc.user.password, $scope.password2);
	};
	
	$scope.saveUser = function(u){
		
		if (!u.account)
			u.account = {id:accountId};
		
		UserSvc.save(u, function(){
			alert('user saved');
			$scope.editorSvc.users.push(u);
			$scope.$emit('refreshModel');
		});
	};
}