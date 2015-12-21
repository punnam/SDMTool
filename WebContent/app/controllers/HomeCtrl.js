(function() {
	'use strict';
	angular.module('app').controller('homeCtrl', homeCtrl);
	homeCtrl.$inject = [ '$scope', '$http', '$location' ];
	function homeCtrl($scope, $http, $filter) {
		alert("Punnam");
		$http.get("rest/getAgentInfo/").success(function(response) {

			$scope.agentInfo = response.data;
		});
	}
	;
})();