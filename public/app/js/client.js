$( document ).ready(function() {
    $("#testForm").submit(function(event) {
    	var url = $("input[name='url']").val();
    	var type = $("select[name='method']").val();
    	var authToken = $("input[name='header-value']").val();
    	var body = $("textarea[name='body']").val();
    	
    	$.ajax({
    		type : type,
    		url : url,
    		data : body,
    		beforeSend: function(xhr){xhr.setRequestHeader('x-auth-token', authToken);},
    		success : function(data) {
    			$("#response").text(JSON.stringify(data));
    		},
    		error : function(jqXHR, textStatus, errorThrown ){
    			$("#response").text(jqXHR.status + " : " + jqXHR.statusText);
    		}
    	});
    	event.preventDefault();
    	return false; // avoid to execute the actual submit of the form.
    });
    
    $('.operation').click( function() {
    	$("input[name='url']").val($(this).attr('data-url'));
    	$("select[name='method']").val($(this).attr('data-method'));
    	$("textarea[name='body']").val($(this).attr('data-body'));
    	return false;
    });
    
    //"{\"loginName\":\"test\", \"password\":\"secret\", \"displayName\":\"Superman\", \"email\":\"super@man.com\"}"
    //"{\"username\":\"test\", \"password\":\"secret\"}"
    //"{\"loginName\":\"test\", \"password\":\"secret\", \"displayName\":\"Superman\", \"email\":\"super@man.com\"}"
});