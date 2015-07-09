'use strict';

angular.module('myApp.controllers')

.controller('CommentsController', ['$http', '$scope', '$translate', 'Avatar',
  function($http, $scope, $translate, Avatar) {

    $scope.addComment = function(post){
      var json = JSON.stringify({ content : post.newComment });
      $http.post('/posts/' + post.id + '/comments', json).
        success(function(data, status, headers, config) {
        	post.comments.push(data);
        });

      post.newComment = "";
    }

    $scope.getAvatar = function(user, size){
      return Avatar(user, size);
    }

    $scope.toggleComments = function(post, event){
      if (post.comments.length > 0 || $scope.isUserLoggeIn()) {
        $(event.target).parent().parent().parent().prev().prev().hide(500);
        $(event.target).parent().parent().parent().prev().slideToggle(500);
      }
      event.preventDefault();
    }

    $scope.toggleRatings = function(post, event){
      if (post.ratings.length > 0 || $scope.isUserLoggeIn()) {
        $(event.target).parent().parent().parent().prev().hide(500);
        $(event.target).parent().parent().parent().prev().prev().slideToggle(500);
      }
      event.preventDefault();
    }

    $scope.deleteComment = function(post, commentId, index, event){
      $http.delete('/comments/' + commentId).
        success(function(data, status, headers, config) {
          post.comments.splice(index, 1);
        });

      event.preventDefault();
    }

    $scope.translationSufix = function(number){
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
