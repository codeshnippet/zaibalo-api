'use strict';

angular.module('myApp.controllers')

.controller('SinglePostController', ['$scope', '$routeParams', 'Avatar', 'UserService', 'PostsService',
function($scope, $routeParams, Avatar, UserService, PostsService) {

  $scope.post = null;

  PostsService.loadPosts('/posts/' + $routeParams.postId, function(post){
    $scope.post = post;
  });

  $scope.deletePost = function(post, index, event){
    PostsService.deletePost(post, function(){
      $scope.post = null;
    });

    event.preventDefault();
  }

  function getRatingBlockElement(){
    return $(event.target).parents('.comment-rating-block').children('.post-rating-block');
  }

  function getCommentBlockElement(){
    return $(event.target).parents('.comment-rating-block').children('.comment-block');
  }

  $scope.getAvatarUrl = function(){
    return UserService.getAvatarUrl();
  }

}]);
