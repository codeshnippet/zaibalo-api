'use strict';

/* Services */

angular.module('myApp.services')
.service('UserService', ['$http', 'CookiesService', function($http, CookiesService){
  var self = this;

  self.isLoggedIn = false;

  self.loginSocial = function(id, provider, name, email, photo){
    var request = {
      clientId : id,
      provider : provider,
      displayName : name,
      email : email,
      photo : photo
    };

    $http.post('/oauth-login', JSON.stringify(request)).
      success(function(data, status, headers, config) {
        self.authenticate(data);
      });
  };

  self.login = function(loginName, password){
    var request = {
      loginName : loginName,
      password : password
    };

    $http.post('/login', JSON.stringify(request)).
      success(function(data, status, headers, config) {
        self.authenticate(data);
      });
  };

  self.authenticate = function(data){
    self.isLoggedIn = true;
    CookiesService.saveAuthCookies(data.user.loginName, data.token);
  };

  self.registerUser = function(loginName, password){
    var request = {
      loginName : loginName,
      password : password
    };

    $http.post('/users', JSON.stringify(request)).
      success(function(data, status, headers, config) {
        self.authenticate(data);
      });
  };
}]);