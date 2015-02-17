'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
  controller('PostsController', ['$http', '$scope', function($http, $scope) {
    var PAGE_SIZE = 10;
    $scope.fromIndex = 0;
    $scope.posts = [];
    $scope.postsCount = 0;
    $scope.newPost = "";

    $http.get('/posts/count').
        success(function(data, status, headers, config) {
            $scope.postsCount = data.count;
        });

    $scope.loadPosts = function(){
        $http.get('/posts?sort=created_at&limit=' + PAGE_SIZE + '&from=' + $scope.fromIndex).
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
        $scope.fromIndex = $scope.fromIndex + 1;
    }

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
  .controller('SinglePostController', ['$http', '$scope', '$routeParams', function($http, $scope, $routeParams) {
    $scope.post = {};
    $scope.postId = $routeParams.postId;
    $scope.comments = [];
    $scope.newComment = "";

    $http.get('/posts/' + $scope.postId).
        success(function(data, status, headers, config) {
            $scope.post = data.post;
            for (var i = 0; i < data.comments.length; i++) {
                $scope.comments.push(data.comments[i]);
            }
        });

    $scope.addComment = function(comments){
        var json = JSON.stringify({ content : $scope.newComment });

        $.post('/posts/' + $scope.postId + '/comments', json, function(data) {
            comments.push(data);
            $scope.$apply();
        }, 'json');

        this.newComment = "";
    }
  }]);
