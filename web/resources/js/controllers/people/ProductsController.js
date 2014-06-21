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

        $scope.title = "Products Controller";
        $scope.products = [];
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

        $scope.removeProduct = function (product) {
            var modalInstance = $modal.open({
                templateUrl: 'confirmationPopup.html',
                controller: 'ConfirmationPopup',
                size: '',
                resolve: {
                    title: function () {
                        return "Do you really want to remove " + product.productName;
                    }
                }
            });

            modalInstance.result.then(function () {
                FileReaderService.deleteProduct(product.productId).then(function (success) {
                    $scope.products = success;
                }, function (error) {
                    console.log('error loading contacts');
                });
            }, function () {
                console.log('modal dismissed');
            });
        };

        $scope.openImage = function (url) {
            var modalInstance = $modal.open({
                templateUrl: 'imageFullSized.html',
                controller: 'UploadController',
                size: 'sm',
                resolve: {
                    title: function () {
                        return url;
                    }
                }
            });
        };


    }]);