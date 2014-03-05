'use strict';

/**
 * Data Grid module
 * directives for the data grid (ng-grid wrapper) and toolbar
 **/


var dataGrid = angular.module('data-grid', ['dynamic-form' /*'ngGrid', 'ajoslin.promise-tracker', 'cgBusy'*/]);


/**
 * bootstrap3 toolbar that tracks the selected item in the grid
 * usage:
 * in the view template use: <div blotterx-toolbar></div>
 * then in the controller configure the buttons to show:
 * $scope.toolbar = {
 *      view: false,
 *      add: true,
 *      edit: false,
 *      export: true,
 *      upload: false,
 *      preferences: true
 *  };
 */
dataGrid.directive('blotterxToolbar', ['$log', function ($log) {
    return {
        restrict: 'A',
        replace: false,
        templateUrl: 'modules/data-grid/button-toolbar.html',
        scope: false,
        controller: 'blotterxCtrl',

        link: function (scope, elem, attrs) {
            if(attrs.configUrl){
                scope.configUrl = attrs.configUrl;
            }

            scope.$on('selectedItemsChanged', function(event, selectedItems) {
                $log.debug('blotterxToolbar:caught selectedItemsChanged event! - new val='+selectedItems);
                scope.selectedItems = selectedItems;
            });
        }
    }}]);


/**
 *  ng-Grid wrapper for server-side paging and filtering and a visual indicator when loading data
 *  usage:
 *  in the view template use: <div blotterx></div>
 *  then in the controller configure ng-Grid's column definitions: $scope.gridColumnDefs = [...]
 */


dataGrid.directive('blotterx', ['serverProxy', '$log', function (serverProxy, $log) {
    return {
        restrict: 'A',
        replace: false,
        template: '<div cg-busy="\'gridTracker\'" class="gridStyle" ng-grid="gridOptions"></div>',
        scope: true, // ng-grid wrapper should use it own (child) scope (see:  https://github.com/angular-ui/ng-grid/issues/206)
        controller: 'blotterxCtrl',

        link: function (scope, elem, attrs) {
            if(attrs.apiUrl){
                scope.apiUrl = attrs.apiUrl;
            }

            // holds the item count in the DB with the current filter applied
            scope.totalServerItems = 0;
            // holds the filter form model
            scope.formModel =  {};
            // holds the sorting and paging settings
            scope.paramObject = { limit: scope.pagingOptions.pageSize, page: scope.pagingOptions.currentPage };


            // Watch for changes in ng-grid's currentPage
            scope.$watch('pagingOptions.currentPage', function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    scope.paramObject.page = newVal;
                    scope.getPagedDataAsync();
                }
            }, true);

            // Watch for changes in ng-grid's pageSize
            scope.$watch('pagingOptions.pageSize', function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    scope.pagingOptions.currentPage = 1;// if pageSize changes - start from first page
                    scope.paramObject.limit = newVal;
                    scope.getPagedDataAsync();
                }
            }, true);

            scope.$watch('sortOptions', function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $log.debug("sortOptions="+angular.toJson(scope.sortOptions.fields));
                    scope.paramObject.sort = newVal.fields[0];
                    scope.paramObject.direction = newVal.directions[0];
                    scope.getPagedDataAsync();
                }
            }, true);

            scope.$watch('selectedItems', function(newVal/*, oldVal*/){
                //$log.debug("selectedItems="+angular.toJson(newVal));
                scope.updateSelectedItems(newVal);
            }, true);


            scope.$on('filterChanged', function(event, filterObj) {
                $log.debug('blotterx: caught filterChanged event! - new val='+angular.toJson(filterObj));
                scope.pagingOptions.currentPage = 1;// if filter changes - start from first page
                scope.getPagedDataAsync(filterObj);
            });

            // Update the paging data for the grid:
            scope.setPagingData = function(data/*, page, pageSize*/){
                scope.myData = data.content;
                scope.totalServerItems = data.totalElements;
                if (!scope.$$phase) {
                    scope.$apply();
                }
            };


            scope.getPagedDataAsync = function (formModel) {
                $log.debug(angular.toJson(scope.paramObject.fields));
                if(formModel) { scope.formModel = formModel; }
                serverProxy.getData(scope.apiUrl, scope.paramObject, scope.formModel, 'gridTracker', function(data){
                    scope.setPagingData(data, scope.pagingOptions.currentPage, scope.pagingOptions.pageSizes);
                });
            };

        }
    }}]);

dataGrid.controller('blotterxCtrl', ['$scope', 'dynamicModalForm',  function($scope, dynamicModalForm) {

    $scope.selectedItems = [];

    $scope.pagingOptions = {
        pageSizes: [10, 25, 50, 198],
        pageSize: 10,
        currentPage: 1
    };

    $scope.sortOptions = {
        columns: [],
        fields: [],
        directions: []
    };

    $scope.gridOptions = {
        data: 'myData',
        columnDefs: $scope.gridColumnDefs, // should be defined in the view's controller
        multiSelect: false,
        selectedItems: $scope.selectedItems,
        enableHighlighting: true, // enable copy of text from grid cells
        enablePaging: true,
        showFooter: true,
        totalServerItems: 'totalServerItems',
        pagingOptions: $scope.pagingOptions,
        sortInfo: $scope.sortOptions,//,
        useExternalSorting: true,
        i18n: "en"
    };

    $scope.updateSelectedItems = function (selectedItems) {
        $scope.$parent.$broadcast("selectedItemsChanged", selectedItems);
    };

    //========  View/Add/Edit Dialog  ========
    $scope.showViewForm = function ($scope) {

        //TODO: implement popup form with disabled controls for 'view-details'
        //dynamicModalForm.openView($scope.configUrl);
    };

    $scope.showAddForm = function(){
        dynamicModalForm.openAdd($scope.configUrl, $scope.apiAddUrl);
    };

    $scope.showEditForm = function(selectedItem){
        dynamicModalForm.openEdit($scope.configUrl, $scope.apiEditUrl, selectedItem);
    };

    $scope.showExportForm = function(){
        dynamicModalForm.openExport();
    }

    $scope.showUploadForm = function(){
        dynamicModalForm.openUpload();
    }
}]);

