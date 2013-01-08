var viewEditorApp = angular.module('viewEditorApp', ['module.services', 'ui', 'module.directives.popover'])

.config(function($routeProvider, $httpProvider, $locationProvider){
	/**
	 * Define routes
	 */
	$routeProvider.when('/fields', {templateUrl: contextPath+'/partials/editor-fields', controller: ViewFieldsListCtrl});
	$routeProvider.when('/notifications', {templateUrl: contextPath+'/partials/editor-notifications', controller: NotificationsListCtrl});
	$routeProvider.when('/events', {templateUrl: contextPath+'/partials/editor-events', controller: EventsListCtrl});
	$routeProvider.when('/filters', {templateUrl: contextPath+'/partials/editor-filters', controller: FiltersListCtrl});
	$routeProvider.when('/users', {templateUrl: contextPath+'/partials/editor-viewUsers', controller: ViewUsersListCtrl});
	$routeProvider.when('/form', {templateUrl: contextPath+'/partials/editor-form', controller: FormEditorCtrl});
	$routeProvider.otherwise({redirectTo: '/fields'});
	
//	$locationProvider.html5Mode(false);
//	$locationProvider.hashPrefix('!');
	
	/**
	 * Define http interceptor
	 */
	var httpInterceptor = ['$rootScope','$q', function(scope, $q) {		 
		function success(response) {
//			console.log('success');
//			console.log(response);
			return response;
		}

		function error(response) {
//			console.log('error');
//			console.log(response);
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
	
	$scope.$on('loginRequired', function(){
		$scope.$broadcast('openModal');
	});
	
	/**
	 * Handle login events
	 */
	$scope.$on('loginRequest', function(event, params) {
		$http({
			method: 'GET', 
			url: contextPath+'/login/ajaxLogin', 
			params: {'username': params.username, 'password': params.password, 'account': accountId}
		}).success(function(data, status) {	
			$scope.$broadcast('closeModal');
			
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
}])


/**
 * Sortable directive
 */
//.directive("ngSortable", function(){
//    return {
//        link: function(scope, element, attrs){                  
//        	element.sortable({
//        		delay:500,
//				forcePlaceholderSize: true,
//                axis: "y",
//                update: function(event, ui) {
//                	// get model
//                    var model = scope.$eval(attrs.ngSortable);
//                    
//                    // loop through items in new order
//                    element.children('.viewfield-row').each(function(index) {
//                    	var item = $(this);
//                        
//                        // get old item index
//                        var oldIndex = parseInt(item.attr("ng-sortable-index"));
//                        
//                        model[oldIndex].sequence = parseInt(index+1);
//                    });
//                    // notify angular of the change
//                    scope.$apply();
//                }
//            });
//        }
//    };
//})

//.directive('modal', ['$timeout', function($timeout) {
//	return {
//		restrict: 'C',
//		require: 'ngModel',
//		link: function(scope, elm, attrs, model) { 
//			scope.$watch(attrs.ngModel, function(value) {
//				elm.modal(value && 'show' || 'hide');
//			});
//			elm.on('show.ui', function() {
//				$timeout(function() {
//					model.$setViewValue(true);
//				});
//			});
//			elm.on('hide.ui', function() {
//				$timeout(function() {
//					model.$setViewValue(false);
//				});
//			});
//		}
//	};
//}])

.filter('hasView', function(){
	return function(user){console.log(user);
		if (user)
			return user.hasView;
	};
})

.filter('noView', function(){
	return function(user){
		if (user)
			return !user.hasView;
	};
})

.directive("tooltip", function(){
    return function(scope, element, attrs) {
            attrs.$observe('title', function(value) {
                // for updating tooltip when title changed
                element.removeData('tooltip');
                
                element.tooltip();
        });
    };
});