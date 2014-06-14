'use strict';

angular
    .module('mainApp')
    .controller('DashboardCtrl', ['$scope', 'ContactService', 'MemberService', 'ClientService', '$location',
        function ($scope, ContactService, MemberService, ClientService, $location) {

        init();

        function init(){

            ClientService.getTrimesterStats().then(function (success) {
                initClientStats(success);
            }, function (error) {
                console.log('error loading contacts');
            });

            ContactService.getTrimesterStats().then(function (success) {
                initContactStats(success);
            }, function (error) {
                console.log('error loading contacts');
            });

            MemberService.getTrimesterStats().then(function (success) {
                initMemberStats(success);
            }, function (error) {
                console.log('error loading contacts');
            });
        }

            function initMemberStats(array){
                var data = new google.visualization.DataTable();
                data.addColumn('string', 'Topping');
                data.addColumn('number', 'Slices');
                var rows = createRows(array);
                data.addRows(rows);
                var options = {
                    title: 'New Members',
                    is3D: true
                };
                $scope.membersData = {data: data, options: options};
            }

            function initContactStats(array){
                var data = new google.visualization.DataTable();
                data.addColumn('string', 'Topping');
                data.addColumn('number', 'Slices');
                var rows = createRows(array);
                data.addRows(rows);
                var options = {
                    title: 'New Contacts',
                    is3D: true
                };
                $scope.contactsData = {data: data, options: options};
            }

            function initClientStats(array){
                var data = new google.visualization.DataTable();
                data.addColumn('string', 'Topping');
                data.addColumn('number', 'Slices');
                var rows = createRows(array);
                data.addRows(rows);
                var options = {
                    title: 'New Clients',
                    is3D: true
                };
                $scope.clientsData = {data: data, options: options};
            }

            function createRows(dataRows) {
                var rows = [];
                $.each(dataRows, function (index, value) {
                    rows.push([value.month, value.number]);
                });

                return rows;
            }
    }]);