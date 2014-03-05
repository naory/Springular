'use strict';

/**
 * authorization module:
 * read user credentials on application load from a server cookie and stores in the $rootScope.
 * provides simple way to show elements based on the user's role: ng-show="isAuthorized('ROLE_NAME')"
 * checks routes for role access.
 */

var authModule = angular.module('authorization',['ngCookies']);


authModule.run(['$rootScope', '$location', '$cookieStore', '$log',  function ($rootScope, $location, $cookieStore, $log) {

    // define currentUser at the $rootScope level
    $rootScope.currentUser = {};

    // get the 'USER' cookie:
    var userJson = $cookieStore.get('USER');
    $log.debug("CurrentUser: " + userJson);
    if (userJson === undefined) {
        $log.error("Expected 'USER' cookie missing!!!!");
    }
    else {
        // store the user credentials and delete the cookie:
        $rootScope.currentUser = angular.fromJson(userJson);
        $cookieStore.remove('USER');
    }

    // to display any element only for a specific role use: ng-show="isAuthorized('ROLE_NAME')"
    $rootScope.isAuthorized = function (access) {
        return ($rootScope.currentUser.roles.indexOf(access) != -1);
    };


    //add handler for the $routeChangeStart event and check the route's access field
    $rootScope.$on("$routeChangeStart", function (event, next/*, current*/) {
        $log.debug("next route requires access: " + next.access);
        if (next.access) {// if route has access restriction:
            if (!$rootScope.isAuthorized(next.access)) {// if user is not authorized redirect to '/unauthorized'
                $log.error("unauthorized!");
                $location.path("/unauthorized");
            }
        }
    });


}]);
