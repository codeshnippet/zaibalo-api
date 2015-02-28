'use strict';

angular.module('myApp.controllers')

.controller('NavigationController', function($translate, $scope) {
  $scope.locale = $translate.use();

  $scope.toggleLanguage = function () {
    var langKey = $scope.locale =='uk_UA' ? 'en_US' : 'uk_UA';
    $translate.use(langKey);
    $scope.locale = langKey;
  };
});
