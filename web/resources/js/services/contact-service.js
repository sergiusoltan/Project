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
            deleteContact = API_URL.rest + "contact/remove",
            getContact = API_URL.rest + "contact/",
            ContactFactory = {};

        ContactFactory.getAllContacts = function () {
            var deferred = $q.defer();
            $http.get(getAllContacts).success(function (success) {
                deferred.resolve(success);
            }).error(function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        ContactFactory.getContact = function (id) {
            var deferred = $q.defer();
            $http.get(getContact+id).success(function (success) {
                deferred.resolve(success);
            }).error(function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        ContactFactory.saveOrUpdate = function (properties) {
            var deferred = $q.defer();

            if (properties.isNew) {
                $http['post'](saveContact, properties.instance)
                    .success(function (success) {
                        deferred.resolve(success);
                    }).error(function (error) {
                        deferred.reject(error);
                    });
            } else {
                $http['post'](updateContact, properties.instance)
                    .success(function (success) {
                        deferred.resolve(success);
                    }).error(function (reason) {
                        deferred.reject(reason);
                    });
            }

            return deferred.promise;
        };

        ContactFactory.deleteContacts = function (properties) {
            var deferred = $q.defer();
            $http['post'](deleteContact, properties)
                .success(function (success) {
                    deferred.resolve(success);
                }).error(function (error) {
                    deferred.reject(error);
                });

            return deferred.promise;
        };

        ContactFactory.getArray = function(success){
                while (!(success instanceof Array)) {
                    success = JSON.parse(success);
                }
                return success;
        };

        return ContactFactory;
    }]);