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

function login(){
	jso.ajax({
	    url: "https://www.googleapis.com/oauth2/v1/userinfo",
	    oauth: {
	        scopes: {
	            request: ["https://www.googleapis.com/auth/userinfo.email"],
	            require: ["https://www.googleapis.com/auth/userinfo.email"]
	        }
	    },
	    dataType: 'json',
	    success: function(data) {
	        alert(data);
	    }
	});
}

function logToken(){
	jso.getToken(function(token) {
	    console.log("I got the token: ", token);
	}, null);
}


var username = "test";
var password = "secret";

$.ajaxSetup({
	username : username,
	password : password
});