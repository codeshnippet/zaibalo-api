'use strict';

angular.module('myApp.controllers')

.controller('TagPostsController', ['PostsService', '$scope', '$routeParams', 'UserService', '$controller',
  function(PostsService, $scope, $routeParams, UserService, $controller) {

    $controller('ParentPostsController', {$scope: $scope});

    $scope.loadPosts('posts/hashtag/' + $routeParams.tag + '?limit=10');

}]);
