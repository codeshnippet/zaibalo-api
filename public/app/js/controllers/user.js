'use strict';

angular.module('myApp.controllers')

.controller('UserController', ['$scope', '$http', '$routeParams', 'Avatar', '$controller', 'PostsService',
function($scope, $http, $routeParams, Avatar, $controller, PostsService) {

  $controller('ParentPostsController', {$scope: $scope});

  $scope.user;

  $http({
      method: 'GET',
      url: '/users/' + $routeParams.login
    }).success(function(data){
      $scope.user = data;
  });

  PostsService.getPostsByUserCount($routeParams.login, function(count) {
    $scope.postsCount = count;
  });

  $scope.loadPosts = function(){
    PostsService.loadPostsByUser($routeParams.login, $scope.fromIndex, function(posts){
      for (var i = 0; i < posts.length; i++) {
        $scope.posts.push(posts[i]);
      }
      $scope.fromIndex = $scope.fromIndex + PostsService.pageSize;
    });
  }

  // $scope.getAvatar = function(user, size){
  //   return Avatar(user, size);
  // }

  $scope.loadPosts();
}]);
