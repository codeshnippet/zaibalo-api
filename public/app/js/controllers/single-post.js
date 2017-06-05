'use strict';

angular.module('zabalo-web.controllers')

.controller('SinglePostController', ['$scope', '$routeParams', 'Avatar', 'UserService', 'PostsService', '$controller',
function($scope, $routeParams, Avatar, UserService, PostsService, $controller) {

  $controller('ParentPostsController', {$scope: $scope});

  $scope.loadPosts('posts/' + $routeParams.postId);

}]);
