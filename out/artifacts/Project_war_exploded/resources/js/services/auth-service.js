/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 11.05.2014
 * Time: 17:11
 * To change this template use File | Settings | File Templates.
 */
'use strict';

angular
    .module('core')
    .factory('AuthFactory', ['UserFactory',function () {
        var currentUser;
        return{
            setUser:function(user){
                currentUser = user;
            },

            isAuthenticated:function(){
                return (currentUser && currentUser.isLogged) ? true : false;
            }
        }
    }]);