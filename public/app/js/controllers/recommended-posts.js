'use strict';

angular.module('myApp.controllers')

.controller('RecommendedPostsController', ['$scope', '$controller', '$http',
  function($scope, $controller, $http) {

    $controller('ParentPostsController', {$scope: $scope});

    $http({
        method: 'GET',
        url: 'similarities/max-rec-threshold'
      }).success(function(data){
        var mySlider = $("#slider").slider({
          max: data.maxRecThreshold,
          value: 0
        });

        mySlider.on('slideStop', function(event){
          $scope.loadPosts('posts/recommended?from=0&limit=10&threshold=' + event.value);
        });

    });

    $scope.loadPosts('posts/recommended');

}]);
