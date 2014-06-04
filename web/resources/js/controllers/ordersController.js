'use strict';

angular
    .module('mainApp')
    .controller('OrdersCtrl', ['$scope', 'UserFactory', 'AuthFactory', '$location', function ($scope, UserFactory, AuthFactory, $location) {
    $scope.title = "Orders Controller";
}]);