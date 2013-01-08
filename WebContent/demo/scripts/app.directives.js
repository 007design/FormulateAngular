angular.module('module.directives', [])

/**
 * Text field
 */
.directive('formulateTextfield', function(AliasSvc){
    return {
        restrict:'E',
        scope:{ field: '=' },
        template: '<input id="input-{{alias}}" type="text" ng-required="isRequired()" ng-model="field.value" ng-pattern="regex"/>',
        link: function(scope, element, attrs){
        	 
    		try {
    			scope.regex = scope.$eval("/"+scope.field.regEx+"/");
        	} catch (x) {
        		if (scope.field.required)
        			scope.regex = /.+/;
        
        		scope.regex = /.*/;
        	}
        	
        	scope.alias = AliasSvc.get(scope.field.label); 
            scope.isRequired = function(){
                return scope.field.required && scope.field.visibility;
            };
            scope.$watch('field.value', function(){
                scope.$emit('fireTriggers', scope.field);
            });
        }
    };
})

/**
 * Textarea
 * This is identical to the textfield, should combine them
 */
.directive('formulateTextarea', function(AliasSvc){
    return {
        restrict:'E',
        scope:{ field: '=' },
        template: '<textarea id="input-{{alias}}" ng-required="isRequired()" ng-model="field.value" ng-pattern="regex"></textarea>',        
        link: function(scope, element, attrs){      
        	
        	try {
    			scope.regex = scope.$eval("/"+scope.field.regEx+"/");
        	} catch (x) {
        		if (scope.field.required)
        			scope.regex = /.+/;
        
        		scope.regex = /.*/;
        	}
        	
        	scope.alias = AliasSvc.get(scope.field.label); 
            scope.isRequired = function(){
                return scope.field.required && scope.field.visibility;
            };
            scope.$watch('field.value', function(){
                scope.$emit('fireTriggers', scope.field);
            });
        }
    };
})
	
/**
 * Dropdown
 */
.directive('formulateDropdown', function(AliasSvc){
    return {
        restrict:'E',
        scope:{ field: '=' },
        template:    '<div>'+
                          '<select id="input-{{alias}}" ng-model="selection" '+ 
                              'ng-options="option as option.value for option in field.options" '+
                              'ng-required="isRequired()">'+
                              '<option value=""></option>'+
                          '</select>'+
                          '<input id="input-{{alias}}-custom" type="text" ng-model="field.value" ng-show="showCustom" ng-required="isRequired()"/>'+
                      '<div>',
        link: function(scope, element, attrs, ctrl) {  
        	scope.alias = AliasSvc.get(scope.field.label); 
        	
        	if (scope.field.custom)
        		scope.field.options.push({id:'other',value:'Other'});
        
            scope.isRequired = function(){
            	if (scope.field)
            		return scope.field.required && scope.field.visibility;
            };      
            angular.forEach(scope.field.options, function(option){
                if (option.defaultValue) {
                    scope.field.value = option.value;
                }
            });
            
            scope.$watch('selection', function(){
            	if (scope.selection){
                	console.log('selection changed');
                	console.log(scope.selection);
            		scope.field.value = scope.selection.value;
            		scope.showCustom = scope.selection.id == 'other';
            		if (scope.showCustom)
            			$('#'+scope.alias+'-custom').focus();
            	}
            });
            
            scope.$watch('field.value', function(){
            	console.log('field.value changed');
            	console.log(scope.selection);
            	console.log(scope.field);
            	
	            if (!scope.selection || !angular.equals(scope.selection.value, scope.field.value))
	            	angular.forEach(scope.field.options, function(o){
	            		if (o.value == scope.field.value) {
	            			scope.selection = o;
	            		}
	            	});
	            
//	            scope.$emit('fireTriggers', scope.field);
            });            
        }
    };
})
	
/**
 * Checkbox Group
 */
.directive('formulateCheckboxes', function(){
    return {
        restrict: 'E',
        require: 'ngModel',
        scope: { ngModel: '=' },
        template:	'<div ng-repeat="option in ngModel.options">'+
        		  		'<label class="inline checkbox">'+
        		  			'<input type="checkbox" id="inputElement-{{alias}}{{$index}}" '+
        		  					'ng-model="option.selected" '+
        		  					'value="{{option.value}}" ng-change="update(option)"/>'+
        		  		'{{label(option)}}</label>'+
        		  		'<input id="input-{{alias}}{{$index}}-custom" type="text" ng-model="otherValue" ng-show="showCustom(option)" ng-required="isRequired()"/>'+
        		  	'</div>',
        link: function(scope, element, attrs, ctrl) { 
            var checkCount = 0;
                        
            /**
             * Add a parser to set the validity of the checkboxes
             * They are valid if they are required, visible and something is selected
             */
            ctrl.$parsers.unshift(function(viewValue) {
            	if (scope.ngModel.required && scope.ngModel.visibility) {
            		ctrl.$setValidity('checkbox', !!checkCount);
            		return viewValue;
            	} else if (!scope.ngModel.visibility) {
            		ctrl.$setValidity('checkbox', true);
            	}
            });
                       
            scope.label = function(option){
            	return option.id=='other'?'Other':option.value;
            };
        	
            scope.showCustom = function(option){
            	return option.selected && option.id == 'other';
            };
        	
            scope.update = function(option) {       	
            	if (option) {
	                if (option.selected) {
	                    checkCount ++;
	                } else {
	                    checkCount --;
	                }
            	}
            	
            	if (scope.ngModel.required && scope.ngModel.visibility)
            		ctrl.$setValidity('checkbox', !!checkCount);
                
                scope.updateValue();
            };
            
            scope.updateValue = function(){
            	var val = [];
                angular.forEach(scope.ngModel.options, function(v,k){
                    if (v.selected) {
                    	if (angular.isNumber(v.id))
                    		val.push(v.value);
                    	else if (v.id.toLowerCase() == 'other')
                    		val.push(scope.otherValue);
                    }
                });
                scope.ngModel.value = angular.toJson(val);

                scope.$emit('fireTriggers', scope.ngModel.value);
            };

            scope.$watch('ngModel.visibility', function(){
            	if (!scope.ngModel.visibility) {
            		scope.ngModel.value="[]";            		
            		ctrl.$setValidity('checkbox', true);
            		angular.forEach(scope.ngModel.options, function(v,k){
                        v.selected = false;
                    });            		
            	} else {
            		if (scope.ngModel.required && scope.ngModel.visibility)
                		ctrl.$setValidity('checkbox', !!checkCount);
            	}
            });
            
            scope.$watch('otherValue', function(){
            	scope.updateValue();
            });
            
            scope.isRequired = function(){
            	if (!scope.ngModel.required) return false;          
                
                return true;
            };
            
            scope.$watch('ngModel.value', function(){
            	if (scope.ngModel.value) {
            		/**
            		 * Try parsing the value into an array
            		 */
            		try {
            			var checkVals = angular.fromJson(scope.ngModel.value);
            			
            			if (angular.isArray(checkVals)) {
            			
            				/**
            				 * Compare each value in the parsed array with the option values
            				 * If a match is found, mark that field as selected
            				 */
            				for (var b=0; b<checkVals.length; b++) {
            					
			            		for (var a=0; a<scope.ngModel.options.length; a++) {
			            			var o = scope.ngModel.options[a];
		
			            			if (o.value == checkVals[b]) {
			            				if (!o.selected) {
				            				o.selected = true; 
				            				scope.update(o);
				            			}
			            				break;
			            			}
				            		
				            		/**
				            		 * If we're on the last checkbox and no match was found for the value
				            		 * Select the 'other' option if the field allows it and populate the value
				            		 */
				            		if (a == scope.ngModel.options.length-1){
				            			if (scope.ngModel.options[a].id.toLowerCase() == 'other') {
				            				if (!o.selected) {
					            				o.selected = true;					            				
					            				scope.update(o);
					            			}
				            				scope.otherValue = checkVals[b];
				            			}
				            		}
			            		}
            				}
            			}
            		} catch (x) {
            			try { console.log(x); } catch (x) {}
            		}
        		}
            	
                scope.$emit('fireTriggers', scope.ngModel);
            });  
            
            if (scope.ngModel.required && scope.ngModel.visibility)
            	ctrl.$setValidity('checkbox', !!checkCount);
            if (scope.ngModel.custom)
        		scope.ngModel.options.push({id:'other',value:'Other'});
        	
            /**
             * Set default values
             */
            for (var o in scope.ngModel.options) {        	
            	
            	if (scope.ngModel.options[o].defaultValue) {
            		scope.ngModel.options[o].selected = true;
            		scope.update(scope.ngModel.options[o]);
            	}
            }
            
        }
    };
})
	
/**
 * Radio Button Group
 */

.directive('formulateRadios', function(AliasSvc){
    return {
        restrict:'E',
        require: 'ngModel',
        scope: { ngModel: '=' },
        template: '<div ng-repeat="option in ngModel.options">'+
        	'<label><input id="input-{{alias}}{{$index}}" '+ 
        		'name="input-{{alias}}" value="{{option.id}}" '+ 
        		'type="radio" ng-model="ngModel.selection" ng-click="toggle(option)"/>'+
        	'{{label(option)}}</label>'+
        	'<input id="input-{{alias}}{{$index}}-custom" type="text" ng-model="ngModel.otherValue" ng-show="showCustom(option)" ng-required="isOtherRequired()"/>'+
        	'</div>',
        link: function(scope, element, attrs, ctrl) {      
        	scope.alias = AliasSvc.get(scope.ngModel.label);   
        	
        	ctrl.$parsers.unshift(function(viewValue) {
            	if (scope.ngModel.required && scope.ngModel.visibility) {
            		ctrl.$setValidity('radio', scope.ngModel.value);
            		return viewValue;
            	} else if (!scope.ngModel.visibility) {
            		ctrl.$setValidity('radio', true);
            	}
            });

        	if (scope.ngModel.required && scope.ngModel.visibility)
        		ctrl.$setValidity('radio', scope.ngModel.value);
        	        	
        	if (scope.ngModel.custom)
        		scope.ngModel.options.push({id:'other',value:'Other'});
        	
        	scope.toggle = function(option){
        		scope.selection = option;
        		if (option.id == 'other')
        			scope.ngModel.value = scope.ngModel.otherValue;
        		else
        			scope.ngModel.value = scope.selection.value;
        		
        		scope.$viewValue = scope.ngModel.value;
        		
        		if (scope.ngModel.required && scope.ngModel.visibility) {
            		var valid = scope.ngModel.value && scope.ngModel.value!=null && scope.ngModel.value!='';
            		ctrl.$setValidity('radio', valid);
            	} else if (!scope.ngModel.visibility) {
            		ctrl.$setValidity('radio', true);
            	}
        	};
        	
            scope.showCustom = function(option){
            	if(scope.selection)
            		return angular.equals(scope.selection,option) && scope.selection.id == 'other';
            	return false;
            };
            
            scope.$watch('ngModel.otherValue', function(){
            	if (scope.ngModel.otherValue)
            		scope.ngModel.value = scope.ngModel.otherValue;
            	
            	if (scope.ngModel.required && scope.ngModel.visibility)
            		ctrl.$setValidity('radio', scope.ngModel.otherValue);
            });
            
            // Toggle default options on init
            setTimeout(function(){
	            angular.forEach(scope.ngModel.options, function(option){
	                if (option.defaultValue)
	                    scope.toggle(option);
	            });
            },0);
            
            scope.label = function(option){
            	return option.id=='other'?'Other':option.value;
            };
            
            scope.isOtherRequired = function(){
            	if (scope.selection)
            		return scope.isRequired && scope.selection.id == 'other';
            	return scope.isRequired && scope.ngModel.visibility;
            };
            
            scope.isRequired = function(){
                return scope.ngModel.required && scope.ngModel.visibility;
            };
            
            scope.$watch('ngModel.value', function(){
            	if (!scope.selection) {
                	if (scope.ngModel.value) {
                		
	            		for (var a=0; a<scope.ngModel.options.length; a++) {
	            			var o = scope.ngModel.options[a];

	            			if (o.value == scope.ngModel.value) {
	            				scope.ngModel.selection = o.id;
	            				scope.toggle(o);
	            				break;
	            			}
	            		}
            		}
            	}
            	
                scope.$emit('fireTriggers', scope.ngModel);
            }); 
        }
    };
})

/**
 * Upload Button
 */

.directive('formulateUploader', function(){
    return {
        restrict:'E',
    	require:'ngModel',
        scope:{ ngModel: '=' },
        transclude:true,
        link: function(scope, element, attrs, ctrl) {

        	ctrl.$parsers.unshift(function(viewValue) {
	        	if (scope.ngModel.required && scope.ngModel.visibility) {	        		
	        		ctrl.$setValidity('upload', scope.ngModel.value.length>0);
	        		return viewValue;
	        	} else if (!scope.ngModel.visibility) {
	        		ctrl.$setValidity('upload', true);
	        	}
	        });

    		ctrl.$setValidity('upload', !(scope.ngModel.required && scope.ngModel.visibility));

	    	setTimeout(function(){
	    		element.find('.qq-upload-button').addClass('btn');
	    	},0);
	    	
	        var fileList = {};
	        scope.uploader = new qq.FileUploader({
	        	element: element[0],
	            action: contextPath+'/upload',
	            debug: true,
	            onSubmit: function(id, fileName){},
	            onProgress: function(id, fileName, loaded, total){},
	            onCancel: function(id, fileName){},
	            onComplete: function(id, fileName, response){
	            	fileList[response.uid] = fileName;
	            	scope.ngModel.value = angular.toJson(fileList);
	                                  	
	            	if (scope.ngModel.required) {
		        		ctrl.$setValidity('upload', fileList != {});
	            	}
		            	
		        	scope.$apply();
	                  
		            scope.$emit('fireTriggers', scope.ngModel);
	            }
	        });
        }
    };
})
	
/**
 * Dashboard Value Display
 */
.directive('valueDisplay', function(){
	return {
		restrict: 'A',
		scope: { valueDisplay: '=', submission: '=' },
		link: function(scope, element, attrs, ctrl){
			var val = '';
			for (var a=0; a<scope.submission.values.length; a++) {
				var v = scope.submission.values[a];
				if (v)
					if (v.field.id == scope.valueDisplay.field.id){
						val = v.value;
						break;
					}
			}
			
			var json = '';								
			var html = '';

			/**
			 * Try parsing the value into an object
			 */
			try {
				json = angular.fromJson(val);
			} catch(x) {
				element.html(val);
				return;
			}

			/**
			 * Checkbox or Radio field
			 */
			if (angular.isArray(json))
				for (var a=0; a<json.length; a++) {
					html += '<div>'+json[a]+'</div>';
				}
			/**
			 * File upload field
			 */
			else if (angular.isObject(json))
				for (var v in json) {
					html += '<div><a href="/formulate/download/'+v+'">'+json[v]+'</a></div>';
				}
			
			else
				html = json;
						 
			element.html(html);
		}
	};
})

/**
 * Boostrap datepicker
 */

.directive('bootstrapDatepicker', function(){
	return {
		require: '?ngModel',
		restrict: 'C',
		link: function($scope, element, attrs, controller) {
			var updateModel;
			updateModel = function(ev) {
				element.datepicker({autoclose: true, format: 'mm/dd/yyyy'});
				element.blur();
				return $scope.$apply(function() {
					return controller.$setViewValue(Date.parse(ev.date));
				});
			};
			if (controller != null) {
				controller.$render = function() {
					try {
						element.datepicker().data().datepicker.date = controller.$viewValue;
						element.datepicker('setValue');
						element.datepicker('update');
					} catch (x) {}
					return controller.$viewValue;
				};
			}
			return attrs.$observe('bootstrapDatepicker', function(value) {
				var options;
				options = {};
				if (angular.isObject(value)) {
					options = value;
				}
				if (typeof(value) === "string" && value.length > 0) {
					try {
					options = angular.fromJson(value);
					} catch (x) {
						console.log(value);
					}
				}
				return element.datepicker({autoclose: true, format: 'mm/dd/yyyy'}).on('changeDate', updateModel);
			});
		}
	};
});