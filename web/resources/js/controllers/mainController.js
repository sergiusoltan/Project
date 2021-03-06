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
    .controller('MainCtrl', ['$scope', '$rootScope', 'UserFactory', 'AuthFactory', '$location', function ($scope, $rootScope ,UserFactory, AuthFactory, $location) {

        init();

        $scope.signIn = function () {
            var properties = {
                login: false,
                email: $scope.email,
                name: $scope.name,
                password: $scope.password,
                rememberme: $scope.rememberme
            };
            UserFactory.saveUser(JSON.stringify(properties)).then(function (success) {
                AuthFactory.showAlert(success, 2500);
                $location.path(LOGIN);
            }, function(error){
                AuthFactory.showAlert(error, 2500);
            });
        };

        $scope.logIn = function () {
            var properties = {
                login: true,
                email: $scope.email,
                password: $scope.password,
                rememberme: $scope.rememberme
            };
            UserFactory.loginUser(JSON.stringify(properties)).then(function (success) {
                $scope.user = success.myHashMap.data;
                AuthFactory.setUser($scope.user);
                if(AuthFactory.isAuthenticated()){
                    $location.path(HOME);
                }
                AuthFactory.showAlert(success.myHashMap.messages, 2500);
            });
        };

        $scope.logout = function () {
            var currentUser = AuthFactory.getUser();
            UserFactory.logoutUser(currentUser).then(function(success){
                AuthFactory.clear();
                $location.path(LOGIN);
            })
        };

        function init() {
            $scope.title = 'Main Controller';
            $scope.loading = {
                header: true,
                content: true
            };
            loadUser();
        }

        $rootScope.$on(SESSION_EXPIRED, function (event, message) {
            $scope.logout();
            AuthFactory.showAlert("Session expired due to inactivity!", 4000);
        });

        function loadUser() {
            UserFactory.getUser().then(function (succes) {
                $scope.user = succes;
            }, function(error){
                alert(error);
            });
        }

    }])
    .value('API_URL', {rest: 'rest/', login: '/_ah/login', logout: '/_ah/logout'})
    .config(['$routeProvider', '$locationProvider','$httpProvider', function ($routeProvider, $locationProvider,$httpProvider) {
        $routeProvider
            .otherwise({redirectTo: HOME })
            .when(LOGIN, {templateUrl: '/resources/views/loginform.html', controller: 'MainCtrl'})
            .when(SIGNIN, {templateUrl: '/resources/views/signinform.html', controller: 'MainCtrl'})
            .when(HOME, {templateUrl: '/resources/views/dashboard.html', controller: 'DashboardCtrl'})
            .when(DISTRIBUTORS, {templateUrl: '/resources/views/people/distributors.html', controller: 'DistributorsCtrl'})
            .when(VIEW_DISTRIBUTOR, {templateUrl: '/resources/views/people/view-distributor.html', controller: 'MemberCtrl'})
            .when(CLIENTS, {templateUrl: '/resources/views/people/clients.html', controller: 'ClientsCtrl'})
            .when(VIEW_CLIENT, {templateUrl: '/resources/views/people/view-client.html', controller: 'ClientCtrl'})
            .when(CONTACTS, {templateUrl: '/resources/views/people/contacts.html', controller: 'ContactsCtrl'})
            .when(VIEW_CONTACTS, {templateUrl: '/resources/views/people/view-contact.html', controller: 'ContactCtrl'})
            .when(PRODUCTS, {templateUrl: '/resources/views/products.html', controller: 'ProductsCtrl'})
            .when(HIERARCHY, {templateUrl: '/resources/views/hierarchy.html', controller: 'HierarchyCtrl'})
            .when(ABOUT, {templateUrl: '/resources/views/about.html', controller: 'MainCtrl'})
            .when(ORDERS, {templateUrl: '/resources/views/orders.html', controller: 'OrdersCtrl'});

        $locationProvider.html5Mode(true);
        $httpProvider.interceptors.push('MyAuthRequestInterceptor');
    }])
    .run(['$rootScope', '$location', 'AuthFactory', '$window', 'API_URL', function ($rootScope, $location, AuthFactory, $window, API_URL) {
        $rootScope.$on("$routeChangeStart", function (event, nextRoute, currentRoute) {
            if ($location.path().indexOf(LOGIN) != -1 || $location.path().indexOf(SIGNIN) != -1) {
                if(AuthFactory.isAuthenticated()){
                    return $location.path(HOME);
                }
                return;
            }
            if (!AuthFactory.isAuthenticated()) {
                if ($location.path().indexOf(API_URL.login) != -1 || $location.path().indexOf(API_URL.logout) != -1) {
                    return $window.location.href = $location.absUrl();
                }
                event.preventDefault();
                return $location.path(LOGIN);
            }
        });
        var lastDigestRun = new Date();
        $rootScope.$watch(function detectIdle() {
            var now = new Date();
            if (now - lastDigestRun > 5*60*1000) {
                if($location.path().indexOf(LOGIN) != -1 && $location.path().indexOf(SIGNIN) != -1){
                    $rootScope.$emit(SESSION_EXPIRED);
                }
            }
            lastDigestRun = now;
        });
    }])
    .factory('MyAuthRequestInterceptor', [ '$q', '$location', 'AuthFactory', '$rootScope',
        function ($q, $location, AuthFactory, $rootScope) {
            return {
                'request': function (config) {
                    if(AuthFactory.isAuthenticated()){
                        config.headers.authorization = AuthFactory.getCredentials();
                    }
                    return config || $q.when(config);
                },
                'response': function(response) {
                    response.headers()["Pragma"] = "no-cache";
                    response.headers()["Cache-Control"] = "no-store, max-age=0, must-revalidate, max-stale=0, post-check=0, pre-check=0"; //HTTP 1.1
                    response.headers()["Expires"] = "-1";
                    return response;
                },
                responseError: function (rejection) {
                    console.log("Found responseError: ", rejection);
//                    if (rejection.status == 401) {
//                        $rootScope.$emit(USER_ALERT, "Access denied (error 401), please login again");
//                    }
                    return $q.reject(rejection);
                }
            }
    }])
;