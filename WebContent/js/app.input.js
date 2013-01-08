/**
 * Init method for user overriding
 */
function init(){}

var inputApp = angular.module('inputApp', ['module.services', 'module.directives'])
.config(function($routeProvider, $httpProvider){
	/**
	 * Define http interceptor
	 */
	var httpInterceptor = ['$rootScope','$q', function(scope, $q) {		 
		function success(response) {
			return response;
		}

		function error(response) {
			var status = response.status;

			// Not logged in
			if (status == 412) {
				var deferred = $q.defer();
				var req = {
						config: response.config,
						deferred: deferred
				};
				scope.requests.push(req);
				scope.$broadcast('loginRequired');
				return deferred.promise;
			}
			// otherwise
			return $q.reject(response);	 
		}

		return function(promise) {
			return promise.then(success, error);
		};	 
	}];

	$httpProvider.responseInterceptors.push(httpInterceptor);
})
.run(['$rootScope', '$http', function($scope, $http) {

	$scope.requests = [];
	
//	$scope.$on('loginRequired', function(){
//		bootstrapModal.show('loginModal');
//	});
	
	/**
	 * Handle login events
	 */
	$scope.$on('loginRequest', function(event, params) {
		$http({
			method: 'GET', 
			url: contextPath+'/login/ajaxLogin', 
			params: {'username': params.username, 'password': params.password, 'account': accountId}
		}).success(function(data, status) {	
			bootstrapModal.hide('loginModal');
			if (data.result != 'success')
				alert(data.message);
			else
				$scope.$broadcast("retryLogin");
		}).error(function(data, status) {
			alert(data.message);
		});
	});
	
	/**
	 * On 'retryLogin' event, resend the failed requests
	 */
	$scope.$on('retryLogin', function() {
		var i, requests = $scope.requests;
		for (i = 0; i < requests.length; i++) {
			retry(requests[i]);
		}
		$scope.requests = [];

		function retry(req) {
			$http(req.config).then(function(response) {
				req.deferred.resolve(response);
			});
		}
	});
}]);	