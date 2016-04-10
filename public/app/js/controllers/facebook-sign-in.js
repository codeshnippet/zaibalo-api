'use strict';

angular.module('myApp.controllers')

.controller('FacebookSignInController', ['$scope', 'UserService', '$facebook', function($scope, UserService, $facebook) {

  $scope.$on('fb.auth.authResponseChange', function() {
        $scope.status = $facebook.isConnected();
        if($scope.status) {
          $facebook.api('/me?fields=name, picture, email').then(function(user) {
            UserService.loginSocial(user.id, user.email, user.name, user.picture.data.url);
          });
        }
      });
}]);
