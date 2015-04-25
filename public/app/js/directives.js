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
})

.directive('tooltip', ['$translate', function($translate) {
	return {
		restrict : 'A',
		replace : true,
		link: function(scope, element, attrs) {
			$(element).tooltip({
				placement: 'top',
				toggle: 'tooltip',
				title: function(){
					return $translate.instant($(this).attr("title-key"));
				}
			});
		}
	};
}])

;
