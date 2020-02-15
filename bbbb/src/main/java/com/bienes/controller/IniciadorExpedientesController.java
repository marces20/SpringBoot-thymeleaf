package com.bienes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bienes.model.IniciadorExpediente;
import com.bienes.service.IIniciadorExpedienteService;

@Controller
@RequestMapping("/iniciador-expedientes")
public class IniciadorExpedientesController {
	
	@Autowired
	private IIniciadorExpedienteService servicioIniciador;
	
	@GetMapping(value = "/suggest-iniciador/{nombre}", produces = { "application/json" })
	public @ResponseBody List<IniciadorExpediente> cargarIniciador(@PathVariable String nombre) {
		
		return servicioIniciador.findByNombre(nombre);
	}
}
