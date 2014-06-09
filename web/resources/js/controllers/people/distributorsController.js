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
    .controller('DistributorsCtrl', ['$scope', 'UserFactory', 'AuthFactory', '$location', '$modal', '$log', function ($scope, UserFactory, AuthFactory, $location, $modal, $log) {

        $scope.distributors = [ {name:'Jane Doe',phone:78943332,position:'Supervisor',email:'janedow@gmail.com', date:new Date()},
            {name:'John Dine',phone:78943332,position:'Distributor',email:'janedow@gmail.com', date:new Date()},
            {name:'Michael Phelps',phone:78943332,position:'Supervisor',email:'janedow@gmail.com', date:new Date()},
            {name:'Phil Jackson',phone:78943332,position:'Supervisor',email:'janedow@gmail.com',date:new Date()}];

        $scope.title = "Distributors Controller";
        $scope.predicate = 'name';

        $scope.openDistributor = function (size, distributor) {
            var modalInstance = $modal.open({
                templateUrl: 'addEditDistributorTemplate.html',
                controller: 'ModalPeopleCtrl',
                size: size,
                resolve: {
                    item: function () {
                        if(distributor != null){
                            return distributor;
                        }
                        return {};
                    },
                    title: function () {
                        if(distributor != null){
                            return "Edit Distributor";
                        }
                        return "Add Distributor";
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