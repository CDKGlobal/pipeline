plugin.controller("MainController", function ($scope, $http) {
	$scope.projectName = "CDPipeline";

	//toggles the button icon from expand to minify,
	//fades the header
	$scope.toggleHeader = function() {
		AJS.$("#fullscreenIcon").toggleClass("fa-compress");
		AJS.$.fn.slideFadeToggle  = function(speed, easing, callback) {
        	return this.animate({opacity: 'toggle', height: 'toggle'}, speed, easing, callback);
		};

		AJS.$("header").slideFadeToggle(800);
	};
});
			
