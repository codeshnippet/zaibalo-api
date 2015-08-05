'use strict';

angular.module('myApp.controllers')

.controller('CommentsController', ['$http', '$scope', '$translate', 'Avatar',
  function($http, $scope, $translate, Avatar) {

    $scope.addComment = function(post){
      var json = JSON.stringify({ content : post.newComment });
      $http.post('/posts/' + post.id + '/comments', json).
        success(function(data, status, headers, config) {
          if(!$scope.hasComments(post)){
            post._embedded = {comments:[]};
          }
        	post._embedded.comments.push(data);
        });

      post.newComment = "";
    }

    $scope.getAvatar = function(user, size){
      return Avatar(user, size);
    }

    $scope.deleteComment = function(post, commentId, index, event){
      $http.delete('/comments/' + commentId).
        success(function(data, status, headers, config) {
          post._embedded.comments.splice(index, 1);
        });

      event.preventDefault();
    }

    $scope.hasComments = function(post){
      return post._embedded && post._embedded.comments && post._embedded.comments.length > 0;
    }

    $scope.translationSufix = function(number){
      if(number == undefined){
        number = 0;
      }
       if((number-number%10)%100!=10){
         if(number%10==1){
           return 1;
         } else if(number%10>=2 && number%10<=4){
           return 2;
         } else {
           return 5;
         }
       } else {
         return 5;
       }
     };
}]);
