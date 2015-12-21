(function() {
	'use strict';

	angular.module('app').controller('deploymentOptionsCtrl',
			deploymentOptionsCtrl);

	deploymentOptionsCtrl.$inject = [ '$scope', '$http', '$location' ];

	function deploymentOptionsCtrl($scope, $http) {
		$http.get("rest/getAllEnvNames/").success(function(response) {
			$scope.allEnvs = response.data;
		});
		$scope.selectedItem = "Select Environment";
		$scope.dropboxitemselected = function(item) {
			$scope.selectedItem = item;
		}
		$scope.repoType = "Select Repo Type";
		$scope.dropboxRepoTypeSelected = function(item) {
			$scope.repoType = item;
		}
		$http.get("rest/deploymentOptions/getAllDeploymentOptions/").success(
				function(response) {
					$scope.allAdmBuildservices = response.data;
				});

		$scope.checkedAdmActions = [];
		$scope.toggleCheck = function(fruit) {
			////alert("Before:"+$scope.checkedAdmActions);
			if ($scope.checkedAdmActions.indexOf(fruit) === -1) {
				$scope.checkedAdmActions.push(fruit);
			} else {
				$scope.checkedAdmActions.splice($scope.checkedAdmActions
						.indexOf(fruit), 1);
			}
			////alert("After:"+$scope.checkedAdmActions);
		};

		$scope.processAdmBuildServices = function() {
			////alert("processAdmBuildServices");
			////alert($scope.selectedItem);
			////alert($scope.allAdmBuildservices);
			var dataObj = {
				envName : $scope.selectedItem,
				actionType : $scope.repoType,
				deploymentServices : $scope.checkedAdmActions
			};
			////alert("Executing processAdmBuildServices:");
			$http({
				data : dataObj,
				method : 'POST',
				url : 'rest/deploymentOptions/processDeploymentOptions/'
			}).then(function successCallback(response) {
				// this callback will be called asynchronously
				// when the response is available
				//$scope.allEnvs.push(dataObj);
				//clearFields();
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		}
	}

})();