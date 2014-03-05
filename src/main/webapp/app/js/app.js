'use strict';

// Declare app level module which depends on the listed modules:
var appModule = angular.module('myApp', [

    'authorization', // module for user authorization
    'server-proxy', // module for making server calls
    'data-grid',    // ng-grid based module with server-side filtering, paging and sorting
    'dynamic-form', // dynamic form module

    'ngRoute',  // routing
    'ngAnimate', // angular animations
    'ngSanitize',// safe parsing of HTML from strings

    '$strap.directives', // bootstrap directives

    'ui.bootstrap', // used for modal dialogs
    'ui.bootstrap.datepicker',// date picker module
    'ui.bootstrap.collapse',

    'ajoslin.promise-tracker', // used for 'Loading' message while calling ajax request to load data
    'cgBusy',   // used for 'Loading' message while calling ajax request to load data
    'ngGrid',   // angular-ui-grid controller

    'myApp.filters',
    'myApp.services',
    'myApp.directives',
    'myApp.controllers'
]);


appModule.config(['$routeProvider', '$locationProvider', function($routeProvider) {
    $routeProvider.when('/unauthorized', {
        templateUrl: 'partials/unauthorized.html'
    });

    $routeProvider.when('/users', {
        templateUrl: 'partials/defaultBlotterTemplate.html',
        controller: 'UsersCtrl',
        access:       'USER'
    });

    $routeProvider.when('/roles', {
        templateUrl: 'partials/defaultBlotterTemplate.html',
        controller: 'RolesCtrl',
        access:       'USER'
    });

    $routeProvider.when('/administration', {
        templateUrl: 'partials/administration.html',
//        controller: 'AdministrationCtrl',
        access:       'ADMIN'
    });
    $routeProvider.when('/test', {
        templateUrl: 'partials/defaultBlotterTemplate.html',
        controller: 'TestCtrl',
        access:       'ADMIN'
    });

    $routeProvider.otherwise({redirectTo: '/users'});
}]);
