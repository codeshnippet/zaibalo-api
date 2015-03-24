'use strict';

angular.module('myApp.controllers')

.controller('SignupController', ['$scope', 'UserService', function($scope, UserService) {
  $scope.user = {};

  $scope.registerUser = function(user){
    UserService.registerUser(user.loginName, user.password);
    $scope.user = {};
  };

  $scope.login = function(user){
    UserService.login(user.loginName, user.password);
    $scope.user = {};
  };
}]);
