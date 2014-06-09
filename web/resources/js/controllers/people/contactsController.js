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
    .controller('ContactsCtrl', ['$scope', 'UserFactory', 'AuthFactory', '$location', '$modal', '$log', function ($scope, UserFactory, AuthFactory, $location, $modal, $log) {

        $scope.contacts = [ {name:'Jane Dawson',phone:78943332,email:'janedow@gmail.com', date:new Date()},
            {name:'Jane Dawson',phone:78943332,email:'janedow@gmail.com', date:new Date()},
            {name:'Jane Dawson',phone:78943332,email:'janedow@gmail.com', date:new Date()},
            {name:'Jane Dawson',phone:78943332,email:'janedow@gmail.com', date:new Date()}];

        $scope.title = "Contact Controller";
        $scope.predicate = 'name';

        $scope.openContact = function (size, contact) {
            var modalInstance = $modal.open({
                templateUrl: 'addEditContactTemplate.html',
                controller: 'ModalPeopleCtrl',
                size: size,
                resolve: {
                    item: function () {
                        if(contact != null){
                            return contact;
                        }
                        return {};
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
                $log.info('Modal dismissed at: ' + new Date());
            });
        };
}]);