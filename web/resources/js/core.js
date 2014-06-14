/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 10.05.2014
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
'use strict';
angular
    .module('core', ['ui.bootstrap', 'ui.bootstrap.modal', 'googleCharts'])
    .value('peopleType', ['Client', 'Contact', 'Member'])
    .value('careerPositions', ['Distributor', 'Senior Consultant', 'Success Builder', 'Qualified Producer', 'Supervisor', 'World Team', 'Active World Team', 'Global Expansion Team', 'Millionaire Team', 'President\'s Team']);

angular
    .module('googleCharts', [])
    .directive('hierarchy', function () {
        return {
            restrict: 'A',
            link: function ($scope, $elm, $attr) {
                var target = $attr.hierarchy;
                $scope.$watch(target, function (value) {
                    var chart = new google.visualization.OrgChart($elm[0]);
                    chart.draw(value, {allowHtml: true, allowCollapse: true, nodeClass: 'hierachyNode', selectedNodeClass: 'hierachyNodeSelected'});
                }, true);
            }
        }
    });

angular
    .module('googleCharts')
    .directive('piechart', function () {
        return {
            restrict: 'A',
            link: function ($scope, $elm, $attr) {
                var target = $attr.piechart;
                $scope.$watch(target, function (value) {
                    var chart = new google.visualization.PieChart($elm[0]);
                    if(value){
                        chart.draw(value.data, value.options);
                    }
                }, true);
            }
        }
    });
