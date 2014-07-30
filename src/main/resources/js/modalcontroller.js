// Controller for the popup window
plugin.controller("ModalController", function ($scope, $modal, $window) {
  
  /* --------------------------------------------------------------------------------------- */
  /* -------------------------- Modal Window with pipeline content ------------------------- */
  /* --------------------------------------------------------------------------------------- */
  $scope.modalOpen = function (size, result, url, modalContent) {

  	$scope.url = url;
  	$scope.result = result;
    var modalInstance = $modal.open({
      templateUrl: modalContent,
      controller: ModalInstanceCtrl,
      size: size,
      resolve: {
        url: function () {
          return $scope.url;
        },
        result: function () {
        	return $scope.result;
        }
      }
    });
  };

  /* --------------------------------------------------------------------------------------- */
  /* -------------------------- Modal Window with bamboo content --------------------------- */
  /* --------------------------------------------------------------------------------------- */
  $scope.isFullScreen = false;
  $scope.url = '';
  var showWindow = false;
  
  // Sets whether to show the window
  $scope.setShowWindow = function(val) {
      showWindow = val;
  };

  // Returns whether to show the window
  $scope.getShowWindow = function() {
      return showWindow;
  };

  // Sets the current url to be the given one. 
  // Sets a new window to open on full screen
  $scope.setupModal = function (url) {
      $scope.url = url;
      if($scope.isFullScreen) {
          $scope.setShowWindow(true);
      }
      else {
          $window.location.assign($scope.url);
      }
  };

  /* --------------------------------------------------------------------------------------- */
  /* ----------------------------------- Hide/Show Header ---------------------------------- */
  /* --------------------------------------------------------------------------------------- */
  //toggles the button icon from expand to minify,
  //fades the header
  $scope.toggleHeader = function() {
    AJS.$("#fullscreenIcon").toggleClass("fa-compress");
    AJS.$.fn.slideFadeToggle  = function(speed, easing, callback) {
          return this.animate({opacity: 'toggle', height: 'toggle'}, speed, easing, callback);
    };
    AJS.$("header").slideFadeToggle(800);
    $scope.isFullScreen = !$scope.isFullScreen;
  };
});



// Helper method for pop up window with pipeline content
var ModalInstanceCtrl = function ($scope, $modalInstance, result, url) {

  $scope.result = result;
  $scope.url = url;

  $scope.modalCancel = function () {
    $modalInstance.dismiss('cancel');
  };
};
			
