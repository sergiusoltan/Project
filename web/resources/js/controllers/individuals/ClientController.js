/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 13.06.2014
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
'use strict';

angular
    .module('mainApp')
    .controller('ClientCtrl', ['$scope', '$location', 'AuthFactory', 'ClientService', '$modal', 'peopleType', function ($scope, $location, AuthFactory, ClientService, $modal, peopleTypes) {

        init();
        $scope.client = {};
        $scope.recomendedBy = {};

        function init(){
            var id = Number($location.path().match(/\/clients\/(\d+)$/)[1]);
            ClientService.getClient(id).then(function(success){
                $scope.client = success;
                if($scope.client.recomendedBy){
                    $scope.recomendedBy = JSON.parse($scope.client.recomendedBy);
                }
            }, function(err){
                alert(err);
            });
        }

    }]);