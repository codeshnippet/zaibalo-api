'use strict';

/* Services */

angular.module('myApp.services')
.service("CookiesService", ['$cookies', function($cookies){
  var self = this;

  self.saveAuthCookies = function(username, token){
    var expireDate = new Date();
    expireDate.setDate(expireDate.getDate() + 90);
    $cookies.put('username', username, {'expires': expireDate});
    $cookies.put('token', token, {'expires': expireDate});
  }

  self.getToken = function(){
    return $cookies.get('token');
  }

  self.getUsername = function(){
    return $cookies.get('username');
  }

  self.isUserLoggedIn = function(){
    return self.getUsername() && self.getToken();
  }

  self.removeAuthCookies = function(){
    $cookies.remove('username');
    $cookies.remove('token');
  }
}]);
