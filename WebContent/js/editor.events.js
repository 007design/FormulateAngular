function EventsListCtrl($scope, EditorSvc, EventsSvc) {
	$scope.editorSvc = EditorSvc;
	
	$scope.comparisonTypes = COMPARISON_TYPES;
	
	/**
	 * Watch for view to be loaded and load the events
	 */
	$scope.$watch('editorSvc.view', function(){
		if ($scope.editorSvc.view)
			if (!$scope.editorSvc.view.events)
				EventsSvc.query({id: $scope.editorSvc.view.id}, function(data){					
					$scope.editorSvc.view.events = data;
					$scope.editorSvc.master.view.notifications = angular.copy(data);
				});
	});
	
	/**
	 * Add an event
	 */
	$scope.addEvent = function(){
		if (!$scope.editorSvc.view.events) $scope.editorSvc.view.events = [];	
		$scope.editorSvc.view.events.push({view: {id: $scope.editorSvc.view.id}, eventConditions:[]});
	};
	
	/**
	 * Add an event condition
	 */
	$scope.addEventCondition = function(eventConditions) {
		eventConditions.push({});
	};
}