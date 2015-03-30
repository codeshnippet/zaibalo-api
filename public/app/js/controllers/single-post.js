'use strict';

angular.module('myApp.controllers')

.controller('SinglePostController', ['$scope', '$routeParams', 'Avatar', 'UserService', 'PostsService',
function($scope, $routeParams, Avatar, UserService, PostsService) {

  $scope.post = {};

  PostsService.loadPostById($routeParams.postId, function(post){
    $scope.post = post;
  });

  $scope.isOwner = function(loginName){
    return UserService.isOwner(loginName);
  }

}]);
