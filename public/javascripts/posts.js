ko.observableArray.fn.pushAll = function(valuesToPush) {
    var underlyingArray = this();
    this.valueWillMutate();
    ko.utils.arrayPushAll(underlyingArray, valuesToPush);
    this.valueHasMutated();
    return this;
};

function User(displayName) {
	var self = this;
	self.displayName = displayName;
}

function Post(id, content, displayName) {
	var self = this;
	self.id = id;
	self.content = ko.editable(content);
	self.author = new User(displayName);
	self.comments = ko.observableArray([]);
	
	self.content.endEditAndSubmit = function() {
		var data = JSON.stringify({ content : self.content() });
		
		$.put('/posts/' + self.id, data, function(response) {
			self.content.endEdit();
		}, 'json');
	}
}

function PostsViewModel() {
	var PAGE_SIZE = 10;
	
	var self = this;

	self.posts = ko.observableArray([]);
	self.fromIndex = ko.observable(0);
	self.postsCount = ko.observable(0);
	
	// Operations
	self.initPostsCount = function(){
		$.getJSON("/posts/count", "json", function(response) {
			self.postsCount(response.count)
		});
	}
	
	self.loadPosts = function(){
		$.getJSON("/posts?sort=created_at&limit=" + PAGE_SIZE + "&from=" + self.fromIndex(), "json", function(allData) {
			var mappedPosts = $.map(allData, function(item) {
				return new Post(item.id, item.content, item.author.displayName);
			});
			self.posts.pushAll(mappedPosts);
		});
	}
	
	self.loadMorePosts = function(){
		if(self.postsCount() > self.posts().length){
			self.fromIndex(self.fromIndex() + PAGE_SIZE);
			self.loadPosts();
		}
	}
	
	self.addPost = function() {
		var data = JSON.stringify({ content : $('#new-post-text').val() });

		$.post('/posts', data, function(response) {
			self.posts.unshift(new Post(response.id, response.content, response.author.displayName));
		}, 'json');
		
		self.fromIndex(self.fromIndex() + 1);
		self.postsCount(self.postsCount() + 1);
	}

	self.deletePost = function(post){
		$.delete('/posts/' + post.id, null, function(response) {
			self.posts.remove(post);
			self.postsCount(self.postsCount() - 1);
			self.fromIndex(self.fromIndex() - 1);
		}, 'json');
	}
	
	//Initialization
	self.initPostsCount();
	
	self.loadPosts();
}

$(document).ready(function() {
	ko.applyBindings(new PostsViewModel());
});