'use strict';

angular.module('myApp.controllers')

.controller('TagPostsController', ['PostsService', '$scope', '$routeParams', 'UserService', '$controller',
  function(PostsService, $scope, $routeParams, UserService, $controller) {

    $controller('ParentPostsController', {$scope: $scope});

    PostsService.getPostsByTagCount($routeParams.tag, function(count) {
        $scope.postsCount = count;
    });

    $scope.loadPosts = function(){
      PostsService.loadPostsByTag($routeParams.tag, $scope.fromIndex, function(posts){
        for (var i = 0; i < posts.length; i++) {
            $scope.posts.push(posts[i]);
        }
        $scope.fromIndex = $scope.fromIndex + PostsService.pageSize;
      });
    }

    $scope.loadPosts();

}]);
