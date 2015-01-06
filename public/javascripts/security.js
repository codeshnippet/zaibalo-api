$.ajaxSetup({
	contentType: "application/json",
	beforeSend: function (request, settings)
    {
		var requestBodyMd5 = CryptoJS.MD5(settings.data).toString();
		var timestamp = new Date().getTime();
		var data = settings.type + '\n' + 
					requestBodyMd5 + '\n' +
					settings.contentType + '\n' +
					timestamp + '\n' +
					settings.url;
		
		var passwordMD5Hex = CryptoJS.MD5($('#password').val()).toString();
		var shaObj = new jsSHA(data, "TEXT");
		this.password = shaObj.getHMAC(passwordMD5Hex, "TEXT", "SHA-1", "B64");
		this.username = $('#username').val();
		
        request.setRequestHeader("X-UTC-Timestamp", timestamp);
    }
});