'use strict';

angular
    .module('mainApp')
    .controller('DashboardCtrl', ['$scope', 'UserFactory', 'AuthFactory', '$location', function ($scope, UserFactory, AuthFactory, $location) {
    $scope.title = "DashBoard Controller";
}]);