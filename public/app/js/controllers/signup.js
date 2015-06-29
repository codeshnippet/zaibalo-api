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
    var errorCallback = function(){
      $scope.registerError = $translate.instant('register.fail');
      $scope.showRegisterError = true;
    };

    UserService.registerUser(user.loginName, user.password, successCallback, errorCallback);
  };

  $scope.login = function(user){
    var errorCallback = function(){
      $scope.loginError = $translate.instant('login.fail');
      $scope.showLoginError = true;
    };

    UserService.login(user.loginName, user.password, successCallback, errorCallback);
  };

}]);
