/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 10.06.2014
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
'use strict';
angular
    .module('mainApp')
    .controller('HierarchyCtrl', ['$scope', 'UserFactory', 'AuthFactory', '$location', '$modal', '$log', function ($scope, UserFactory, AuthFactory, $location, $modal, $log) {
        init();

        function init(){
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Name');
            data.addColumn('string', 'Manager');
            data.addColumn('string', 'ToolTip');
            data.addRows([
                [{v:'Mike', f:'Mike<div style="color:red; font-style:italic">President</div>'}, '', 'The President'],
                [{v:'Jim', f:'Jim<div style="color:red; font-style:italic">Vice President</div>'}, 'Mike', 'VP'],
                ['Alice', 'Mike', ''],
                ['Bob', 'Jim', 'Bob Sponge'],
                ['Carol', 'Bob', '']
            ]);
            $scope.dataTable = data;
        }

        $scope.title = "Hierarchy Controller";
}]);