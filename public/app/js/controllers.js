'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
  controller('PostsController', ['$http', '$scope', '$routeParams', function($http, $scope, $routeParams) {
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
  });
