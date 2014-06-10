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
    .controller('MainCtrl', ['$scope', 'UserFactory', 'AuthFactory', '$location', function ($scope, UserFactory, AuthFactory, $location) {

    init();

    $scope.signIn = function () {
        var properties = {
            login:false,
            email:$scope.email,
            name:$scope.name,
            password:$scope.password,
            rememberme:$scope.rememberme
        }
        UserFactory.saveUser(JSON.stringify(properties)).then(function (success) {
            alert(success);
        });
    };

    $scope.logIn = function () {
        var properties = {
            login:true,
            email:$scope.email,
            password:$scope.password,
            rememberme:$scope.rememberme
        }
        UserFactory.loginUser(JSON.stringify(properties)).then(function (success) {
            $scope.user = success.myHashMap.data;
            AuthFactory.setUser($scope.user);
            $location.path(HOME);
            //alert(success.myHashMap.messages);
        });
    };


    $scope.$on(LOADING_CONTENT_EVENT, function (e, value) {
        $scope.loading.content = value;
    });

    $scope.$on(GOOGLE_LOGIN_EVENT, function (e, value) {
        loadUser();
    });

    function init() {
        $scope.title = 'Main Controller';
        $scope.$emit(LOADING_CONTENT_EVENT, true);
        $scope.loading = {
            header:true,
            content:true
        };
        $scope.name = "test";
        $scope.email = "test@test.test";
        $scope.password = "testtest";
        $scope.rememberme = false;

        loadUser();
    }

    function loadUser() {
        UserFactory.getUser().then(function (succes) {
            $scope.user = succes;
            AuthFactory.setUser($scope.user);
        }).finally(function () {
                $scope.$emit(LOADING_CONTENT_EVENT, false);
                $scope.$emit(LOADING_HEADER_EVENT, false);
            });
    }

}])
    .value('API_URL', {rest:'rest/', login:'/_ah/login', logout:'/_ah/logout'})
    .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider
        .otherwise({redirectTo:HOME })
        .when(LOGIN, {templateUrl:'resources/views/loginform.html', controller:'MainCtrl'})
        .when(SIGNIN, {templateUrl:'resources/views/signinform.html', controller:'MainCtrl'})
        .when(HOME, {templateUrl:'resources/views/dashboard.html', controller:'DashboardCtrl'})
        .when(DISTRIBUTORS, {templateUrl:'resources/views/people/distributors.html', controller:'DistributorsCtrl'})
        .when(VIEW_DISTRIBUTOR, {templateUrl:'resources/views/people/view-distributor.html', controller:'DistributorsCtrl'})
        .when(CLIENTS, {templateUrl:'resources/views/people/clients.html', controller:'ClientsCtrl'})
        .when(VIEW_CLIENT, {templateUrl:'resources/views/people/clients.html', controller:'ClientsCtrl'})
        .when(CONTACTS, {templateUrl:'resources/views/people/contacts.html', controller:'ContactsCtrl'})
        .when(VIEW_CONTACTS, {templateUrl:'resources/views/people/contacts.html', controller:'ContactsCtrl'})
        .when(PRODUCTS, {templateUrl:'resources/views/products.html', controller:'ProductsCtrl'})
        .when(HIERARCHY, {templateUrl:'resources/views/hierarchy.html', controller:'HierarchyCtrl'})
        .when(ABOUT, {templateUrl:'resources/views/about.html', controller:'MainCtrl'})
        .when(ORDERS, {templateUrl:'resources/views/orders.html', controller:'OrdersCtrl'});

    $locationProvider.html5Mode(true);
}])
    .run(['$rootScope', '$location', 'AuthFactory', '$window', 'API_URL', function ($rootScope, $location, AuthFactory, $window, API_URL) {
    $rootScope.$on("$routeChangeStart", function (event, nextRoute, currentRoute) {
        if($location.path().indexOf(LOGIN) != -1 || $location.path().indexOf(SIGNIN) != -1){
            return;
        }
        if (!AuthFactory.isAuthenticated()) {

            if ($location.path().indexOf(API_URL.login) != -1 || $location.path().indexOf(API_URL.logout) != -1) {
//                    $rootScope.$emit(GOOGLE_LOGIN_EVENT,true);
                return $window.location.href = $location.absUrl();
            }
            event.preventDefault();
            return $location.path(LOGIN);
        }
    });
}])
;