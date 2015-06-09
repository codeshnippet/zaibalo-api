'use strict';

angular.module('myApp.controllers')

.controller('SinglePostController', ['$scope', '$routeParams', 'Avatar', 'UserService', 'PostsService',
function($scope, $routeParams, Avatar, UserService, PostsService) {

  $scope.post = null;

  PostsService.loadPostById($routeParams.postId, function(post){
    $scope.post = post;
  });

  $scope.deletePost = function(post, index, event){
    PostsService.deletePost(post, function(){
      $scope.post = null;
    });

    event.preventDefault();
  }

}]);
