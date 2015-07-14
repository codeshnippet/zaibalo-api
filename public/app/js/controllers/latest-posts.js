'use strict';

angular.module('myApp.controllers')

.controller('LatestPostsController', ['PostsService', '$scope', 'UserService', '$controller',
  function(PostsService, $scope, UserService, $controller) {

    $controller('ParentPostsController', {$scope: $scope});

    PostsService.getLatestPostsCount(function(count) {
        $scope.postsCount = count;
    });

    $scope.loadPosts = function(){
      PostsService.loadLatestPosts($scope.fromIndex, function(postsResource){
        $scope.postsResource = postsResource;
        $scope.fromIndex = $scope.fromIndex + PostsService.pageSize;
      });
    }

    $scope.loadPosts();
}]);
