plugin.controller("BoardController", function ($scope, autoRefresh) {

	$scope.results = autoRefresh.data;

});

plugin.factory('autoRefresh', function ($http, $timeout) {
	var data = { resp: {}};
	var poller = function() {
		$http.get('?type=json').then( function(r) {
			data.resp = r.data;
			//console.log(data);
			$timeout(poller, 5000);
		});
	};
	poller();

	return {
		data: data
	};
});