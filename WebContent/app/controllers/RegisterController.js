(function() {
	'use strict';

	angular.module('app').controller('RegisterController', RegisterController);

	RegisterController.$inject = [ '$scope', '$http', '$location' ];

	function RegisterController($scope, $http, $location) {
		var vm = this;

		vm.register = register;

		function register() {
			vm.dataLoading = true;

			var dataObj = {
				firstName : vm.user.firstName,
				lastName : vm.user.lastName,
				userId : vm.user.username,
				password : vm.user.password
			};
			var headerObj = {
				userId : "Punnam",
				sessionId : "xxxxx",
			};
			alert('Punnam register:' + dataObj);
			$http({
				data : dataObj,
				method : 'POST',
				url : 'rest/UserInfo/registerUser/'
			}).then(function successCallback(response) {
				alert("Success p");
				if (response.data) {
					//FlashService.Success('Registration successful', true);
					alert("Success");
					//vm.dataLoading=true;
					$location.path('/login');
					$Scope.flash = {
						message : 'Registration successful',
						type : 'success',
						keepAfterLocationChange : 'false'
					};

				} else {
					//FlashService.Error(response.message);
					$Scope.flash = {
						message : 'Fail',
						type : 'error',
						keepAfterLocationChange : 'false'
					};
					vm.dataLoading = false;
				}
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		}
	}

})();