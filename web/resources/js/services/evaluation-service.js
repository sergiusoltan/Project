'use strict';

angular
    .module('core')
    .factory('EvaluationService', ['$http', '$q', 'API_URL','$location', function ($http, $q, API_URL, $location) {

        var getAllEvaluations = API_URL.rest + "evaluation/findAll/",
            saveEvaluation = API_URL.rest + "evaluation/save/",
            EvaluationService = {};

        EvaluationService.getAllEvaluations = function (id) {
            var deferred = $q.defer();
            var url = this.getHost() + getAllEvaluations + id;
            $http.get(url).success(function (success) {
                deferred.resolve(success);
            }).error(function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        EvaluationService.saveEvaluation = function (id, evaluation) {
            var deferred = $q.defer();
            var url = this.getHost() + saveEvaluation + id;
            $http['post'](url, evaluation).success(function (success) {
                deferred.resolve(success);
            }).error(function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        EvaluationService.getHost = function(){
            var port = $location.port();
            var host = $location.protocol() + "://" + $location.host();
            if(port){
                host = host + ":" + port;
            }
            return host+"/";
        };

        return EvaluationService;
    }]);