(function ( $ ) {
	$.fn.modalSearch = function(options) {
		
		var initSettings = {
				resizable: false,
				autoOpen: false,
				modal: true,
		        buttons: {
			          Cancelar: function() {
			            $( this ).dialog( "destroy" );
			          }
			    	},
		    	close: function(event, ui ){
		    		$(this).dialog( "destroy" );
		    	}
			}
			
		var create = function ( settings ){
		        var contenido = $('<div></div>');
		        var dialogo = contenido.dialog( settings );
		        dialogo.on( "dialogopen", function( event, ui ) {
		    		$.get(settings.url, function(data){
		    			contenido.html(data);
		    		});
		        });
		    	return dialogo;
		    }
		
		return this.each(function () {
				var data = $(this).data();
		        var settings = $.extend(data, initSettings, options);
				var inputLabel = $(settings.label);
				var inputId = $(settings.field);
				
				$(this).on('click', function(){
					var autoDialogo = create(settings);
					autoDialogo.dialog('open');
					
					$(document).on(settings.listen, function(event, respuesta){
						inputLabel.val(respuesta.nombre)
						inputId.val(respuesta.id);
						inputId.change();
						autoDialogo.dialog().dialog("close");
						$(this).off(settings.listen);
					});
				});
			
				$(inputLabel).on('change', function(){
					if($(this).val() == ""){
						inputId.val("");
						inputId.change();
					}
				}).on('click', function(){
					$(this).select();
					return false;
				}).on('blur', function(){
					if(inputId.val() == ""){
						inputLabel.val("");
					}
				});
	        
		});
	}

 
}( jQuery ));