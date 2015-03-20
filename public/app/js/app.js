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