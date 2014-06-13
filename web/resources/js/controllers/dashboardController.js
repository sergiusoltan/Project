'use strict';

angular
    .module('mainApp')
    .controller('DashboardCtrl', ['$scope', 'UserFactory', 'AuthFactory', '$location', function ($scope, UserFactory, AuthFactory, $location) {

        init();

        $scope.img;

        function init(){
            var data = google.visualization.arrayToDataTable([
                ['Task', 'Hours per Day'],
                ['Work',     11],
                ['Eat',      2],
                ['Commute',  2],
                ['Watch TV', 2],
                ['Sleep',    7]
            ]);

            $scope.pieData = data;

            var options = {
                title: 'My Daily Activities',
                is3D: true
            };

            $scope.pieOptions = options;

        }
    }]);