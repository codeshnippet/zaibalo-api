'use strict';

/* Services */

angular.module('myApp.services')
.factory("Avatar", function AvatarFactory(){
  var getSizeParameter = function(provider, size){
    if(provider == 'GOOGLE_PLUS' && size == 'S') {
      return '?sz=32';
    }

    if(provider == 'GOOGLE_PLUS' && size == 'M') {
      return '?sz=200';
    }

    if(provider == 'AVATARS_IO' && size == 'S') {
      return '?size=small';
    }

    if(provider == 'AVATARS_IO' && size == 'M') {
      return '?size=large';
    }

    if(provider == 'FACEBOOK' && size == 'S'){
      return '?width=32&height=32';
    }

    if(provider == 'FACEBOOK' && size == 'M'){
      return '?width=100&height=100';
    }

    return '';
  };
  return function(user, size){
    if (user != null && typeof user != 'undefined' && typeof user.photo != 'undefined'){
      return user.photo + getSizeParameter(user.photoProvider, size);
    }
    return '';
  };
});
