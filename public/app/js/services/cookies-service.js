'use strict';

/* Services */

angular.module('myApp.services')
.service("CookiesService", ['$cookies', function($cookies){
  var self = this;

  self.saveAuthCookies = function(username, token){
    $cookies.username = username;
    $cookies.token = token;
  }

  self.getToken = function(){
    return $cookies.token;
  }

  self.getUsername = function(){
    return $cookies.username;
  }

  self.removeAuthCookies = function(){
    delete $cookies.username;
    delete $cookies.token;
  }
}]);
