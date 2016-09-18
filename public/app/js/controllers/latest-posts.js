'use strict';

angular.module('myApp.controllers')

.controller('LatestPostsController', ['PostsService', '$scope', 'UserService', '$controller',
  function(PostsService, $scope, UserService, $controller) {

    $controller('ParentPostsController', {$scope: $scope});

    $scope.loadPosts('posts?limit=10');
}]);
