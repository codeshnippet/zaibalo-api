'use strict';

/* Controllers */

angular.module('myApp.controllers', ['myApp.services']).
  controller('PostsController', ['$http', '$scope', '$routeParams', 'PostService', function($http, $scope, $routeParams, PostService) {
    var PAGE_SIZE = 10;
    $scope.fromIndex = 0;
    $scope.posts = [];
    $scope.postsCount = 0;
    $scope.postsRoot = $routeParams.tag == undefined ? '/posts' : '/posts/hashtag/' + $routeParams.tag;
    $scope.newPost = "";

    $http.get($scope.postsRoot + '/count').
        success(function(data, status, headers, config) {
            $scope.postsCount = data.count;
        });

    $scope.loadPosts = function(){
        $http.get($scope.postsRoot + '?sort=created_at&limit=' + PAGE_SIZE + '&from=' + $scope.fromIndex).
        success(function(posts, status, headers, config) {
            for (var i = 0; i < posts.length; i++) {
                $scope.posts.push(posts[i]);
            }
            $scope.fromIndex = $scope.fromIndex + PAGE_SIZE;
        });
    }

    $scope.addPost = function(posts){
        var json = JSON.stringify({ content : $scope.newPost });

        $.post('/posts', json, function(data) {
            posts.unshift(data);
            $scope.$apply();
        }, 'json');

        this.newPost = "";
        $scope.fromIndex++;
    }

    $scope.deletePost = function(postId, index, event){
      PostService.deletePost(postId, $scope, function(){
        $scope.posts.splice(index, 1);
        $scope.postsCount--;
      });

      event.preventDefault();
    }

    $scope.addComment = function(post){
      PostService.addComment(post, $scope);
    }

    $scope.toggleComments = PostService.toggleComments;
    $scope.translationSufix = PostService.translationSufix;

    $scope.loadPosts();
  }])

  .controller('ProfileController', [function() {

  }])

  .controller('NavigationController', function($translate, $scope) {
    $scope.toggleLanguage = function () {
      var langKey = $translate.use() =='uk_UA' ? 'en_US' : 'uk_UA';
      $translate.use(langKey);
    };
  })

  .controller('SinglePostController', ['$http', '$scope', '$routeParams', 'PostService', function($http, $scope, $routeParams, PostService) {
    $scope.post = null;

    $http.get('/posts/' + $routeParams.postId).
    success(function(post, status, headers, config) {
      $scope.post = post;
    });

    $scope.addComment = function(post){
      PostService.addComment(post, $scope);
    }

    $scope.deletePost = function(postId, index, event){
      PostService.deletePost(postId, $scope, function(){
        $scope.post = null;
      });

      event.preventDefault();
    }

    $scope.toggleComments = PostService.toggleComments;
    $scope.translationSufix = PostService.translationSufix;
  }]);
