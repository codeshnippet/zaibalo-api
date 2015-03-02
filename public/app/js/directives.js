'use strict';

/* Directives */

angular.module('myApp.directives', [])

.directive('loginPopup', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/login-popup.html',
		replace : true
	};
})

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
});
