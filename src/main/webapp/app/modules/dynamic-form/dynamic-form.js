'use strict';

/**
 * Dynamic Form module
 *
 * Dynamic UI forms for filter/add/edit
 * Based on JSON configuration loaded from the server
 **/


var dynamicForm = angular.module('dynamic-form', ['ui.multiselect', 'ui.bootstrap.datepicker']);

/**
 * factory for dynamic forms as bootstrap3 modal dialogs
 */
dynamicForm.factory('dynamicModalForm', ['serverProxy', 'optionsResolver', '$modal', function (serverProxy, optionsResolver, $modal) {

    var showAddEditForm = function (formConfigUrl, action, actionUrl, selectedItem) {
        console.log("showAddEditForm(" + formConfigUrl + ", " + angular.toJson(selectedItem) + ")");

        optionsResolver.getResolvedConfig(formConfigUrl, {action: action}, function (data) {

            var formConfig = data;
            console.log("got Config: " + angular.toJson(data));

            var modalAddEditDialogOptions = {
                backdrop: 'static',
                keyboard: true,
                resolve: {
                    action: function () {
                        return action;
                    },
                    actionUrl: function () {
                        return actionUrl;
                    },
                    selectedItem: function () {
                        return selectedItem;
                    },
                    formConfig: function () {
                        return formConfig;
                    }
                },
                templateUrl: 'modules/dynamic-form/modal_add_edit.html',
                controller: 'ModalAddEditDialogController'   // see controller definition (below)
            };
            var modalInstance = $modal.open(modalAddEditDialogOptions);
            modalInstance.result.then();
            //TODO modal dialog does not get the focus - see: https://github.com/twbs/bootstrap/issues/3101
        })
    };

    var showExportForm = function(){
        console.log("showExportForm()");

        var modalExportDialogOptions = {
            backdrop: 'static',//true,
            keyboard: true,
//            resolve: {
//                action: function() { return action },
//                selectedItem: function () { return selectedItem; },
//                formConfig: function () { return formConfig; }
//            },
            templateUrl: 'modules/dynamic-form/modal_export.html',
            controller:'ModalExportDialogController'   // see controller definition (below)
        };
        var modalInstance = $modal.open(modalExportDialogOptions);
        modalInstance.result.then();
        //TODO modal dialog does not get the focus - see: https://github.com/twbs/bootstrap/issues/3101
    };



    var showSuccessDialog = function(message){
        console.log("Success!!!");

        var modalSuccessDialogOptions = {
            //scope: $scope,
            backdrop: 'static',//true,
            keyboard: true,
            windowClass: 'dialog-small',
            templateUrl: 'modules/dynamic-form/modal_success.html',
            controller: 'ModalSuccessDialogController',
            resolve: {}
        };
        var modalInstance = $modal.open(modalSuccessDialogOptions);
        return modalInstance.result.then();
    }

    // Factory methods:
    return {

        openAdd: function (formConfigUrl, addUrl) {
            return showAddEditForm(formConfigUrl, "ADD", addUrl, null);
        },

        openEdit: function (formConfigUrl, editUrl, selectedItem) {
            return showAddEditForm(formConfigUrl, "EDIT", editUrl, selectedItem);
        },

        openExport: function () {
            return showExportForm();
        },

        openUpload: function () {
            return showExportForm();
        },

        showSuccessDialog: function(message){
            return showSuccessDialog(message);
        }
    }
}]);

//===============================================================
// $modalInstance controllers:
//===============================================================

dynamicForm.controller('ModalAddEditDialogController', ['$scope', 'serverProxy', '$modalInstance', 'dynamicModalForm', 'action', 'actionUrl', 'selectedItem', 'formConfig', function($scope, serverProxy, $modalInstance, dynamicModalForm, action, actionUrl, selectedItem, formConfig) {
    $scope.action = action;

    $scope.showFormErrors = false;

    console.log("got action: "+$scope.action);
    $scope.formModel = {};
    $scope.formConfig = formConfig;
    if(selectedItem != undefined){ $scope.formModel = selectedItem; }

    //close the $modalInstance
    $scope.close = function(result){
        $modalInstance.close(result);
    };

    var handleSuccessDialogClose = function(){
        if(action === 'ADD'){
            $scope.formModel = {}; // keep parent dialog open and reset the model (allow users to reuse the form to add multiple users)
        }
        else if(action === 'EDIT'){
            $scope.close() // close the parent edit dialog as well
        }
    }

    $scope.save = function(formObject) {
        console.log("formObject.$valid"+ formObject.$valid);
            if(!formObject.$valid){ // if form fails client-side validations - show the errors to the user
            $scope.showFormErrors = true;
        }
        else{ // save the instance:

            formObject.$setPristine();// prevent multiple clicks
            $scope.showFormErrors = false;

            console.log("Saving: "+angular.toJson( $scope.formModel ));
            serverProxy.postData(actionUrl,  $scope.formModel, function(data){

                console.log("got result: "+angular.toJson(data));
                console.log("errors: "+ Object.keys(data.errors).length);

                if(Object.keys(data.errors).length === 0){// no errors
                    dynamicModalForm.showSuccessDialog("Yahoooo!!!!")
                        //same handling on submit and on dismiss
                        .then(handleSuccessDialogClose, handleSuccessDialogClose);


                    // refresh grid data after successful save:
                    $scope.$parent.$broadcast('filterChanged');

                }
                else{// show server validation errors:
                    console.log("server validation errors !!!");
                    $scope.formModel = data;
                }
            });
        }
    }
}]);

dynamicForm.controller('ModalExportDialogController', ['$scope', 'serverProxy', '$modalInstance', function($scope, serverProxy, $modalInstance) {

    $scope.formModel = {};

    //close the $modalInstance
    $scope.close = function(result){
        $modalInstance.close(result);
    };

    $scope.export = function() {

    }
}]);

dynamicForm.controller('ModalSuccessDialogController', ['$scope', '$modalInstance', function($scope, $modalInstance) {

    //close the $modalInstance
    $scope.close = function(result){
        $modalInstance.close(result);
    };
}]);


//===============================================================
// $modalInstance directives:
//===============================================================

// <div date-range/>
dynamicForm.directive('dateRange', [function () {
    return {
        restrict: 'A',
        replace: true, // replace the dateRange <div> element with the template
        template: '<div class="input-daterange well" style="overflow:hidden; padding: 0px;">' +
            '<input id="f_date" type="text" class="form-control input-sm pull-left" style="width: 45%; float: left;" ng-model="formModel[field.name+\'_from\']" datepicker-popup="dd-MM-yyyy"  max="maxDate" show-weeks="false" show-button-bar="true" ng-required="true"/>' +
            '<span style="width: 10%; display: inline-block; text-align: center; align-self: baseline"> to </span>' +
            '<input id="t_date" type="text" class="form-control input-sm" style="width: 45%; float: right;" ng-model="formModel[field.name+\'_to\']" datepicker-popup="dd-MM-yyyy"  min="minDate" show-weeks="false" show-button-bar="true" ng-required="true"/>' +
            '</div>',

        scope: false, // must use own scope - some forms may have multiple date ranges to filter by
        link: function (scope, elem, attrs) {
            // restrict user from selecting invalid range:
            scope.$watch("formModel[field.name+\'_from\']", function(newVal, oldVal){
                if(newVal !== oldVal){
                    console.log("startDate Changed! value = "+newVal);
                    scope.minDate = newVal;
                }
            });

            scope.$watch("formModel[field.name+\'_to\']", function(newVal, oldVal){
                if(newVal !== oldVal){
                    console.log("endDate Changed! value = "+newVal);
                    scope.maxDate = newVal;
                }
            });
        }
    }}]);



// <div dynamic-filter-form config-url="/api/users/form-config"/>
dynamicForm.directive('dynamicFilterForm', ['optionsResolver', function (optionsResolver) {
    return {
        restrict: 'A',
        templateUrl: 'modules/dynamic-form/form-filter.html',
        scope: true, // use own scope
        link: function (scope, elem, attrs) {

            scope.$parent.isCollapsed = true;// load page with the filter-form hidden

            if(attrs.configUrl){
                scope.configUrl = attrs.configUrl;
            }

            // Define local fields config array
            scope.formConfig = {};
            // Define a filter object for URL parameters
            scope.formModel = {};


            // the defaultFilter does not actually change - it is used to reset the filter back to the defaults.
            // but angular calls the $watch on the initial load which is a good place to reset the form and load the data:
            scope.$watch('defaultFilter', function () {
                scope.resetFilter();
            }, true);

            // Clear the filter:
            scope.resetFilter = function(){
                scope.formModel = angular.copy(scope.defaultFilter);
                scope.$parent.filterOn = false;
                scope.$parent.$broadcast('filterChanged', scope.formModel);
                scope.filterReset = true;
            };


            // Filter the data:
            scope.filter = function(){
                if(scope.filterForm.$dirty){
                    scope.$parent.filterOn = true;
                }
                scope.filterForm.$setPristine(); // disable the filter button until some value changes (prevent multiple clicks)
                scope.$parent.$broadcast('filterChanged', scope.formModel);
                scope.filterReset = angular.equals(scope.formModel, scope.defaultFilter);
                console.log("filterReset="+scope.filterReset);
            };

            optionsResolver.getResolvedConfig(scope.configUrl, {action: "FILTER"}, function(data){
                scope.formConfig = data;
                console.log("got Config: "+angular.toJson(data));
            })
        }
    }}]);



// Dynamic form implementations:
// https://github.com/saymedia/angularjs-dynamic-form/blob/master/src/angulardynamicform.js
dynamicForm.directive('smartFormInput', ['$compile', function (compile) {
    return {
        restrict: 'C', // directive as class
        scope : false,
        link: function (scope, element) {

            scope.datepickerPopupConfig = {
                showWeekNumbers: false
            };

            function setUpType() {
                var fieldName = scope.field.name;
                //var t = '<div ng-class="{\'form-group\': true, \'has-error\': formModel.errors[field.name] != undefined}">';
                var t = '<div ng-class="{\'form-group\': true, \'has-error\': (!addEditForm[field.name].$valid && showFormErrors) || (formModel.errors[field.name] != undefined && addEditForm[field.name].$pristine)}">';
                t += '<label class="control-label">{{field.label}}</label>';
                switch (scope.field.inputType) {
                    case 'none':
                        t = '';
                        break;
                    case 'select':
                        t += '<select style="width: 100%" class="form-control input-sm" ng-model="formModel[field.name]" ng-options="o.id as o.name for o in field.options" name="'+ scope.field.name +'" ng-required="'+scope.field.required+'">'
                        t += scope.field.defaultTemplate + '<option value="">select a value</option></select>';
                        t += '</div>';
                        break;
                    case 'multiSelect':
                        t += '<multiselect class="input-xlarge" multiple="true" ng-model="formModel[field.name]" options="o.name for o in field.options" change="selected()" name="'+ scope.field.name +'" ng-required="'+scope.field.required+'"></multiselect>\n';
                        t += '</div>';
                        break;
                    case 'date':
                        t += '<input class="form-control input-sm" type="text" datepicker-popup="dd-MM-yyyy" ng-model="formModel[field.name]" min="minDate" max="maxDate" show-weeks="false" show-button-bar="true" name="'+ scope.field.name +'" ng-required="'+scope.field.required+'" />';
                        t += '</div>';
                        break;
                    case 'dateRange': // used only as a filterType - for inputType use two 'date' fields
                        // using angular-ui-bootstrap
                        t += '<div date-range></div>';
                        t += '</div>';
                        break;
                    case 'text':
                    case 'password':
                    case 'url':
                    case 'number':
                    case 'email':
                        t += '<input type="' + scope.field.inputType + '" ng-model="formModel[field.name]" tooltip="{{formModel.errors[field.name]}}" class="form-control input-sm" name="'+ scope.field.name +'" ng-required="'+scope.field.required+'">';
                        t += '</div>';
                        break;
                    case 'checkbox':
                        t += '<label class="checkbox" style="padding-left: 0px">';
                        t += '<input type="checkbox" class="input-md" style="vertical-align: bottom; margin-left: 0px; padding-left: 0px; margin-right: 10px" ng-model="formModel[field.name]" tooltip="{{formModel.errors[field.name]}}" name="'+ scope.field.name +'" >';
                        t += '{{field.checkboxText}}</label>';
                        t += '</div>';
                        break;
                    case 'radio':
                        t += '<label class="radio" ng-repeat="o.id as o.name for o in field.options">\n' +
                            '<input type="radio" ng-model="formModel[field.name]" value="{{option.id}}" tooltip="{{formModel.errors[field.name]}}"/>{{option.name}}' +
                            '</label>';
                        t += '</div>';
                        break;
                    case 'textarea':
                        t += '<textarea class="form-control" style="resize: none;" rows="3" type="text" ng-model="formModel[field.name]" tooltip="{{formModel.errors[field.name]}}" name="'+ scope.field.name +'" ng-required="'+scope.field.required+'">';
                        t += '</div>';
                        break;
                    case 'custom':
                        t = scope.field.defaultTemplate;
                        break;
                    default: // same as 'none'
                        t = '';
                        break;
                }

                element.html(t);
                compile(element.contents())(scope);
            }

            if (scope.field.name)
                setUpType();
        }
    };
}]);
