'use strict';

angular.module('myApp.controllers')

.controller('FacebookSignInController', ['$scope', 'UserService', function($scope, UserService) {

  $scope.renderSignInButton = function() {

    FB.init({
      appId      : '1413679762278869',
      cookie     : true,  // enable cookies to allow the server to access
                          // the session
      xfbml      : true,  // parse social plugins on this page
      version    : 'v2.2' // use version 2.2
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

  $scope.renderSignInButton();
}]);
