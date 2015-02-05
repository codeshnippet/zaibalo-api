(
	function() {
		var app = angular.module('zaibaloApp', []);

		app.controller('PostsController', ['$http', '$scope', function($http, $scope) {
			var PAGE_SIZE = 10;

			var self = this;
			self.fromIndex = 0;
			self.posts = [];
			self.postsCount = 0;
			self.newPost = "";
			
			$http.get('/posts/count').
				success(function(data, status, headers, config) {
					self.postsCount = data.count;
				});
			
			self.loadPosts = function(){
				$http.get('/posts?sort=created_at&limit=' + PAGE_SIZE + '&from=' + self.fromIndex).
				success(function(posts, status, headers, config) {
					for (var i = 0; i < posts.length; i++) {
						self.posts.unshift(posts[i]);
					}
					self.fromIndex = self.fromIndex + PAGE_SIZE;
				});
			}

			self.addPost = function(posts){
				var json = JSON.stringify({ content : self.newPost });

				$.post('/posts', json, function(data) {
					posts.unshift(data);
					$scope.$apply();
				}, 'json');

				this.newPost = "";
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