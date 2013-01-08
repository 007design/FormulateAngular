function FormEditorCtrl($scope, EditorSvc, FormSvc) {
	$scope.editorSvc = EditorSvc;
		
	$scope.setDefault = function(option, options) {
		if (option.defaultValue)
			angular.forEach(options, function(v,i){
				if (!angular.equals(v, option))
					v.defaultValue = false;
			});
	};
	
	$scope.addField = function() {
		// Add a field to the master object
		$scope.editorSvc.form.fields.push({form:{id:$scope.editorSvc.form.id},fieldOptions:[]});
	};
	
	$scope.addOption = function(field) {
		
		// Add a field option
		field.fieldOptions.push({value:""});
	};
	
	$scope.$watch('editorSvc.form', function(){
		// When the editing form changes
		// Check if there's an existing master
		if ($scope.editorSvc.form && $scope.master) {
			// If there is, check if the ID matches
			if ($scope.master.id == $scope.editorSvc.form.id)
				return
		}

		// If there is no master or it doesn't match the editing selection
		$scope.master = angular.copy($scope.editorSvc.form);
	});
	
	$scope.isSaveDisabled = function(){
		return angular.equals($scope.master, $scope.editorSvc.form); 
	};
	
	$scope.save = function(){
		FormSvc.save($scope.editorSvc.form);
	};
	$scope.cancel = function(){
		$scope.editorSvc.form = undefined;
	};
}