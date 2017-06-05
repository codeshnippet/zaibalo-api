'use strict';

angular.module('zabalo-web.controllers')

.controller('PostRatingsController', ['$http', '$scope', '$translate', 'Avatar',
  function($http, $scope, $translate, Avatar) {

    $scope.getAvatar = function(user, size){
      return Avatar(user, size);
    }

}]);
