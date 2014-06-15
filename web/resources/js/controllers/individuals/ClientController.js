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
            $scope.id = Number($location.path().match(/\/clients\/(\d+)$/)[1]);
            ClientService.getClient($scope.id).then(function(success){
                $scope.client = success;
                if($scope.client.recomendedBy){
                    $scope.recomendedBy = JSON.parse($scope.client.recomendedBy);
                }
            }, function(err){
                alert(err);
            });

            createData();
        }

        $scope.editClient = function () {
            var modalInstance = $modal.open({
                templateUrl: 'updateClientInfo.html',
                controller: 'ModalPeopleCtrl',
                size: '',
                resolve: {
                    items: function () {
                        return null;
                    },
                    item: function () {
                        return $scope.client;
                    },
                    title: function () {
                        return null;
                    }
                }
            });

            modalInstance.result.then(function (item) {
                ClientService.updateInfo(item.instance).then(function (success) {
                    ClientService.getClient($scope.id).then(function(success){
                        $scope.client = success;
                    }, function(error){
                        console.log("failed to get member with" + error );
                    });
                }, function (error) {
                    console.log('failed to update member with ' + error);
                });
            }, function () {
                console.log('modal dismissed');
            });
        };

        function createData(){
            var data = new google.visualization.DataTable();
            data.addColumn('date', 'Date');

            data.addColumn('number', 'Weight');
            data.addColumn('string', 'Weight title');

            data.addColumn('number', 'Fat percent');
            data.addColumn('string', 'Fat percent title');

            data.addColumn('number', 'Muscle Mass');
            data.addColumn('string', 'Muscle Mass title');

            data.addColumn('number', 'IMC');
            data.addColumn('string', 'IMC title');

            data.addColumn('number', 'Mineralization');
            data.addColumn('string', 'Mineralization title');

            data.addColumn('number', 'Metabolic age');
            data.addColumn('string', 'Metabolic age title');

            data.addColumn('number', 'Hydration');
            data.addColumn('string', 'Hydration title');

            data.addColumn('number', 'Visceral fat');
            data.addColumn('string', 'Visceral title');

            data.addRows([
                [new Date(2314, 2, 15),
                    120, 'Weight 120 kg',
                    40, 'Fat 40%',
                    60, 'Muscle Mass 60kg',
                    4, 'IMC 4',
                    15, 'Mineralization 15',
                    45, 'Metabolic Age 45 years',
                    50, 'Hydration 50%',
                    12, 'Visceral Fat 12 kg'],
                [new Date(2314, 3, 15),
                    110, 'Weight 120 kg',
                    30, 'Fat 40%',
                    50, 'Muscle Mass 60kg',
                    3, 'IMC 4',
                    13, 'Mineralization 15',
                    35, 'Metabolic Age 45 years',
                    40, 'Hydration 50%',
                    8, 'Visceral Fat 12 kg'],
                [new Date(2314, 4, 15),
                    90, 'Weight 120 kg',
                    10, 'Fat 40%',
                    30, 'Muscle Mass 60kg',
                    4, 'IMC 4',
                    10, 'Mineralization 15',
                    30, 'Metabolic Age 45 years',
                    35, 'Hydration 50%',
                    8, 'Visceral Fat 12 kg'],
                [new Date(2314, 4, 22),
                    90, 'Weight 120 kg',
                    10, 'Fat 40%',
                    30, 'Muscle Mass 60kg',
                    4, 'IMC 4',
                    10, 'Mineralization 15',
                    30, 'Metabolic Age 45 years',
                    35, 'Hydration 50%',
                    8, 'Visceral Fat 12 kg'],
                [new Date(2314, 4, 29),
                    90, 'Weight 120 kg',
                    8, 'Fat 40%',
                    20, 'Muscle Mass 60kg',
                    1, 'IMC 4',
                    8, 'Mineralization 15',
                    15, 'Metabolic Age 45 years',
                    20, 'Hydration 50%',
                    4, 'Visceral Fat 12 kg']
            ]);

            var options = {
                displayAnnotations: true,
                displayAnnotationsFilter:true,
                thickness:2
            };

            $scope.monitor = {data:data,options:options};
        }

    }]);