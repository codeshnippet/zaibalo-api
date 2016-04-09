'use strict';

angular.module('myApp.controllers')

.controller('GoogleSignInController', ['$scope', 'UserService', function($scope, UserService) {

    $scope.signInCallback = function(authResult) {
        $scope.$apply(function() {
          $scope.getUserInfo();
        });
    };

    // Render the sign in button.
    $scope.renderSignInButton = function() {
        gapi.signin.render('signInButton',
            {
                'callback': $scope.signInCallback, // Function handling the callback.
                'clientid': '357766608319-8sklovn68l82ir061h2deu2o28cnl42a.apps.googleusercontent.com', // CLIENT_ID from developer console which has been explained earlier.
                'requestvisibleactions': 'http://schemas.google.com/AddActivity', // Visible actions, scope and cookie policy wont be described now,
                                                                                  // as their explanation is available in Google+ API Documentation.
                'scope': 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email',
                'cookiepolicy': 'single_host_origin'
            }
        );
    }

    // Process user info.
    // userInfo is a JSON object.
   $scope.processUserInfo = function(userInfo) {
       var user = {
         id: userInfo.id,
         provider: 'GOOGLE_PLUS',
         displayName: userInfo.displayName,
         email: userInfo['emails'][0]['value'],
         photo: userInfo.image.url.split("?")[0]
       };

       UserService.loginSocial(user.id, user.email, user.displayName, user.photo);
   }

    // When callback is received, process user info.
   $scope.userInfoCallback = function(userInfo) {
       $scope.$apply(function() {
           $scope.processUserInfo(userInfo);
       });
   };

    // Request user info.
   $scope.getUserInfo = function() {
       gapi.client.request(
           {
               'path':'/plus/v1/people/me',
               'method':'GET',
               'callback': $scope.userInfoCallback
           }
       );
   };

    $scope.loadScript = function(oCallback) {
      window.___gcfg = {
        lang: 'uk'
      };

      var oHead = document.getElementsByTagName('head')[0];
      var oScript = document.createElement('script');
      oScript.type = 'text/javascript';
      oScript.src = 'https://apis.google.com/js/client:plusone.js';
      // most browsers
      oScript.onload = oCallback;
      // IE 6 & 7
      oScript.onreadystatechange = function() {
        if (this.readyState == 'complete') {
          oCallback();
        }
      }
      oHead.appendChild(oScript);
    }

    // Call start function on load.
    $scope.loadScript($scope.renderSignInButton);
}]);
