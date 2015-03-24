'use strict';

angular.module('myApp.controllers')

.controller('CommentsController', ['$http', '$scope', '$translate', 'Avatar',
  function($http, $scope, $translate, Avatar) {

    $scope.addComment = function(post){
      var json = JSON.stringify({ content : post.newComment });
      $http.post('/posts/' + post.id + '/comments', JSON.stringify({ content : $scope.newPost })).
        success(function(data, status, headers, config) {
        	post.comments.push(data);
        });

      post.newComment = "";
    }

    $scope.getAvatar = function(user, size){
      return Avatar(user, size);
    }

    $scope.toggleComments = function(event, target){
      $(target).prev().slideToggle(500);
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
