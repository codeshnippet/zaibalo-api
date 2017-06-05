'use strict';

/* Directives */

angular.module('zabalo-web.directives', [])

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

.directive('facebookSignIn', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/facebook-sign-in.html',
		replace : true,
		controller: 'FacebookSignInController'
	};
});
