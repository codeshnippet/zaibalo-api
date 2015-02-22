'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('myApp.services', [])
  .factory("PostService", function() {
    return {
      addComment: function(post, $scope){
          var json = JSON.stringify({ content : post.newComment });

          $.post('/posts/' + post.id + '/comments', json, function(data) {
              post.comments.push(data);
              $scope.$apply();
          }, 'json');

          post.newComment = "";
      },

      deletePost: function(postId, $scope, onSuccess){
        $.delete('/posts/' + postId, '', function(data) {
          onSuccess();
          $scope.$apply();
        }, 'json');
      },

      toggleComments: function(event, target){
        $(target).prev().slideToggle(500);
        event.preventDefault();
      },

      translationSufix: function(number){
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
       }
    };
  });
