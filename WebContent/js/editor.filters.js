function FiltersListCtrl($scope, EditorSvc, FiltersSvc) {
	$scope.editorSvc = EditorSvc;
	
	$scope.comparisonTypes = COMPARISON_TYPES;
	
	/**
	 * Load the filters
	 */
	$scope.$watch('editorSvc.view', function(){
		if ($scope.editorSvc.view)
			if (!$scope.editorSvc.view.filters)
				FiltersSvc.query({id: $scope.editorSvc.view.id}, function(data){			
					$scope.editorSvc.view.filters = data;
					
					console.log(angular.toJson(data));
					
					$scope.editorSvc.master.view.notifications = angular.copy(data);
				});
	});
	
	/**
	 * Add a filter
	 */
	$scope.addFilter = function(){
		if (!$scope.editorSvc.view.filters) $scope.editorSvc.view.filters = [];	
		$scope.editorSvc.view.filters.push({view:{id:viewId}});
	};
}