/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 09.06.2014
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
'use strict';

angular
    .module('mainApp')
    .controller('ClientsCtrl', ['$scope', 'ClientService', 'AuthFactory', '$location', '$modal', '$log', function ($scope, ClientService, AuthFactory, $location, $modal, $log) {

        $scope.clients = [];
        $scope.selectedItems = [];
        initData();

        $scope.onSelect = function (client) {
            if ($scope.selectedItems.indexOf(client.id) != -1) {
                var index = $scope.selectedItems.indexOf(client.id);
                $scope.selectedItems.splice(index, 1);
                return;
            }
            $scope.selectedItems.push(client.id);
        };

        $scope.getRecomendedByName = function(recomendedBy){
            if(recomendedBy){
                return JSON.parse(recomendedBy).name;
            }
        };

        $scope.removeClient = function () {
            var modalInstance = $modal.open({
                templateUrl: 'confirmationPopup.html',
                controller: 'ConfirmationPopup',
                size: '',
                resolve: {
                    title: function () {
                        var suffix  = $scope.selectedItems.length > 1 ? " clients?" : " client?";
                        return "Do you really want to remove " + $scope.selectedItems.length + suffix;
                    }
                }
            });

            modalInstance.result.then(function () {
                ClientService.deleteClients($scope.selectedItems).then(function (success) {
                    $scope.selectedItems = [];
                    $scope.clients = success;
                }, function (error) {
                    console.log('error loading clients');
                });
            }, function () {
                console.log('modal dismissed');
            });
        };

        function initData() {
            $scope.title = "Clients Controller";
            $scope.predicate = 'name';
            ClientService.getAllClients().then(function (success) {
                $scope.clients = success;
            }, function (error) {
                console.log('error loading clients');
            });
        }

        $scope.openClient = function (size, client) {
            var modalInstance = $modal.open({
                templateUrl: 'addEditClientTemplate.html',
                controller: 'ModalPeopleCtrl',
                size: size,
                resolve: {
                    items: function () {
                        return $scope.clients;
                    },
                    item: function () {
                        if(client != null){
                            return client;
                        }
                        return {type:CLIENT};
                    },
                    title: function () {
                        if(client != null){
                            return "Edit Client";
                        }
                        return "Add client";
                    }
                }
            });

            modalInstance.result.then(function (returnedObject) {
                ClientService.saveOrUpdate(returnedObject).then(function (success) {
                    $scope.clients = success;
                }, function (error) {
                    console.log('failed to save or update client' + error);
                });
                console.log(returnedObject);
            }, function () {
                console.log('modal dismissed');
            });
        };
}]);

angular
    .module('mainApp')
    .controller('ModalPeopleCtrl', ['$scope', '$modalInstance','items','item','title','careerPositions','FileReaderService', function ($scope, $modalInstance,items, item, title, careerPositions, FileReaderService) {

        $scope.careerPositions = careerPositions;

        $scope.new = item.name == null;
        $scope.title = title;
        $scope.item = item;
        $scope.items = items;

        $scope.filterItem = function(item){
            return item.name != $scope.item.name;
        };

        $scope.isRecomendedSelected = function(item){
            return item && $scope.getRecomendedByName() == item.name;
        };

        $scope.getRecomendedByName = function(){
            if($scope.item.recomendedBy){
                return JSON.parse($scope.item.recomendedBy).name;
            }
            return '';
        };

        $scope.getFile = function (file) {
            $scope.progress = 0;
            $scope.file = file;
            FileReaderService.readFile(file, $scope)
                .then(function(result) {
                    $scope.imageSrc = result;
                });
        };

        $scope.$on("fileProgress", function(e, progress) {
            $scope.progress = progress.loaded / progress.total;
        });

        $scope.ok = function () {
            $modalInstance.close({instance: $scope.item, isNew: $scope.new, file: $scope.file});
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);

angular
    .module('mainApp')
    .controller('ConfirmationPopup', ['$scope', '$modalInstance','title', function ($scope, $modalInstance, title) {

        $scope.title = title;

        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);

angular
    .module('mainApp')
    .controller('UploadController', ['$scope', '$modalInstance','title', 'item','FileReaderService', function ($scope, $modalInstance, title, item, FileReaderService) {

        $scope.title = title;
        $scope.item = item;
//        $scope.size = '';
//        $scope.$watch('size', function(value){
//            var url = $scope.title.split("=")[0];
//            $scope.title = value.length > 1 ? url + "=s"+value : url;
//        });

        $scope.ok = function () {
            FileReaderService.createUploadUrl().then(function(success){
                $scope.item.uploadUrl = success.uploadUrl;
                FileReaderService.submitProduct($scope.item, $scope.file).then(function(allproducts){
                    $modalInstance.close(allproducts);
                }, function(error){
                    console.log(error);
                });
            }, function(error){
                console.log(error);
            });
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.getFile = function (file) {
            $scope.progress = 0;
            $scope.file = file;
            FileReaderService.readFile(file, $scope)
                .then(function(result) {
                    $scope.imageSrc = result;
                });
        };

        $scope.$on("fileProgress", function(e, progress) {
            $scope.progress = progress.loaded / progress.total;
        });
    }]);