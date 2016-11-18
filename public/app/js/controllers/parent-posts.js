'use strict';

angular.module('myApp.controllers')

.controller('ParentPostsController', ['$scope', 'PostsService', 'UserService', 'Avatar', function($scope, PostsService, UserService, Avatar) {

  $scope.postsResource = [];

  $scope.deletePost = function(postId, index, event){
    PostsService.deletePost(postId, function(){
      $scope.postsResource._embedded.posts.splice(index, 1);
    });

    event.preventDefault();
  }

  $scope.addPost = function(){
    PostsService.addPost($scope.newPost, function(post){
      $scope.postsResource._embedded.posts.unshift(post);
    });
    this.newPost = "";
  }

  function getRatingBlockElement(){
    return $(event.target).parents('.comment-rating-block').children('.post-rating-block');
  }

  function getCommentBlockElement(){
    return $(event.target).parents('.comment-rating-block').children('.comment-block');
  }

  $scope.toggleComments = function(post, event){
    if ($scope.hasComments(post) || $scope.isUserLoggeIn()) {
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

  $scope.shareOnFacebook = function(post, event){
	  FB.ui({
		  method: 'share',
		  href: 'http://zaibalo-api.herokuapp.com/#/post/' + post.id,
		  description: post.content,
		  picture: $scope.getAvatar(post.author, 'M'),
		  link: 'http://zaibalo-api.herokuapp.com/#/post/' + post.id
		}, function(response){});
	  event.preventDefault();
  }

  $scope.getAvatar = function(user, size){
      return Avatar(user, size);
    }

  $scope.getAvatarUrl = function(){
	  return UserService.getAvatarUrl();
  }

  $scope.hasComments = function(post){
    return post._embedded && post._embedded.comments && post._embedded.comments.length > 0;
  }

  $scope.loadPostsCallback = function(postsResource){
      if($scope.hasPosts()){
        postsResource._embedded.posts = $scope.postsResource._embedded.posts.concat(postsResource._embedded.posts);
      }
      $scope.postsResource = postsResource;
  };

  $scope.loadPosts = function(url){
    PostsService.loadPosts(url, $scope.loadPostsCallback);
  };

  $scope.hasPosts = function(){
    return $scope.postsResource._embedded &&
      $scope.postsResource._embedded.posts &&
      $scope.postsResource._embedded.posts.length > 0;
  };

}]);
