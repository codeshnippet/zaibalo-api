'use strict';

angular.module('myApp.controllers')

.controller('FacebookSignInController', ['$scope', 'UserService', function($scope, UserService) {

  $scope.renderSignInButton = function() {

    FB.init({
      appId: '1413679762278869',
      channelUrl: 'partial/facebook-channel.html',
      status: true,
      cookie: true,
      xfbml: true
    });

    FB.Event.subscribe('auth.authResponseChange', function(res) {
      if (res.status === 'connected') {
    	UserService.loginSocial(res.authResponse.accessToken, 'FACEBOOK');
      }
    });

  };
  // Call start function on load.
  $scope.renderSignInButton();
}]);
