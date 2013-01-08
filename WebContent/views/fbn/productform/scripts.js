function init(){
	$('#inputFormArea').prepend(
			'<div class="row-fluid" id="headerArea">'+
				'<div id="headerRight"></div><div id="headerLeft"></div>'+
			'</div>'
			);
	
	$('#input-totalForPo, #input-qtyOnPo').keyup(function(){
		calculate();
	});
	
	$('#input-totalForPo').keyup(function(){
		$(this).val( "$"+$(this).val().replace(/[^\d|\.]/g,'') );
	});

	$('#input-productType, #input-packaging, #input-numberOfPages, #input-priceClass').change(function(){
		calculate();
	});
}

function calculate() {
	var costPerPiece = $('#input-totalForPo').val().replace('$','') / $('#input-qtyOnPo').val();
	if (isNaN(costPerPiece))
		total = 0;
	
	var packQty = $('#input-packaging option:selected').text().match(/\d+/);
	if (isNaN(packQty))
		packQty = 1;
	
	var pageCnt = $('#input-numberOfPages option:selected').text().match(/\d+/);
	if (isNaN(pageCnt))
		pageCnt = $('#input-numberOfPages-custom').val().match(/\d+/);
	
	
	if ($('#input-productType').val() == 0) {
		var total = (costPerPiece * packQty);		
		var x = parseFloat((total*1.3)+4.5).toFixed(2);
		
		if (x != 'Infinity')
			$('#input-fieldPrice').val( isNaN(total) ? '' : "$"+x+" per "+$('#input-packaging option:selected').text().toLowerCase() ); 
	} else {
		if (!isNaN(pageCnt)) {
			if (pageCnt == 2) $('#input-fieldPrice').val( '$25.35' );
			else if (pageCnt == 4) $('#input-fieldPrice').val( '$42.25' );
			else if (pageCnt == 8) $('#input-fieldPrice').val( '$66.11' );
			else if (pageCnt == 12) $('#input-fieldPrice').val( '$97.24' );
			else if (pageCnt == 16) $('#input-fieldPrice').val( 'N/A' );
			else if (pageCnt == 24) $('#input-fieldPrice').val( 'N/A' );
			else $('#input-fieldPrice').val( 'N/A' );			
		}
	}
	
	$('#input-defaultPrice, #input-fieldPrice, #input-ourPrice, #input-theirPrice').each(function(){	
		//if ($(this).val().length<1)
			$(this).val( $('#input-fieldPrice').val() ).change();
	});
}