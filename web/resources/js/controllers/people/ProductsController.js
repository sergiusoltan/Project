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
    .controller('ProductsCtrl', ['$scope', '$modal', 'FileReaderService', function ($scope, $modal, FileReaderService) {

        $scope.title = "Distributors Controller";
        $scope.products = [
//            {name:"Celulos", description:'Celulos description here'},
//            {name:"Multivitamine", description:'Celulos description here'},
//            {name:"Fibre", description:'Celulos description here'},
//            {name:"Ceai", description:'Celulos description here'},
//            {name:"Formula 1", description:'Celulos description here'},
//            {name:"Batoane proteice", description:'Celulos description here'}
        ];
        init();
        function init(){
            FileReaderService.getAllProducts().then(function(products){
                $scope.products = products;
            });
        }

        $scope.addProduct = function () {
            var modalInstance = $modal.open({
                templateUrl: 'createProduct.html',
                controller: 'UploadController',
                size: '',
                resolve: {
                    title: function () {
                        return "Add new product";
                    }
                }
            });

            modalInstance.result.then(function (success) {
                FileReaderService.getAllProducts().then(function(products){
                    $scope.products = products;
                }, function(error){
                    console.log(error);
                });

            });
        };

    }]);