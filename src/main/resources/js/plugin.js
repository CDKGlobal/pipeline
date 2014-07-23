var plugin = angular.module("CDPipeline", ['ui.bootstrap', 'ngAnimate']);

// Controller for the popup window
var ModalController = function ($scope, $modal) {
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
};

// Please note that $modalInstance represents a modal window (instance) dependency.
// It is not the same as the $modal service used above.

var ModalInstanceCtrl = function ($scope, $modalInstance, result, url) {

  $scope.result = result;
  $scope.url = url;

  $scope.modalCancel = function () {
    $modalInstance.dismiss('cancel');
  };
};