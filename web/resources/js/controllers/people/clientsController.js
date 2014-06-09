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
    .controller('ClientsCtrl', ['$scope', 'UserFactory', 'AuthFactory', '$location', '$modal', '$log', function ($scope, UserFactory, AuthFactory, $location, $modal, $log) {

        $scope.clients = [{name:'Jane Doe1',phone:178943332,email:'janedow4@gmail.com'},
            {name:'Jane Doe2',phone:278943332,email:'janedow3@gmail.com'},
            {name:'Jane Doe3',phone:378943332,email:'janedow2@gmail.com'},
            {name:'Jane Doe4',phone:478943332,email:'janedow1@gmail.com'}];

        $scope.title = "Clients Controller";
        $scope.predicate = 'name';

        $scope.openClient = function (size, client) {
            var modalInstance = $modal.open({
                templateUrl: 'addEditClientTemplate.html',
                controller: 'ModalPeopleCtrl',
                size: size,
                resolve: {
                    item: function () {
                        if(client != null){
                            return client;
                        }
                        return {};
                    },
                    title: function () {
                        if(client != null){
                            return "Edit Client";
                        }
                        return "Add client";
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

angular
    .module('mainApp')
    .controller('ModalPeopleCtrl', ['$scope', '$modalInstance','item','title', function ($scope, $modalInstance, item, title) {

        $scope.title = title;
        $scope.item = item;

        $scope.ok = function () {
            $modalInstance.close($scope.item);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);