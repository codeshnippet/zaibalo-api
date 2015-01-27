var loginPopup = function(){
	var self = this;
	self.showPopup = function(loginFunction, opts){
		$('#login-button').off();
		$('#login-error').addClass('hide');
		$('#login-button').on('click', loginFunction.call(opts));
		$('#test_modal').modal('show');
	}
	
	self.hide = function(){
		$('#test_modal').modal('hide');
	}
	
	self.showErrorMessage = function(){
		$('#login-error').removeClass('hide');
	}
}