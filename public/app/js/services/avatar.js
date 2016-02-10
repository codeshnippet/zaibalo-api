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

    return '';
  };
  return function(user, size){
    if(user)
    return user.photo + getSizeParameter(user.photoProvider, size);
  };
});
