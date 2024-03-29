package com.bienes.service.db;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bienes.model.Expediente;
import com.bienes.model.ExpedienteMovimiento;
import com.bienes.repository.ExpedienteMovimientoRepository;
import com.bienes.service.IExpedienteMovimientoService;
import com.bienes.util.DateUtils;

@Service
public class ExpedienteMovimientoServiceJpa implements IExpedienteMovimientoService{
	
	@Autowired 
	private ExpedienteMovimientoRepository expeMoviRepo;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public ExpedienteMovimiento buscarPorId(Integer idExpedienteMovimiento) {
		Optional<ExpedienteMovimiento> optional = expeMoviRepo.findById(idExpedienteMovimiento);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	@Override
	public void guardar(ExpedienteMovimiento expedienteMovimiento) {
		expeMoviRepo.save(expedienteMovimiento);
		
	}

	@Override
	public void eliminar(Integer idExpedienteMovimiento) {
		expeMoviRepo.deleteById(idExpedienteMovimiento);
		
	}

	@Override
	public List<ExpedienteMovimiento> getByExpedienteId(Integer idExpediente) {
		return expeMoviRepo.getByExpedienteId(idExpediente);
	}
	
	@Override
	public List<ExpedienteMovimiento> getByCodigo(Integer codigo) {
		return expeMoviRepo.getByCodigo(codigo);
	}

	@Override
	public ExpedienteMovimiento getLastMovimiento(Expediente expediente) {
		List<ExpedienteMovimiento> r = expeMoviRepo.getLastMovimiento(expediente);
		if(r.size() > 0) {
			return r.get(0);
		}else {
			return null;
		}
	}

	@Override
	public String getMovimientoAnteriorEnTiempo(ExpedienteMovimiento expedienteMovimiento) {
		List<ExpedienteMovimiento> le = expeMoviRepo.getMovimientoAnterior(expedienteMovimiento.getId(), expedienteMovimiento.getExpediente());
		String r = "";
		if(le.size() > 0) {
			ExpedienteMovimiento ex = le.get(0);
			if(ex.getFecha_salida() != null){
				r = DateUtils.restarDates(ex.getFecha_llegada(), ex.getFecha_salida());
			}else{
				r = DateUtils.restarDates(ex.getFecha_llegada(), new Date());
			}
		} 
		
		return r;
	}

	@Override
	public ExpedienteMovimiento getMovimientoAnterior(ExpedienteMovimiento expedienteMovimiento){	
		System.out.println("111111111111-------------"+expedienteMovimiento.getId());
		System.out.println("222222222-------------"+expedienteMovimiento.getExpediente());
		List<ExpedienteMovimiento> r = expeMoviRepo.getMovimientoAnterior(expedienteMovimiento.getId(), expedienteMovimiento.getExpediente());
		System.out.println("33333333-------------"+r.size());
		if(r.size() > 0) {
			return r.get(0);
		}else {
			return null;
		}
	}

	@Override
	public boolean isLastMovimientoServicioUsuario(Expediente expediente,Integer idOrga) {
		 
		ExpedienteMovimiento e = getLastMovimiento(expediente);
		 
		if(e != null && e.getOrganigrama().getId().equals(idOrga)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isLastMovimientoUsuario(Expediente expediente,Integer idUsuario) {
		 
		ExpedienteMovimiento e = getLastMovimiento(expediente);
		 
		if(e != null && e.getUsuario().getId().equals(idUsuario)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String tiempoEnMovimiento(ExpedienteMovimiento f){
		String r = "";
		
		if(f.getFecha_salida() != null){
			r = DateUtils.restarDates(f.getFecha_llegada(), f.getFecha_salida());
		}else{
			r = DateUtils.restarDates(f.getFecha_llegada(), new Date());
		}
		
		return r;
	}

	@Override
	public Integer getSecuenceCodigo() {
		return expeMoviRepo.getSecuenceCodigo();
	}

}
