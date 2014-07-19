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


// The search button on the menu bar
plugin.filter('searchFor', keywordSearch);


function keywordSearch(){
    
	// All filters must return a function. The first parameter
	// is the data that is to be filtered, and the second is an
	// argument that may be passed with a colon (searchFor:searchString)
    
	return function(arr, searchString){
        
		if(!searchString){
			return arr;
		}
		var result = [];
		searchString = searchString.toLowerCase();
		// Using the forEach helper method to loop through the array
		angular.forEach(arr, function(item){
            if(item.projectName.toLowerCase().indexOf(searchString) !== -1 |
                item.planName.toLowerCase().indexOf(searchString) !== -1) {
                    result.push(item);
            }
            for (i = 0; i < item.contributors.length; i++) {
                if (item.contributors[i].username.toLowerCase().indexOf(searchString) !== -1 |
                     item.contributors[i].fullname.toLowerCase().indexOf(searchString) !== -1) {
                    result.push(item)
                }
            }
        });
        
		return result;
	};
}

