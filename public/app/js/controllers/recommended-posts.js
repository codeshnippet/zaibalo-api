'use strict';

angular.module('myApp.controllers')

.controller('RecommendedPostsController', ['PostsService', '$scope', '$routeParams', 'UserService', '$controller',
  function(PostsService, $scope, $routeParams, UserService, $controller) {

    $controller('ParentPostsController', {$scope: $scope});

    $scope.loadPosts('/posts/recommended');

}]);
