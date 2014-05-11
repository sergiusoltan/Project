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
            return $http.get(loginUrl);
        };

        UserFactory.saveData = function (User) {
        };

        return UserFactory;
    }]);