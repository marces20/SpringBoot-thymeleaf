package com.bienes.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.ocpsoft.prettytime.Duration;
import org.ocpsoft.prettytime.PrettyTime;

public class DateUtils {
	public static String formatDate(Date date) {
		if(date != null){
			SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
			String date1 = format1.format(date);
			return date1;
		}else{
			return "";
		}
  
	}

	public static String formatDate(Date date, String formato) {
		if(date != null){
			SimpleDateFormat format1 = new SimpleDateFormat(formato);
			String date1 = format1.format(date);
			return date1;
		}else{
			return "";
		}
  
	}
	
	public static Date formatDate(String date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        
        try {
            return df.parse(date);
        } catch (ParseException ex) { }
 
        return null;
	}
	
	public static int compareDate(Date date1,Date date2) {
		int r = 0;
		try {
		 
			SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
			
			r = format1.parse(format1.format(date1)).compareTo(format1.parse(format1.format(date2)));
			return r;
		 } catch (Exception ex) { }
		
		return 0;
	}
	
	
    
    public static String getMesLetras(int Mes){
    	String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};	
    	String retornaMes = meses[Mes];
        return retornaMes;
    }
	
    public static int getLastDayOfMonth(Date fecha){
    	
    	Calendar calendar = Calendar.getInstance();  
        calendar.setTime(fecha);  
        
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        return maxDay;
    }
    
    public static String getNow(){
    	return DateUtils.formatDate(new Date());
    }
    
    public static String restarDates(Date f1,Date f2){
    	    	
    	PrettyTime p = new PrettyTime(f2);
    	List<Duration> durations = p.calculatePreciseDuration(f1);
    	String r =  p.format(durations);
    	r = r.replace("hace instantes", "");
    	r = (r.isEmpty())?"5 minutos":r.replace("hace", "");
    	//r = r.replace("", "5 minutos");
    	
    	return r;
    	
    }
    
    public static int getYearFromDate(Date date) {
        int result = -1;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            result = cal.get(Calendar.YEAR);
        }
        return result;
    }
    
    public static java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
}
