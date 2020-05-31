package com.bienes.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bienes.model.Pedidofondo;
import com.bienes.model.PedidofondoLinea;
import com.bienes.service.IPedidoFondoLineaService;
import com.bienes.service.IPedidofondoService;
import com.bienes.util.NumberUtils;
import com.bienes.util.PageRender;

@Controller
@RequestMapping("/pedidofondolineas")
public class PedidofondoLineasController {
		
	@Autowired
	private IPedidoFondoLineaService pedidofondoLineaService;	
	
	@Autowired
	private IPedidofondoService pedidofondoService;
	
	@GetMapping("/index/{pedidofondo_id}")
	public String mostrarIndex(
									
								  HttpServletRequest request,
								  
								  Model model,
								  @PathVariable("pedidofondo_id") Integer idPedidofondo,
								  @RequestParam(defaultValue = "0") Integer page, 
								  @RequestParam(defaultValue = "25") Integer pageSize,
								  @RequestParam(required = false) String numero
							  ){
			
		
		Pedidofondo pedidofondo = pedidofondoService.buscarPorId(idPedidofondo);		
		if(pedidofondo != null) {
			Pageable pageRequest = PageRequest.of(page, pageSize);
			Page<PedidofondoLinea> pedidofondoLineas = pedidofondoLineaService.getPageByPedidoFondo(pageRequest, pedidofondo);
			PageRender<PedidofondoLinea> pageRender = new PageRender<PedidofondoLinea>(request.getRequestURI()+"?"+request.getQueryString(), pedidofondoLineas,pageSize,page);
			
			model.addAttribute("page", pageRender);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("pedidofondoLineas", pedidofondoLineas);
			model.addAttribute("utils", NumberUtils.class);
		}	
		return "pedidofondo/pedidofondoLinea/index";
	}
	
	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET , produces = "application/json")
	public @ResponseBody Map<String, Object> eliminar(@PathVariable("id") int idPedidofondoLinea, RedirectAttributes attributes) {
		
		Map<String, Object> r = new HashMap<String, Object>();
		boolean error = false;
		String errorStr = "";
		try {
			pedidofondoLineaService.eliminar(idPedidofondoLinea);
			attributes.addFlashAttribute("msg", "La linea fue eliminada!.");
			r.put("success", true);
		} catch (Exception e) {
			errorStr += "Error no se ha podido eliminar la linea."+e;
			r.put("error", errorStr);
		}	
		return r;
	}
}
