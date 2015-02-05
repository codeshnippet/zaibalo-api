function User(displayName) {
	this.displayName = displayName;
}

function Post(id, content, displayName, creationTimestamp) {
	this.id = id;
	this.content = content;
	this.author = new User(displayName);
	this.comments = [];
	this.creationTimestamp = creationTimestamp;
}

(
	function() {
		var app = angular.module('zaibaloApp', []);

		app.controller('PostsController', ['$http', '$scope', function($http, $scope) {
			var PAGE_SIZE = 10;

			var self = this;
			self.fromIndex = 0;
			self.posts = [];
			self.postsCount = 0;
			self.newPost = {
				author: {
					displayName: $('#username').val()
				}
			};
			
			$http.get('/posts/count').
				success(function(data, status, headers, config) {
					self.postsCount = data.count;
				});
			
			self.loadPosts = function(){
				$http.get('/posts?sort=created_at&limit=' + PAGE_SIZE + '&from=' + self.fromIndex).
				success(function(data, status, headers, config) {
					for (var i = 0; i < data.length; i++) {
						var post = new Post(data[i].id, data[i].content, data[i].author.displayName, data[i].creationTimestamp);
						self.posts.unshift(post);
					}
					self.fromIndex = self.fromIndex + PAGE_SIZE;
				});
			}

			self.addPost = function(posts){
				var data = JSON.stringify({ content : self.newPost.content });

				$.post('/posts', data, function(data) {
					posts.unshift(new Post(data.id, data.content, data.author.displayName, data.creationTimestamp));
					$scope.$apply();
				}, 'json');

				this.newPost = {};
				self.fromIndex = self.fromIndex + 1;
			}
			
			self.loadMorePosts = function(){
				if(self.postsCount > self.posts.length){
					self.fromIndex = self.fromIndex + PAGE_SIZE;
					self.loadPosts();
				}
			}
			
			self.loadPosts();
		}]);
	}
)();