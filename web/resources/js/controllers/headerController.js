/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 10.05.2014
 * Time: 11:07
 * To change this template use File | Settings | File Templates.
 */
angular
    .module('mainApp')
    .controller('HeaderCtrl', ['$scope', function($scope) {


        $scope.title = 'Header Controller';

        $scope.$on(LOADING_HEADER_EVENT, function (e, value) {
            $scope.loading.header = value;
        });
}]);