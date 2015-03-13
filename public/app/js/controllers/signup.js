'use strict';

angular.module('myApp.controllers')

.controller('SignupController', ['$scope', function($scope) {
  $scope.user = {};

  $scope.registerUser = function(user){
    var json = JSON.stringify(user);
    $.post('/users', json, function(data) {
      alert('Success:'+JSON.stringify(data));
      }, 'json');
      $scope.user = {};
    };
}]);