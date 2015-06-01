'use strict';

angular.module('myApp.controllers')

.controller('SignupController', ['$scope', '$location', 'UserService', '$translate',
  function($scope, $location, UserService, $translate) {

  $scope.user = {};
  $scope.showLoginError = false;
  $scope.loginError = 'dd';

  var successCallback = function(){
    var returnTo = $location.search().returnTo ? $location.search().returnTo : '/';
    $location.search({});
    $location.path(returnTo);
    $scope.user = {};
  };

  $scope.registerUser = function(user){
    var errorCallback = function(){
      alert("error");
    };

    UserService.registerUser(user.loginName, user.password, successCallback, errorCallback);
    $scope.user = {};
  };

  $scope.login = function(user){
    var errorCallback = function(){
      $scope.showLoginError = true;
      $scope.loginError = $translate('login.fail').value;
    };

    UserService.login(user.loginName, user.password, successCallback, errorCallback);
  };

}]);
