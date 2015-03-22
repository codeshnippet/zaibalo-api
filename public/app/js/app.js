'use strict';

// Declare app level module which depends on filters, and services
var app = angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives', 'myApp.controllers', 'pascalprecht.translate', 'ngSanitize', 'bd.timedistance']);

angular.module('myApp.services', []);

angular.module('myApp.controllers', []);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when('/', {
      templateUrl: 'partials/index.html',
      controller: 'IndexController'
      })
    .when('/tag/:tag', {
      templateUrl: 'partials/index.html',
      controller: 'IndexController'
      })
    .when('/post/:postId', {
      templateUrl: 'partials/posts.html',
      controller: 'PostsController'
      })
    .when('/@:login', {
      templateUrl: 'partials/user.html',
      controller: 'UserController'
      })
    .when('/signup', {
      templateUrl: 'partials/signup.html',
      controller: 'SignupController'
      })
    .otherwise({redirectTo: '/'});
}]);

app.config(function ($translateProvider) {
    $translateProvider.useStaticFilesLoader({
      prefix: 'app/messages/locale-',
      suffix: '.json'
    });
    $translateProvider.use('uk_UA');
});

app.run(['$rootScope', '$window',
  function($rootScope, $window) {

  $rootScope.user = {};

  $window.fbAsyncInit = function() {

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
                user.clientId = res.id;
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
                    var json = JSON.stringify(user);
                    $.post('/oauth-login', json, function(data) {
                        saveAuthValues(data.user.loginName, data.token);
                    }, 'json');
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
