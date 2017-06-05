'use strict';

angular.module('zabalo-web.controllers')

.controller('NavigationController', ['$translate', '$scope', 'UserService', function($translate, $scope, UserService) {
  $scope.locale = $translate.use();

  $scope.isUserLoggeIn = function() {
    return UserService.isUserLoggedIn();
  };

  $scope.logout = function(event){
    UserService.logout();
    event.preventDefault();
  }

  $scope.toggleLanguage = function() {
    var langKey = $scope.locale =='uk_UA' ? 'en_US' : 'uk_UA';
    $translate.use(langKey);
    $scope.locale = langKey;
  };

}]);
