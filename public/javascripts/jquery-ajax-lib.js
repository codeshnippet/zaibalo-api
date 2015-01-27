$.ajaxPrefilter(function(opts, originalOpts, jqXHR) {
    if (opts.refreshRequest) {
        return;
    }
    jqXHR.fail(function() {
        var args = Array.prototype.slice.call(arguments);
        if (jqXHR.status === 401) {
        	var newOpts = $.extend({}, originalOpts, {
        		refreshRequest: true
        	});
        	loginPopup.showPopup(function(opts){
        		$.ajax(opts)
        		.done(function() {
        			loginPopup.hide();
        		})
        		.fail(function() {
        			loginPopup.showErrorMessage();
        		}, newOpts);
        	});
        }
    });
});

$.put = function(url, data, callback, type) {

	if ($.isFunction(data)) {
		type = type || callback, callback = data, data = {}
	}

	return $.ajax({
		url : url,
		type : 'PUT',
		success : callback,
		data : data,
		contentType : type
	});
}

$.delete = function(url, data, callback, type) {
	if ( $.isFunction(data) ){
		type = type || callback, callback = data, data = {}
	}
	 
	return $.ajax({
		url: url,
		type: 'DELETE',
		success: callback,
		data: data,
		contentType: type
	});
}