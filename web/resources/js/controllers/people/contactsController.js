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
    .controller('ContactsCtrl', ['$scope', '$location', 'AuthFactory', 'ContactService', '$modal', 'peopleType', function ($scope, $location, AuthFactory, ContactService, $modal, peopleTypes) {

        $scope.contacts = [];
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

        $scope.removeContact = function () {
            var modalInstance = $modal.open({
                templateUrl: 'confirmationPopup.html',
                controller: 'ConfirmationPopup',
                size: '',
                resolve: {
                    title: function () {
                        var suffix  = $scope.selectedItems.length > 1 ? " contacts?" : " contact?";
                        return "Do you really want to remove " + $scope.selectedItems.length + suffix;
                    }
                }
            });

            modalInstance.result.then(function () {
                ContactService.deleteContacts($scope.selectedItems).then(function (success) {
                    $scope.selectedItems = [];
                    $scope.contacts = success;
                }, function (error) {
                    console.log('error loading contacts');
                });
            }, function () {
                console.log('modal dismissed');
            });
        };

        function initData() {
            $scope.predicate = 'name';
            $scope.types = peopleTypes;
            $scope.title = "Contact Controller";
            ContactService.getAllContacts().then(function (success) {
                $scope.contacts = success;
            }, function (error) {
                console.log('error loading contacts');
            });
        }

        $scope.openContact = function (size, contact) {
            var modalInstance = $modal.open({
                templateUrl: 'addEditContactTemplate.html',
                controller: 'ModalPeopleCtrl',
                size: size,
                resolve: {
                    items: function () {
                        return $scope.contacts;
                    },
                    item: function () {
                        if (contact != null) {
                            return contact;
                        }
                        return {};
                    },
                    title: function () {
                        if (contact != null) {
                            return "Edit Contact";
                        }
                        return "Add Contact";
                    }

                }
            });

            modalInstance.result.then(function (returnedObject) {
                ContactService.saveOrUpdate(returnedObject).then(function (success) {
                    $scope.contacts = success;
                }, function (error) {
                    console.log('failed to save or update contact' + error);
                });
            }, function () {
                console.log('modal dismissed');
            });
        };
    }]);