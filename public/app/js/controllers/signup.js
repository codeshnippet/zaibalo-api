'use strict';

angular.module('myApp.controllers')

.controller('SignupController', ['$scope', 'UserService', '$translate',
  function($scope, UserService, $translate) {

  $scope.user = {};
  $scope.showLoginError = false;
  $scope.loginError = '';

  $scope.showRegisterError = false;
  $scope.registerError = '';

  var successCallback = function(){
    $scope.user = {};
  };

  $scope.registerUser = function(user){
    var errorCallback = function(message){
      $scope.registerError = $translate.instant(message);
      $scope.showRegisterError = true;
    };

    UserService.registerUser(user.loginName, user.password, successCallback, errorCallback);
  };

  $scope.login = function(user){
    var errorCallback = function(message){
      $scope.loginError = $translate.instant(message);
      $scope.showLoginError = true;
    };

    UserService.login(user.loginName, user.password, successCallback, errorCallback);
  };

}]);
