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
    .factory('MemberService', ['$http', '$q', 'API_URL','$location', function ($http, $q, API_URL, $location) {

        var getAllMembers = API_URL.rest + "member/findAll",
            saveMember = API_URL.rest + "member/save",
            updateMember = API_URL.rest + "member/update",
            deleteMember = API_URL.rest + "member/remove",
            getMember = API_URL.rest + "member/",
            getTrimester = API_URL.rest + "member/trimester",
            uploadUrl = API_URL.rest + "member/url",
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

        MemberFactory.getMember = function (id) {
            var deferred = $q.defer();
            var url = this.getHost() + getMember + id;
            $http.get(url).success(function (success) {
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

        MemberFactory.updateInfo = function (model) {
            var deferred = $q.defer();
            var url = this.getHost() + updateMember;
            $http['post'](url, model)
                .success(function (success) {
                    deferred.resolve(success);
                }).error(function (error) {
                    deferred.reject(error);
                });
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

        MemberFactory.getTrimesterStats = function () {
            var deferred = $q.defer();
            $http.get(getTrimester).success(function (success) {
                deferred.resolve(success);
            }).error(function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        MemberFactory.createUploadUrl = function(){
            var deferred = $q.defer();
            var url = this.getHost() + uploadUrl;
            $http.get(url).success(function (success) {
                deferred.resolve(success);
            }).error(function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        MemberFactory.uploadWithImage = function(item, file){
            var deferred = $q.defer();
            var formData = new FormData();
            formData.append('file', file);
            formData.append('item', JSON.stringify(item.instance));
            $http.post(item.uploadUrl, formData, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            }).success(function (success) {
                    deferred.resolve(success);
                }).error(function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        MemberFactory.getHost = function(){
            var port = $location.port();
            var host = $location.protocol() + "://" + $location.host();
            if(port){
                host = host + ":" + port;
            }
            return host+"/";
        };

        return MemberFactory;
    }]);