$(document).ready(function(){
	$('#filtersArea').prepend(
		'<div id="headerRight"></div><div id="headerLeft"></div>'
	);
	
	$('#filtersArea .btn-group').prepend('<div id="filtersLabel">Filter</div>');
	
	$('#input-poNumber').attr('maxlength','10');
});