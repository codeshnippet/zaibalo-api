'use strict';

angular.module('myApp.controllers')

.controller('FacebookSignInController', ['$rootScope', '$window', 'UserService', function($rootScope, $window, UserService) {

  $rootScope.user = {};

  FB.init({
    appId: '1413679762278869',
    channelUrl: 'partial/facebook-channel.html',
    status: true,
    cookie: true,
    xfbml: true
  });

  $window.fbAsyncInit = function() {

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
      else {
        //logout and remove cookies;
      }

  });


  };

  (function(d){
    // load the Facebook javascript SDK

    var js,
    id = 'facebook-jssdk',
    ref = d.getElementsByTagName('script')[0];

    if (d.getElementById(id)) {
      return;
    }

    js = d.createElement('script');
    js.id = id;
    js.async = true;
    js.src = "//connect.facebook.net/en_US/all.js";

    ref.parentNode.insertBefore(js, ref);

  }(document));
}]);
