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
        },

        getUser:function(){
            var sessionUser = sessionStorage.getItem('loggedUser');
            sessionUser = sessionUser ? JSON.parse(sessionUser) : {};
            return currentUser ? currentUser : sessionUser;
        },

        isAuthenticated:function () {
            if(!currentUser){
                currentUser = this.getUser();
            }
            return currentUser && currentUser.sessionToken != null;
        },

        getCredentials:function(){
            if(this.isAuthenticated()){
                return 'Basic ' + currentUser.email + ":" + currentUser.sessionToken;
            }
            return '';
        },

        clear:function(){
            currentUser = {};
            sessionStorage.clear();
        }
    };
}]);