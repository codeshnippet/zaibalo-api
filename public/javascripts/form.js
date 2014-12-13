$( document ).ready(function() {
    $("#testForm").submit(function(event) {
    	var url = $("input[name='url']").val();
    	var type = $("select[name='method']").val();
    	
    	$.ajax({
    		type : type,
    		url : url,
    		data : $("textarea[name='body']").val(),
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
});