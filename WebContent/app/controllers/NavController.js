(function () {
    'use strict';

    angular
        .module('app')
        .controller('NavController', NavController);

    NavController.$inject = ['$scope','$rootScope','$http','$location','AuthenticationService'];
    function NavController($scope,$rootScope, $http,$location,AuthenticationService) {
		var vm = this;
  	  $rootScope.showlogIn = !$rootScope.globals.currentUser;
	  $rootScope.showlogout = $rootScope.globals.currentUser;
		$scope.logOut = function() {
			(function initController() {
				//reset login status
				//alert('hitting auth')
				AuthenticationService.ClearCredentials();
			})();
			/**
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
			}); **/
		  	  $rootScope.showlogIn = !$rootScope.globals.currentUser;
			  $rootScope.showlogout = $rootScope.globals.currentUser;
		};
	}

})();