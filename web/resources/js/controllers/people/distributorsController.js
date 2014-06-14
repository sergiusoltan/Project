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

        $scope.distributors = [];
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

        $scope.removeMember = function () {
            var modalInstance = $modal.open({
                templateUrl: 'confirmationPopup.html',
                controller: 'ConfirmationPopup',
                size: '',
                resolve: {
                    title: function () {
                        var suffix  = $scope.selectedItems.length > 1 ? " members?" : " member?";
                        return "Do you really want to remove " + $scope.selectedItems.length + suffix;
                    }
                }
            });

            modalInstance.result.then(function () {
                MemberService.deleteMembers($scope.selectedItems).then(function (success) {
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
            $scope.title = "Contact Controller";
            MemberService.getAllMembers().then(function (success) {
                $scope.distributors = success;
            }, function (error) {
                console.log('error loading contacts');
            });
        }

        $scope.openDistributor = function (size, distributor) {
            var modalInstance = $modal.open({
                templateUrl: 'addEditDistributorTemplate.html',
                controller: 'ModalPeopleCtrl',
                size: size,
                resolve: {
                    items: function () {
                        return $scope.distributors;
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
                MemberService.saveOrUpdate(returnedObject).then(function (success) {
                    $scope.distributors = success;
                }, function (error) {
                    console.log('failed to save or update contact' + error);
                });
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };
}]);