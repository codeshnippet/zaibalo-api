'use strict';

angular.module('myApp.controllers')

.controller('PostsController', ['$http', '$scope', '$routeParams', 'PostService', '$translate',
  function($http, $scope, $routeParams, PostService, $translate) {
    var PAGE_SIZE = 10;
    $scope.fromIndex = 0;
    $scope.posts = [];
    $scope.postsCount;
    $scope.postsRoot = '/posts';
    $scope.newPost = "";

    if ($routeParams.tag != undefined) {
      $scope.postsRoot += '/tag/' + $routeParams.tag;
    } else if($routeParams.postId != undefined) {
      $scope.postsRoot += '/' + $routeParams.postId;
      $scope.postsCount = 1;
    }

    if($scope.postsCount == undefined) {
      $http.get($scope.postsRoot + '/count').
        success(function(data, status, headers, config) {
            $scope.postsCount = data.count;
        });
    }

    $scope.loadPosts = function(){
        $http.get($scope.postsRoot + '?sort=created_at&limit=' + PAGE_SIZE + '&from=' + $scope.fromIndex).
        success(function(posts, status, headers, config) {
            if(posts.constructor !== Array){
              posts = [posts];
            }
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
      $.delete('/posts/' + postId, '', function(data) {
        $scope.posts.splice(index, 1);
        $scope.postsCount--;
        $scope.$apply();
      }, 'json');

      event.preventDefault();
    }

    $scope.addComment = function(post){
      var json = JSON.stringify({ content : post.newComment });

      $.post('/posts/' + post.id + '/comments', json, function(data) {
          post.comments.push(data);
          $scope.$apply();
      }, 'json');

      post.newComment = "";
    }

    $scope.toggleComments = function(event, target){
      $(target).prev().slideToggle(500);
      event.preventDefault();
    }

    $scope.translationSufix = function(number){
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

    $scope.loadPosts();
}]);
