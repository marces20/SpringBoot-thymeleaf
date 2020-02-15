$(function(){

	$(".date").inputmask("99/99/9999");
	$(".numeroExpediente").inputmask("9999");
	
	 
		$('#exampleModal').on('show.bs.modal', function (event) {
		  var button = $(event.relatedTarget) // Button that triggered the modal
		  var url = button.data('url');
		  var title = button.data('title')
		 
		  var modal = $(this)
		  $.get(url, function(data){
			  modal.find('.modal-content').html(data);
			  
		  });	
		   
		})
		
		
		
});

$(document).ready(function(){
	
	if($("#orga").length){
		var options = {
				script:"/suggestCie/",
				varname:"",
				json:true,
				shownoresults:true,
				maxresults:6,
				callback: function (obj) { 		
											$("#orga_id").val(obj.id); 
										 }
			};
		var as_json = new bsn.AutoSuggest('orga', options);
	}

});
 
	
function getCheckListadoSeleccionados(){
	return $("input[name='check_listado[]']").serialize();
}


