var module = angular.module('myApp');

module.factory('https-redirect',['$q', '$location', '$window', function($q, $location, $window){
    return {
        response: function(response){
          if ($location.host() !== 'localhost' && $location.protocol() !== 'https') {
            $window.location.href = $location.absUrl().replace('http', 'https');
          }
          return response;
        }
    }
}]);
