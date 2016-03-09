(function() {
	'use strict';

	angular.module('app').controller('commParamsCtrl', commParamsCtrl);
	commParamsCtrl.$inject = [ '$scope', '$http', '$filter' ];

	function commParamsCtrl($scope, $http, $filter) {
		$http.get("rest/getAllCommTemplates/").success(function(response) {
			$scope.allCommTemplates = response.data;
		});
		$scope.dropboxitemselected = function(commTempl) {
			//alert(commTempl.code);
			$scope.selectedItem = commTempl.description;
			$scope.getCommParamByCode(commTempl);
			$scope.code=commTempl.code;
		}
		var clearFields = function() {
			$scope.id = null;
			$scope.code = null;
			$scope.param = null;
			$scope.order=null;
			$scope.description = null;
			$scope.createdTime = null;
			$scope.updatedTime = null;
			$scope.createdUser = null;
			$scope.updatedUser = null;
		}
		$scope.deleteCommParam = function(commParam) {
			$http({
				data : commParam,
				method : 'POST',
				url : 'rest/deleteCommParam/'
			}).then(function successCallback(response) {
				// this callback will be called asynchronously
				// when the response is available
				//$scope.allEnvs = $filter('filter')($scope.allEnvs, {id:envObj.id});
				$scope.reloadAllCommParam(commParam);
				clearFields();
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		};
		$scope.getCommParamByCode = function(commTempl) {
			var dataObj = {
					code:commTempl.code	
				};
			$http({
				data : dataObj,
				method : 'POST',
				url : 'rest/admin/getAllCommParamsByCode/'
			}).then(function successCallback(response) {
				$scope.allCommParams = response.data.data;
				clearFields();
				$scope.code=commTempl.code;
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		};
		$scope.modifyCommParam = function(commParam) {
			$scope.id = commParam.id;
			$scope.code = commParam.code;
			
			$scope.param = commParam.param;
			$scope.order = commParam.order;
			$scope.description = commParam.description;
			$scope.createdTime = commParam.createdTime;
			$scope.updatedTime = commParam.updatedTime;
			$scope.createdUser = commParam.createdUser;
			$scope.updatedUser = commParam.updatedUser;
		};
		$scope.reloadAllCommTempls = function() {
			$http.get("rest/getAllCommTemplates/").success(function(response) {
				$scope.allCommTemplates = response.data;
			});
		};
		$scope.reloadAllCommParam = function(dataObj) {
			$scope.getCommParamByCode(dataObj);
		};
		$scope.addCommParamInfo = function() {
			alert("addCommParamInfo");
			var dataObj = {
				id : $scope.id,
				code : $scope.code,
				param:$scope.param,
				order:$scope.order,
				description:$scope.description,
				createdTime : $scope.createdTime,
				updatedTime : $scope.updatedTime,
				createdUser : $scope.createdUser,
				updatedUser : $scope.updatedUser	
			};
			alert(angular.toJson(dataObj, true));
			$http({
				data : dataObj,
				method : 'POST',
				url : 'rest/creatCommParam/'
			}).then(function successCallback(response) {
				$scope.reloadAllCommParam(dataObj);
				clearFields();
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		}
	}
	;

})();