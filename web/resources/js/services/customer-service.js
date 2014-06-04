'use strict';

angular
    .module('core')
    .factory('CustomerFactory', [function () {
    var customers = [{name:'ion',email:'ion@ion.com'},{name:'ion1',email:'ion1@ion.com'},{name:'ion2',email:'ion2@ion.com'},{name:'ion3',email:'ion3@ion.com'}];
    return{
        getCustomers: function(){
            return customers;
        },

        saveCustomer: function(customer){
            customers.push(customer);
        }

    };
}]);
