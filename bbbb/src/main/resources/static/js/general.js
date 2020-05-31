$(function(){

	$(".date").inputmask("99/99/9999");
	$(".numeroExpediente").inputmask("9999");
	$(".inputNumericMask").numeric_input();
	$('table tr.pointer td:not(:has(.notSeleccion))').click( function(){
		window.location.href = $(this).closest('tr').attr('href');
	}); 
	
	$('.searchModal').modalSearch();
	
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
		
		$(document).on('click', '#cancelarPaseExpediente', function(){
			var str = "";
			if($('#id').val().length > 0){
		    	str += 'expediente_id='+$('#id').val();
			}else{
				if($("input[name='check_listado[]']").length > 0){
					str += $("input[name='check_listado[]']").serialize()
				}
			}
			
			var url = $(this).attr('data-url');
			var mensaje = "¿Confirma que desea cancelar el pase?";
			
			if(confirm(mensaje)){
				$this = $(this);
				
				$.get(url,str ,function(data){
					if(data.success){
						location.reload();
					} else {
						alert(data.error);
					}
				});
			}
			return false;
		});
		
		$(document).on('click', '#asignarAMiServicioExpediente', function(){
			var str = "";
			if($('#id').val().length > 0){
		    	str += 'expediente_id='+$('#id').val();
			}else{
				if($("input[name='check_listado[]']").length > 0){
					str += $("input[name='check_listado[]']").serialize()
				}
			}
			
			var url = $(this).attr('data-url');
			var mensaje = "¿Confirma asignarse el expediente?";
			
			if(confirm(mensaje)){
				$this = $(this);
				
				$.get(url,str ,function(data){
					if(data.success){
						location.reload();
					} else {
						alert(data.error);
					}
				});
			}
			return false;
		});
		
		$('.cargarPedidoFondo').click( function() { 
			 
			var url = $(this).attr("data-url");
			var factura = $(this).attr("data-factura");
			var dialogo = $('<div></div>');
			var str = "";
			
			if($('#idPedidofondo').val().length > 0){
				str += "pedidofondo_id="+$('#idPedidofondo').val();
			}
			
			str += "&factura_id="+factura;
			 
			 
			dialogo.dialog({
				title: "Cargar Pedido Fondo",
		    	resizable: false,
				autoOpen: true,
				modal: true,
				height: 350,
				width:650,
		        buttons: {
			          Cerrar: function() {
			            $( this ).dialog( "destroy" );
			            location.reload();
			          }
			    },
		    	close: function(event, ui ){
		    		$(this).dialog( "destroy" );
		    		location.reload();
		    		
		    	},
			    open: function( event, ui ) {
					$.get(url,str,  function(data){
						dialogo.html(data);
					});	
			    }
		    });
		});
		
		$('#reportePaseExpediente').click( function() { 
			 
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
			            location.reload();
			          }
			    },
		    	close: function(event, ui ){
		    		$(this).dialog( "destroy" );
		    		location.reload();
		    		
		    	},
			    open: function( event, ui ) {
					$.get(url,  function(data){
						dialogo.html(data);
					});	
			    }
		    });
		});
		
		$( "#expediente" ).autocomplete({
			  minLength: 1,
			  source: function( request, response ) {
			   // Fetch data
				   $.ajax({
				    url: "/expedientes/suggest-expediente/"+request.term,
				    type: 'get',
				    dataType: "json",
				    data: {},
				    success: function( data ) {
				    	
				    	    if(!data.length){
			    		        var result = [
			    		            {
			    		            	value: 'No se encuentran resultados', 
			    		                id: 0
			    		            }
			    		        ];
			    		        response(result);
			    		    }else{
			    		    	
						    	 response($.map(data.slice(0,20), function (item) {
						    		 return {
					                        value: item.nombre+'/'+item.ejercicio.nombre,
					                        id: item.id
					                    }
				                    }))
				    		
				    	  }
				    }
				   });
			  },
			  select: function (event, ui) {
			   		// Set selection
				   if(ui.item.id != 0){
					   $('#expediente').val(ui.item.value); // display the selected text
					   $('#expediente_id').val(ui.item.id); // save selected id to input
				   }else{
					   $('#expediente').val(''); // display the selected text
					   $('#expediente_id').val('');
				   }
				   return false;
			  }

		});
		
		
		$( "#organigrama" ).autocomplete({
			  minLength: 3,
			  source: function( request, response ) {
			   // Fetch data
				   $.ajax({
				    url: "/organigramas/suggest-organigrama/"+request.term,
				    type: 'get',
				    dataType: "json",
				    data: {},
				    success: function( data ) {
				    	 if(!data.length){
			    		        var result = [
			    		            {
			    		            	value: 'No se encuentran resultados', 
			    		                id: 0
			    		            }
			    		        ];
			    		        response(result);
			    		    }else{
						    	 response($.map(data.slice(0,20), function (item) {
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
				    }
				   });
			  },
			  select: function (event, ui) {
			   		// Set selection
				   if(ui.item.id != 0){
					   $('#organigrama').val(ui.item.value); // display the selected text
					   $('#organigrama_id').val(ui.item.id); // save selected id to input
				   }else{
					   $('#organigrama').val(''); // display the selected text
					   $('#organigrama_id').val('');
				   }
				   return false;
			  }

		}); 
		
		$( "#proveedor" ).autocomplete({
			  minLength: 2,
			  source: function( request, response ) {
			   // Fetch data
				   $.ajax({
				    url: "/proveedores/suggest-proveedor/"+request.term,
				    type: 'get',
				    dataType: "json",
				    data: {},
				    success: function( data ) {
				    	 if(!data.length){
			    		        var result = [
			    		            {
			    		            	value: 'No se encuentran resultados', 
			    		                id: 0
			    		            }
			    		        ];
			    		        response(result);
			    		    }else{
						    	 response($.map(data.slice(0,20), function (item) {
						    		 return {
					                        value: item.nombre,
					                        id: item.id
					                    }
				                    }))
				    		
				    	 }
				    }
				   });
			  },
			  select: function (event, ui) {
			   		// Set selection
				   if(ui.item.id != 0){
					   $('#proveedor').val(ui.item.value); // display the selected text
					   $('#proveedor_id').val(ui.item.id); // save selected id to input
				   }else{
					   $('#proveedor').val(''); // display the selected text
					   $('#proveedor_id').val('');
				   }
				   return false;
			  }

		}); 
		
		$( "#iniciador" ).autocomplete({
			  minLength: 3,
			  source: function( request, response ) {
			   // Fetch data
				   $.ajax({
				    url: "/iniciador-expedientes/suggest-iniciador/"+request.term,
				    type: 'get',
				    dataType: "json",
				    data: {},
				    success: function( data ) {
				    	if(!data.length){
		    		        var result = [
		    		            {
		    		            	value: 'No se encuentran resultados', 
		    		                id: 0
		    		            }
		    		        ];
		    		        response(result);
		    		    }else{
					    	 response($.map(data.slice(0,20), function (item) {
					    		 return {
				                        value: item.nombre,
				                        id: item.id
				                    }
			                    }))
			    		}
				    }
				   });
			  },
			  select: function (event, ui) {
			   		// Set selection
				   if(ui.item.id != 0){
					   $('#iniciador').val(ui.item.value); // display the selected text
					   $('#iniciador_id').val(ui.item.id); // save selected id to input
				   }else{
					   $('#iniciador').val(''); // display the selected text
					   $('#iniciador_id').val('');
				   }
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


