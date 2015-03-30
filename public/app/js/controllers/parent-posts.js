'use strict';

angular.module('myApp.controllers')

.controller('ParentPostsController', ['$scope', 'PostsService', 'UserService', function($scope, PostsService, UserService) {

  $scope.fromIndex = 0;
  $scope.posts = [];
  $scope.postsCount;

  $scope.deletePost = function(postId, index, event){
    PostsService.deletePost(postId, function(){
      $scope.posts.splice(index, 1);
      $scope.postsCount--;
    });

    event.preventDefault();
  }

  $scope.addPost = function(posts){
    PostsService.addPost($scope.newPost, function(post){
      posts.unshift(post);
    });
    this.newPost = "";
    $scope.fromIndex++;
  }

  $scope.isOwner = function(loginName){
    return UserService.isOwner(loginName);
  }

}]);
