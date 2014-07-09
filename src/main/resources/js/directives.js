plugin.directive("stripsContainer", function() {

});

//currently doesn't work
plugin.directive("close", function() {
	return {
		restrict: 'EA',
		replace: true,
		transclude: 'element',
		template: ""
	};
});