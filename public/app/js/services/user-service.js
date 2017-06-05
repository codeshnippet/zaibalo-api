'use strict';

/* Services */

angular.module('zabalo-web.services')
.service('UserService', ['$http', 'CookiesService', '$location', 'Avatar', '$facebook', function($http, CookiesService, $location, Avatar, $facebook){
  var self = this;

  self.loginSocial = function(externalId, email, displayName, photo){
    var request = {
      email : email,
      displayName : displayName,
      photo : photo,
      externalId : externalId
    };

    $http.post('oauth-login', JSON.stringify(request)).
      success(function(data, status, headers, config) {
        self.authenticate(data);
        self.redirectBack();
      });
  };

  self.login = function(loginName, password, success, error){
    var request = {
      loginName : loginName,
      password : password
    };

    $http.post('login', JSON.stringify(request)).
      success(function(data, status, headers, config) {
        self.authenticate(data);
        self.redirectBack();
        success();
      }).error(function(data,status,headers,config) {
        error(data);
      });
  };

  self.authenticate = function(data){
    CookiesService.saveAuthCookies(data.user.loginName, data.token, Avatar(data.user, 'S'));
  };

  self.logout = function(){
    CookiesService.removeAuthCookies();
    self.fbLogoutUser();
    $location.path('/signup');
  }

  self.isUserLoggedIn = function(){
    return CookiesService.isUserLoggedIn();
  }

  self.isOwner = function(loginName){
    return self.getUsername() == loginName;
  }

  self.getUsername = function(){
    return CookiesService.getUsername();
  }

  self.getAvatarUrl = function(){
	  return CookiesService.getAvatarUrl();
  }

  self.registerUser = function(loginName, password, success, error){
    var request = {
      loginName : loginName,
      password : password
    };

    $http.post('users', JSON.stringify(request)).
      success(function(data, status, headers, config) {
        self.authenticate(data);
        success();
        self.redirectBack();
      }).error(function(data,status,headers,config) {
        error(data);
      });
  };

  self.fbLogoutUser = function() {
    $facebook.logout();
  };

  self.redirectBack = function(){
    var returnTo = $location.search().returnTo ? $location.search().returnTo : '/';
    $location.search({});
    $location.path(returnTo);
  };
}]);
