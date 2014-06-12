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
    .controller('ContactsCtrl', ['$scope', '$location', 'ContactService', '$modal', 'peopleType', function ($scope, $location, ContactService, $modal, peopleTypes) {

        $scope.contacts = [
            {id: 1, name: 'Jane Dawson', phone: 78943332, type: CONTACT, recomendedBy: "John1", date: new Date()},
            {id: 2, name: 'Jane Dawson', phone: 78943332, type: TEAM_MEMBER, recomendedBy: "John1", date: new Date()},
            {id: 3, name: 'Jane Dawson', phone: 78943332, type: CLIENT, recomendedBy: "John1", date: new Date()},
            {id: 4, name: 'Jane Dawson', phone: 78943332, type: CONTACT, recomendedBy: "John1", date: new Date()}
        ];
        $scope.selectedItems = [];
//        initData();

        $scope.onSelect = function (contact) {
            if($scope.selectedItems[contact.id]){
                var index = $scope.selectedItems.indexOf(contact.id);
                $scope.selectedItems.splice(index, 1);
                console.log($scope.selectedItems);
                return;
            }
            $scope.selectedItems[contact.id] = true;
            console.log($scope.selectedItems);
        };

        $scope.removeContact = function () {
//            ContactService.deleteContact($scope.selectedItems).then(function (success) {
//                console.log('remove contacts');
//                console.log(success);
//                $scope.contacts = success;
//            },function (error) {
//                console.log('error loading contacts');
//            });
            $.each($scope.contacts, function(index, value){
                if($scope.selectedItems[value.id]){
                    $scope.contacts.splice(index,1);
                }
            });
        };

        function initData() {
            $scope.predicate = 'name';
            $scope.types = peopleTypes;
            $scope.title = "Contact Controller";
            ContactService.getAllContacts().then(function (success) {
                console.log('get All contacts contact');
                console.log(success);
                $scope.contacts = success;
            },function (error) {
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
                        return {type: CONTACT};
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
                $scope.$emit(LOADING_CONTENT_EVENT, true);
                ContactService.saveOrUpdate(returnedObject).then(function (success) {
                    console.log('success on save or update contact');
                    console.log(success);
                    $scope.contacts = success;
                },function (error) {
                    console.log('failed to save or update contact' + error);
                }).finally(function () {
                        $scope.$emit(LOADING_CONTENT_EVENT, false);
                    });
            }, function () {
                console.log('modal dismissed');
            });
        };
    }]);