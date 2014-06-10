/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 10.05.2014
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
'use strict';
angular
    .module('core', ['ui.bootstrap','ui.bootstrap.modal','googleCharts']);

angular
    .module('googleCharts',[])
    .directive('hierarchy', function() {
        return {
            restrict: 'A',
            link: function($scope, $elm, $attr) {
                var target = $attr.hierarchy;
                console.log(target);
                $scope.$watch(target, function(value) {
                    console.log(value);
                    var chart = new google.visualization.OrgChart($elm[0]);
                    chart.draw(value, {allowHtml:true});
                }, true );
            }
        }
    });