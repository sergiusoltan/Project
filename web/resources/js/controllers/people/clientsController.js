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

        $scope.onSelect = function (contact) {
            if ($scope.selectedItems.indexOf(contact.id) != -1) {
                var index = $scope.selectedItems.indexOf(contact.id);
                $scope.selectedItems.splice(index, 1);
                return;
            }
            $scope.selectedItems.push(contact.id);
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
                    console.log('error loading contacts');
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
    .controller('ModalPeopleCtrl', ['$scope', '$modalInstance','items','item','title','careerPositions', function ($scope, $modalInstance,items, item, title, careerPositions) {

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

        $scope.ok = function () {
            $modalInstance.close({instance:$scope.item,isNew:$scope.new});
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