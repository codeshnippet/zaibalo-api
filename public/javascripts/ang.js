function User(displayName) {
	this.displayName = displayName;
}

function Post(id, content, displayName) {
	this.id = id;
	this.content = content;
	this.author = new User(displayName);
	this.comments = [];
}

var interceptor = function ($q, $location) {
    return {
        request: function (config) {
        	console.log(config);
        	var timestamp = new Date().getTime();
        	var passwordMD5 = getPasswordMd5();
        	var username = getUsername();
        	var password = getHMAC(config.method, config.url, config.headers['Content-Type'], JSON.stringify(config.data), timestamp, passwordMD5);
        	config.headers['Authorization'] = 'Basic ' + btoa(username + ':' + password);
        	config.headers[getTimestampHeaderName()] = timestamp;
            return config;
        },

        response: function (result) {
            return result;
        },

        responseError: function (rejection) {
            console.log('Failed with', rejection.status, 'status');
            if (rejection.status == 401) {
                //Show login request popup, resend ajax
            }

            return $q.reject(rejection);
        }
    }
};


(
	function() {
		var app = angular.module('zaibaloApp', []);
	
		app.config(['$httpProvider', function ($httpProvider) {
	        $httpProvider.interceptors.push(interceptor);
	    }]);

		app.controller('PostsController', ['$http', function($http) {
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

			self.addPost = function(){
				self.newPost.creationTimestamp = new Date().getTime();
				var data = JSON.stringify({ content : self.newPost.content });

				$http.post('/posts', self.newPost).
					success(function(data, status, headers, config) {
						self.posts.unshift(new Post(data.id, data.content, data.author.displayName));
					});

				this.newPost = {};
			}
		}]);
	}
)();