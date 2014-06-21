/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 10.05.2014
 * Time: 11:07
 * To change this template use File | Settings | File Templates.
 */
angular
    .module('mainApp')
    .controller('HeaderCtrl', ['$scope', '$rootScope' , '$location', function ($scope, $rootScope, $location) {

        $scope.title = 'Header Controller';
        $scope.isLogged = false;

        $scope.navClass = function (page) {
            var currentRoute = $location.path().substring(1) || '';
            return page === currentRoute ? 'active' : 'innactive';
        };

        $rootScope.$on(USER_EVENT, function (e, value) {
            $scope.isLogged = value;
        });

        $scope.$on(LOADING_HEADER_EVENT, function (e, value) {
            $scope.loading.header = value;
        });
    }
    ]);