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

//TODO has to return password hash from cookies or saved in a page variable
function getPasswordMd5(){
	return CryptoJS.MD5($('#password').val()).toString();
}
//TODO has to return username from cookies or saved in a page variable
function getUsername(){
	return $('#username').val();
}