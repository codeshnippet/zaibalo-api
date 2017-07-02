'use strict';

angular.module('zabalo-web.controllers')

.controller('RecommendedPostsController', ['$scope', '$controller', '$http',
  function($scope, $controller, $http) {

    $controller('ParentPostsController', {$scope: $scope});

    $http({
      method: 'GET',
      url: 'similarities/max-rec-threshold'
    }).success(function(data){
      $scope.loadPosts('posts/recommended?from=0&limit=10&threshold=' + data.maxRecThreshold);
    });

}]);
