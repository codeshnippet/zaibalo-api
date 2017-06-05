'use strict';

// Declare app level module which depends on filters, and services
var app = angular.module('zabalo-web', ['zabalo-web.filters', 'zabalo-web.services',
'zabalo-web.directives', 'zabalo-web.controllers', 'pascalprecht.translate',
'ngSanitize', 'bd.timedistance', 'ngRoute', 'ngCookies', 'angular-loading-bar',
'ngFacebook', 'xeditable'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when('/', {
      templateUrl: 'partials/posts-block.html',
      controller: 'LatestPostsController'
      })
    .when('/tag/:tag', {
      templateUrl: 'partials/posts.html',
      controller: 'TagPostsController'
      })
    .when('/post/:postId', {
      templateUrl: 'partials/single-post.html',
      controller: 'SinglePostController'
      })
    .when('/@:login', {
      templateUrl: 'partials/user.html',
      controller: 'UserController'
      })
    .when('/signup', {
      templateUrl: 'partials/signup.html',
      controller: 'SignupController'
      })
    .when('/recommended', {
      templateUrl: 'partials/posts.html',
      controller: 'RecommendedPostsController'
      })
    .otherwise({redirectTo: '/'});
}])

.config(function ($translateProvider) {
    $translateProvider.useStaticFilesLoader({
      prefix: 'messages/locale-',
      suffix: '.json'
    });
    $translateProvider.use('uk_UA');
})

.config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('tokenInjector');
    $httpProvider.interceptors.push('authHttpResponseInterceptor');
}])

.config( function( $facebookProvider ) {
  $facebookProvider.setAppId(228511024151460)
  .setPermissions(['email','public_profile'])
  .setCustomInit({
    channelUrl : 'partial/facebook-channel.html',
    xfbml      : true,
    status     : true,
    cookie     : true,
    xfbml      : true
  })
  .setVersion("v2.2");
})

.run( function( $rootScope ) {
  (function(d){

    var js,
    id = 'facebook-jssdk',
    ref = d.getElementsByTagName('script')[0];

    if (d.getElementById(id)) {
      return;
    }

    js = d.createElement('script');
    js.id = id;
    js.async = true;
    js.src = "//connect.facebook.net/uk_UA/all.js";

    ref.parentNode.insertBefore(js, ref);

  }(document));
})

.run(function(editableOptions) {
  editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
})

.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
  cfpLoadingBarProvider.includeSpinner = false;
}]);

angular.module('zabalo-web.services', []);

angular.module('zabalo-web.controllers', []);
