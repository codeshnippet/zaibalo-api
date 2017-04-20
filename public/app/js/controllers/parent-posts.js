'use strict';

angular.module('myApp.controllers')

.controller('ParentPostsController', ['$scope', 'PostsService', 'UserService', 'Avatar',
function($scope, PostsService, UserService, Avatar) {

  $scope.postsResource = [];
  $scope.post = null;

  $scope.deletePost = function(postId, index, event){
    PostsService.deletePost(postId, function(){
      $scope.postsResource._embedded.posts.splice(index, 1);
      $scope.post = null;
    });

    event.preventDefault();
  }

  $scope.addPost = function(){
    PostsService.addPost($scope.newPost, function(post){
      if(typeof $scope.postsResource._embedded == 'undefined'){
        $scope.postsResource._embedded = {
          posts: []
        }
      }
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
    if (post._embedded.ratings._embedded &&
      post._embedded.ratings._embedded.ratings.length > 0) {
      getCommentBlockElement().hide(500);
      getRatingBlockElement().slideToggle(500);
    }
    event.preventDefault();
  }

  $scope.shareOnFacebook = function(post, event){
	  FB.ui({
		  method: 'share',
		  href: 'https://zaibalo-api.herokuapp.com/#/post/' + post.id,
		  description: post.content,
		  picture: $scope.getAvatar(post.author, 'M'),
		  link: 'https://zaibalo-api.herokuapp.com/#/post/' + post.id
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

      if(typeof $scope.postsResource._embedded != 'undefined'){
        $scope.post = $scope.postsResource._embedded.posts[0];
      }
  };

  $scope.loadPosts = function(url){
    PostsService.loadPosts(url, $scope.loadPostsCallback);
  };

  $scope.hasPosts = function(){
    return $scope.postsResource._embedded &&
      $scope.postsResource._embedded.posts &&
      $scope.postsResource._embedded.posts.length > 0;
  };

  $scope.translationSufix = function(number){
    if(number == undefined){
      return '';
    }
    if((number-number%10)%100!=10){
      if(number%10==1){
        return 1;
      } else if(number%10>=2 && number%10<=4){
        return 2;
      } else {
        return 5;
      }
    } else {
      return 5;
    }
  };

  $scope.ratePostUp = function(post, event){
    PostsService.ratePost(post, true);
    event.preventDefault();
  };

  $scope.ratePostDown = function(post, event){
    PostsService.ratePost(post, false);
    event.preventDefault();
  };

}]);
