//controller and any services relating to the main boaard

plugin.controller("BoardController", function ($scope, autoRefresh) {
	$scope.projectName = "CDPipeline";
	$scope.results = autoRefresh.data;

});

//polls a REST endpoint every five seconds and automatically refreshes
plugin.factory('autoRefresh', function ($http, $timeout) {
	var data = { resp: {}};
	var poller = function() {
		$http.get('?type=json').then( function(r) {
			data.resp = r.data;
			$timeout(poller, 5000);
		});
	};
	poller();

	return {
		data: data
	};
});

plugin.filter("emptyToEnd", function () {
	return function (array, key) {
		if(!angular.isArray(array)) return;
        var present = array.filter(function (item) {
            return item[key];
        });
        var empty = array.filter(function (item) {
            return !item[key]
        });
		return present.concat(empty);
	};
});

var ModalController = function ($scope, $modal) {

  $scope.modalOpen = function (result, url) {

  	$scope.url = url;
  	$scope.result = result;
    var modalInstance = $modal.open({
      templateUrl: 'ModalContent.html',
      controller: ModalInstanceCtrl,
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

};