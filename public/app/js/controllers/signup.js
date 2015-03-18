'use strict';

angular.module('myApp.controllers')

.controller('SignupController', ['$scope', function($scope) {
  $scope.user = {};

  $scope.registerUser = function(user){
    var json = JSON.stringify(user);
    $.post('/users', json, function(data) {
      saveAuthValues(data.user.loginName, data.token);
      }, 'json');
      $scope.user = {};
    };

  $scope.login = function(user){
    var json = JSON.stringify(user);
    $.post('/login', json, function(data) {
      saveAuthValues(data.user.loginName, data.token);
      }, 'json');
      $scope.user = {};
  };
}]);
