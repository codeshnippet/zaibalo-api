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

.directive('post', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/post.html',
		replace : true
	};
})

.directive('comments', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/comments.html',
		replace : true
	};
});
