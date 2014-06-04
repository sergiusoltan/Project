'use strict';

angular
    .module('core')
    .factory('OrdersFactory', [function () {
    var customers = [{name:'ion',email:'ion@ion.com'},{name:'ion1',email:'ion1@ion.com'},{name:'ion2',email:'ion2@ion.com'},{name:'ion3',email:'ion3@ion.com'}];
    return{
        getCustomersOrders: function(){
            return customers;
        },

        saveCustomerOrder: function(customer){
            customers.push(customer);
        },

        getAllOrders:function(){

        }

    };
}]);
