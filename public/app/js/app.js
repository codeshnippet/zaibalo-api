'use strict';

// Declare app level module which depends on filters, and services
var app = angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives', 'myApp.controllers', 'pascalprecht.translate', 'ngSanitize', 'bd.timedistance', 'ngRoute', 'ngCookies'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when('/', {
      templateUrl: 'partials/posts.html',
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
    .otherwise({redirectTo: '/'});
}])

.config(function ($translateProvider) {
    $translateProvider.useStaticFilesLoader({
      prefix: 'app/messages/locale-',
      suffix: '.json'
    });
    $translateProvider.use('uk_UA');
})

.config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('tokenInjector');
    $httpProvider.interceptors.push('authHttpResponseInterceptor');
}]);

angular.module('myApp.services', []);

angular.module('myApp.controllers', []);
