package modelo;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Halstead {
	
	private float longitud;
	private float volumen;


	public Halstead(String codigo) {
		calcularMetricas(codigo);
	}

	private void calcularMetricas(String codigo) {
		HashMap<String,Integer> operadores = new HashMap<>();
		HashMap<String,Integer> operandos = new HashMap<String,Integer>();
		
		Reader reader = new StringReader(codigo);
		StreamTokenizer st = new StreamTokenizer(reader);
		try {
			int token;
			while((token = st.nextToken()) != StreamTokenizer.TT_EOF) {
				if(st.ttype==StreamTokenizer.TT_NUMBER){
					incrementarContador(operandos, String.valueOf(st.nval), 1);
				}else if(st.ttype==StreamTokenizer.TT_WORD) {
					if (esKeyword(st.sval)) {
						incrementarContador(operadores, st.sval, 1);
					} else if (!Character.isUpperCase(st.sval.charAt(0))) {
						incrementarContador(operandos, st.sval, 1);
					}
				} else if(st.ttype != StreamTokenizer.TT_EOF && st.ttype != StreamTokenizer.TT_EOL) {
					switch ((char) token) {
						case '+':
						case '-':
						case '*':
						case '/':
						case '%':
						case '=':
						case '<':
						case '>':
						case '&':
						case '|':
						case '!':
							incrementarContador(operadores, Character.toString((char)token), 1);
							break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int operadoresUnicos = 0, operandosUnicos = 0;
		Iterator it = operadores.entrySet().iterator();
	    while (it.hasNext()) {
	    	
	        Entry entrada = (Entry)it.next();
	        if ((int)entrada.getValue() > 0) {
	        	operadoresUnicos++;
	        }
	    }
	    it = operandos.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry entrada = (Entry)it.next();
	        if ((int)entrada.getValue() > 0) {
	        	operandosUnicos++;
	        }
	    }	
	    longitud = operadoresUnicos + operandosUnicos;
	    
	    float operandosTotales = 0, operadoresTotales = 0;
	    for (int v : operandos.values()) {
	        operandosTotales += v;
	    }
	    for (int v : operadores.values()) {
	    	operadoresTotales += v;
	    }
	    volumen = (float) (longitud * Math.log10(operandosTotales + operadoresTotales) / Math.log10(2));
	}
	
	private void incrementarContador(HashMap<String, Integer> mapa, String key, int incremento) {
		int actual = (mapa.containsKey(key)) ? mapa.get(key) : 0;
		mapa.put(key, actual + incremento );
	}
	
	private boolean esKeyword(String s) {
		switch(s) {
			case "abstract":
			case "for":
			case "new":
			case "switch":
			case "default":
			case "continue":
			case "synchronized":
			case "boolean":
			case "do":
			case "if":
			case "private":
			case "this":
			case "break":
			case "double":
			case "implements":
			case "protected":
			case "throw":
			case "byte":
			case "else":
			case "public":
			case "throws":
			case "case":
			case "enum":
			case "return":
			case "catch":
			case "extends":
			case "int":
			case "short":
			case "try":
			case "char":
			case "interface":
			case "static":
			case "final":
			case "void":
			case "class":
			case "finally":
			case "long":
			case "const":
			case "float":
			case "super":
			case "while":
				return true;
		default:
				return false;
		}
	}
	public float getLongitud() {
		return longitud;
	}
	public float getVolumen() {
		return volumen;
	}
}
