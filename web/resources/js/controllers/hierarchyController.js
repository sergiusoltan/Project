/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 10.06.2014
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
'use strict';
angular
    .module('mainApp')
    .controller('HierarchyCtrl', ['$scope', 'ContactService', 'AuthFactory', '$location', '$modal', '$log', function ($scope, ContactService, AuthFactory, $location, $modal, $log) {
        $scope.users = [
//            {id:'1',name:'Mike',uri:'distributor/mike.scot',sponsor:'',position:'Supervisor'},
//            {id:'11', name:'Jim',uri:'distributor/mike.scot', sponsor:'1',position:'Supervisor'},
//            {id:'12',name:'Alice',uri:'distributor/mike.scot',sponsor:'11',position:'Supervisor'},
//            {id:'2',name:'Alice',uri:'distributor/mike.scot',sponsor:'1',position:'Distributor'},
//            {id:'21',name:'Bob',uri:'client/mike.scot',sponsor:'2',position:'Client'},
//            {id:'22',name:'Michael',uri:'distributor/mike.scot',sponsor:'2',position:'Contact'},
//            {id:'3',name:'Michael',uri:'contact/mike.scot',sponsor:'1',position:'Contact'},
//            {id:'31',name:'Eliot',uri:'contact/mike.scot',sponsor:'3',position:'Contact'},
//            {id:'32',name:'Elena',uri:'contact/mike.scot',sponsor:'3',position:'Contact'}
        ];

        init();

        $scope.$watch('selected', function(value) {
            var root = value;
            if(!root.name){
                root = JSON.parse(value);
            }
            initData(root);
        });

        function init(){
            $scope.title = "Hierarchy Controller";
            ContactService.getAllContacts().then(function (success) {
                console.log('get All contacts contact');
                $scope.users = ContactService.getArray(success);
                console.log($scope.users);
                $scope.selected = getSelectedRoot();
                initData($scope.selected);
            }, function (error) {
                console.log('error loading contacts');
            });
        }

        function getSelectedRoot(){
            var selectedItem = $scope.users[0];
            $scope.users.some(function(value){
                if(!value.recomendedBy){
                    selectedItem = value;
                    return true;
                }
                return false;
            });
            return selectedItem;
        }

        function initData(root){
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Name');
            data.addColumn('string', 'Manager');
            data.addColumn('string', 'ToolTip');
            var rows = [];
            if(root){
                traverse(root);
            }
            data.addRows(rows);
            $scope.dataTable = data;

            function traverse(root) {
                var nextLevelClients = [],
                    sponsors = [];
                rows.push([getFormattedName(root),null,root.type]);
                sponsors.push(root.id);
                nextLevelClients = findDirect(sponsors);
                while(sponsors.length){
                    sponsors = [];
                    $.each(nextLevelClients, function (index, client) {
                        sponsors.push(client.id);
                        rows.push([getFormattedName(client), client.recomendedById + '', client.type]);
                    });
                    nextLevelClients = findDirect(sponsors);
                }
            }
        }

        function getFormattedName(node){
            return {
                v:node.id + "",
                f:"<div class='"+node.type+"'>"+ node.name +"</br><a href=\"/"+node.type.toLowerCase()+"s/"+node.id+"\">"+node.type+"</a></div>"
            }
        }

        function findDirect(ids){
            var foundClients = $.grep($scope.users, function(client) {
                if(client.recomendedById){
                    return ids.indexOf(client.recomendedById) != -1;
                }
                return false;
            });
            return foundClients;
        }
}]);