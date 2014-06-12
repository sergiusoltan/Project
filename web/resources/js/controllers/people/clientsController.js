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

        $scope.clients = [
            {id:1,name: 'Jane Doe1', recomendedBy: '', type: CLIENT, phone: 178943332, email: 'janedow4@gmail.com'},
            {id:2,name: 'Jane Doe2', recomendedBy: '', type: CLIENT, phone: 278943332, email: 'janedow3@gmail.com'},
            {id:3,name: 'Jane Doe3', recomendedBy: '', type: CLIENT, phone: 378943332, email: 'janedow2@gmail.com'},
            {id:4,name: 'Jane Doe4', recomendedBy: '', type: CLIENT, phone: 478943332, email: 'janedow1@gmail.com'}
        ];

        $scope.selectedItems = [];

        $scope.onSelect = function (contact) {
            if ($scope.selectedItems[contact.id]) {
                var index = $scope.selectedItems.indexOf(contact.id);
                $scope.selectedItems.splice(index, 1);
                console.log($scope.selectedItems);
                return;
            }
            $scope.selectedItems[contact.id] = true;
            console.log($scope.selectedItems);
        };

        $scope.title = "Clients Controller";
        $scope.predicate = 'name';

        $scope.openClient = function (size, client) {
            var modalInstance = $modal.open({
                templateUrl: 'addEditClientTemplate.html',
                controller: 'ModalPeopleCtrl',
                size: size,
                resolve: {
                    items: function () {
                        var currentUser = AuthFactory.getUser();
                        var items = new Array();
                        items.push($scope.contacts);
                        items.push({id: currentUser.id, name: currentUser.name, email: currentUser.email});
                        return items;
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

            modalInstance.result.then(function (selectedItem) {
                $scope.selected = selectedItem;
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
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

        $scope.ok = function () {
            $modalInstance.close({instance:$scope.item,isNew:$scope.new});
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);