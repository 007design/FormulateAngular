function NotificationsListCtrl($scope, EditorSvc, NotificationsSvc) {
	$scope.editorSvc = EditorSvc;
	
	$scope.fieldVariables = [];
	
	$scope.comparisonTypes = COMPARISON_TYPES;
	
	/**
	 * Watch for view to be loaded and load the notifications
	 */
	$scope.$watch('editorSvc.view', function(){
		if ($scope.editorSvc.view)
			if (!$scope.editorSvc.view.notifications)
				NotificationsSvc.query({id: $scope.editorSvc.view.id}, function(data){
					console.log(data);
					$scope.editorSvc.view.notifications = data;
					
					$scope.editorSvc.master.view.notifications = angular.copy(data);
					
					$scope.updateVariables();
				});
	});
	
	/**
	 * Populate the notification variables by camelCase-ing the viewfield labels 
	 */
	$scope.updateVariables = function(){
		if ($scope.editorSvc.view.viewFields) {
			angular.forEach($scope.editorSvc.view.viewFields, function(v,i){
				var bits = v.label.toLowerCase().replace(/[^a-z|0-9| ]/g, "").replace(/ +/g," ").split(" ");
				for (var a=0; a<bits.length; a++) {
					if (a==0)
						$scope.fieldVariables[i] = bits[a];
					else
						$scope.fieldVariables[i] += bits[a].substr(0,1).toUpperCase() + bits[a].substr(1);
				}
			});
		}
	};
	
	/**
	 * Add a notification
	 */
	$scope.addNotification = function(){
		if (!$scope.editorSvc.view.notifications) $scope.editorSvc.view.notifications = [];	
		$scope.editorSvc.view.notifications.push({view: {id:$scope.editorSvc.view.id}, notificationConditions:[], recipients:[], format:1, initial:0});
	};
	
	/**
	 * Add a notification condition
	 */
	$scope.addNotificationCondition = function(notificationConditions) {
		notificationConditions.push({});
	};
	
	/**
	 * Add a recipient
	 */
	$scope.addRecipient = function(recipients, recipient) {
		recipients.push(angular.copy(recipient));
	};
}