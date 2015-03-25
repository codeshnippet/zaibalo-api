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
    var token = $cookies.token;
    return token ? token : '';
  }

  self.getUsername = function(){
    var username = $cookies.username;
    return username ? username : '';
  }

  self.removeAuthCookies = function(){
    delete $cookies.username;
    delete $cookies.token;
  }
}]);
