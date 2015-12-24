(function () {
    'use strict';

    angular
        .module('app')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$scope','$rootScope','$http','$location','AuthenticationService'];
    function LoginController($scope,$rootScope,$http,$location,AuthenticationService) {
		var vm = this;

		// vm.login = login;

		(function initController() {
			//reset login status
			AuthenticationService.ClearCredentials();
		})();
		$scope.id = 1;
		$scope.logIn = function() {
			var dataObj = {
					id: $scope.id,
				userId : $scope.username,
				password : $scope.password
			};
			$http({
				data : dataObj,
				method : 'POST',
				url : 'rest/UserInfo/logIn/'
			}).then(function successCallback(response) {
		
				var loggedIn = response.data.data;
				if (loggedIn==true) {
					//FlashService.Success('Registration successful', true);
					//vm.dataLoading=true;
					$rootScope.showlogIn = false;
					$rootScope.showlogout = true;
					AuthenticationService.SetCredentials($scope.username, $scope.password);
					$location.path('/');
					$location.replace();
					$Scope.flash = {
		                    message: 'login successful',
		                    type: 'success', 
		                    keepAfterLocationChange: 'false'
		                };
					
					
				} else {
					//FlashService.Error(response.message);
					$location.path('/login.html');
				       $Scope.flash = {
				                message: 'Fail',
				                type: 'error',
				                keepAfterLocationChange: 'false'
				            };
					vm.dataLoading = false;
					$rootScope.showlogIn = true;
					$rootScope.showlogout = false;
				}
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});


		};
		$scope.logout = function() {
			(function initController() {
				//reset login status
				AuthenticationService.ClearCredentials();
			})();
			$http({
				data : dataObj,
				method : 'POST',
				url : 'rest/UserInfo/logOut/'
			}).then(function successCallback(response) {
		
				var loggedIn = response.data.data;
				alert("Success block:"+loggedIn);
				if (loggedIn==true) {
					//FlashService.Success('Registration successful', true);
					alert("Success fwd to /index.html");
					//vm.dataLoading=true;
					AuthenticationService.SetCredentials($scope.username, $scope.password);
					$location.path('/');
					$location.replace();
					$Scope.flash = {
		                    message: 'login successful',
		                    type: 'success', 
		                    keepAfterLocationChange: 'false'
		                };
					
					
				} else {
					//FlashService.Error(response.message);
					$location.path('/login.html');
				       $Scope.flash = {
				                message: 'Fail',
				                type: 'error',
				                keepAfterLocationChange: 'false'
				            };
					vm.dataLoading = false;
				}
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});

		};
	}

})();