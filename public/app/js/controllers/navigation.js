'use strict';

angular.module('zabalo-web.controllers')

.controller('NavigationController', ['$translate', '$scope', 'UserService', '$http', function($translate, $scope, UserService, $http) {
  $scope.locale = $translate.use();
  $scope.showRecommended = false;

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

    $http({
      method: 'GET',
      url: 'similarities/max-rec-threshold'
    }).success(function(data){
      if(data.maxRecThreshold > 0){
        $scope.showRecommended = true;
      }
    });

}]);
