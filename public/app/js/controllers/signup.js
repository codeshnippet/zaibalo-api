'use strict';

angular.module('myApp.controllers')

.controller('SignupController', ['$scope', '$location', 'UserService', function($scope, $location, UserService) {
  $scope.user = {};

  $scope.registerUser = function(user){
    UserService.registerUser(user.loginName, user.password);
    $scope.user = {};
  };

  $scope.login = function(user){
    var successCallback = function(){
      var returnTo = $location.search().returnTo ? $location.search().returnTo : '/';
      $location.search({});
      $location.path(returnTo);
      $scope.user = {};
    };

    var errorCallback = function(){
      alert("Wrong username/password");
    };

    UserService.login(user.loginName, user.password, successCallback, errorCallback);
  };

}]);
