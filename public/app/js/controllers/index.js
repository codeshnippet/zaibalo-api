'use strict';

angular.module('myApp.controllers')

.controller('IndexController', ['$http', '$scope', '$routeParams', '$translate',
  function($http, $scope, $routeParams, $translate) {

    $scope.newPost = "";

    $scope.addPost = function(posts){
        var json = JSON.stringify({ content : $scope.newPost });

        $.post('/posts', json, function(data) {
            posts.unshift(data);
            $scope.$apply();
        }, 'json');

        this.newPost = "";
        $scope.fromIndex++;
    }
}]);
