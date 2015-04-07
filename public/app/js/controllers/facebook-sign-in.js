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

        var user = {
          provider: 'FACEBOOK'
        }

        FB.api(
            "/me",
            function (res) {
              if (res && !res.error) {
                user.id = res.id;
                user.displayName = res.name;
                user.email = res.email;
              }
              /* make the API call */
              FB.api(
                  "/me/picture",
                  function (res) {
                    if (res && !res.error) {
                      user.photo = res.data.url;
                    }

                    UserService.loginSocial(user.id, user.provider, user.displayName, user.email, user.photo);
                  }
              );
            }
        );

      }
    });

  };
  // Call start function on load.
  $scope.renderSignInButton();
}]);
