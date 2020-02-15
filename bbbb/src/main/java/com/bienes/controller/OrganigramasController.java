package com.bienes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bienes.model.Organigrama;
import com.bienes.service.IOrganigramaService;

@Controller
@RequestMapping("/organigramas")
public class OrganigramasController {
	
	@Autowired
	private IOrganigramaService serviceOrganigrama;
	
	@GetMapping(value = "/suggest-organigrama/{nombre}", produces = { "application/json" })
	public @ResponseBody List<Organigrama> cargarOrganigrama(@PathVariable String nombre) {
		return serviceOrganigrama.findByNombre(nombre);
	}
}
