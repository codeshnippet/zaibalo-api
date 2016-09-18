'use strict';

angular.module('myApp.controllers')

.controller('UserController', ['$scope', '$http', '$routeParams', 'Avatar', '$controller', 'PostsService',
function($scope, $http, $routeParams, Avatar, $controller, PostsService) {

  $controller('ParentPostsController', {$scope: $scope});

  $scope.user = {};

  $http({
      method: 'GET',
      url: 'users/' + $routeParams.login
    }).success(function(data){
      $scope.user = data;
  });

  $scope.getAvatar = function(user, size){
    return Avatar(user, size);
  }

  $scope.$watch('user.displayName', function(newVal, oldVal) {
    if (newVal !== oldVal && oldVal !== undefined) {
      $http.put('users/' + $scope.user.id, JSON.stringify($scope.user)).
        success(function(user, status, headers, config) {
          $scope.user = user;
        });
    }
  });

  $scope.loadPosts('users/' + $routeParams.login + '/posts');
}]);
