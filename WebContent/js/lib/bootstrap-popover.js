angular.module('module.directives.popover', [])
//.directive('bootstrapPopover', [
//    function() {
//    	var $, defaults, getBounds, linkFn;
//    	$ = jQuery;
//    	defaults = {
//    			placement: 'right',
//    			margin: 0
//    	};
//    	getBounds = function($el) {
//    		return $.extend($el.offset(), {
//    			width: $el[0].offsetWidth || $el.width(),
//    			height: $el[0].offsetHeight || $el.height()
//    		});
//    	};
//    	linkFn = function(scope, elm, attrs) {
//    		var $this, currentSource, directiveOptions, hidePopover, showPopover, togglePopover;
//    		$this = $(elm).hide().addClass('popover');
//    		directiveOptions = {
//    				placement: attrs.placement
//    		};
//    		currentSource = null;
//    		showPopover = function(options) {
//    			var $source, decidePosition, margin, placement, popBounds, sourceBounds;
//    			$source = options.$source, placement = options.placement, margin = options.margin;
//    			if ($source === currentSource) {
//    				return;
//    			}
//    			popBounds = getBounds($this);
//    			sourceBounds = getBounds($source);
//    			decidePosition = function() {
//    				switch (placement) {
//    				case 'inside':
//    					return {
//    					top: sourceBounds.top,
//    					left: sourceBounds.left
//    				};
//    				case 'left':
//    					return {
//    					top: sourceBounds.top + sourceBounds.height / 2 - popBounds.height / 2,
//    					left: sourceBounds.left - popBounds.width - margin
//    				};
//    				case 'top':
//    					return {
//    					top: sourceBounds.top - popBounds.height - margin,
//    					left: sourceBounds.left + sourceBounds.width / 2 - popBounds.width / 2
//    				};
//    				case 'right':
//    					return {
//    					top: sourceBounds.top + sourceBounds.height / 2 - popBounds.height / 2,
//    					left: sourceBounds.left + sourceBounds.width + margin
//    				};
//    				case 'bottom':
//    					return {
//    					top: sourceBounds.top + sourceBounds.height + margin,
//    					left: sourceBounds.left + sourceBounds.width / 2 - popBounds.width / 2
//    				};
//    				}
//    			};
//    			$this.css(decidePosition()).fadeIn(250);
//    			return currentSource = $source;
//    		};
//    		hidePopover = function() {
//    			$this.fadeOut(250);
//    			return currentSource = null;
//    		};
//    		togglePopover = function(options) {
//    			if ($this.css('display') === 'none') {
//    				return showPopover(options);
//    			} else {
//    				return hidePopover();
//    			}
//    		};
//    		return $this.bind('popoverShow', function(evt, eventOptions) {
//    			return showPopover($.extend(defaults, directiveOptions, eventOptions));
//    		}).bind('popoverHide', function() {
//    			return hidePopover();
//    		}).bind('popoverToggle', function(evt, eventOptions) {
//    			return togglePopover($.extend(defaults, directiveOptions, eventOptions));
//    		});
//    	};
//    	return {
//    		restrict: 'E',
//    		scope: {
//    			title: '='
//    		},
//    		link: linkFn,
//    		transclude: true,
//    		template: "<div class=\"arrow\"></div>\n<div class=\"popover-inner\">\n	<h3 class=\"popover-title\">{{title}}</h3>\n	<div class=\"popover-content\" ng-transclude></div>\n</div>"
//    	};
//	}
//])
//.directive('popTarget', [
//     function() {
//    	 var $, linkFn;
//    	 $ = jQuery;
//    	 linkFn = function(scope, elm, attrs) {
//    		 var $popover, $this, bindPopoverEvent, setPopoverOpenCloseEvents;
//    		 $popover = $(attrs.popTarget);
//    		 $this = $(elm);
//    		 bindPopoverEvent = function(sourceEventType, popoverEventType, callback) {
//    			 return $this.bind(sourceEventType, function() {
//    				 if (typeof callback === "function") {
//    					 callback();
//    				 }
//    				 return $popover.trigger(popoverEventType, [{
//                        	$source: $this,
//                        	placement: attrs.popPlacement,
//                        	eventType: attrs.popEvent,
//                        	margin: parseInt(attrs.popMargin || '0')
//                        }
//    				 ]);
//    			 });
//    		 };
//    		 setPopoverOpenCloseEvents = {
//    				 hover: function() {
//    					 return bindPopoverEvent('mouseover', 'popoverShow', function() {
//    						 var mouseInCount, onMouseout, onMouseover;
//    						 mouseInCount = 1;
//    						 onMouseover = function() {
//    							 return mouseInCount++;
//    						 };
//    						 onMouseout = function() {
//    							 mouseInCount--;
//    							 return setTimeout(function() {
//    								 if (mouseInCount === 0) {
//    									 $popover.trigger('popoverHide');
//    									 $this.unbind('mouseover', onMouseover).unbind('mouseout', onMouseout);
//    									 return $popover.unbind('mouseover', onMouseover).unbind('mouseout', onMouseout);
//    								 }
//    							 }, 150);
//    						 };
//    						 $this.bind('mouseover', onMouseover).bind('mouseout', onMouseout);
//    						 return $popover.bind('mouseover', onMouseover).bind('mouseout', onMouseout);
//    					 });
//    				 },
//    				 focus: function() {
//    					 bindPopoverEvent('focus', 'popoverShow');
//    					 return bindPopoverEvent('blur', 'popoverHide');
//    				 },
//    				 click: function() {
//    					 return bindPopoverEvent('click', 'popoverToggle');
//    				 }
//    		 };
//    		 if (attrs.popEvent != null) {
//    			 return setPopoverOpenCloseEvents[attrs.popEvent]();
//    		 }
//    	 };
//    	 return {
//    		 restrict: 'A',
//    		 link: linkFn
//    	 };
//     }
// ])
 
 .directive("uiPopover", function($interpolate){
    return function(scope, element, iAttrs) {
            iAttrs.$observe('title', function(value) {
                // for updating tooltip when title changed
                element.removeData('tooltip');
                
                element.popover({placement:'right'});
        });
    };
})
.directive("uiTooltip", function($interpolate){
    return function(scope, element, iAttrs) {
            iAttrs.$observe('title', function(value) {
                // for updating tooltip when title changed
                element.removeData('tooltip');
                
                element.tooltip({placement:'right'});
        });
    };
});
