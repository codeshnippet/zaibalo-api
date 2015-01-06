function User(displayName) {
	var self = this;
	self.displayName = displayName;
}

function Post(id, content, displayName) {
	var self = this;
	self.id = id;
	self.content = ko.editable(content);
	self.author = new User(displayName);
	
	self.content.endEditAndSubmit = function() {
		var data = JSON.stringify({ content : self.content() });
		
		$.put('/posts/' + self.id, data, function(response) {
			self.content.endEdit();
		}, 'json');
	}
}

function PostsViewModel() {
	var self = this;

	self.posts = ko.observableArray([]);
	self.pageIndex = ko.observable(0);

	// Operations
	self.addPost = function() {
		var data = JSON.stringify({ content : $('#new-post-text').val() });

		$.post('/posts', data, function(response) {
			self.posts.unshift(new Post(response.id, response.content, response.author.displayName));
		}, 'json');
	}

	self.loadPosts = function(){
		var PAGE_SIZE = 10;
		self.pageIndex(self.pageIndex() + 1);
		$.getJSON("/posts?sort=created_at&count=" + PAGE_SIZE + "&page=" + self.pageIndex(), "json", function(allData) {
			var mappedPosts = $.map(allData, function(item) {
				return new Post(item.id, item.content, item.author.displayName);
			});
			self.posts.pushAll(mappedPosts);
		});
	}
	
	self.deletePost = function(post){
		$.delete('/posts/' + post.id, null, function(response) {
			self.posts.remove(post);
		}, 'json');
	}
	
	//Initialization
	self.loadPosts();
}

$(document).ready(function() {
	ko.applyBindings(new PostsViewModel());
});