package com.bienes.view.pdf;

import java.awt.Color;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
 
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.bienes.model.ExpedienteMovimiento;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


@Component("expediente/expedienteMovimiento")
public class ExpedienteMovimientoPdfView extends AbstractPdfView {
	

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
									HttpServletRequest request, HttpServletResponse response) throws Exception {
																  
		ExpedienteMovimiento m = (ExpedienteMovimiento)model.get("expedienteMovimiento");
		
		PdfPTable tabla = new PdfPTable(1);
		tabla.setSpacingAfter(20);
		PdfPCell cell = null;
		cell = new PdfPCell();
		cell.setBackgroundColor(new Color(195, 230, 203));
		cell.setPadding(8f);
		
		if(m == null) {
			cell.setBackgroundColor(Color.red);
			tabla.addCell(cell);
			tabla.addCell("ESTE EXPEDIENTE NO TIENE MOVIMIENTOS ASIGNADOS.!!!");
		}else {
			tabla.addCell(cell);
			tabla.addCell("ID: " +  m.getId());
			
			
		}
		 
		document.add(tabla);
	}
	
	
	
}
