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
        if(hasPosts()){
          for(var i =0; i < postsResource._embedded.posts.length; i++){
            $scope.postsResource._embedded.posts.push(postsResource._embedded.posts[i]);
          }
        } else {
          $scope.postsResource = postsResource;
        }
        $scope.fromIndex = $scope.fromIndex + PostsService.pageSize;
      });
    };

    function hasPosts(){
      return $scope.postsResource._embedded &&
        $scope.postsResource._embedded.posts &&
        $scope.postsResource._embedded.posts.length > 0;
    };

    $scope.loadPosts();
}]);
