/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 10.05.2014
 * Time: 11:07
 * To change this template use File | Settings | File Templates.
 */
angular
    .module('mainApp')
    .controller('HeaderCtrl', ['$scope' , '$location', function ($scope, $location) {

    $scope.title = 'Header Controller';

    $scope.navClass = function (page) {
        var currentRoute = $location.path().substring(1) || '';
        return page === currentRoute ? 'active' : 'innactive';
    };

    $scope.logout = function () {
        alert('logout');
    };

    $scope.$on(LOADING_HEADER_EVENT, function (e, value) {
        $scope.loading.header = value;
    });
    }
]);