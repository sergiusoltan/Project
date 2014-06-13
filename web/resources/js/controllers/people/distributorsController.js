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
    .controller('DistributorsCtrl', ['$scope', 'MemberService', 'AuthFactory', '$location', '$modal', '$log', function ($scope, MemberService, AuthFactory, $location, $modal, $log) {

        $scope.distributors = [
//            {id:1, name: 'Jane Doe', phone: 78943332, position: SUCCESS_BUILDER, recomendedBy: '', type: TEAM_MEMBER, email: 'janedow@gmail.com', date: new Date()},
//            {id:2, name: 'John Dine', phone: 78943332, position: SUPERVISOR, recomendedBy: '', type: TEAM_MEMBER, email: 'janedow@gmail.com', date: new Date()},
//            {id:3, name: 'Michael Phelps', phone: 78943332, position: WORLDT, recomendedBy: '', type: TEAM_MEMBER, email: 'janedow@gmail.com', date: new Date()},
//            {id:4, name: 'Phil Jackson', phone: 78943332, position: GLOBALT, recomendedBy: '', type: TEAM_MEMBER, email: 'janedow@gmail.com', date: new Date()}
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

        $scope.removeMember = function () {
//            MemberService.deleteMembers($scope.selectedItems).then(function (success) {
//                $scope.selectedItems = [];
//                console.log('remove contacts');
//                $scope.contacts = getArray(success);
//                console.log($scope.contacts);
//            }, function (error) {
//                console.log('error loading contacts');
//            });
        };

        function initData() {
            $scope.predicate = 'name';
            $scope.title = "Contact Controller";
            MemberService.getAllMembers().then(function (success) {
                console.log('get All contacts contact');
                $scope.distributors = getArray(success);
                console.log($scope.distributors);
            }, function (error) {
                console.log('error loading contacts');
            });
        }

        function getArray(success) {
            while (!(success instanceof Array)) {
                success = JSON.parse(success);
            }
            return success;
        }

        $scope.title = "Members Controller";
        $scope.predicate = 'name';

        $scope.openDistributor = function (size, distributor) {
            var modalInstance = $modal.open({
                templateUrl: 'addEditDistributorTemplate.html',
                controller: 'ModalPeopleCtrl',
                size: size,
                resolve: {
                    items: function () {
                        var items = new Array();
                        items.push($scope.distributors);
                        return items;
                    },
                    item: function () {
                        if(distributor != null){
                            return distributor;
                        }
                        return {type:TEAM_MEMBER};
                    },
                    title: function () {
                        if(distributor != null){
                            return "Edit Member";
                        }
                        return "Add Member";
                    }
                }
            });

            modalInstance.result.then(function (returnedObject) {
//                MemberService.saveOrUpdate(returnedObject).then(function (success) {
//                    console.log('success on save or update contact');
//                    $scope.contacts = getArray(success);
//                    console.log($scope.contacts);
//                }, function (error) {
//                    console.log('failed to save or update contact' + error);
//                });
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };
}]);