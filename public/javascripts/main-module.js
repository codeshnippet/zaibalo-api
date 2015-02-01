function User(displayName) {
	this.displayName = displayName;
}

function Post(id, content, displayName) {
	this.id = id;
	this.content = content;
	this.author = new User(displayName);
	this.comments = [];
}

(
	function() {
		var app = angular.module('zaibaloApp', []);

		app.controller('PostsController', ['$http', '$scope', function($http, $scope) {
			var self = this;
			self.posts = [];
			self.newPost = {
				author: {
					displayName: $('#username').val()
				}
			};
			
			$http.get('/posts?sort=created_at&limit=10&from=0').
				success(function(data, status, headers, config) {
					var mappedPosts = $.map(data, function(item) {
						return new Post(item.id, item.content, item.author.displayName);
					});
					self.posts = mappedPosts;
				});

			self.addPost = function(posts){
				self.newPost.creationTimestamp = new Date().getTime();
				var data = JSON.stringify({ content : self.newPost.content });

				$.post('/posts', data, function(data) {
					posts.unshift(new Post(data.id, data.content, data.author.displayName));
					$scope.$apply();
				}, 'json');

				this.newPost = {};
			}
		}]);
	}
)();