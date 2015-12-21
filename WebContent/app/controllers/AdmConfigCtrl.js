(function () {
    'use strict';

    angular
        .module('app')
        .controller('admConfigCtrl', admConfigCtrl);

    admConfigCtrl.$inject = ['$scope','$http','$location'];
 function admConfigCtrl($scope, $http) {
    	$http.get("rest/getAllEnvNames/").success(function(response) {
    		$scope.allEnvs = response.data;
    	});
    	$scope.envName = "Select Environment";

    	$scope.dropboxitemselected = function(item) {
    		$scope.envName = item;
    	}

    	$scope.repoType = "Select Improt/Export/DDLSync";
    	$scope.dropboxAdmConfigTypeSelected = function(item) {
    		$scope.admConfigType = item;
    	}
    	$http.get("rest/admConfig/getAllAdmConfig/").success(function(response) {
    		$scope.allAdmConfigs = response.data;
    	});

    	var clearFields = function() {

    		$scope.envName = "Select Environment";
    		$scope.repoType = "Select Improt/Export";
    		$scope.id = "";
    		$scope.envName = "";
    		$scope.admConfigType="";
    		$scope.userId = "";
    		$scope.password = "";
    		$scope.seibelServer = "";
    		$scope.rowId = "";
    		$scope.logFilePath = "";
    		$scope.createdTime = "";
    		$scope.updatedTime = "";
    		$scope.createdUser = "";
    		$scope.updatedUser = "";
    	}
    	$scope.deleteAdmConfig = function(admConfig) {
    		$http({
    			data : admConfig,
    			method : 'POST',
    			url : 'rest/admConfig/deleteAdmConfig/'
    		}).then(function successCallback(response) {
    			// this callback will be called asynchronously
    			// when the response is available
    			//$scope.allEnvs = $filter('filter')($scope.allEnvs, {id:envObj.id});
    			$scope.reloadAllAdmConfigs();
    			clearFields();
    		}, function errorCallback(response) {
    			// called asynchronously if an error occurs
    			// or server returns response with an error status.
    		});
    	};
    	$scope.modifyAdmConfig = function(admConfig) {

    		alert("In modify:" + admConfig.name);

    		$scope.id = admConfig.id;
    		$scope.envName = admConfig.envName;
    		$scope.admConfigType=admConfig.admConfigType;
    		$scope.userId = admConfig.userId;
    		$scope.password = admConfig.password;
    		$scope.seibelServer = admConfig.seibelServer;
    		$scope.rowId = admConfig.rowId;
    		$scope.logFilePath = admConfig.logFilePath;
    		$scope.createdTime = envObj.createdTime;
    		$scope.updatedTime = envObj.updatedTime;
    		$scope.createdUser = envObj.createdUser;
    		$scope.updatedUser = envObj.updatedUser;
    	};
    	$scope.reloadAllAdmConfigs = function() {
    		$http.get("rest/admConfig/getAllAdmConfig/").success(function(response) {
    			$scope.allAdmConfigs = response.data;
    		});
    	};
    	$scope.addAdmConfig = function() {
    		var dataObj = {
    			id:$scope.id,
    			envName : $scope.envName,
    			userId : $scope.userId,
    			password : $scope.password,
    			seibelServer : $scope.seibelServer,
    			rowId : $scope.rowId,
    			logFilePath : $scope.logFilePath,
    			admConfigType:$scope.admConfigType,
    			createdTime : $scope.createdTime,
    			updatedTime : $scope.updatedTime,
    			createdUser : $scope.createdUser,
    			updatedUser : $scope.updatedUser
    		};
    		
    		$http({
    			data : dataObj,
    			method : 'POST',
    			url : 'rest/createAdmConfig/'
    		}).then(function successCallback(response) {
    			// this callback will be called asynchronously
    			// when the response is available
    			$scope.reloadAllAdmConfigs();
    			clearFields();
    		}, function errorCallback(response) {
    			// called asynchronously if an error occurs
    			// or server returns response with an error status.
    		});
    	}
    };

})();