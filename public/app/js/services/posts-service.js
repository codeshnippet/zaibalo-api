'use strict';

/* Services */

angular.module('myApp.services')
.service("PostsService", ['$http', function($http){
  var self = this;

  // Constants

  self.pageSize = 10;
  self.sortBy = 'created_at';

  // Interface

  self.loadLatestPosts = function(from, callback){
    loadPosts(callback, 'latest', null, from);
  }

  self.loadPostById = function(postId, callback){
    loadPosts(callback, 'post-by-id', postId);
  }

  self.loadPostsByTag = function(tag, from, callback){
    loadPosts(callback, 'posts-by-tag', tag, from);
  }

  self.loadPostsByUser = function(user, from, callback){
    loadPosts(callback, 'posts-by-user', user, from);
  }

  self.getLatestPostsCount = function(callback){
    getPostsCount(callback, 'latest');
  }

  self.getPostsByTagCount = function(tag, callback){
    getPostsCount(callback, 'posts-by-tag', tag);
  }

  self.getPostsByUserCount = function(user, callback){
    getPostsCount(callback, 'posts-by-user', user);
  }

  self.deletePost = function(postId, callback){
    $http.delete(buildPostsUrl('post-by-id', postId)).
      success(function(data, status, headers, config) {
        callback();
    });
  }

  self.addPost = function(content, callback){
    $http.post('/posts', JSON.stringify({ content : content })).
      success(function(post, status, headers, config) {
        callback(post);
      });
  }

  // Private methods

  function getPostsCount(callback, type, param){
    $http.get(buildPostsCountUrl(type, param, true)).
      success(function(data, status, headers, config) {
          callback(data.count);
      });
  }

  function loadPosts(callback, type, param, from){
      $http({
        url: buildPostsUrl(type, param),
        method: "GET",
        params: {
          sort: self.sortBy,
          limit: self.pageSize,
          from: from
        }
      }).
      success(function(posts, status, headers, config) {
          callback(posts);
      });
  }

  function buildPostsCountUrl(type, param){
    return buildPostsUrl(type, param) + '/count';
  }

  function buildPostsUrl(type, param){
    var urlRoot;
    switch (type)
    {
       case 'latest':
         urlRoot = '/posts';
         break;
       case 'post-by-id':
         urlRoot = '/posts/' + param;
         break;
       case 'posts-by-user':
         urlRoot = '/users/' + param + '/posts';
         break;
      case 'posts-by-tag':
         urlRoot = '/posts/hashtag/' + param;
         break;
      default:
         console.error('Uknown case: ' + type);
         break;
    }

    return urlRoot;
  }

}]);
