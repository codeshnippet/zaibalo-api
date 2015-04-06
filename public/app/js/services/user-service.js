'use strict';

/* Services */

angular.module('myApp.services')
.service('UserService', ['$http', 'CookiesService', function($http, CookiesService){
  var self = this;

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

  self.login = function(loginName, password, success, error){
    var request = {
      loginName : loginName,
      password : password
    };

    $http.post('/login', JSON.stringify(request)).
      success(function(data, status, headers, config) {
        self.authenticate(data);
        success();
      }).error(function(data,status,headers,config) {
        error();
      });
  };

  self.authenticate = function(data){
    CookiesService.saveAuthCookies(data.user.loginName, data.token);
  };

  self.logout = function(){
    CookiesService.removeAuthCookies();
    self.fbLogoutUser();
    gapi.auth.signOut();
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

  self.fbLogoutUser = function() {
    FB.getLoginStatus(function(response) {
        if (response && response.status === 'connected') {
          FB.logout(function(response) {
          });
        }
    });
  };
}]);
