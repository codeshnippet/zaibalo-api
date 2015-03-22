function FacebookSignInController($scope) {

  $scope.getUserInfo = function() {
    FB.api('/me', function(res) {
      $rootScope.$apply(function() {
        alert(res);
      });
    });
  }

}
