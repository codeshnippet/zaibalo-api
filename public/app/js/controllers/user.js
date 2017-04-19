'use strict';

angular.module('myApp.controllers')

.controller('UserController', ['$scope', '$http', '$routeParams', 'Avatar', '$controller', 'PostsService',
function($scope, $http, $routeParams, Avatar, $controller, PostsService) {

  $controller('ParentPostsController', {$scope: $scope});

  $scope.user = null;

  $http({
      method: 'GET',
      url: 'users/' + $routeParams.login
    }).success(function(data){
      $scope.user = data;
  });

  $scope.$watch('user.displayName', function(newVal, oldVal) {
    if (newVal !== oldVal && oldVal !== undefined) {
      $http.put($scope.user._links.editUser.href, JSON.stringify($scope.user)).
        success(function(user, status, headers, config) {
          $scope.user = user;
        });
    }
  });

  $scope.loadPosts('users/' + $routeParams.login + '/posts?limit=10');
}]);
