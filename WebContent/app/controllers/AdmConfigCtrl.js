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

    	$scope.clearFields = function() {

    		$scope.envName = "Select Environment";
    		$scope.repoType = "Select Improt/Export";
    		$scope.id = null;
    		$scope.envName = null;
    		$scope.admConfigType=null;
    		$scope.seibelServer = null;
    		$scope.sessionId = null;
    		$scope.createdTime = null;
    		$scope.updatedTime = null;
    		$scope.createdUser = null;
    		$scope.updatedUser = null;
    		$scope.modifyEnv = false;
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

    		//alert("In modify:" + admConfig.name);

    		$scope.id = admConfig.id;
    		$scope.envName = admConfig.envName;
    		$scope.admConfigType=admConfig.admConfigType;
    		$scope.seibelServer = admConfig.seibelServer;
    		$scope.sessionId = admConfig.sessionId;
    		$scope.createdTime = admConfig.createdTime;
    		$scope.updatedTime = admConfig.updatedTime;
    		$scope.createdUser = admConfig.createdUser;
    		$scope.updatedUser = admConfig.updatedUser;
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
    			seibelServer : $scope.seibelServer,
    			sessionId : $scope.sessionId,
    			admConfigType:$scope.admConfigType,
    			createdTime : $scope.createdTime,
    			updatedTime : $scope.updatedTime,
    			createdUser : $scope.createdUser,
    			updatedUser : $scope.updatedUser
    		};
    		//alert(angular.toJson(dataObj, true));
    		$http({
    			data : dataObj,
    			method : 'POST',
    			url : 'rest/createAdmConfig/'
    		}).then(function successCallback(response) {
    			// this callback will be called asynchronously
    			// when the response is available
    			$scope.reloadAllAdmConfigs();
    			$scope.clearFields();
    		}, function errorCallback(response) {
    			// called asynchronously if an error occurs
    			// or server returns response with an error status.
    		});
    	}
    };

})();