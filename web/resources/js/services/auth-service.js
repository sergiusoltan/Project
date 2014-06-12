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
    .factory('AuthFactory', [function () {
    var currentUser;
    return{
        setUser:function (user) {
            sessionStorage.setItem('loggedUser',JSON.stringify(user));
            currentUser = user;
            console.log('AuthFactory -> setUser currentUser');
            console.log(currentUser);
        },

        getUser:function(){
            var sessionUser = sessionStorage.getItem('loggedUser');
            sessionUser = sessionUser ? JSON.parse(sessionUser) : {};
            console.log('AuthFactory -> getUser sessionUser');
            console.log(sessionUser);
            console.log('AuthFactory -> getUser currentUser');
            console.log(currentUser);
            return currentUser ? currentUser : sessionUser;
        },

        isAuthenticated:function () {
            if(!currentUser){
                currentUser = this.getUser();
            }
            console.log('AuthFactory -> isAuthenticated currentUser');
            console.log(currentUser);
            return (currentUser && currentUser.isLogged) ? true : false;
        },

        clear:function(){
            currentUser = {};
            sessionStorage.clear();
        }
    };
}]);