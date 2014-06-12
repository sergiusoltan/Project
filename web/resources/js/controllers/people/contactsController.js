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
    .controller('ContactsCtrl', ['$scope', 'UserFactory', 'AuthFactory', '$location', '$modal','peopleType', function ($scope, UserFactory, AuthFactory, $location, $modal, peopleTypes) {

        $scope.contacts = [
            {name: 'Jane Dawson', phone: 78943332, type: CONTACT, recomendedBy: "John1", date: new Date()},
            {name: 'Jane Dawson', phone: 78943332, type: TEAM_MEMBER, recomendedBy: "John1", date: new Date()},
            {name: 'Jane Dawson', phone: 78943332, type: CLIENT, recomendedBy: "John1", date: new Date()},
            {name: 'Jane Dawson', phone: 78943332, type: CONTACT, recomendedBy: "John1", date: new Date()}
        ];

        $scope.title = "Contact Controller";
        $scope.predicate = 'name';
        $scope.types = peopleTypes;

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
                        if(contact != null){
                            return contact;
                        }
                        return {type:CONTACT};
                    },
                    title: function () {
                        if(contact != null){
                            return "Edit Contact";
                        }
                        return "Add Contact";
                    }

                }
            });

            modalInstance.result.then(function (selectedItem) {

                $scope.selected = selectedItem;
            }, function () {
                console.log('modal dismissed');
            });
        };
}]);