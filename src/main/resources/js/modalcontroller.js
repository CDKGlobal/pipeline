// Controller for the popup window
plugin.controller("ModalController", function ($scope, $modal, $window, $http, $rootScope) {
  
  /* --------------------------------------------------------------------------------------- */
  /* -------------------------- Modal Window with bamboo content --------------------------- */
  /* --------------------------------------------------------------------------------------- */
  $scope.modalOpenUrl = function (url) {

    if($scope.isFullScreen) {
      $scope.url = url;
      var modalInstance = $modal.open({
        templateUrl: "Details.html",
        controller: ModalInstanceCtrl,
        size: "lg",
        resolve: {
          url: function () {
            return $scope.url;
          },
          contentData: function () {
            return "";
          }
        }
      });
    }
    else {
      $window.location.assign(url);
    }
  };

  /* --------------------------------------------------------------------------------------- */
  /* -------------------------- Modal Window with pipeline content ------------------------- */
  /* --------------------------------------------------------------------------------------- */
  $scope.modalOpenContent = function (size, dataType, result, modalContent) {
    $scope.contentData = {};
    if(dataType != ''){
      $scope.contentData = { planName: result.planName, numChanges: result.numChanges, resultData: {}};
      $http.get('?data=' + dataType + '&plankey=' + result.planKey).then( function(r) {
        $scope.contentData.resultData = r.data;
        $rootScope.dataLoaded = true;
      });
    }
    var modalInstance = $modal.open({
      templateUrl: modalContent,
      controller: ModalInstanceCtrl,
      size: size,
      resolve: {
        url: function () {
          return null;
        },
        contentData: function () {
          return $scope.contentData;
        }
      }
    });
  };

  /* --------------------------------------------------------------------------------------- */
  /* ----------------------------------- Hide/Show Header ---------------------------------- */
  /* --------------------------------------------------------------------------------------- */
    $scope.isFullScreen = false;

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
var ModalInstanceCtrl = function ($scope, $modalInstance, contentData, url) {

  $scope.contentData = contentData;
  $scope.url = url;

  $scope.modalCancel = function () {
    $modalInstance.dismiss('cancel');
  };
};
			
