'use strict';

/* Directives */

angular.module('myApp.directives', [])

.directive('navigationBar', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/navigation-bar.html',
		replace : true
	};
})

.directive('posts', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/posts.html',
		replace : true,
		controller: 'PostsController'
	};
})

.directive('comments', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/comments.html',
		replace : true,
		controller: 'CommentsController'
	};
})

.directive('googleSignIn', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/google-sign-in.html',
		replace : true,
		controller: 'GoogleSignInController'
	};
})

.directive('facebookSignIn', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/facebook-sign-in.html',
		replace : true,
		controller: 'FacebookSignInController'
	};
});
