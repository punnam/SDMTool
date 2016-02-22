(function() {
	'use strict';

	angular.module('app').controller('commTemplCtrl', commTemplCtrl);
	envSetupCtrl.$inject = [ '$scope', '$http', '$filter' ];

	function commTemplCtrl($scope, $http, $filter) {
		$http.get("rest/getAllCommTemplates/").success(function(response) {
			$scope.allCommTemplates = response.data;
		});
		var clearFields = function() {
			$scope.id = null;
			$scope.code = null;
			$scope.command = null;
			$scope.uiScreenLocation=null;
			$scope.description = null;
			$scope.createdTime = null;
			$scope.updatedTime = null;
			$scope.createdUser = null;
			$scope.updatedUser = null;
		}
		$scope.deleteCommTempl = function(commTempl) {
			$http({
				data : commTempl,
				method : 'POST',
				url : 'rest/deleteCommTempl/'
			}).then(function successCallback(response) {
				// this callback will be called asynchronously
				// when the response is available
				//$scope.allEnvs = $filter('filter')($scope.allEnvs, {id:envObj.id});
				$scope.reloadAllCommTempls();
				clearFields();
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		};
		$scope.modifyCommTempl = function(commTempl) {
			$scope.id = commTempl.id;
			$scope.code = commTempl.code;
			$scope.command = commTempl.command;
			$scope.description = commTempl.description;
			$scope.uiScreenLocation = commTempl.uiScreenLocation;
			$scope.createdTime = commTempl.createdTime;
			$scope.updatedTime = commTempl.updatedTime;
			$scope.createdUser = commTempl.createdUser;
			$scope.updatedUser = commTempl.updatedUser;
		};
		$scope.reloadAllCommTempls = function() {
			$http.get("rest/getAllCommTemplates/").success(function(response) {
				$scope.allCommTemplates = response.data;
			});
		};
		$scope.addEnvInfo = function() {
			var dataObj = {
				id : $scope.id,
				code : $scope.code,
				command:$scope.command,
				description:$scope.description,
				uiScreenLocation:$scope.uiScreenLocation,
				createdTime : $scope.createdTime,
				updatedTime : $scope.updatedTime,
				createdUser : $scope.createdUser,
				updatedUser : $scope.updatedUser	
			};

			$http({
				data : dataObj,
				method : 'POST',
				url : 'rest/creatCommTempl/'
			}).then(function successCallback(response) {
				$scope.reloadAllCommTempls();
				clearFields();
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		}
	}
	;

})();