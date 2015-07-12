'use strict';

angular.module('myApp.controllers')

.controller('LatestPostsController', ['PostsService', '$scope', 'UserService', '$controller',
  function(PostsService, $scope, UserService, $controller) {

    $controller('ParentPostsController', {$scope: $scope});

    PostsService.getLatestPostsCount(function(count) {
        $scope.postsCount = count;
    });

    $scope.loadPosts = function(){
      PostsService.loadLatestPosts($scope.fromIndex, function(posts){
        for (var i = 0; i < posts.length; i++) {
          if(!posts[i]._embedded){
            posts[i]._embedded = {
              comments :[]
            };
          }
          $scope.posts.push(posts[i]);
        }
        $scope.fromIndex = $scope.fromIndex + PostsService.pageSize;
      });
    }

    $scope.loadPosts();
}]);
