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

  function getRatingBlockElement(){
    return $(event.target).parents('.comment-rating-block').children('.post-rating-block');
  }

  function getCommentBlockElement(){
    return $(event.target).parents('.comment-rating-block').children('.comment-block');
  }

  $scope.toggleComments = function(post, event){
    if (post._embedded.comments.length > 0 || $scope.isUserLoggeIn()) {
      getRatingBlockElement().hide(500);
      getCommentBlockElement().slideToggle(500);
    }
    event.preventDefault();
  }

  $scope.toggleRatings = function(post, event){
    if (post.ratings.length > 0) {
      getCommentBlockElement().hide(500);
      getRatingBlockElement().slideToggle(500);
    }
    event.preventDefault();
  }

  $scope.getAvatarUrl = function(){
    return UserService.getAvatarUrl();
  }

}]);
