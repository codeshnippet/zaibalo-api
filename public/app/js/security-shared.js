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

function getTimestampHeaderName(){
	return 'x-utc-timestamp';
}

function getUsernameHeaderName(){
	return 'x-auth-username';
}

function getAuthTokenHeaderName(){
	return 'x-auth-token';
}

function saveAuthValues(username, token){
	$.cookie("username", username);
	$.cookie("token", token);
}

function getToken(){
	return $.cookie("token");
}

function getUsername(){
	return $.cookie("username");
}
