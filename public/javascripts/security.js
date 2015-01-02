JSO.enablejQuery($);

var jso = new JSO({
	providerID : "google",
	client_id : "357766608319-8sklovn68l82ir061h2deu2o28cnl42a.apps.googleusercontent.com",
	redirect_uri : "https://zaibalo-api.herokuapp.com/",
	authorization : "https://accounts.google.com/o/oauth2/auth",
	scopes : {
		request : [ "https://www.googleapis.com/auth/userinfo.profile" ]
	}
});

jso.callback();

function userInfo(){
	var getUserInfo = function(data){
		$.getJSON("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + data.access_token, function(allData) {
			console.log(allData);
		});
	}
	jso.getToken(getUserInfo, null);
}

function login(){
	jso.getToken(function(token) {}, jso.config.config);
}

var username = "test";
var password = "secret";

$.ajaxSetup({
	username : username,
	password : password
});