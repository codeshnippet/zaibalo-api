var module = angular.module('myApp');

module.factory('authHttpResponseInterceptor',['$q', '$location', function($q, $location){
    return {
        response: function(response){
            if (response.status === 401) {
                console.log("Response 401");
            }
            return response || $q.when(response);
        },
        responseError: function(rejection) {
            if (rejection.status === 401) {
                console.log("Response Error 401",rejection);
                var returnPath = $location.path();
                $location.path('/signup').search('returnTo', returnPath);
            }
            return $q.reject(rejection);
        }
    }
}]);
