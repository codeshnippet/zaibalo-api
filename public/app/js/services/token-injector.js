function getHMAC(type, url, contentType, body, timestamp, token){
	var requestBodyMd5 = CryptoJS.MD5(body).toString();
	var data = type + '\n' +
	requestBodyMd5 + '\n' +
	contentType + '\n' +
	timestamp + '\n' +
	normalizeUrl(url);

	var shaObj = new jsSHA(data.toLowerCase(), "TEXT");
	return shaObj.getHMAC(token, "TEXT", "SHA-1", "B64");
}

function normalizeUrl(url){
    return url.startsWith("/") ? url.substring(1) : url;
}

var module = angular.module('zabalo-web');

module.factory('tokenInjector', ['CookiesService', '$q','$location', function(CookiesService, $q, $location) {
    var tokenInjector = {
        request: function(config) {
            if (true) {

              var timestamp = new Date().getTime();
							var token = CookiesService.getToken() ? CookiesService.getToken() : '';
							var username = CookiesService.getUsername() ? CookiesService.getUsername() : '';
							var contentType = config.headers['Content-Type'] ? config.headers['Content-Type'] : '';

        			var authToken = getHMAC(config.method, config.url, contentType, config.data, timestamp, token);

              config.headers['x-auth-token'] = authToken;
              config.headers['x-auth-username'] = username;
              config.headers['x-utc-timestamp'] = timestamp;
            }
            return config;
        }
    };
    return tokenInjector;
}]);
