'use strict';

// Declare app level module which depends on filters, and services
var app = angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives', 'myApp.controllers', 'pascalprecht.translate', 'ngSanitize', 'bd.timedistance'])

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/',              {templateUrl: 'partials/posts.html',       controller: 'PostsController'});
    $routeProvider.when('/hashtag/:tag',  {templateUrl: 'partials/posts.html',       controller: 'PostsController'});
    $routeProvider.when('/profile',       {templateUrl: 'partials/profile.html',     controller: 'ProfileController'});
    $routeProvider.when('/post/:postId',  {templateUrl: 'partials/post.html',        controller: 'SinglePostController'});

    $routeProvider.otherwise({redirectTo: '/'});
}]);

app.config(function ($translateProvider) {
    $translateProvider.useStaticFilesLoader({
      prefix: 'app/messages/locale-',
      suffix: '.json'
    });
    $translateProvider.use('uk_UA');
});
