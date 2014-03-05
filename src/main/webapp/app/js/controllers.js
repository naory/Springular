'use strict';

/* Controllers */
var appControllers = angular.module('myApp.controllers', []);


appControllers.controller('UsersCtrl', ['$scope', function($scope) {

    $scope.blotterName = 'Users';
    // server URL to get the form config JSON:
    $scope.configUrl = '/api/users/form-config';
    // server URL to get the grid data:
    $scope.apiUrl = '/api/users';

    // server URL to add an instance
    $scope.apiAddUrl = '/api/create-user';
    // server URL to edit an instance
    $scope.apiEditUrl = '/api/update-user';


    // define the toolbar functions
    $scope.toolbar = {
        filter: true,
        view: false,
        add: true,
        edit: true,
        export: true,
        upload: true,
        preferences: true
    };


    // take full control over the grid columns here: (See 'ColumnDefs Options' section in: http://angular-ui.github.io/ng-grid/)
    // var myHeaderCellTemplate = "partials/gridHeaderTemplate.html";

    $scope.gridColumnDefs = [
        {field: 'firstName', displayName:'First Name', resizable: true, sortable: true},
        {field: 'lastName', displayName:'Last Name', resizable: true, sortable: true}, //, headerCellTemplate: myHeaderCellTemplate},
        {field: 'userId', displayName:'User ID', resizable: true, sortable: true},
        {field: 'email', displayName:'Email', resizable: true, sortable: true},
        {field: 'phone', displayName:'Phone', resizable: true, sortable: true},
        {field: 'roles', displayName:'Roles', resizable: true, sortable: false, cellFilter: 'listProperty:\'name\''},
        {field: 'active', displayName:'Active', resizable: true, sortable: false, cellFilter: 'yesNoBoolean'},
        {field: 'updateDate', displayName:'Update Date', resizable: true, sortable: true, cellFilter: 'date:\'dd MMM yyyy - hh:mm:ss a\''}
    ];

    // set the default filter values (when resetting the filter form these values will be restored)
    var now = new Date();
    $scope.defaultFilter = {
        "active": "true",
        "updateDate_from": new Date(now.getDate() -7),// 7 days ago
        "updateDate_to": now
    };

}]);



appControllers.controller('RolesCtrl', ['$scope', 'serverProxy', function($scope) {

    $scope.blotterName = 'Roles';
    // server URL to get the form-config:
    $scope.configUrl = '/api/roles/form-config';
    // server URL to get the grid data:
    $scope.apiUrl = '/api/roles';

    // define the toolbar functions
    $scope.toolbar = {
        filter: true,
        view: false,
        add: true,
        edit: false,
        export: true,
        upload: false,
        preferences: true
    };

    $scope.gridColumnDefs = [
        {field: 'id', displayName: 'ID'},
        {field: 'name', displayName: 'Name'},
        {field: 'description', displayName: 'Description'}
    ];

}]);





appControllers.controller('TestCtrl', ['$scope', function($scope) {

    $scope.blotterName = 'Test';
    // server URL to get the form config JSON:
    $scope.configUrl = '/api/test/form-config';
    // server URL to get the grid data:
    $scope.apiUrl = '/api/users';



    // define the toolbar functions
    $scope.toolbar = {
        filter: true,
        view: false,
        add: true,
        edit: true,
        export: true,
        upload: true,
        preferences: true
    };


    $scope.gridColumnDefs = [
        {field: 'firstName', displayName:'First Name', resizable: true, sortable: true},
        {field: 'lastName', displayName:'Last Name', resizable: true, sortable: true}, //, headerCellTemplate: myHeaderCellTemplate},
        {field: 'userId', displayName:'User ID', resizable: true, sortable: true},
        {field: 'roles', displayName:'Roles', resizable: true, sortable: false, cellFilter: 'listProperty:\'name\''},
        {field: 'active', displayName:'Active', resizable: true, sortable: false, cellFilter: 'yesNoBoolean'},
        {field: 'updateDate', displayName:'Update Date', resizable: true, sortable: true, cellFilter: 'date:\'dd MMM yyyy - hh:mm:ss a\''}
    ];

    // set the default filter values (when resetting the filter form these values will be restored)
    var now = new Date();
    $scope.defaultFilter = {
        "active": "true",
        "updateDate_from": new Date(now.getDate() -7),// 7 days ago
        "updateDate_to": now
    };


}]);
