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
    .factory('UserFactory', ['$http', '$q', 'API_URL', function ($http, $q, API_URL) {

    var loginUrl = API_URL.rest + "auth/find",
        saveUserUrl = API_URL.rest + "auth/save",
        loginUserUrl = API_URL.rest + "auth/login",
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

    return UserFactory;
}]);