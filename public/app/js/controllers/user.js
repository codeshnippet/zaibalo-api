'use strict';

angular.module('myApp.controllers')

.controller('UserController', ['$scope', '$http', '$routeParams', function($scope, $http, $routeParams) {
  $scope.user;

  $http({
      method: 'GET',
      url: '/users/' + $routeParams.login
    }).success(function(data){
      $scope.user = data;
    });
}]);
