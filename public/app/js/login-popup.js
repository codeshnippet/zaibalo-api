function getLoginPopup() {
	var self = this;
	self.showPopup = function(loginFunction, opts) {
		$('#login-button').off();
		$('#login-error').addClass('hide');
		$('#login-button').on('click', function() {
			saveAuthValues($('#username').val(), $('#password').val());
			loginFunction(opts);
		});
		$('#login_modal').modal('show');
	}

	self.hide = function() {
		$('#login_modal').modal('hide');
	}

	self.showErrorMessage = function() {
		$('#login-error').removeClass('hide');
	}

	return self;
}
