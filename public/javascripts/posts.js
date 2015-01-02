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
			self.posts.unshift(new Post(response.id, $('#new-post-text').val(), response.author.displayName));
		}, 'json');
	}

	self.loadPosts = function(){
		var PAGE_SIZE = 10;
		self.pageIndex(self.pageIndex() + 1);
		$.getJSON("/posts?sort=created_at&count=" + PAGE_SIZE + "&page=" + self.pageIndex(), function(allData) {
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

ko.observableArray.fn.pushAll = function(valuesToPush) {
    var underlyingArray = this();
    this.valueWillMutate();
    ko.utils.arrayPushAll(underlyingArray, valuesToPush);
    this.valueHasMutated();
    return this;
};

$.ajaxSetup({
    statusCode: {
        401: function(error, callback){
        	alert(error.responseText);
        },
        403: function(error, callback){
        	alert(error.responseText);
        }
    },
    username: "test",
    password: "secret"
});

$.put = function(url, data, callback, type) {

	if ($.isFunction(data)) {
		type = type || callback, callback = data, data = {}
	}

	return $.ajax({
		url : url,
		type : 'PUT',
		success : callback,
		data : data,
		contentType : type
	});
}

$.delete = function(url, data, callback, type) {
	if ( $.isFunction(data) ){
		type = type || callback, callback = data, data = {}
	}
	 
	return $.ajax({
		url: url,
		type: 'DELETE',
		success: callback,
		data: data,
		contentType: type
	});
}

$(document).ready(function() {
	ko.applyBindings(new PostsViewModel());
});