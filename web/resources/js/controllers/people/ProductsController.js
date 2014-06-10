/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 10.06.2014
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
'use strict';

angular
    .module('mainApp')
    .controller('ProductsCtrl', ['$scope', 'UserFactory', 'AuthFactory', '$location', '$modal', '$log', function ($scope, UserFactory, AuthFactory, $location, $modal, $log) {

        $scope.title = "Distributors Controller";
        $scope.products = [
            {name:"Celulos", description:'Celulos description here'},
            {name:"Multivitamine", description:'Celulos description here'},
            {name:"Fibre", description:'Celulos description here'},
            {name:"Ceai", description:'Celulos description here'},
            {name:"Formula 1", description:'Celulos description here'},
            {name:"Batoane proteice", description:'Celulos description here'}
        ];

}]);