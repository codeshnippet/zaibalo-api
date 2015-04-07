'use strict';

angular.module('myApp.controllers')

.controller('SinglePostController', ['$scope', '$routeParams', 'Avatar', 'UserService', 'PostsService',
function($scope, $routeParams, Avatar, UserService, PostsService) {

  $scope.post = null;

  PostsService.loadPostById($routeParams.postId, function(post){
    $scope.post = post;
  });

  $scope.deletePost = function(postId, index, event){
    PostsService.deletePost(postId, function(){
      $scope.post = null;
    });

    event.preventDefault();
  }

  $scope.isOwner = function(loginName){
    return UserService.isOwner(loginName);
  }

}]);
