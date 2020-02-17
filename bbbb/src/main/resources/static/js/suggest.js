 
	
	
	$(document).ready(function() {
		 
		/*if($("#orga").length){
			 
			var options = {
					script:"/organigramas/suggest-organigrama/",
					varname:"",
					json:true,
					shownoresults:true,
					maxresults:6,
					callback: function (obj) { 		
												$("#orga_id").val(obj.id); 
											 }
				};
			var as_json = new bsn.AutoSuggest('orga', options);
		} 	*/
		 
		
		
			/*$("#organigrama").autocomplete({
	
				source : function(request, response) {
					$.ajax({
						url : "/organigramas/suggest-organigrama/" + request.term,
						dataType : "json",
						data : {
							term : request.term
						},
						success : function(data) {
							response($.map(data, function(item) {
								return {
									value : item.id,
									label : item.nombre
								};
							}));
						},
					});
				},
				select : function(event, ui) {
					$("#organigrama").val(ui.item.label);
					$("#organigrama_id").val(ui.item.value);
	
					return false;
				}
			});
		 
		$("#iniciador").autocomplete({

			source : function(request, response) {
				$.ajax({
					url : "/iniciador-expedientes/suggest-iniciador/" + request.term,
					dataType : "json",
					data : {
						term : request.term
					},
					success : function(data) {
						response($.map(data, function(item) {
							return {
								value : item.id,
								label : item.nombre,
							};
						}));
					},
				});
			},
			select : function(event, ui) {
				$("#iniciador").val(ui.item.label);
				$("#iniciador_id").val(ui.item.value);

				return false;
			}
		});*/
	});