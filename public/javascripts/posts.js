function User(displayName) {
	var self = this;
	self.displayName = displayName;
}

function Post(content, displayName) {
	var self = this;
	self.content = content;
	self.author = new User(displayName);
}

function PostsViewModel() {
	var self = this;

	self.posts = ko.observableArray([]);
	self.shouldShowLogin = ko.observable(!isAuthenticated());
	self.authUserName = ko.observable(getAuthUserName());

	$.getJSON("/posts", function(allData) {
		var mappedPosts = $.map(allData, function(item) {
			return new Post(item.content, item.author.displayName)
		});
		self.posts(mappedPosts);
	});

	// Operations
	self.addPost = function() {
		var data = JSON.stringify({ content : $('#new-post-text').val() });
		
		$.post('/posts', data, function(response) {
			self.posts.push(new Post($('#new-post-text').val(), self.authUserName()));
		}, 'json');
	}

	self.login = function() {
		var data = JSON.stringify({
			username : $('#username').val(),
			password : $('#password').val()
		});
		
		$.post('/login', data, function(response) {
			self.shouldShowLogin(false);
			self.authUserName(response.displayName);
			$.cookie("authToken", response.authToken);
			$.cookie("username", response.displayName);
		}, 'json');
	}
	
}

function isAuthenticated() {
	return getAuthTokenFromCookies() != undefined;
}

function getAuthUserName() {
	return getAuthTokenFromCookies() != undefined ? $.cookie("username") : 'Anonymous';
}

function getAuthTokenFromCookies(){
	return $.cookie("authToken");
}

$.ajaxSetup({
    statusCode: {
        403: function(error, callback){
        	alert(error.responseText);
        }
    }
});

$(document).ready(function() {
	ko.applyBindings(new PostsViewModel());
});