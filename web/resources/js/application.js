/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 10.05.2014
 * Time: 11:03
 * To change this template use File | Settings | File Templates.
 */
'use strict';

/** Module declaration **/
(function(angular) {

    var dependencies = ['ngRoute', 'ngAnimate', 'ngResource','core'];
    angular.module('mainApp', dependencies);

    google.setOnLoadCallback(function() {
        angular.bootstrap(document.body, ['mainApp']);
    });
    google.load('visualization', '1.1', {packages: ['corechart','table','orgchart','annotationchart']});
})(angular);