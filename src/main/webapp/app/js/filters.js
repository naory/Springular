'use strict';

/* Filters */

var appFilters = angular.module('myApp.filters',[]);

appFilters.filter('interpolate', ['version', function(version) {
    return function(text) {
        return String(text).replace(/\%VERSION\%/mg, version);
    }
}]);


appFilters.filter('capitalize', [function() {
    return function(text) {
        var s = String(text).toLowerCase();
        return s.charAt(0).toUpperCase() + s.slice(1);
    }
}]);

// filter the 'items' list to a string list of the named property
appFilters.filter('listProperty', [function() {
    return function(items, propName){

        var arrayToReturn = [];
        for (var i=0; i<items.length; i++){
            if (items[i][propName] != undefined) {
                arrayToReturn.push(items[i][propName]);
            }
        }

        return arrayToReturn.toString();
    };
}]);


appFilters.filter('yesNoBoolean', [function() {
    return function(text) {
        var s = String(text).toLowerCase();
        if(s === 'true') return 'Yes';
        else if (s === 'false') return 'No';
        else return s;
    }
}]);