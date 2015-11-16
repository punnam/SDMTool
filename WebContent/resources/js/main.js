/**
 * AngularJS Tutorial 1
 * @author Nick Kaye <nick.c.kaye@gmail.com>
 */

/**
 * Main AngularJS Web Application
 */
var app = angular.module('app', [ 'ngRoute' ]);

/**
 * Configure the Routes
 */
app.config([ '$routeProvider', '$locationProvider', function($routeProvider,$locationProvider) {
	$routeProvider
	// Home
	.when("/", {
		templateUrl : "partials/home.html",
		controller : "PageCtrl"
	}).when("/repos", {
		templateUrl : "partials/repos.html",
		controller : "reposCtrl"
	}).when("/environmentSetup", {
		templateUrl : "partials/environmentSetup.html",
		controller : "envSetupCtrl"
	}).when("/deploymentPackage", {
		templateUrl : "partials/deploymentPackage.html",
		controller : "deploymentPackageCtrl"
	}).when("/deploymentOptions", {
		templateUrl : "partials/deploymentOptions.html",
		controller : "deploymentOptionsCtrl"
	}).when("/admConfig", {
		templateUrl : "partials/admConfig.html",
		controller : "admConfigCtrl"
	}).when('/login', {
        controller: 'LoginController',
        templateUrl: 'partials/login.html',
        controllerAs: 'vm'
    }).when('/register', {
        controller: 'RegisterController',
        templateUrl: 'partials/register.view.html',
        controllerAs: 'vm'
    })
	
	
	// else 404
	.otherwise("/404", {
		templateUrl : "partials/404.html",
		controller : "PageCtrl"
	});
} ]);

app.controller('envSetupCtrl', [ '$scope', '$http', '$filter',
		function($scope, $http, $filter) {
			$http.get("rest/getAllenvs/").success(function(response) {
				$scope.allEnvs = response.data;
			});
			var clearFields = function() {
				$scope.name = "";
				$scope.id = "";
				$scope.hostName = "";
				$scope.service = "";
				$scope.serverName = "";
				$scope.seibelPath = "";
				$scope.serverHost = "";
				$scope.admPath = "";
				$scope.enterpriseName = "";
				$scope.modifyEnv = false;
				$scope.createTime = "";
				$scope.updateTime = "";
				$scope.createUser = "";
				$scope.updateUser = "";
			}
			$scope.deleteEnvInfo = function(envObj) {
				$http({
					data : envObj,
					method : 'POST',
					url : 'rest/deleteEnv/'
				}).then(function successCallback(response) {
					// this callback will be called asynchronously
					// when the response is available
					//$scope.allEnvs = $filter('filter')($scope.allEnvs, {id:envObj.id});
					$scope.reloadAllEnvInfo();
					clearFields();
				}, function errorCallback(response) {
					// called asynchronously if an error occurs
					// or server returns response with an error status.
				});
			};
			$scope.modifyEnvInfo = function(envObj) {
				$scope.name = envObj.name;
				$scope.id = envObj.id;
				$scope.hostName = envObj.hostName;
				$scope.service = envObj.service;
				$scope.serverName = envObj.serverName;
				$scope.seibelPath = envObj.seibelPath;
				$scope.serverHost = envObj.serverHost;
				$scope.admPath = envObj.admPath;
				$scope.enterpriseName = envObj.enterpriseName;
				$scope.createTime = envObj.createTime;
				$scope.updateTime = envObj.updateTime;
				$scope.createUser = envObj.createUser;
				$scope.updateUser = envObj.updateUser;
			};
			$scope.reloadAllEnvInfo = function() {
				$http.get("rest/getAllenvs/").success(function(response) {
					$scope.allEnvs = response.data;
				});
			};
			$scope.addEnvInfo = function() {
				var dataObj = {
					name : $scope.name,
					id : $scope.id,
					hostName : $scope.hostName,
					service : $scope.service,
					serverName : $scope.serverName,
					seibelPath : $scope.seibelPath,
					serverHost : $scope.serverHost,
					admPath : $scope.admPath,
					enterpriseName : $scope.enterpriseName,
					createTime : $scope.createTime,
					updateTime : $scope.updateTime,
					createUser : $scope.createUser,
					updateUser : $scope.updateUser
				};

				$http({
					data : dataObj,
					method : 'POST',
					url : 'rest/createEnv/'
				}).then(function successCallback(response) {
					$scope.reloadAllEnvInfo();
					clearFields();
				}, function errorCallback(response) {
					// called asynchronously if an error occurs
					// or server returns response with an error status.
				});
			}
		} ]);

/**Start reposCtrl**/
app.controller('reposCtrl', [ '$scope', '$http', function($scope, $http) {
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
		
	};
	$scope.reloadAllRepos = function() {
		$http.get("rest/getAllRepos/").success(function(response) {
			$scope.allRepos = response.data;
		});
	};
	$scope.addRepos = function() {
		var dataObj = {
			id:$scope.id,
			envName : $scope.envName,
			userId : $scope.userId,
			password : $scope.password,
			odbc : $scope.odbc,
			filePath : $scope.filePath,
			repoType : $scope.repoType,
			repoName : $scope.repoName,
			logFilePath : $scope.logFilePath,
			tableDDLSync : $scope.tableDDLSync,
			indexDDLSync : $scope.indexDDLSync

		};

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
} ]);

/**Start admConfigCtrl**/
app.controller('admConfigCtrl', [ '$scope', '$http', function($scope, $http) {
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
			admConfigType:$scope.admConfigType
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
} ]);
/**Start deploymentOptionsCtrl**/
app.controller('deploymentOptionsCtrl', [
		'$scope',
		'$http',
		function($scope, $http) {
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
		} ]);

/**End deploymentOptionsCtrl**/

/**Start selectActionsCtrl**/
app.controller('deploymentPackageCtrl', [
		'$scope',
		'$http',
		function($scope, $http) {
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
				alert("processSelectActionsServices()");
				alert($scope.selectedItem);
				alert($scope.checkedAdmActions);
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
					// this callback will be called asynchronously
					// when the response is available
					//$scope.allEnvs.push(dataObj);
					//clearFields();
				}, function errorCallback(response) {
					// called asynchronously if an error occurs
					// or server returns response with an error status.
				});
			}
		} ]);

/**End selectActionsCtrl**/

/**Start deploymentOptionsCtrl**/
app.controller('commandEditorCtrl', [
		'$scope',
		'$http',
		function($scope, $http) {
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
		} ]);

/**End deploymentOptionsCtrl**/

/**
 * Controls the Blog
 */
app.controller('BlogCtrl', function(/* $scope, $location, $http */) {
	console.log("Blog Controller reporting for duty.");
});

/** login **/
app.controller('LoginController', ['$scope','$http', function($scope,$http) {
			var vm = this;

			// vm.login = login;

			(function initController() {
				// reset login status
				// AuthenticationService.ClearCredentials();
			})();
			$scope.id = 1;
			$scope.logIn = function() {
				var dataObj = {
						id: $scope.id,
					userId : $scope.username,
					password : $scope.password
				};
				alert('Punnam login:' + dataObj);
				$http({
					data : dataObj,
					method : 'POST',
					url : 'rest/UserInfo/logIn/'
				}).then(function successCallback(response) {

				}, function errorCallback(response) {
					// called asynchronously if an error occurs
					// or server returns response with an error status.
				});

			};
		}]);
/**Register Controller**/
/** login **/
app.controller('RegisterController', [ '$scope', '$http','$location',
		function($scope, $http,$location) {
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
				alert('Punnam refister:' + dataObj);
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
			                    message: 'Registration successful',
			                    type: 'success', 
			                    keepAfterLocationChange: 'false'
			                };
						
					} else {
						//FlashService.Error(response.message);
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
			}
		} ]);
/**
 * Controls all other Pages
 */
app.controller('homeCtrl', [ '$scope', '$http', '$filter',
                          		function($scope, $http, $filter) {
                          	alert("Punnam");
                          			$http.get("rest/getAgentInfo/").success(function(response) {
                          				
                          				$scope.agentInfo = response.data;
                          			});

                          		} ]);