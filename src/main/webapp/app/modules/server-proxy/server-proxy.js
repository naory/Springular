'use strict';

/** 
* Server Proxy Service 
* 
* a wrapper around $http that provides:
 * 1. getConfig     - a convenience function for getting config JSON (cached by default)
 * 2. getData       - a convenience function for getting data JSON (never cached)
 * 3. postData      - a convenience function for POSTing JSON data (never cached)
 * 4. a common way to handle errors by displaying the server error in a modal dialog.
**/


var serverProxyService = angular.module('server-proxy', ['ngSanitize']);


serverProxyService.factory('serverProxy', ['$http', '$modal','$log', function ($http, $modal, $log) {

    var defaultTimeout = 5000;// milliseconds

    function httpCall(method, url, paramsObj, dataObj, cache, tracker, successHandler) {
        $log.debug("calling URL="+url+" \nparams: "+ angular.toJson(paramsObj)+ "\nobject: "+angular.toJson(dataObj));
        return $http({method: method, url: url, params: paramsObj, data: dataObj, cache: cache, tracker: tracker, timeout: defaultTimeout})
            .success(function (data) {
                return successHandler(data); })
            .error(function (data, status, headers, config) {
                console.log("Error calling $http."+method+"('"+url+"') : status="+ status + " data="+data+ " config="+angular.toJson(config));
                if(status === 0){ data = "Request Timeout"}
                var modalErrorDialogOptions = {
                    backdrop: 'static',
                    keyboard: true,
                    resolve: {
                        serverError: function () {
                            return {
                                title:      "Server Error: "+ status,
                                message:    data
                            };
                        }
                    },
                    templateUrl: 'modules/server-proxy/modal_error.html',
                    controller:'ModalErrorDialogController'
                };

                var $modalInstance = $modal.open(modalErrorDialogOptions);
                $modalInstance.result.then();
            })
    }

    return {
        getConfig: function (url, paramsObj, successHandler) {
            return httpCall('GET',url, paramsObj, null, true, null, successHandler);
        },
        getData: function(url, paramsObj, dataObj, tracker, successHandler){
            return httpCall('POST', url, paramsObj, dataObj, false, tracker, successHandler);
        },
        postData: function(url, dataObj, successHandler){
            return httpCall('POST', url, null, dataObj, false, null, successHandler);
        }
    };
}]);

serverProxyService.controller('ModalErrorDialogController', ['$scope', '$modalInstance', '$sanitize', 'serverError', function($scope, $modalInstance, $sanitize, serverError) {
    $scope.serverError = serverError;
    $scope.htmlString = $sanitize(serverError.message);
    //close the $modalInstance
    $scope.close = function(result){
        $modalInstance.close(result);
    };

}]);


serverProxyService.factory('optionsResolver', ['serverProxy', '$q', '$log', function (serverProxy, $q, $log) {

    // get the array of promises for all occurrences of optionsUrl in the config fields
    var getOptionPromisses = function(configData){
        var promises = [];
        for (var i = 0; i < configData.fields.length; i++) {
            if(configData.fields[i].optionsUrl){
                // replace the optionsUrl with the resolved options:
                $log.debug("Creating promise for URL: " + configData.fields[i].optionsUrl);
                var promise = serverProxy.getConfig(configData.fields[i].optionsUrl, null, function(optionsData){
                    return optionsData;
                });
                promises.push(promise);
            }
        }
        return promises;
    };

    var replaceUrlsWithOptions = function(configData, optionsArray){
        var j = 0;
        for (var i = 0; i < configData.fields.length; i++) {
            if(configData.fields[i].optionsUrl){
                // replace the optionsUrl with the resolved options:
                $log.debug("Setting "+configData.fields[i].map+" to "+ angular.toJson(optionsArray[j].data));
                configData.fields[i].options = optionsArray[j].data;
                configData.fields[i].optionsUrl = undefined;
                j++;
            }
        }
        return configData;
    };


    // resolves all occurrences of 'optionsUrl' to the actual options array.
    // resolvedConfigHandler will be called with the resolved config data.
    var resolveOptions = function (configData, resolvedConfigHandler) {
        var promises = getOptionPromisses(configData);

        $q.all(promises).then(function (results) {
            $log.debug("Options results: "+angular.toJson(results));
            // this block gets executed once all futures are resolved
            var resolvedConfigData = replaceUrlsWithOptions(configData, results);
            resolvedConfigHandler(resolvedConfigData);
        });

    };


    return{
        getResolvedConfig: function (formConfigUrl, paramObj, resolvedConfigHandler) {
            serverProxy.getConfig(formConfigUrl, paramObj, function (data) {
                resolveOptions(data, resolvedConfigHandler);
            });
        }
    };
}]);