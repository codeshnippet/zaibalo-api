'use strict';

angular.module('myApp.controllers')

.controller('UserController', ['$scope', '$http', '$routeParams', 'Avatar', '$controller', 'PostsService',
function($scope, $http, $routeParams, Avatar, $controller, PostsService) {

  $controller('ParentPostsController', {$scope: $scope});

  $scope.user = null;

  $http({
      method: 'GET',
      url: '/users/' + $routeParams.login
    }).success(function(data){
      $scope.user = data;
  });

  $scope.getAvatar = function(user, size){
    return Avatar(user, size);
  }

  $scope.loadPosts('/users/' + $routeParams.login + '/posts');
}]);
