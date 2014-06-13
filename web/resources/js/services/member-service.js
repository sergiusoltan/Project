/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 12.06.2014
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
'use strict';

angular
    .module('core')
    .factory('MemberService', ['$http', '$q', 'API_URL', function ($http, $q, API_URL) {

        var getAllMembers = API_URL.rest + "member/findAll",
            saveMember = API_URL.rest + "member/save",
            updateMember = API_URL.rest + "member/update",
            deleteMember = API_URL.rest + "member/remove",
            MemberFactory = {};

        MemberFactory.getAllMembers = function () {
            var deferred = $q.defer();
            $http.get(getAllMembers).success(function (success) {
                deferred.resolve(success);
            }).error(function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        MemberFactory.saveOrUpdate = function (properties) {
            var deferred = $q.defer();

            if (properties.isNew) {
                $http['post'](saveMember, properties.instance)
                    .success(function (success) {
                        deferred.resolve(success);
                    }).error(function (error) {
                        deferred.reject(error);
                    });
            } else {
                $http['post'](updateMember, properties.instance)
                    .success(function (success) {
                        deferred.resolve(success);
                    }).error(function (reason) {
                        deferred.reject(reason);
                    });
            }

            return deferred.promise;
        };

        MemberFactory.deleteMembers = function (properties) {
            var deferred = $q.defer();
            $http['post'](deleteMember, properties)
                .success(function (success) {
                    deferred.resolve(success);
                }).error(function (error) {
                    deferred.reject(error);
                });

            return deferred.promise;
        };

        MemberFactory.getArray = function(success){
            while (!(success instanceof Array)) {
                success = JSON.parse(success);
            }
            return success;
        };

        return MemberFactory;
    }]);