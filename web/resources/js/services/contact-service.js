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
    .factory('ContactService', ['$http', '$q', 'API_URL','$location', function ($http, $q, API_URL, $location) {

        var getAllContacts = API_URL.rest + "contact/findAll",
            saveContact = API_URL.rest + "contact/save",
            updateContact = API_URL.rest + "contact/update",
            deleteContact = API_URL.rest + "contact/remove",
            getContact = API_URL.rest + "contact/",
            getTrimester = API_URL.rest + "contact/trimester",
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
            var url = this.getHost() + getContact + id;
            $http.get(url).success(function (success) {
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

        ContactFactory.getTrimesterStats = function () {
            var deferred = $q.defer();
            $http.get(getTrimester).success(function (success) {
                deferred.resolve(success);
            }).error(function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        ContactFactory.updateInfo = function (model) {
            var deferred = $q.defer();
            var url = this.getHost() + updateContact;
            $http['post'](url, model)
                .success(function (success) {
                    deferred.resolve(success);
                }).error(function (error) {
                    deferred.reject(error);
                });
            return deferred.promise;
        };

        ContactFactory.getHost = function(){
            var port = $location.port();
            var host = $location.protocol() + "://" + $location.host();
            if(port){
                host = host + ":" + port;
            }
            return host+"/";
        };

        return ContactFactory;
    }]);