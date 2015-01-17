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
        	$( "#login-button" ).off();
        	$('#login-error').addClass('hide');
        	$('#login-button').on('click', function(event) {
        		$.ajax(newOpts)
        			.done(function() {
        				$('#test_modal').modal('hide');
        			})
        			.fail(function() {
        				$('#login-error').removeClass('hide');
        			});
            });
        	$('#test_modal').modal('show');
        }
    });
});

ko.observableArray.fn.pushAll = function(valuesToPush) {
    var underlyingArray = this();
    this.valueWillMutate();
    ko.utils.arrayPushAll(underlyingArray, valuesToPush);
    this.valueHasMutated();
    return this;
};

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