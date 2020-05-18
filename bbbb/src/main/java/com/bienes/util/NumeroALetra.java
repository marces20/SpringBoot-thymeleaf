package com.bienes.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat; 

public class NumeroALetra {
	 

	    private static final String[] UNIDADES = { "", "UN ", "DOS ", "TRES ",
	            "CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE ", "DIEZ ",
	            "ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS",
	            "DIECISIETE", "DIECIOCHO", "DIECINUEVE", "VEINTE" };

	    private static final String[] DECENAS = { "VEINTI", "TREINTA ", "CUARENTA ",
	            "CINCUENTA ", "SESENTA ", "SETENTA ", "OCHENTA ", "NOVENTA ",
	            "CIEN " };

	    private static final String[] CENTENAS = { "CIENTO ", "DOSCIENTOS ",
	            "TRESCIENTOS ", "CUATROCIENTOS ", "QUINIENTOS ", "SEISCIENTOS ",
	            "SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS " };

	    /**
	     * Convierte a letras un numero de la forma $123,456.32
	     * 
	     * @param number
	     *            Numero en representacion texto
	     * @throws NumberFormatException
	     *             Si valor del numero no es valido (fuera de rango o )
	     * @return Numero en letras
	     */
	    public static String convertNumberToLetter(String number)
	            throws NumberFormatException {
	    	BigDecimal b = new BigDecimal(number);
	    	return convertNumberToLetter(b,true);
	    }
	    
	    public static String convertNumberToLetterSinPesos(String number)throws NumberFormatException{
	    	BigDecimal b = new BigDecimal(number);
	    	return convertNumberToLetter(b,false);
	    }
	    
	    /*public static String convertNumberToLetterBigDecimal2(BigDecimal number,boolean a)
	            throws NumberFormatException {
	    	
	    	return convertNumberToLetter(number,a);
	    }*/
	    
	    public static String convertNumberToLetterBigDecimal(BigDecimal number)
	            throws NumberFormatException {
	    	return convertNumberToLetter(String.valueOf(number));
	    }
	    
	    public static String convertNumberToLetterBigDecimal(Integer number)
	            throws NumberFormatException {
	    	return convertNumberToLetter(String.valueOf(number));
	    }

	    /**
	     * Convierte un numero en representacion numerica a uno en representacion de
	     * texto. El numero es valido si esta entre 0 y 999'999.999
	     * 
	     * @param number
	     *            Numero a convertir
	     * @return Numero convertido a texto
	     * @throws NumberFormatException
	     *             Si el numero esta fuera del rango
	     */
	    public static String convertNumberToLetter(BigDecimal doubleNumber,boolean conPesos)
	            throws NumberFormatException {
	    	//Logger.debug("11111111 "+doubleNumber); 
	        StringBuilder converted = new StringBuilder();

	        String patternThreeDecimalPoints = "#.###";

	        DecimalFormat format = new DecimalFormat(patternThreeDecimalPoints);
	        format.setRoundingMode(RoundingMode.DOWN);
	        
	        
	        // formateamos el numero, para ajustarlo a el formato de tres puntos
	        // decimales
	        //String formatedDouble = format.format(doubleNumber);
	        
	        doubleNumber = doubleNumber.setScale(2, BigDecimal.ROUND_DOWN);
	        
	        //doubleNumber = Double.parseDouble(formatedDouble);

	        // Validamos que sea un numero legal
	        BigDecimal s = new BigDecimal(999999999);
	        
	        if (doubleNumber.compareTo(s) > 0)
	            throw new NumberFormatException(
	                    "El numero es mayor de 999'999.999, "
	                            + "no es posible convertirlo");
	        BigDecimal sa = new BigDecimal(0);
	        
	        String menos = "";
	        if (doubleNumber.compareTo(sa) < 0) {
	        	doubleNumber = doubleNumber.multiply(new BigDecimal(-1));
	        	menos = "MENOS ";
	            //throw new NumberFormatException("El numero debe ser positivo");
	        }
	        String splitNumber[] = String.valueOf(doubleNumber).replace('.', '#')
	                .split("#");

	        // Descompone el trio de millones
	        int millon = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
	                8))
	                + String.valueOf(getDigitAt(splitNumber[0], 7))
	                + String.valueOf(getDigitAt(splitNumber[0], 6)));
	        if (millon == 1)
	            converted.append("UN MILLON ");
	        else if (millon > 1)
	            converted.append(convertNumber(String.valueOf(millon))
	                    + "MILLONES ");

	        // Descompone el trio de miles
	        int miles = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
	                5))
	                + String.valueOf(getDigitAt(splitNumber[0], 4))
	                + String.valueOf(getDigitAt(splitNumber[0], 3)));
	        if (miles == 1)
	            converted.append("MIL ");
	        else if (miles > 1)
	            converted.append(convertNumber(String.valueOf(miles)) + "MIL ");

	        // Descompone el ultimo trio de unidades
	        int cientos = Integer.parseInt(String.valueOf(getDigitAt(
	                splitNumber[0], 2))
	                + String.valueOf(getDigitAt(splitNumber[0], 1))
	                + String.valueOf(getDigitAt(splitNumber[0], 0)));
	        if (cientos == 1)
	            converted.append("UN");

	        if (millon + miles + cientos == 0)
	            converted.append("CERO ");
	        if (cientos > 1)
	            converted.append(convertNumber(String.valueOf(cientos)));
	        if(conPesos){
	        	converted.append(" PESOS");
	        }
	        // Descompone los centavos
	        int centavos = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[1], 2))
	                + String.valueOf(getDigitAt(splitNumber[1], 1))
	                + String.valueOf(getDigitAt(splitNumber[1], 0)));
	        
	        String centaString = String.valueOf(centavos);
	        if(centaString.length() == 1){
	        	centaString = centaString+"0";
	        }
	        centavos = new Integer(centaString);
	        
	        if (centavos == 1)
	            converted.append(" CON UN CENTAVO");
	        else if (centavos > 1){
	        	 
	        	if(splitNumber[1].length() == 1){
	        		splitNumber[1] = splitNumber[1]+"0";
	        	}
	        	
	        	if(getDigitAt(splitNumber[1],1) == 0){
	        		String cent = String.valueOf(getDigitAt(splitNumber[1],0)); 
	        		if(cent.compareTo("1") == 0){
	        			converted.append(" CON UN CENTAVO");
	        		}else{
	        			converted.append(" CON " + convertNumber(String.valueOf(cent)) + " CENTAVOS");
	        		}
	        	}else{
		        	converted.append(" CON " + convertNumber(String.valueOf(centavos)) + " CENTAVOS");
	        	}
	        }
	        
	        
	        
	        return menos+converted.toString();
	    }

	    /**
	     * Convierte los trios de numeros que componen las unidades, las decenas y
	     * las centenas del numero.
	     * 
	     * @param number
	     *            Numero a convetir en digitos
	     * @return Numero convertido en letras
	     */
	    private static String convertNumber(String number) {
	    	
	    	if (number.length() > 3)
	            throw new NumberFormatException(
	                    "La longitud maxima debe ser 3 digitos");

	        // Caso especial con el 100
	        if (number.equals("100")) {
	            return "CIEN";
	        }

	        StringBuilder output = new StringBuilder();
	        if (getDigitAt(number, 2) != 0)
	            output.append(CENTENAS[getDigitAt(number, 2) - 1]);

	        int k = Integer.parseInt(String.valueOf(getDigitAt(number, 1))
	                + String.valueOf(getDigitAt(number, 0)));

	        if (k <= 20)
	            output.append(UNIDADES[k]);
	        else if (k > 30 && getDigitAt(number, 0) != 0)
	            output.append(DECENAS[getDigitAt(number, 1) - 2] + "Y "
	                    + UNIDADES[getDigitAt(number, 0)]);
	        else
	            output.append(DECENAS[getDigitAt(number, 1) - 2]
	                    + UNIDADES[getDigitAt(number, 0)]);

	        return output.toString();
	    }

	    /**
	     * Retorna el digito numerico en la posicion indicada de derecha a izquierda
	     * 
	     * @param origin
	     *            Cadena en la cual se busca el digito
	     * @param position
	     *            Posicion de derecha a izquierda a retornar
	     * @return Digito ubicado en la posicion indicada
	     */
	    private static int getDigitAt(String origin, int position) {
	        if (origin.length() > position && position >= 0)
	            return origin.charAt(origin.length() - position - 1) - 48;
	        return 0;
	    }

}