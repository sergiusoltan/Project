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
    .factory('ContactService', ['$http', '$q', 'API_URL', function ($http, $q, API_URL) {

        var getAllContacts = API_URL.rest + "contact/findAll",
            saveContact = API_URL.rest + "contact/save",
            updateContact = API_URL.rest + "contact/update",
            ContactFactory = {};

        ContactFactory.getAll = function () {
            var deferred = $q.defer();
            $http.get(loginUrl).success(function (success) {
                    deferred.resolve(success);
                },
                function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        ContactFactory.saveUser = function (properties) {
            var deferred = $q.defer();

            $http['post'](saveUserUrl, properties)
                .success(function (success) {
                    deferred.resolve(success);
                }).error(function (error) {
                    deferred.reject(error);
                });

            return deferred.promise;
        };

        ContactFactory.loginUser = function (properties) {
            var deferred = $q.defer();

            $http['post'](loginUserUrl, properties)
                .success(function (success) {
                    deferred.resolve(success);
                }).error(function (error) {
                    deferred.reject(error);
                });

            return deferred.promise;
        };

        return ContactFactory;
}]);