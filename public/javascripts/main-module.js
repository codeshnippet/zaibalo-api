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

		app.controller('PostsController', ['$http', function($http) {
			var self = this;
			self.posts = [];
			self.newPost = {
				author: {
					displayName: $('#username').val()
				}
			};
			
			$.getJSON('/posts?sort=created_at&limit=10&from=0', "json", function(allData) {
				$.each(allData, function( index, item ) {
					self.posts.push(new Post(item.id, item.content, item.author.displayName));
				});
			});

			self.addPost = function(){
				self.newPost.creationTimestamp = new Date().getTime();
				var data = JSON.stringify({ content : self.newPost.content });

				$.post('/posts', data, function(data) {
					self.posts.unshift(new Post(data.id, data.content, data.author.displayName));
				}, 'json');

				this.newPost = {};
			}
		}]);
	}
)();