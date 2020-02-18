$(function(){

	$(".date").inputmask("99/99/9999");
	$(".numeroExpediente").inputmask("9999");
	
	$('table tr.pointer td:not(:has(.notSeleccion))').click( function(){
		window.location.href = $(this).closest('tr').attr('href');
	}); 
	
	$('#checkAll').change( function(){
		var table = $(this).closest('table');
		table.find("input[name='check_listado[]']").prop("checked", $(this).prop( "checked" ) );
	});
		/*$('#exampleModal').on('show.bs.modal', function (event) {
		  var button = $(event.relatedTarget) // Button that triggered the modal
		  var url = button.data('url');
		  var title = button.data('title')
		 
		  var modal = $(this)
		  $.get(url, function(data){
			  modal.find('.modal-content').html(data);
			  
		  });	
		   
		})*/
		
		
		$('#reportePaseExpediente').click( function() { //abrir modal para mostrar mensaje informe rentas
			 
			var url = $(this).attr("data-url");
			var dialogo = $('<div></div>');

			dialogo.dialog({
				title: "Pase Expedientes",
		    	resizable: false,
				autoOpen: true,
				modal: true,
				height: 350,
				width:650,
		        buttons: {
			          Cerrar: function() {
			            $( this ).dialog( "destroy" );
			          }
			    },
		    	close: function(event, ui ){
		    		$(this).dialog( "destroy" );
		    	},
			    open: function( event, ui ) {
					$.get(url,  function(data){
						dialogo.html(data);
					});	
			    }
		    });
		});
		
		
		$( "#organigrama" ).autocomplete({
			  source: function( request, response ) {
			   // Fetch data
				   $.ajax({
				    url: "/organigramas/suggest-organigrama/"+request.term,
				    type: 'get',
				    dataType: "json",
				    data: {},
				    success: function( data ) {
				    	response($.map(data, function (item) {
		                    return {
		                        //Indicamos el Valor
		                        value: item.nombre,
		                        //el Label si lo desean
		                       // label: item.Nombre,
		                        //y el ID
		                        id: item.id
		                    }
		                }))
				    }
				   });
			  },
			  select: function (event, ui) {
			   		// Set selection
				   $('#organigrama').val(ui.item.value); // display the selected text
				   $('#organigrama_id').val(ui.item.id); // save selected id to input
				   return false;
			  }

		}); 
		
		$( "#iniciador" ).autocomplete({
			  source: function( request, response ) {
			   // Fetch data
				   $.ajax({
				    url: "/iniciador-expedientes/suggest-iniciador/"+request.term,
				    type: 'get',
				    dataType: "json",
				    data: {},
				    success: function( data ) {
				    	response($.map(data, function (item) {
		                    return {
		                        //Indicamos el Valor
		                        value: item.nombre,
		                        //el Label si lo desean
		                       // label: item.Nombre,
		                        //y el ID
		                        id: item.id
		                    }
		                }))
				    }
				   });
			  },
			  select: function (event, ui) {
			   		// Set selection
				   $('#iniciador').val(ui.item.value); // display the selected text
				   $('#iniciador_id').val(ui.item.id); // save selected id to input
				   return false;
			  }

		}); 
		
		
});


 
function getLoading () {
	return $('<div id="loading"></div>');
}
	
function getCheckListadoSeleccionados(){
	return $("input[name='check_listado[]']").serialize();
}


