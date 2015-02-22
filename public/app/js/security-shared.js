function getHMAC(type, url, contentType, body, timestamp, passwordMD5){
	var requestBodyMd5 = CryptoJS.MD5(body).toString();
	var data = type + '\n' +
	requestBodyMd5 + '\n' +
	contentType + '\n' +
	timestamp + '\n' +
	url;

	var shaObj = new jsSHA(data.toLowerCase(), "TEXT");
	return shaObj.getHMAC(passwordMD5, "TEXT", "SHA-1", "B64");
}

function getTimestampHeaderName(){
	return 'x-utc-timestamp';
}

function saveAuthValues(username, password){
	$.cookie("username", username);
	$.cookie("password", CryptoJS.MD5(password).toString());
}

function getPasswordMd5(){
	var passwordHash = $.cookie("password");
	if(passwordHash == undefined)
		passwordHash = '';
	return passwordHash;
}

function getUsername(){
	var username = $.cookie("username");
	if(username == undefined)
		username = 'username';
	return username;
}
