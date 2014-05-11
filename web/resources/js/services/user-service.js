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

        var loginUrl = API_URL + "login", UserFactory = {};

        UserFactory.getUser = function () {
            var deferred = $q.defer();
            $http.get(loginUrl).success(function(success){
                deferred.resolve(success);
            },
            function(reason){
                deferred.reject(reason);
            });
            return deferred.promise;
        };

        UserFactory.saveData = function (User) {
        };

        return UserFactory;
    }]);