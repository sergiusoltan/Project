/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 10.05.2014
 * Time: 11:52
 * To change this template use File | Settings | File Templates.
 */
'use strict';

angular
    .module('core')
    .factory('UserFactory', ['$http', '$q', 'API_URL', '$location', function ($http, $q, API_URL, $location) {

        var loginUrl = API_URL.rest + "auth/find",
            saveUserUrl = API_URL.rest + "auth/save",
            loginUserUrl = API_URL.rest + "auth/login",
            logoutUserUrl = API_URL.rest + "auth/logout",
            UserFactory = {};

        UserFactory.getUser = function () {
            var deferred = $q.defer();
            $http.get(loginUrl).success(function (success) {
                    deferred.resolve(success);
                },
                function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        UserFactory.saveUser = function (properties) {
            var deferred = $q.defer();

            $http['post'](saveUserUrl, properties)
                .success(function (success) {
                    deferred.resolve(success);
                }).error(function (error) {
                    deferred.reject(error);
                });

            return deferred.promise;
        };

        UserFactory.loginUser = function (properties) {
            var deferred = $q.defer();

            $http['post'](loginUserUrl, properties)
                .success(function (success) {
                    deferred.resolve(success);
                }).error(function (error) {
                    deferred.reject(error);
                });

            return deferred.promise;
        };

        UserFactory.logoutUser = function (properties) {
            var deferred = $q.defer();
            var url = this.getHost() + logoutUserUrl;
            $http['post'](url, properties)
                .success(function (success) {
                    deferred.resolve(success);
                }).error(function (error) {
                    deferred.reject(error);
                });

            return deferred.promise;
        };

        UserFactory.getHost = function(){
            var port = $location.port();
            var host = $location.protocol() + "://" + $location.host();
            if(port){
                host = host + ":" + port;
            }
            return host+"/";
        };

        return UserFactory;
    }]);