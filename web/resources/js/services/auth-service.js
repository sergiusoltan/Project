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
    .factory('AuthFactory', ['$rootScope','$timeout', function ($rootScope, $timeout) {
    var currentUser;
    return{
        setUser:function (user) {
            sessionStorage.setItem('loggedUser',JSON.stringify(user));
            currentUser = user;
            $rootScope.$emit(USER_EVENT, true);
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

        showAlert : function(data, time){
            var alertMessage = data;
            try{
                alertMessage = data.replace(/[\[\]]/g,"");
            }catch (e){
                alertMessage = data;
            }
            $rootScope.$emit(USER_ALERT, alertMessage);
            $timeout(function(){
                $rootScope.$emit(USER_ALERT, "");
            },time);
        },

        clear:function(){
            currentUser = {};
            sessionStorage.clear();
            $rootScope.$emit(USER_EVENT, false);
        }
    };
}]);