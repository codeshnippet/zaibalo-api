'use strict';

angular.module('myApp.controllers')

.controller('IndexController', ['$http', '$scope', '$routeParams', '$translate',
  function($http, $scope, $routeParams, $translate) {

    $scope.newPost = "";

    $scope.addPost = function(posts){
      $http.post('/posts', JSON.stringify({ content : $scope.newPost })).
        success(function(data, status, headers, config) {
          posts.unshift(data);
        });

        this.newPost = "";
        $scope.fromIndex++;
    }
}]);
