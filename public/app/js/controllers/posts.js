'use strict';

angular.module('myApp.controllers')

.controller('PostsController', ['$http', '$scope', '$routeParams', '$translate', 'Avatar',
  function($http, $scope, $routeParams, $translate, Avatar) {
    var PAGE_SIZE = 10;
    $scope.fromIndex = 0;
    $scope.posts = [];
    $scope.postsCount;
    $scope.postsRoot = '/posts';

    if ($routeParams.tag != undefined) {
      $scope.postsRoot += '/hashtag/' + $routeParams.tag;
    } else if($routeParams.postId != undefined) {
      $scope.postsRoot += '/' + $routeParams.postId;
      $scope.postsCount = 1;
    } else if($routeParams.login != undefined) {
      $scope.postsRoot = '/users/' + $routeParams.login + '/posts';
    }

    if($scope.postsCount == undefined) {
      $http.get($scope.postsRoot + '/count').
        success(function(data, status, headers, config) {
            $scope.postsCount = data.count;
        });
    }

    $scope.getAvatar = function(user, size){
      return Avatar(user, size);
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

    $scope.loadInstagramPosts = function(){
      var endPoint = "https://api.instagram.com/v1/tags/zaibalo/media/recent?client_id=e3c2ae0fb7904532bfa625cb0f272e99&callback=JSON_CALLBACK&min_tag_id=1424964418148688";
  		$http.jsonp(endPoint).success(function(response) {
  			for(var i=0; i<response.data.length; i++) {
  				if (typeof $scope.have[response.data[i].id]==="undefined") {
  					$scope.pics.push(response.data[i]) ;
  					$scope.have[response.data[i].id] = "1";
  				}
  			}
         });
    };

    $scope.deletePost = function(postId, index, event){
      $.delete('/posts/' + postId, '', function(data) {
        $scope.posts.splice(index, 1);
        $scope.postsCount--;
        $scope.$apply();
      }, 'json');

      event.preventDefault();
    }

    $scope.loadPosts();
}]);
