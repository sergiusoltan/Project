/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 16.06.2014
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */
'use strict';

angular
    .module('core')
    .factory('FileReaderService', ['$q', '$http', 'API_URL', function ($q, $http, API_URL) {

        var getUploadUrl = API_URL.rest + "uploads/url",
            getAllProductsUrl = API_URL.rest + "uploads/getAll",
            FileReaderService = {};

        var onLoad = function(reader, deferred, scope) {
            return function () {
                scope.$apply(function () {
                    deferred.resolve(reader.result);
                });
            };
        };
        var onError = function (reader, deferred, scope) {
            return function () {
                scope.$apply(function () {
                    deferred.reject(reader.result);
                });
            };
        };
        var onProgress = function(reader, scope) {
            return function (event) {
                scope.$broadcast("fileProgress",
                    {
                        total: event.total,
                        loaded: event.loaded
                    });
            };
        };
        var getReader = function(deferred, scope) {
            var reader = new FileReader();
            reader.onload = onLoad(reader, deferred, scope);
            reader.onerror = onError(reader, deferred, scope);
            reader.onprogress = onProgress(reader, scope);
            return reader;
        };
        var readAsDataURL = function (file, scope) {
            var deferred = $q.defer();

            var reader = getReader(deferred, scope);
            reader.readAsDataURL(file);

            return deferred.promise;
        };

        FileReaderService.readFile = function(file,scope){
            return readAsDataURL(file,scope);
        };

        FileReaderService.createUploadUrl = function(){
            var deferred = $q.defer();
            $http.get(getUploadUrl).success(function (success) {
                deferred.resolve(success);
            }).error(function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        FileReaderService.getAllProducts = function(){
            var deferred = $q.defer();
            $http.get(getAllProductsUrl).success(function (success) {
                deferred.resolve(success);
            }).error(function (reason) {
                    deferred.reject(reason);
                });
            return deferred.promise;
        };

        FileReaderService.submitProduct = function(item, file){
            var deferred = $q.defer();
            var formData = new FormData();
            formData.append('file', file);
            formData.append('item', JSON.stringify(item));
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


        FileReaderService.getHost = function(){
            var port = $location.port();
            var host = $location.protocol() + "://" + $location.host();
            if(port){
                host = host + ":" + port;
            }
            return host+"/";
        };

        return FileReaderService;
    }]);