/**
 * AngularJS Tutorial 1
 * @author Nick Kaye <nick.c.kaye@gmail.com>
 */

/**
 * Main AngularJS Web Application
 */

var auth = angular.module('Authentication',[]);
var app = angular.module('app', ['ngRoute', 'ngCookies','Authentication']);

/**
 * Configure the Routes
 */
app.config([ '$routeProvider', '$locationProvider', function($routeProvider,$locationProvider) {
	$routeProvider
	// Home
	.when("/", {
		templateUrl : "partials/ 	.html",
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
    }).when('/logout', {
        controller: 'LogoutController',
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
app.run(['$rootScope', '$location', '$cookieStore', '$http',
      function ($rootScope, $location, $cookieStore, $http) {
          // keep user logged in after page refresh
          $rootScope.globals = $cookieStore.get('globals') || {};
          if ($rootScope.globals.currentUser) {
              $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
          }

          $rootScope.$on('$locationChangeStart', function (event, next, current) {
              // redirect to login page if not logged in
        	  $rootScope.showlogIn = !$rootScope.globals.currentUser;
        	  $rootScope.showlogout = $rootScope.globals.currentUser;
        	  if ($location.path() == '/login' || !$rootScope.globals.currentUser) {
                  $location.path('/login');
              }
          });
      }]);

/**
 * Controls the Blog
 */
app.controller('BlogCtrl', function(/* $scope, $location, $http */) {
	console.log("Blog Controller reporting for duty.");
});