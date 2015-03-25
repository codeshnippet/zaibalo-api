function getHMAC(type, url, contentType, body, timestamp, token){
	var requestBodyMd5 = CryptoJS.MD5(body).toString();
	var data = type + '\n' +
	requestBodyMd5 + '\n' +
	contentType + '\n' +
	timestamp + '\n' +
	url;

	var shaObj = new jsSHA(data.toLowerCase(), "TEXT");
	return shaObj.getHMAC(token, "TEXT", "SHA-1", "B64");
}

var module = angular.module('myApp');

module.factory('tokenInjector', ['CookiesService', function(CookiesService) {
    var tokenInjector = {
        request: function(config) {
            if (true) {
							var contentType = config.headers['Content-Type'] ? config.headers['Content-Type'] : '';
              var timestamp = new Date().getTime();
        			var authToken = getHMAC(config.method, config.url, contentType, config.data, timestamp, CookiesService.getToken());
              config.headers['x-auth-token'] = authToken;
              config.headers['x-auth-username'] = CookiesService.getUsername();
              config.headers['x-utc-timestamp'] = timestamp;
            }
            return config;
        }
    };
    return tokenInjector;
}]);
