(function() {
	'use strict';

	angular.module('app').controller('envSetupCtrl', envSetupCtrl);
	envSetupCtrl.$inject = [ '$scope', '$http', '$filter' ];

	function envSetupCtrl($scope, $http, $filter) {
		$http.get("rest/getAllenvs/").success(function(response) {
			$scope.allEnvs = response.data;
		});
		var clearFields = function() {
			$scope.name = null;
			$scope.desc = null;
			$scope.id = null;
			$scope.hostName = null;
			$scope.service = null;
			$scope.serverName = null;
			$scope.seibelPath = null;
			$scope.serverHost = null;
			$scope.sourcePath = null;
			$scope.enterpriseName = null;
			$scope.logFilePath = null;
			$scope.modifyEnv = false;
			$scope.createdTime = null;
			$scope.updatedTime = null;
			$scope.createdUser = null;
			$scope.updatedUser = null;
		}
		$scope.deleteEnvInfo = function(envObj) {
			$http({
				data : envObj,
				method : 'POST',
				url : 'rest/deleteEnv/'
			}).then(function successCallback(response) {
				// this callback will be called asynchronously
				// when the response is available
				//$scope.allEnvs = $filter('filter')($scope.allEnvs, {id:envObj.id});
				$scope.reloadAllEnvInfo();
				clearFields();
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		};
		$scope.modifyEnvInfo = function(envObj) {
			$scope.name = envObj.name;
			$scope.desc = envObj.desc;
			$scope.id = envObj.id;
			$scope.hostName = envObj.hostName;
			$scope.service = envObj.service;
			$scope.serverName = envObj.serverName;
			$scope.seibelPath = envObj.seibelPath;
			$scope.serverHost = envObj.serverHost;
			$scope.sourcePath = envObj.sourcePath;
			$scope.enterpriseName = envObj.enterpriseName;
			$scope.logFilePath = envObj.logFilePath;
			$scope.createdTime = envObj.createdTime;
			$scope.updatedTime = envObj.updatedTime;
			$scope.createdUser = envObj.createdUser;
			$scope.updatedUser = envObj.updatedUser;
		};
		$scope.reloadAllEnvInfo = function() {
			$http.get("rest/getAllenvs/").success(function(response) {
				$scope.allEnvs = response.data;
			});
		};
		$scope.addEnvInfo = function() {
			var dataObj = {
				name : $scope.name,
				desc:$scope.name,
				id : $scope.id,
				hostName : $scope.hostName,
				service : $scope.service,
				serverName : $scope.serverName,
				seibelPath : $scope.seibelPath,
				serverHost : $scope.serverHost,
				sourcePath : $scope.sourcePath,
				enterpriseName : $scope.enterpriseName,
				logFilePath : $scope.logFilePath,
				createdTime : $scope.createdTime,
				updatedTime : $scope.updatedTime,
				createdUser : $scope.createdUser,
				updatedUser : $scope.updatedUser
			};

			$http({
				data : dataObj,
				method : 'POST',
				url : 'rest/createEnv/'
			}).then(function successCallback(response) {
				$scope.reloadAllEnvInfo();
				clearFields();
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		}
	}
	;

})();