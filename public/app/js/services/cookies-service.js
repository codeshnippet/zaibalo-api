'use strict';

/* Services */

angular.module('myApp.services')
.service("CookiesService", ['$cookies', function($cookies){
  var self = this;

  var TOKEN = 'token';
  var USERNAME = 'username';
  var AVATAR_URL = 'avatar-url';
  
  self.saveAuthCookies = function(username, token, avatarUrl){
    var expireDate = new Date();
    expireDate.setDate(expireDate.getDate() + 90);
    $cookies.put(USERNAME, username, {'expires': expireDate});
    $cookies.put(TOKEN, token, {'expires': expireDate});
    $cookies.put(AVATAR_URL, avatarUrl, {'expires': expireDate});
  }

  self.getToken = function(){
    return $cookies.get(TOKEN);
  }

  self.getUsername = function(){
    return $cookies.get(USERNAME);
  }
  
  self.getAvatarUrl = function(){
    return $cookies.get(AVATAR_URL);
  }

  self.isUserLoggedIn = function(){
    return self.getUsername() && self.getToken();
  }

  self.removeAuthCookies = function(){
    $cookies.remove(USERNAME);
    $cookies.remove(TOKEN);
    $cookies.remove(AVATAR_URL);
  }
}]);
