/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 10.05.2014
 * Time: 09:57
 * To change this template use File | Settings | File Templates.
 */
'use strict';

angular
    .module('mainApp')
    .controller('MainCtrl', ['$scope', 'UserFactory', 'AuthFactory', function ($scope, UserFactory, AuthFactory) {

        init();

        $scope.$on(LOADING_CONTENT_EVENT, function (e, value) {
            $scope.loading.content = value;
        });

        function init(){
            $scope.title = 'Main Controller';
            $scope.$emit(LOADING_CONTENT_EVENT, true);
            $scope.loading = {
                header: true,
                content: true
            };

            UserFactory.getUser().then(function(succes){
                $scope.user = succes.data;
                AuthFactory.setUser($scope.user);
            }).finally(function(){
                $scope.$emit(LOADING_CONTENT_EVENT, false);
                $scope.$emit(LOADING_HEADER_EVENT, false);
            });
        }

    }])
    .value('API_URL', 'rest/')
    .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $routeProvider
            .otherwise({redirectTo: '/' })
            .when("/login", {templateUrl: 'resources/views/loginform.html', controller: 'MainCtrl'})
            .when("/signin", {templateUrl: 'resources/views/signinform.html', controller: 'MainCtrl'})
            .when("/", {templateUrl: 'resources/views/empty.html', controller: 'MainCtrl'});

        $locationProvider.html5Mode(true);
    }])
    .run(['$rootScope','$location', 'AuthFactory',function($rootScope, $location, AuthFactory) {
        $rootScope.$on("$routeChangeStart", function(event, nextRoute, currentRoute) {
            if (!AuthFactory.isAuthenticated()) {
                event.preventDefault();
                $location.path("/login");
            }
        });
    }])
;