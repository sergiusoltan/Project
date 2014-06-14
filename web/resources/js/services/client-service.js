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
    .factory('ClientService', ['$http', '$q', 'API_URL', '$location', function ($http, $q, API_URL, $location) {

        var getAllClients = API_URL.rest + "client/findAll",
            saveClient = API_URL.rest + "client/save",
            updateClient = API_URL.rest + "client/update",
            deleteClient = API_URL.rest + "client/remove",
            getClient = API_URL.rest + "client/",
            getTrimester = API_URL.rest + "client/trimester",
            ClientFactory = {};

        ClientFactory.getAllClients = function () {
            var deferred = $q.defer();
            $http.get(getAllClients).success(function (success) {
                deferred.resolve(success);
            }).error(function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        ClientFactory.getClient = function (id) {
            var deferred = $q.defer();
            var url = this.getHost() + getClient + id;
            $http.get(url).success(function (success) {
                deferred.resolve(success);
            }).error(function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };


        ClientFactory.saveOrUpdate = function (properties) {
            var deferred = $q.defer();

            if (properties.isNew) {
                $http['post'](saveClient, properties.instance)
                    .success(function (success) {
                        deferred.resolve(success);
                    }).error(function (error) {
                        deferred.reject(error);
                    });
            } else {
                $http['post'](updateClient, properties.instance)
                    .success(function (success) {
                        deferred.resolve(success);
                    }).error(function (reason) {
                        deferred.reject(reason);
                    });
            }

            return deferred.promise;
        };

        ClientFactory.deleteClients = function (properties) {
            var deferred = $q.defer();
            $http['post'](deleteClient, properties)
                .success(function (success) {
                    deferred.resolve(success);
                }).error(function (error) {
                    deferred.reject(error);
                });

            return deferred.promise;
        };

        ClientFactory.getTrimesterStats = function () {
            var deferred = $q.defer();
            $http.get(getTrimester).success(function (success) {
                deferred.resolve(success);
            }).error(function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        ClientFactory.getHost = function(){
            var port = $location.port();
            var host = $location.protocol() + "://" + $location.host();
            if(port){
                host = host + ":" + port;
            }
            return host+"/";
        };

        return ClientFactory;
    }]);