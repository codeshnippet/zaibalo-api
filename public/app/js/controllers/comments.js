'use strict';

angular.module('myApp.controllers')

.controller('CommentsController', ['$http', '$scope', '$translate', 'Avatar',
  function($http, $scope, $translate, Avatar) {

    $scope.addComment = function(post){
      var json = JSON.stringify({ content : post.newComment });

      $.post('/posts/' + post.id + '/comments', json, function(data) {
          post.comments.push(data);
          $scope.$apply();
      }, 'json');

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
