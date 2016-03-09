(function() {
	'use strict';

	angular.module('app').controller('deploymentPackageCtrl',
			deploymentPackageCtrl);

	deploymentPackageCtrl.$inject = [ '$scope', '$http', '$location' ];

	function deploymentPackageCtrl($scope, $http) {
		$http.get("rest/getAllEnvNames/").success(function(response) {
			$scope.allEnvs = response.data;
		});
		$scope.selectedItem = "Select Environment";
		$scope.dropboxitemselected = function(item) {
			$scope.selectedItem = item;
		}

		$http.get("rest/deploymentOptions/getAllDeploymentPackages/").success(
				function(response) {
					$scope.allSelectActions = response.data;
				});

		$scope.checkedAdmActions = [];
		$scope.toggleCheck = function(fruit) {
			if ($scope.checkedAdmActions.indexOf(fruit) === -1) {
				$scope.checkedAdmActions.push(fruit);
			} else {
				$scope.checkedAdmActions.splice($scope.checkedAdmActions
						.indexOf(fruit), 1);
			}
		};

		$scope.processDeploymentPackageServices = function() {
			//alert("processSelectActionsServices()");
			//alert($scope.selectedItem);
			//alert($scope.checkedAdmActions);
			if($scope.selectedItem == "Select Environment"){
				$scope.selectedItem = null;
			}
			var dataObj = {
				envName : $scope.selectedItem,
				deploymentServices : $scope.checkedAdmActions
			};
			//alert("Executing processAdmBuildServices:");
			$http({
				data : dataObj,
				method : 'POST',
				url : 'rest/deploymentOptions/processDeploymentOptions/'
			}).then(function successCallback(response) {
				var resultData = response.data.data;
				var errors = resultData.error;
				var commandExecResults = resultData.result;
				$scope.allErrors = errors;
				$scope.commandExecResults = commandExecResults;
				$scope.selectedItem = "Select Environment";
				//$scope.checkedAdmActions = [];
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		}
	}
	;

})();