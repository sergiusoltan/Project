/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 13.06.2014
 * Time: 23:23
 * To change this template use File | Settings | File Templates.
 */
'use strict';

angular
    .module('mainApp')
    .controller('MemberCtrl', ['$scope', '$location', 'AuthFactory', 'MemberService', '$modal', 'peopleType', function ($scope, $location, AuthFactory, MemberService, $modal, peopleTypes) {

        init();
        $scope.member = {};
        $scope.recomendedBy = {};

        function init(){
            var id = Number($location.path().match(/\/members\/(\d+)$/)[1]);
            MemberService.getMember(id).then(function(success){
                $scope.member = JSON.parse(success);
                if($scope.member.recomendedBy){
                    $scope.recomendedBy = JSON.parse($scope.member.recomendedBy);
                }
            }, function(err){
                alert(err);
            });
        }

    }]);