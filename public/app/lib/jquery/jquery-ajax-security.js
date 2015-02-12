$.ajaxSetup({
	contentType: "application/json",
	beforeSend: function (request, settings)
    {
		var timestamp = new Date().getTime();
		var passwordMD5 = getPasswordMd5();
		this.username = getUsername();
		this.password = getHMAC(settings.type, settings.url, settings.contentType, settings.data, timestamp, passwordMD5);
		
        request.setRequestHeader(getTimestampHeaderName(), timestamp);
    }
});