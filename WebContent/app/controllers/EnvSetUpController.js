(function() {
	'use strict';

	angular.module('app').controller('envSetupCtrl', envSetupCtrl);
	envSetupCtrl.$inject = [ '$scope', '$http', '$filter' ];
	var selectEnvString = "Select Environment";
	function envSetupCtrl($scope, $http, $filter) {
		$http.get("rest/getAllenvs/").success(function(response) {
			$scope.allEnvs = response.data;
		});
				
		$scope.envName = selectEnvString;
		$scope.dropboxitemselected = function(item) {
			$scope.envName = item;
		}

		$scope.showEnvTxtBox = false;
		var clearFields = function() {
			$scope.showEnvTxtBox = false;
			$scope.envName = "Select Environment";
			$scope.name = null;
			$scope.desc = null;
			$scope.id = null;
			$scope.hostName = null;
			$scope.service = null;
			$scope.serverName = null;
			$scope.seibelPath = null;
			$scope.serverHost = null;
			$scope.migrationPath = null;
			$scope.enterpriseName = null;
			$scope.logFilePath = null;
			$scope.modifyEnv = false;
			$scope.createdTime = null;
			$scope.updatedTime = null;
			$scope.createdUser = null;
			$scope.updatedUser = null;
			$scope.envForm.$setPristine(true);
			
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
			$scope.showEnvTxtBox = false;
			$scope.name = envObj.name;
			alert($scope.name);
			if($scope.name != "" && $scope.name!= null ){
				$scope.envName = envObj.name;
			}else{
				$scope.envName = "Select Environment";
			}	
			$scope.desc = envObj.desc;
			$scope.id = envObj.id;
			$scope.hostName = envObj.hostName;
			$scope.service = envObj.service;
			$scope.serverName = envObj.serverName;
			$scope.seibelPath = envObj.seibelPath;
			$scope.serverHost = envObj.serverHost;
			$scope.migrationPath = envObj.migrationPath;
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
				name : $scope.envName,
				desc:$scope.name,
				id : $scope.id,
				hostName : $scope.hostName,
				service : $scope.service,
				serverName : $scope.serverName,
				seibelPath : $scope.seibelPath,
				serverHost : $scope.serverHost,
				migrationPath : $scope.migrationPath,
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
		};
		$scope.addNewEnvName = function() {
			$scope.showEnvTxtBox = true;

		};
		$scope.showExisitingEnvNames = function() {
			$scope.showEnvTxtBox = false;
		};
	}
	;

})();