/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 10.05.2014
 * Time: 11:07
 * To change this template use File | Settings | File Templates.
 */
angular
    .module('mainApp')
    .controller('HeaderCtrl', ['$scope' , '$location', 'UserFactory', 'AuthFactory', function ($scope, $location, UserFactory, AuthFactory) {

        $scope.title = 'Header Controller';

        $scope.navClass = function (page) {
            var currentRoute = $location.path().substring(1) || '';
            return page === currentRoute ? 'active' : 'innactive';
        };

        $scope.loggedUser = function(){
            return AuthFactory.getUser().isLogged;
        }

        $scope.logout = function () {
            var currentUser = AuthFactory.getUser();
            UserFactory.logoutUser(currentUser).then(function(success){
                AuthFactory.clear();
                $location.path(LOGIN);
            })
        };

        $scope.$on(LOADING_HEADER_EVENT, function (e, value) {
            $scope.loading.header = value;
        });
    }
    ]);