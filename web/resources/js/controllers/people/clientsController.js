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

        $scope.clients = [
//            {id:1,name: 'Jane Doe1', recomendedBy: '', type: CLIENT, phone: 178943332, email: 'janedow4@gmail.com'},
//            {id:2,name: 'Jane Doe2', recomendedBy: '', type: CLIENT, phone: 278943332, email: 'janedow3@gmail.com'},
//            {id:3,name: 'Jane Doe3', recomendedBy: '', type: CLIENT, phone: 378943332, email: 'janedow2@gmail.com'},
//            {id:4,name: 'Jane Doe4', recomendedBy: '', type: CLIENT, phone: 478943332, email: 'janedow1@gmail.com'}
        ];

        $scope.selectedItems = [];
        initData();

        $scope.onSelect = function (contact) {
            if ($scope.selectedItems.indexOf(contact.id) != -1) {
                var index = $scope.selectedItems.indexOf(contact.id);
                $scope.selectedItems.splice(index, 1);
                console.log($scope.selectedItems);
                return;
            }
            $scope.selectedItems.push(contact.id);
            console.log($scope.selectedItems);
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
                    console.log('remove contacts');
                    $scope.clients = getArray(success);
                    console.log($scope.clients);
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
                console.log('get All clients');
                $scope.clients = getArray(success);
                console.log($scope.clients);
            }, function (error) {
                console.log('error loading clients');
            });
        }

        function getArray(success) {
            while (!(success instanceof Array)) {
                success = JSON.parse(success);
            }
            return success;
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
                    console.log('success on save or update client');
                    $scope.clients = getArray(success);
                    console.log($scope.clients);
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