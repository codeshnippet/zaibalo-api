//AJAX Error handling
$.ajaxSetup({
    statusCode: {
        401: function(error, callback){
        	alert(error.responseText);
        },
        403: function(error, callback){
        	alert(error.responseText);
        }
    }
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