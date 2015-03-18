$.ajaxSetup({
	contentType: "application/json",
	beforeSend: function (request, settings)
    {
			var token = getToken();
			if(!token){
				return;
			}
			var timestamp = new Date().getTime();
			var authToken = getHMAC(settings.type, settings.url, settings.contentType, settings.data, timestamp, token);

			request.setRequestHeader(getAuthTokenHeaderName(), authToken);

	    request.setRequestHeader(getTimestampHeaderName(), timestamp);

			request.setRequestHeader(getUsernameHeaderName(), getUsername());
    }
});
