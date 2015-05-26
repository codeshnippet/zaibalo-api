'use strict';

angular.module('myApp.controllers')

.controller('SignupController', ['$scope', '$location', 'UserService', function($scope, $location, UserService) {
  $scope.user = {};

  var successCallback = function(){
    var returnTo = $location.search().returnTo ? $location.search().returnTo : '/';
    $location.search({});
    $location.path(returnTo);
    $scope.user = {};
  };

  $scope.registerUser = function(user){
    //TODO
    var errorCallback = function(){
      alert("Registration failed.");
    };

    UserService.registerUser(user.loginName, user.password, successCallback, errorCallback);
    $scope.user = {};
  };

  $scope.login = function(user){
    //TODO
    var errorCallback = function(){
      alert("Login failed. Wrong username/password");
    };

    UserService.login(user.loginName, user.password, successCallback, errorCallback);
  };

}]);
