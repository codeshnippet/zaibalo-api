'use strict';

/* Services */

angular.module('myApp.services')
.service("PostsService", ['$http', function($http){
  var self = this;

  // Interface
  self.deletePost = function(post, callback){
    $http.delete(post._links.delete.href).
      success(function(data, status, headers, config) {
        callback();
    });
  };

  self.addPost = function(content, callback){
    $http.post('posts', JSON.stringify({ content : content })).
      success(function(post) {
        callback(post);
      });
  };

  self.loadPosts = function(url, callback){
      $http({
        url: url,
        method: "GET",
        params: {
          limit: self.pageSize
        }
      }).
      success(function(data, status, headers, config) {
          callback(data);
      });
  };

  self.ratePost = function(post, isPositive){
    $http.post(post._links.ratePost.href, JSON.stringify(
      { isPositive: isPositive }
    ))
    .success(function(data) {
      post.ratings = data;
    })
    .error(function(error_code){
      alert(error_code);
    });
  };

}]);
