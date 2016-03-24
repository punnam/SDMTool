(function() {
	'use strict';

	angular.module('app').controller('forgotPassController', RegisterController);

	RegisterController.$inject = ['$scope', '$http', '$location'];

	function RegisterController($scope, $http, $location) {
		var vm = this;
		vm.register = register;
		function register() {
			vm.dataLoading = true;
			var dataObj = {
				firstName : vm.user.firstName,
				lastName : vm.user.lastName,
				userId : vm.user.username,
				password : vm.user.password,
				confirmPassword : vm.user.confirmPassword
			};
			//alert('Punnam reset Password:' + dataObj);
			$http({
				data : dataObj,
				method : 'POST',
				url : 'rest/UserInfo/resetPassword/'
			}).then(function successCallback(response) {
				//alert("Success p:"+response.data.data);
				if (response.data.data != undefined) {
					//FlashService.Success('Registration successful', true);
					//alert("Success");
					//vm.dataLoading=true;
					$location.path('/login');
					$Scope.flash = {
						message : 'Reset Password successful',
						type : 'success',
						keepAfterLocationChange : 'false'
					};
				} else {
					//alert("fail");
					//FlashService.Error(response.message);
					$location.path('/forgotPass');
					$Scope.flash = {
						message : 'Fail',
						type : 'error',
						keepAfterLocationChange : 'false'
					};
					//vm.dataLoading = false;
				}
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		}
	}
})();