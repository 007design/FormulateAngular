var app = angular.module('app', ['module.services']);

function MainCtrl($scope){
	$scope.messageClass = function(){
		return hasErrors ? 'alert-error' : 'alert-info'; 
	};
	
	$scope.username = username;
}
