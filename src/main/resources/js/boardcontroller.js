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