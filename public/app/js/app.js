'use strict';

// Declare app level module which depends on filters, and services
var app = angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives', 'myApp.controllers', 'pascalprecht.translate', 'ngSanitize', 'bd.timedistance']);

angular.module('myApp.controllers', []);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when('/', {
      templateUrl: 'partials/posts.html',
      controller: 'PostsController'
      })
    .when('/tag/:tag', {
      templateUrl: 'partials/posts.html',
      controller: 'PostsController'
      })
    .when('/user/:displayName', {
      templateUrl: 'partials/user.html',
      controller: 'UserController'
      })
    .when('/post/:postId', {
      templateUrl: 'partials/posts.html',
      controller: 'PostsController'
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
