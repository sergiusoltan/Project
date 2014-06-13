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
    .controller('ContactCtrl', ['$scope', '$location', 'AuthFactory', 'ContactService', '$modal', 'peopleType', function ($scope, $location, AuthFactory, ContactService, $modal, peopleTypes) {

        init();
        $scope.contact = {};
        $scope.recomendedBy = {};

        function init(){
            var id = Number($location.path().match(/\/contacts\/(\d+)$/)[1]);
            ContactService.getContact(id).then(function(success){
                $scope.contact = JSON.parse(success);
                if($scope.contact.recomendedBy){
                    $scope.recomendedBy = JSON.parse($scope.contact.recomendedBy);
                }
            }, function(err){
                alert(err);
            });
        }
    }]);