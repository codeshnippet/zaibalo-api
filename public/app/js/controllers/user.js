'use strict';

angular.module('myApp.controllers')

.controller('UserController', ['$scope', '$http', '$routeParams', 'Avatar', function($scope, $http, $routeParams, Avatar) {
  $scope.user;

  $scope.getAvatar = function(aUser, size){
    return Avatar(aUser, size);
  }

  $http({
      method: 'GET',
      url: '/users/' + $routeParams.login
    }).success(function(data){
      $scope.user = data;
    });
}]);
