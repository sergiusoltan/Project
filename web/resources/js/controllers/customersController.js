'use strict';

angular
    .module('mainApp')
    .controller('CustomersCtrl', ['$scope', 'UserFactory', 'AuthFactory', '$location', function ($scope, UserFactory, AuthFactory, $location) {
    $scope.title = "Customers Controller";
}]);