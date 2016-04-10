'use strict';

angular.module('myApp.controllers')

.controller('FacebookSignInController', ['$scope', 'UserService', '$facebook', function($scope, UserService, $facebook) {

  $scope.init = function(){
    $facebook.parse();
  };

  $scope.$on('fb.auth.authResponseChange', function() {
    if($facebook.isConnected()) {
      $facebook.api('/me?fields=name, picture, email').then(function(user) {
        UserService.loginSocial(user.id, user.email, user.name, user.picture.data.url);
      });
    }
  });

  $scope.init();

}]);
