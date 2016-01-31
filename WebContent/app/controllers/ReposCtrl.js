(function() {
	'use strict';

	angular.module('app').controller('reposCtrl', reposCtrl);

	reposCtrl.$inject = [ '$scope', '$http', '$location' ];
	function reposCtrl($scope, $http) {
		$http.get("rest/getAllEnvNames/").success(function(response) {
			$scope.allEnvs = response.data;
		});
		$scope.envName = "Select Environment";

		$scope.dropboxitemselected = function(item) {
			$scope.envName = item;
		}

		$scope.repoType = "Select Repo Type";
		$scope.dropboxRepoTypeSelected = function(item) {
			$scope.repoType = item;
		}
		$http.get("rest/getAllRepos/").success(function(response) {
			$scope.allRepos = response.data;
		});

		var clearFields = function() {

			$scope.envName = "Select Environment";
			$scope.repoType = "Select Improt/Export";
			$scope.id = "";
			$scope.envName = "";
			$scope.userId = "";
			$scope.password = "";
			$scope.odbc = "";
			$scope.filePath = "";
			$scope.repoType = "";
			$scope.repoName = "";
			$scope.logFilePath = "";
			$scope.tableDDLSync = "";
			$scope.indexDDLSync = "";
			$scope.createdTime = "";
			$scope.updatedTime = "";
			$scope.createdUser = "";
			$scope.updatedUser = "";

		}
		$scope.deleteRepos = function(repoObj) {
			$http({
				data : repoObj,
				method : 'POST',
				url : 'rest/repos/deleteRepo/'
			}).then(function successCallback(response) {
				// this callback will be called asynchronously
				// when the response is available
				//$scope.allEnvs = $filter('filter')($scope.allEnvs, {id:envObj.id});
				$scope.reloadAllRepos();
				clearFields();
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		};
		$scope.modifyRepos = function(repoObj) {
			$scope.id = repoObj.id;
			$scope.envName = repoObj.envName;
			$scope.userId = repoObj.userId;
			$scope.password = repoObj.password;
			$scope.odbc = repoObj.odbc;
			$scope.filePath = repoObj.filePath;
			$scope.repoType = repoObj.repoType;
			$scope.repoName = repoObj.repoName;
			$scope.logFilePath = repoObj.logFilePath;
			$scope.tableDDLSync = repoObj.tableDDLSync;
			$scope.indexDDLSync = repoObj.indexDDLSync;
			$scope.createdTime = repoObj.createdTime;
			$scope.updatedTime = repoObj.updatedTime;
			$scope.createdUser = repoObj.createdUser;
			$scope.updatedUser = repoObj.updatedUser;

		};
		$scope.reloadAllRepos = function() {
			$http.get("rest/getAllRepos/").success(function(response) {
				$scope.allRepos = response.data;
			});
		};
		$scope.addRepos = function() {
			var dataObj = {
				id : $scope.id,
				envName : $scope.envName,
				userId : $scope.userId,
				password : $scope.password,
				odbc : $scope.odbc,
				filePath : $scope.filePath,
				repoType : $scope.repoType,
				repoName : $scope.repoName,
				logFilePath : $scope.logFilePath,
				tableDDLSync : $scope.tableDDLSync,
				indexDDLSync : $scope.indexDDLSync,
				createdTime : $scope.createdTime,
				updatedTime : $scope.updatedTime,
				createdUser : $scope.createdUser,
				updatedUser : $scope.updatedUser
			};
alert(dataObj);
			$http({
				data : dataObj,
				method : 'POST',
				url : 'rest/createRepo/'
			}).then(function successCallback(response) {
				// this callback will be called asynchronously
				// when the response is available
				$scope.reloadAllRepos();
				clearFields();
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		}
	}
	;

})();