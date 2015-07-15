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

.directive('comments', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/comments.html',
		replace : true,
		controller: 'CommentsController'
	};
})

.directive('postRatings', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/post-ratings.html',
		replace : true,
		controller: 'PostRatingsController'
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
