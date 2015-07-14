'use strict';

angular.module('myApp.controllers')

.controller('TagPostsController', ['PostsService', '$scope', '$routeParams', 'UserService', '$controller',
  function(PostsService, $scope, $routeParams, UserService, $controller) {

    $controller('ParentPostsController', {$scope: $scope});

    PostsService.getPostsByTagCount($routeParams.tag, function(count) {
        $scope.postsCount = count;
    });

    $scope.loadPosts = function(){
      PostsService.loadPostsByTag($routeParams.tag, $scope.fromIndex, function(postsResource){
        $scope.postsResource = postsResource;
        $scope.fromIndex = $scope.fromIndex + PostsService.pageSize;
      });
    }

    $scope.loadPosts();

}]);
